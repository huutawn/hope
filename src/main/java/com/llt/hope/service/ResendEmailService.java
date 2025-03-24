package com.llt.hope.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ResendEmailService
{
    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_KEY = "re_UMpdYfjz_M9xnHA7tsbmzMXff4ZSFZ3pk";
    private final String RESEND_API_URL = "https://api.resend.com/emails";

    public String sendEmail(String to, String subject, String htmlContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(API_KEY); // Dùng setBearerAuth() thay vì set("Authorization", "Bearer " + API_KEY)
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> emailRequest = Map.of(
                "from", "no-reply@hopevn.site",
                "to", List.of(to),
                "subject", subject,
                "html", htmlContent
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
                RESEND_API_URL,
                new org.springframework.http.HttpEntity<>(emailRequest, headers),
                String.class
        );

        return response.getBody();
    }
}
