package eu.cybershu.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.util.HashMap;

@Service
public class QRCodeService {
    public static final int QR_WIDTH = 400;
    public static final int QR_HEIGHT = 400;

    public BufferedImage generateQRCode(String barcodeText) throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public String readQrText(BufferedImage bufferedImage) throws NotFoundException {
        var hints = new HashMap<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, Charset.defaultCharset().toString());

        BinaryBitmap binaryMap=new BinaryBitmap(new GlobalHistogramBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        Result result = new MultiFormatReader().decode(binaryMap, hints);
        return result.getText();
    }
}
