package com.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class JsonToJavaPojoConverter {

    private static final String LOMBOK_IMPORTS = """
            import lombok.Data;
            import lombok.Builder;
            import lombok.NoArgsConstructor;
            import lombok.AllArgsConstructor;
            import com.google.gson.annotations.SerializedName;
            import java.util.List;
            """;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("JSON to Java POJO Converter with Lombok and Gson");
        System.out.println("-----------------------------------------------");

        // Get JSON input
        System.out.println("Enter JSON (or leave empty to use sample JSON):");
        String jsonInput = scanner.nextLine().trim();

        if (jsonInput.isEmpty()) {
            jsonInput = """
                {
                    "user_id": 12345,
                    "user_name": "John Doe",
                    "is_active": true,
                    "account_balance": 1250.75,
                    "address": {
                        "street": "123 Main St",
                        "city": "New York",
                        "zip_code": "10001"
                    },
                    "orders": [
                        {
                            "order_id": "ORD123",
                            "items": [
                                {
                                    "product_id": "P100",
                                    "quantity": 2,
                                    "price": 25.50
                                }
                            ],
                            "order_date": "2023-05-15"
                        }
                    ]
                }
                """;
            System.out.println("Using sample JSON:");
            System.out.println(jsonInput);
        }

        // Get package name
        System.out.println("Enter Java package name (e.g., com.example.model):");
        String packageName = scanner.nextLine().trim();
        if (packageName.isEmpty()) {
            packageName = "com.example.model";
            System.out.println("Using default package: " + packageName);
        }

        // Get root class name
        System.out.println("Enter root class name (e.g., User):");
        String rootClassName = scanner.nextLine().trim();
        if (rootClassName.isEmpty()) {
            rootClassName = "User";
            System.out.println("Using default class name: " + rootClassName);
        }

        // Get output directory
        System.out.println("Enter output directory (leave empty for current directory):");
        String outputDir = scanner.nextLine().trim();
        if (outputDir.isEmpty()) {
            outputDir = Paths.get("").toAbsolutePath() + File.separator + "src/test/java";
            System.out.println("Using default output directory: " + outputDir);
        }

        // Process JSON and generate classes
        JsonElement rootElement = JsonParser.parseString(jsonInput);
        Map<String, String> classes = new HashMap<>();
        processNode(rootElement.getAsJsonObject(), rootClassName, classes, packageName);

        // Create package directory structure
        String packagePath = outputDir + File.separator + packageName.replace(".", File.separator);
        Path outputPath = Paths.get(packagePath);
        Files.createDirectories(outputPath);

        // Write classes to files
        for (Map.Entry<String, String> entry : classes.entrySet()) {
            String className = entry.getKey();
            String classContent = entry.getValue();

            Path filePath = outputPath.resolve(className + ".java");
            Files.writeString(filePath, classContent);
            System.out.println("Generated: " + filePath);
        }

        System.out.println("\nSuccessfully generated Java POJO classes with Lombok annotations and Gson support!");
        scanner.close();
    }

    private static void processNode(JsonObject node, String className, Map<String, String> classes, String packageName) {
        if (classes.containsKey(className)) {
            return;
        }

        StringBuilder classBuilder = new StringBuilder();
        classBuilder.append("package ").append(packageName).append(";\n\n");
        classBuilder.append(LOMBOK_IMPORTS).append("\n");
        classBuilder.append("@Data\n");
        classBuilder.append("@Builder\n");
        classBuilder.append("@NoArgsConstructor\n");
        classBuilder.append("@AllArgsConstructor\n");
        classBuilder.append("public class ").append(className).append(" {\n\n");

        for (Map.Entry<String, JsonElement> entry : node.entrySet()) {
            String fieldName = entry.getKey();
            JsonElement valueNode = entry.getValue();
            String javaFieldName = toCamelCase(fieldName);
            String javaType = getJavaType(valueNode, fieldName);

            classBuilder.append("    @SerializedName(\"").append(fieldName).append("\")\n");
            classBuilder.append("    private ").append(javaType).append(" ").append(javaFieldName).append(";\n\n");

            if (valueNode.isJsonObject()) {
                String nestedClassName = capitalize(javaFieldName);
                processNode(valueNode.getAsJsonObject(), nestedClassName, classes, packageName);
            } else if (valueNode.isJsonArray() && valueNode.getAsJsonArray().size() > 0 && valueNode.getAsJsonArray().get(0).isJsonObject()) {
                String nestedClassName = capitalize(javaFieldName) + "Item";
                processNode(valueNode.getAsJsonArray().get(0).getAsJsonObject(), nestedClassName, classes, packageName);
            }
        }

        classBuilder.append("}");
        classes.put(className, classBuilder.toString());
    }

    private static String getJavaType(JsonElement node, String fieldName) {
        if (node.isJsonPrimitive()) {
            if (node.getAsJsonPrimitive().isBoolean()) {
                return "Boolean";
            } else if (node.getAsJsonPrimitive().isNumber()) {
                return node.getAsJsonPrimitive().getAsString().contains(".") ? "Double" : "Integer";
            } else if (node.getAsJsonPrimitive().isString()) {
                return "String";
            }
        } else if (node.isJsonObject()) {
            return capitalize(toCamelCase(fieldName));
        } else if (node.isJsonArray()) {
            JsonArray array = node.getAsJsonArray();
            if (array.size() > 0) {
                JsonElement firstElement = array.get(0);
                if (firstElement.isJsonObject()) {
                    return "List<" + capitalize(toCamelCase(fieldName)) + "Item>";
                }
                return "List<" + getJavaType(firstElement, fieldName) + ">";
            }
            return "List<Object>";
        }
        return "Object";
    }

    private static String toCamelCase(String fieldName) {
        String[] parts = fieldName.split("_");
        StringBuilder camelCase = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            camelCase.append(parts[i].substring(0, 1).toUpperCase())
                    .append(parts[i].substring(1).toLowerCase());
        }
        return camelCase.toString();
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}