package com.example.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class WebPageToMarkdown {
    public static void main(String[] args) {
        String url = "https://docs.github.com/en/copilot/example-prompts-for-github-copilot-chat/refactoring-code/fixing-lint-errors"; // replace with your desired URL

        // Configure proxy
        //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.example.com", 8080));

        try {
            // Fetch the HTML content
            Document doc = Jsoup.connect(url).get();
            //Document doc = Jsoup.connect(url).proxy(proxy).get();

            // Extract the body content
            Element body = doc.body();

            // Use Flexmark to convert HTML to Markdown
            FlexmarkHtmlConverter converter = FlexmarkHtmlConverter.builder().build();
            String markdown = converter.convert(body.html());

            // Write Markdown to file
            try (FileWriter writer = new FileWriter("src/test/resources/markdown.md")) {
                writer.write(markdown);
            }

            System.out.println("Conversion successful, markdown saved to src/test/resources/markdown.md");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
