package com.llt.hope.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;
import com.llt.hope.dto.request.SePayWebhookRequest;
import com.llt.hope.dto.response.StartTransactionResponse;
import com.llt.hope.dto.response.VolunteerResponse;
import com.llt.hope.entity.*;
import com.llt.hope.exception.AppException;
import com.llt.hope.exception.ErrorCode;
import com.llt.hope.repository.jpa.*;
import com.llt.hope.utils.SecurityUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SePayWebHookService {

    TransactionRepository transactionRepository;
    FundBalanceRepository fundBalanceRepository;
    UserRepository userRepository;
    QRService qrService;
    CloudinaryService cloudinaryService;
    MediaFileRepository mediaFileRepository;
    PostVolunteerRepository postVolunteerRepository;
    SupportRepository supportRepository;

    public VolunteerResponse handleWebhook(SePayWebhookRequest webhookData) {
        // Chỉ xử lý nếu tiền vào (transferType = "in")
        if (!"in".equalsIgnoreCase(webhookData.getTransferType())) {
            log.warn("⛔ Bỏ qua giao dịch không phải tiền vào: {}", webhookData);
            return null;
        }

        String content = webhookData.getContent();
        // Regex để tìm chuỗi: hope_<userCode> hoặc hope_<userCode>_<postId>
        // - `hope_` : Chuỗi cố định
        // - `([a-zA-Z0-9]{8})` : Bắt nhóm 1 - userCode gồm 8 ký tự chữ và số
        // - `(?:_(\\d+))?` : Nhóm không bắt tùy chọn, chứa:
        // - `_` : Dấu gạch dưới
        // - `(\\d+)` : Bắt nhóm 2 - postId là một hoặc nhiều chữ số
        Pattern pattern = Pattern.compile("hope_([a-zA-Z0-9]{8})(?:_(\\d+))?");
        Matcher matcher = pattern.matcher(content);

        if (!matcher.find()) {
            log.warn("⛔ Nội dung không chứa mã 'hope' hợp lệ: {}", content);
            return null;
        }

        String userCode = matcher.group(1);
        Long postId = null;
        // group(2) là postId, có thể null nếu không có trong nội dung
        if (matcher.group(2) != null) {
            try {
                postId = Long.parseLong(matcher.group(2));
            } catch (NumberFormatException e) {
                log.warn("⛔ Không thể phân tích Post ID từ nội dung (bỏ qua postId): {}", content);
                // Nếu postId không hợp lệ, coi như là đóng góp chung
            }
        }

        User user = userRepository.findByCode(userCode).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Xử lý quyên góp cho bài đăng cụ thể
        if (postId != null) {
            PostVolunteer post = postVolunteerRepository
                    .findById(postId)
                    .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

            // Cập nhật tổng số tiền của bài đăng
            post.setTotalAmount(post.getTotalAmount().add(webhookData.getTransferAmount()));
            postVolunteerRepository.save(post);
            log.info("✅ Cập nhật số tiền cho bài đăng ID {}: {}", postId, post.getTotalAmount());

            // Tạo bản ghi ủng hộ (Support)
            Support support = Support.builder()
                    .user(user)
                    .postVolunteer(post)
                    .donatedMoney(webhookData.getTransferAmount()) // Sửa tên trường cho đúng với Entity
                    .donatedAt(LocalDateTime.now()) // Sửa tên trường cho đúng với Entity
                    .build();
            supportRepository.save(support);
            log.info("✅ Đã lưu lịch sử ủng hộ cho người dùng {} vào bài đăng {}", user.getEmail(), postId);

        } else {
            // Xử lý quyên góp chung (logic cũ)
            BigDecimal fund = user.getFund() == null ? BigDecimal.ZERO : user.getFund();
            user.setFund(fund.add(webhookData.getTransferAmount()));
            userRepository.save(user);
            log.info("✅ Cập nhật quỹ chung cho người dùng {}: {}", user.getEmail(), user.getFund());
        }

        // Tạo bản ghi giao dịch
        Transaction transaction = new Transaction();
        transaction.setTransactionId(webhookData.getId().toString());
        transaction.setAccountNumber(webhookData.getAccountNumber());
        transaction.setTransactionDate(webhookData.getTransactionDate());
        transaction.setAmount(webhookData.getTransferAmount());
        transaction.setContent(webhookData.getContent()); // Lưu nội dung gốc
        transaction.setDescription(webhookData.getContent());
        transaction.setReferenceNumber(webhookData.getReferenceCode());
        transaction.setUser(user);

        transaction = transactionRepository.save(transaction);
        log.info("✅ Giao dịch webhook đã được lưu vào database: {}", transaction.getId());

        // Cập nhật số dư quỹ chung
        FundBalance fundBalance = fundBalanceRepository.findById(1L).orElse(new FundBalance());
        fundBalance.setBalance(fundBalance.getBalance().add(transaction.getAmount()));
        fundBalance = fundBalanceRepository.save(fundBalance);
        log.info("💰 Cập nhật số dư quỹ chung: {}", fundBalance.getBalance());

        // Xóa mã code của người dùng sau khi giao dịch thành công
        user.setCode(null);
        userRepository.save(user);

        VolunteerResponse volunteerResponse = VolunteerResponse.builder()
                .id(transaction.getId())
                .accountNumber(transaction.getAccountNumber())
                .transactionDate(transaction.getTransactionDate())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .content(webhookData.getContent())
                .referenceNumber(transaction.getReferenceNumber())
                .fundBalance(fundBalance)
                .build();

        return volunteerResponse;
    }

    public BigDecimal getFundBalance() {
        FundBalance fundBalance = fundBalanceRepository.findById(1L).orElse(new FundBalance());
        return fundBalance.getBalance();
    }

    public StartTransactionResponse startTransaction(Double amount) throws IOException, WriterException {
        String email =
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String code = generateCode(8);
        user.setCode(code);
        user.setOtpExpiryDate(LocalDateTime.now());
        userRepository.save(user);
        String content = "hope" + code;

        var file = qrService.generateBankQrFile(amount + "", content, email + amount);

        String url = cloudinaryService.uploadFile(file, code, email);
        MediaFile mediaFile =
                MediaFile.builder().url(url).createdAt(LocalDateTime.now()).build();
        mediaFileRepository.save(mediaFile);
        StartTransactionResponse startTransactionResponse =
                StartTransactionResponse.builder().qr(mediaFile.getUrl()).build();
        return startTransactionResponse;
    }

    public String generateCode(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
