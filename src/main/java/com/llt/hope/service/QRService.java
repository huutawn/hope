package com.llt.hope.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class QRService {
    String baseQR="0002010102111531397007040052044600000000628391638550010A000000727012500069704230111000062839160208QRIBFTTA5204513753037045802VN5914NGUYEN HUU TAN6006Ha Noi8707CLASSIC6304B0BD";

    public String generateBankQrFile( String amount, String content,  String fileName)
            throws IOException, WriterException {

        String qrPayload = generateBankQr(baseQR, amount, content);

        // Tạo folder nếu chưa tồn tại
        Path dirPath = Path.of("D:\\qr");
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // Đường dẫn file
        File outputFile = dirPath.resolve(fileName+".png").toFile();

        // Sinh ảnh QR
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrPayload, BarcodeFormat.QR_CODE, 300, 300);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", outputFile.toPath());

        return "success";
    }

    public String generateBankQr(String accountInfo, String amount, String content) {
        StringBuilder payload = new StringBuilder();

        // 00 - Version
        payload.append("00").append("02").append("01");
        // 01 - Method
        payload.append("01").append("02").append("11");
        // 26 - Merchant Account Information (accountInfo là toàn bộ tag 26 hoặc 38)
        payload.append(accountInfo);
        // 52 - Merchant Category Code (default 0000)
        payload.append("52").append("04").append("0000");
        // 53 - Currency (VND = 704)
        payload.append("53").append("03").append("704");
        // 58 - Country Code
        payload.append("58").append("02").append("VN");
        // 59 - Merchant Name
        payload.append("59").append(String.format("%02d", "NGUYEN HUU TAN".length()))
                .append("NGUYEN HUU TAN");
        // 60 - Merchant City
        payload.append("60").append(String.format("%02d", "Ha Noi".length()))
                .append("Ha Noi");

        // 54 - Transaction Amount
        if (amount != null && !amount.isEmpty()) {
            payload.append("54").append(String.format("%02d", amount.length()))
                    .append(amount);
        }

        // 62 - Additional Data Field
        if (content != null && !content.isEmpty()) {
            String contentField = "08" + String.format("%02d", content.length()) + content;
            payload.append("62").append(String.format("%02d", contentField.length()))
                    .append(contentField);
        }

        // 63 - CRC
        String payloadWithoutCRC = payload.toString() + "63" + "04";
        String crc = String.format("%04X", crc16(payloadWithoutCRC.getBytes(StandardCharsets.UTF_8)));

        return payloadWithoutCRC + crc;
    }

    private int crc16(byte[] bytes) {
        int crc = 0xFFFF;
        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
                crc &= 0xFFFF;
            }
        }
        return crc & 0xFFFF;
    }

}
