package com.mailsender.utils;

import com.mailsender.data.joke.Joke;
import com.mailsender.data.joke.TranslatedJoke;
import com.nylas.Files;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RequestFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.xhtmlrenderer.simple.Graphics2DRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;


public class MessageImageCreator {
    public static Random rnd = new Random();

    @Value("${password}")
    private String password;

    @Value("${html_template}")
    private String htmlTemplatePath;

    @Value("${html_resource}")
    private String htmlResourcePath;

    @Value("${png_resource}")
    private String pngResourcePath;

    private String wrapJokesToHtmlTable(Joke... jokes) {
        StringBuffer resultText = new StringBuffer();
        resultText.append("" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"></meta>\n" +
                "    <style>\n" +
                "\n" +
                "        .lineLeft {\n" +
                "            border-left: 1px solid green;\n" +
                "            padding-left: 10px;\n" +
                "            margin-left: 10px;\n" +
                "        }\n" +
                "        .lineUp {\n" +
                "            border-top: 1px solid green;\n" +
                "            padding-left: 10px;\n" +
                "            margin-left: 10px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<table style=\"margin-bottom: 20px\">"
        );

        int maxCellsNumber = Arrays.stream(jokes).max(new Comparator<Joke>() {
            @Override
            public int compare(Joke o1, Joke o2) {
                if (o1.getCellsNumber() > o2.getCellsNumber()){
                    return 1;
                } else if (o1.getCellsNumber() < o2.getCellsNumber()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }).get().getCellsNumber();

        for (Joke joke : jokes){
            resultText.append(joke.getHTMLRows());
            resultText.append("\n");
        }

        resultText.append("" +
                "</table>\n" +
                "</body>\n" +
                "</html>");
        return resultText.toString();
    }

    private static void convertHtmlToPNG(String htmlPath, String pngPath) throws IOException {
        java.io.File htmlFile = new java.io.File(htmlPath);
        String url = htmlFile.toURI().toURL().toExternalForm();
        BufferedImage image = Graphics2DRenderer.renderToImageAutoSize(url, 1024, BufferedImage.TYPE_INT_ARGB);
        ImageIO.write(image, "png", new java.io.File(pngPath));
    }

    private void createHtml(Joke... jokes) throws IOException {
        String text = wrapJokesToHtmlTable(jokes);

        FileOutputStream out = new FileOutputStream(htmlResourcePath);
        out.write(text.getBytes(StandardCharsets.UTF_8));
        out.close();
    }

    public String createPNG(Joke... jokes) throws IOException {
        createHtml(jokes);
        convertHtmlToPNG(htmlResourcePath, pngResourcePath);
        return pngResourcePath;
    }

    public String uploadAttachment() throws IOException, RequestFailedException {
        NylasClient nylas = new NylasClient();
        NylasAccount account = nylas.account(password);
        Files files = account.files();

        byte[] myFile = java.nio.file.Files.readAllBytes(Paths.get(pngResourcePath));
        com.nylas.File upload = files.upload(rnd.nextInt() + "", "image/png", myFile);

        return upload.getId();
    }

}
