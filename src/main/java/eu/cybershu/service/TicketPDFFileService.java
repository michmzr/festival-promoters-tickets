package eu.cybershu.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import eu.cybershu.service.dto.TicketPDFData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Slf4j
@Service
public class TicketPDFFileService {
    private final String templatesDir;
    private final String templateFileName;

    public TicketPDFFileService(
        @Value("${application.tickets.templateDir:/templates/ticket}")
            String templatesDir,
        @Value("${application.tickets.templateFileName:ticket-pdf-template}")
            String templateFileName) {
        this.templatesDir = templatesDir;
        this.templateFileName = templateFileName;
    }

    private static String encodeBytesToBase64(byte[] fileContent) {
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public byte[] generateTicketPDFFile(TicketPDFData ticketPDFData) throws DocumentException, IOException {
        log.info("Generating ticket from data....");

        Map<String, Object> fileTicketData = new HashMap<>();

        String qrPath = "data:"
            + ticketPDFData.getQrFileContentType()
            + ";base64," + encodeBytesToBase64(ticketPDFData.getQrFile());
        fileTicketData.put("qrPath", qrPath);
        fileTicketData.put("uuid", ticketPDFData.getUuid());
        fileTicketData.put("typeDescription", ticketPDFData.getTicketType().getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.GERMAN)
            .withZone(ZoneId.systemDefault());
        Instant instant = Instant.now();
        String issueDate = formatter.format(instant);
        fileTicketData.put("date", issueDate);

        var fileData = Map.of(
            "qrPath", qrPath,
            "ticket", fileTicketData,
            "guest", ticketPDFData.getGuest(),
            "promotor", ObjectUtils.defaultIfNull(ticketPDFData.getPromotor(), emptyMap()),
            "ticketType", ObjectUtils.defaultIfNull(ticketPDFData.getTicketType(), emptyMap())
        );

        return exportPdfFileToBytes(templatesDir, templateFileName, fileData);
    }

    public byte[] exportPdfFileToBytes(String templateDir,
                                       String templateFileName, Map<String, Object> data)
        throws DocumentException, IOException {
        String htmlContent = generateHtml(templateDir, templateFileName, data);

        ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont(
            "fonts/arial-unicode-ms.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(fileOutputStream, false);
        renderer.finishPDF();
        return fileOutputStream.toByteArray();
    }

    public String generateHtml(String templateDir, String templateFileName, Map<String, Object> data) {
        TemplateEngine templateEngine = createTemplateEngine(templateDir);
        Context context = new Context();
        context.setVariables(data);
        return templateEngine.process(templateFileName, context);
    }

    private TemplateEngine createTemplateEngine(String templateDir) {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix(templateDir);
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(1);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }
}
