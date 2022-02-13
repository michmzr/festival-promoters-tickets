package eu.cybershu.service;

import com.google.zxing.WriterException;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.*;
import com.google.zxing.common.*;
import com.google.zxing.client.j2se.*;

import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.util.HashMap;
import javax.imageio.ImageIO
;
@Service
public class QRCodeService {
    public BufferedImage generateQRCode(String barcodeText) throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

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
