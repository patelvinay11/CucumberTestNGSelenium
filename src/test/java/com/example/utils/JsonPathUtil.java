package com.example.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;

public class JsonPathUtil {

    private static final Gson gson = new GsonBuilder().setLenient().serializeNulls().setPrettyPrinting().create();
    private static final Configuration gsonConfiguration = Configuration.builder()
            .jsonProvider(new GsonJsonProvider())
            .mappingProvider(new GsonMappingProvider())
            .build();

    public static void main(String[] args) {
        // Example JSON string
        String jsonString = "{\"store\": {\"book\": [{\"category\": \"reference\",\"author\": \"Nigel Rees\",\"title\": \"Sayings of the Century\",\"price\": 8.95},{\"category\": \"fiction\",\"author\": \"Evelyn Waugh\",\"title\": \"Sword of Honour\",\"price\": 12.99}],\"bicycle\": {\"color\": \"red\",\"price\": 19.95}}}";

        System.out.println("Original JSON:");
        System.out.println(prettyPrintJson(jsonString));
        System.out.println();

        // Example modifications
        try {
            // Set single value
            String path1 = "$.store.bicycle.color";
            String newValue1 = "blue";
            jsonString = setJsonPathValue(jsonString, path1, newValue1);
            System.out.println("After setting " + path1 + " to " + newValue1 + ":");
            System.out.println(jsonString);
            System.out.println();

            // Set array element
            String path2 = "$.store.book[0].price";
            double newValue2 = 9.99;
            jsonString = setJsonPathValue(jsonString, path2, newValue2);
            System.out.println("After setting " + path2 + " to " + newValue2 + ":");
            System.out.println(jsonString);
            System.out.println();

            // Add new field
            String path3 = "$.store.bicycle.model";
            String newValue3 = "Mountain Bike";
            jsonString = setJsonPathValue(jsonString, path3, newValue3);
            System.out.println("After setting " + path3 + " to " + newValue3 + ":");
            System.out.println(jsonString);
            System.out.println();

            // Get value
            System.out.println("New value of " + path1 + " is: " + getJsonPathValue(jsonString, path1));
            System.out.println("New value of " + path2 + " is: " + getJsonPathValue(jsonString, path2));
            System.out.println("New value of " + path3 + " is: " + getJsonPathValue(jsonString, path3));

        } catch (Exception e) {
            System.err.println("Error modifying JSON: " + e.getMessage());
        }
    }

    /**
     * Sets a value at the specified JsonPath and returns the modified JSON string
     *
     * @param jsonString The original JSON string
     * @param jsonPath The JsonPath expression pointing to where the value should be set
     * @param value The new value to set
     * @return Modified JSON string
     * @throws Exception If the path is invalid or modification fails
     */
    public static String setJsonPathValue(String jsonString, String jsonPath, Object value) throws Exception {
        try {
            // Parse the JSON document
            DocumentContext documentContext = JsonPath.parse(jsonString, gsonConfiguration);

            // Set the value at the specified path
            documentContext.set(jsonPath, value);

            // Return the modified JSON as string
            return prettyPrintJson(documentContext.jsonString());
        } catch (PathNotFoundException e) {
            // If path doesn't exist, try to create it
            if (jsonPath.matches(".*\\.[A-Za-z_$][A-Za-z0-9_$]*$")) {
                // This is a simple path that we can attempt to create
                DocumentContext documentContext = JsonPath.parse(jsonString, gsonConfiguration);
                JsonPath path = JsonPath.compile(jsonPath);
                documentContext.put(JsonPath.compile(jsonPath.replaceAll("\\.[^.]+$", "")),
                        jsonPath.replaceAll(".*\\.", ""),
                        value);
                return prettyPrintJson(documentContext.jsonString());
            }
            throw new Exception("Path not found and cannot be created automatically: " + jsonPath, e);
        } catch (Exception e) {
            throw new Exception("Failed to set value at path: " + jsonPath, e);
        }
    }

    /**
     * Gets a value from the specified JsonPath
     *
     * @param jsonString The JSON string to read from
     * @param jsonPath The JsonPath expression to evaluate
     * @return The value at the specified path
     * @throws Exception If the path is invalid or value cannot be read
     */
    public static Object getJsonPathValue(String jsonString, String jsonPath) throws Exception {
        try {
            return JsonPath.parse(jsonString, gsonConfiguration).read(jsonPath);
        } catch (PathNotFoundException e) {
            throw new Exception("Path not found: " + jsonPath, e);
        } catch (Exception e) {
            throw new Exception("Failed to read value at path: " + jsonPath, e);
        }
    }

    /**
     * Converts JSON string to pretty printed format
     */
    public static String prettyPrintJson(String jsonString) {
        try {
            // Parse and then convert back to pretty JSON
            Object json = gson.fromJson(jsonString, Object.class);
            return gson.toJson(json);
        } catch (Exception e) {
            System.err.println("Error pretty printing JSON: " + e.getMessage());
            return jsonString; // return original if pretty printing fails
        }
    }

}