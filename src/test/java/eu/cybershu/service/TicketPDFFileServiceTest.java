package eu.cybershu.service;

import eu.cybershu.domain.Guest;
import eu.cybershu.domain.Promotor;
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

    private TicketPDFFileService ticketPDFFileService = new TicketPDFFileService(
        templateDir, templateFileName);

    @Test
    @SneakyThrows
    void givenArtistDataExpectGeneratedPDFFile(@TempDir File tempDir) {
        //given
        TicketPDFData pdfData = TicketPDFData
            .builder()
            .uuid(UUID.randomUUID().toString())
            .qrFile(qrFile())
            .qrFileContentType("image/png")
            .ticketType(ticket4Days())
            .guest(guest())
            .ticketType(ticket4Days())
            .artistName("Szure i Bure")
            .build();

        //when
        var ticketBytes = ticketPDFFileService.generateTicketPDFFile(pdfData);

        //then
        String outputFile = tempDir.getAbsolutePath() + "files/ticket_generated_artist.pdf";
        File file = new File(outputFile);
        FileUtils.writeByteArrayToFile(file, ticketBytes);

        assertThat(file).isFile();
    }

    @Test
    @SneakyThrows
    void givenPromotorExpectGeneratedPDFFile(@TempDir File tempDir) {
        //given
        TicketPDFData pdfData = TicketPDFData
            .builder()
            .uuid(UUID.randomUUID().toString())
            .qrFile(qrFile())
            .qrFileContentType("image/png")
            .ticketType(ticket4Days())
            .guest(guest())
            .ticketType(ticket4Days())
            .promotor(promotor())
            .artistName(null)
            .build();

        //when
        var ticketBytes = ticketPDFFileService.generateTicketPDFFile(pdfData);

        //then
        String outputFile = tempDir.getAbsolutePath() + "files/ticket_generated_promotor.pdf";
        File file = new File(outputFile);
        FileUtils.writeByteArrayToFile(file, ticketBytes);

        assertThat(file).isFile();
    }

    private Promotor promotor() {
        Promotor promotor = new Promotor();
        promotor.setName("Rene");
        promotor.setLastName("Kozyglusz");
        promotor.setEmail("rene@kozyglusz.fr");
        return promotor;
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
        ticketType.setName("Karnet Organic Festival Dzieci Kwiaty 2022 - 4 dni w naturze");
        ticketType.setProductId("345");
        ticketType.setProductUrl("http://organic/bilet-4-dni.html");
        return ticketType;
    }

}
