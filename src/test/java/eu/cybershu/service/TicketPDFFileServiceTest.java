package eu.cybershu.service;

import eu.cybershu.domain.Guest;
import eu.cybershu.domain.TicketType;
import eu.cybershu.service.dto.TicketPDFData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class TicketPDFFileServiceTest {
    private final String templateDir = "/templates/ticket/";
    private final String templateFileName = "ticket-pdf-template";

    TicketPDFFileService ticketPDFFileService = new TicketPDFFileService(
        templateDir, templateFileName);

    @Test
    @SneakyThrows
    void givenDataExpectGeneratedPDFFile(@TempDir File tempDir) {
        //given
        TicketPDFData pdfData = TicketPDFData
            .builder()
            .uuid(UUID.randomUUID().toString())
            .qrFile(qrFile())
            .qrFileContentType("image/png")
            .ticketType(ticket4Days())
            .guest(guest())
            .ticketType(ticket4Days())
            .build();

        //when
        var ticketBytes = ticketPDFFileService.generateTicketPDFFile(pdfData);

        //then
        String outputFile = tempDir.getAbsolutePath() + "files/ticket_generated.pdf";
        File file = new File(outputFile);
        FileUtils.writeByteArrayToFile(
            file, ticketBytes);

        assertThat(file).isFile();
    }

    private Guest guest() {
        return Guest.builder()
            .name("Janusz")
            .lastName("Nosacz")
            .email("najlepsy1963@janusz.pl")
            .build();
    }

    private byte[] qrFile() throws IOException {
        var resource = new ClassPathResource("data/tickets/qr.png");
        FileInputStream fileInputStream = new FileInputStream(resource.getFile());
        return fileInputStream.readAllBytes();
    }

    private TicketType ticket4Days() {
        var ticketType = new TicketType();
        ticketType.setId(2L);
        ticketType.setName("Karnet 4 dniowy");
        ticketType.setProductId("345");
        ticketType.setProductUrl("http://organic/bilet-4-dni.html");
        return ticketType;
    }

}
