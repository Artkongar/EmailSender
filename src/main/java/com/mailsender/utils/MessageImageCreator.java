package com.mailsender.utils;

import com.mailsender.data.TranslatedJoke;
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
import java.util.Random;
import java.util.stream.Collectors;


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

    private String wrapTranslatedJoke(TranslatedJoke translatedJoke, String russianJoke) {
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
        if (translatedJoke.isHasPunchline()) {
            resultText.append("" +
                    "<tr>\n" +
                    "        <td/>\n" +
                    "        <td class=\"lineLeft\">\n" +
                    "            <div style=\"color: #076b91; font-size: 80%\">\n" +
                    translatedJoke.getSubject() +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td class=\"lineLeft\">\n" +
                    "            <div style=\"color: #076b91; font-size: 80%\">\n" +
                    translatedJoke.getSubjectRu() +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td class=\"lineUp\">\n" +
                    "            <div style=\"color: #076b91; font-size: 80%\">\n" +
                    "                Затравка\n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td class=\"lineUp lineLeft\">\n" +
                    "            <div style=\"color: #1896FF;\">\n" +
                    translatedJoke.getSetup() +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td class=\"lineLeft lineUp\">\n" +
                    "            <div style=\"color: #DA58A3;\">\n" +
                    translatedJoke.getSetupRu() +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td class=\"lineUp\">\n" +
                    "            <div style=\"color: #076b91; font-size: 80%\">\n" +
                    "                Панчлайн\n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td class=\"lineLeft lineUp\">\n" +
                    "            <div style=\"color: #58DAC8;\">\n" +
                    translatedJoke.getPunchline() +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td class=\"lineLeft lineUp\">\n" +
                    "            <div style=\"color: #58DAC8;\">\n" +
                    translatedJoke.getPunchlineRu() +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "</tr>"
            );
        } else {
            resultText.append("" +
                    "<tr>\n" +
                    "    <td class=\"lineUp\"\n" +
                    "        <div style=\"color: #076b91;\">\n" +
                    translatedJoke.getJoke() +
                    "            \n" +
                    "        </div>\n" +
                    "    </td>\n" +
                    "    <td class=\"lineUp\"\n" +
                    "        <div style=\"color: #076b91;\">\n" +
                    translatedJoke.getJokeRu() +
                    "            \n" +
                    "        </div>\n" +
                    "    </td>\n" +
                    "</tr>");
        }
        resultText.append("" +
                "<tr>\n" +
                "    <td class=\"lineUp\" colspan=\"3\">\n" +
                "        <div style=\"color: #076b91;\">\n" +
                        russianJoke +
                "            \n" +
                "        </div>\n" +
                "    </td>\n" +
                "</tr>");
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

    private void createHtml(TranslatedJoke translatedJoke, String russianJoke) throws IOException {
        String text = wrapTranslatedJoke(translatedJoke, russianJoke);

        FileOutputStream out = new FileOutputStream(htmlResourcePath);
        out.write(text.getBytes(StandardCharsets.UTF_8));
        out.close();
    }

    public String createPNG(TranslatedJoke translatedJoke, String russianJoke) throws IOException {
        createHtml(translatedJoke, russianJoke);
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
