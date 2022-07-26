package com.mailsender.utils;

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

    private static void convertHtmlToPNG(String htmlPath, String pngPath) throws IOException {
        java.io.File htmlFile = new java.io.File(htmlPath);
        String url = htmlFile.toURI().toURL().toExternalForm();
        BufferedImage image = Graphics2DRenderer.renderToImageAutoSize(url, 1024, BufferedImage.TYPE_INT_ARGB);
        ImageIO.write(image, "png", new java.io.File(pngPath));
    }

    private void createHtml(String... args) throws IOException {
        java.io.File htmlTemplatePath = new java.io.File(this.htmlTemplatePath);
        FileInputStream in = new FileInputStream(htmlTemplatePath);
        String text = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        text = String.format(text, args);
        in.close();

        FileOutputStream out = new FileOutputStream(htmlResourcePath);
        out.write(text.getBytes(StandardCharsets.UTF_8));
        out.close();
    }

    public String createPNG(String... args) throws IOException {
        createHtml(args);
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
