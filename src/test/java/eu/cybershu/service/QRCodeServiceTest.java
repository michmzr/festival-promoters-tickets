package eu.cybershu.service;

import com.google.zxing.*;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;

class QRCodeServiceTest {
    QRCodeService generatorService = new QRCodeService();

    @Test
    public void given_text_expect_generated_QR_code() throws WriterException, NotFoundException {
        //given
        String qrText = "https://organic.pl/test/qr/cde/32223?userId=5";

        //when
        BufferedImage qrImage = generatorService.generateQRCode(qrText);

        //then
        assertThat(qrImage).isNotNull();
        assertThat(generatorService.readQrText(qrImage)).isEqualTo(qrText);
    }
}
