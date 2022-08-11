package com.mailsender.utils;

import com.mailsender.utils.service_handler.ServiceContent;
import com.mailsender.service.ServiceContentGenerator;
import com.nylas.Files;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RequestFailedException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.xhtmlrenderer.simple.Graphics2DRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Random;


public class MessageImageCreator {
    public static Random rnd = new Random();

    @Autowired
    private ServiceContentGenerator serviceContentGenerator;

    @Value("${password}")
    private String password;

    private String generatedResourcePath = new StringBuffer()
            .append("src" + File.separator)
            .append("main" + File.separator)
            .append("resources"+ File.separator)
            .append("generated_resources"+ File.separator).toString();


    private String wrapServicesContentToHtmlTable(ServiceContent... serviceContents) throws Exception {
        StringBuffer resultText = new StringBuffer();
        resultText.append("" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"></meta>\n" +
                "    <style>\n" +
                "\n" +
                "        th, td {\n" +
                "            border: 1px solid #000000;\n" +
                "            border-collapse: collapse;\n" +
                "        }" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div style=\"text-align: center\">"
        );

        for (int i = 0; i < serviceContents.length; i++) {
            ServiceContent serviceContent = serviceContents[i];
            resultText.append("<div>\n");
            resultText.append(serviceContent.getHTMLRows());
            resultText.append("</div>\n");
        }

        resultText.append("" +
                "</div>" +
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

    private void createHtml(String htmlPath, ServiceContent... serviceContents) throws Exception {
        try {
            String text = wrapServicesContentToHtmlTable(serviceContents);

            File htmlResourceFile = new File(htmlPath);
            htmlResourceFile.createNewFile();

            FileOutputStream out = new FileOutputStream(htmlResourceFile);
            out.write(text.getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (Exception e) {
            System.out.println("Can not create HTML");
            throw e;
        }
    }

    public String uploadImageContent() throws Exception {
        long threadId = Thread.currentThread().getId();
        String htmlPath = generatedResourcePath + threadId + "_content.html";
        String pngPath = generatedResourcePath + threadId + "content.png";

        int attempts = 5;
        int attemptsCount = 0;
        while (true) {
            try {
                ServiceContent translatedServiceContent = serviceContentGenerator.getTranslatedJoke();
                ServiceContent russianServiceContent = serviceContentGenerator.getRussianJoke();
                ServiceContent weatherServiceContent = serviceContentGenerator.getFiveDayWeatherData();

                File generatedResourcesPath = new File(generatedResourcePath);
                if (!generatedResourcesPath.exists()){
                    generatedResourcesPath.mkdir();
                }
                createHtml(htmlPath, translatedServiceContent, russianServiceContent, weatherServiceContent);
                convertHtmlToPNG(htmlPath, pngPath);
                break;
            } catch (Exception e) {
                attemptsCount++;
                System.out.println("Can not create PNG. Trying again...(" + attemptsCount + ")");
                if (attemptsCount > attempts) {
                    throw e;
                }
            }
        }
        String uploadedImageId = uploadAttachment(pngPath);
        new File(htmlPath).delete();
        new File(pngPath).delete();

        return uploadedImageId;
    }

    public String uploadAttachment(String pngResourcePath) throws IOException, RequestFailedException {
        NylasClient nylas = new NylasClient();
        NylasAccount account = nylas.account(password);
        Files files = account.files();

        byte[] myFile = java.nio.file.Files.readAllBytes(Paths.get(pngResourcePath));
        int imageName = rnd.nextInt();
        if (imageName < 0) {
            imageName *= -1;
        }
        com.nylas.File upload = files.upload("Уведомление №" + imageName + ".png", "image/png", myFile);

        return upload.getId();
    }

    public static InputStream createGetRequest(String endpoint) throws IOException, ParseException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(endpoint);
        HttpResponse httpresponse = httpclient.execute(httpget);
        return httpresponse.getEntity().getContent();
    }

}
