package com.llt.hope.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", System.getenv("CLOUDINARY_CLOUD_NAME"));
        config.put("api_key", System.getenv("CLOUDINARY_API_KEY"));
        config.put("api_secret", System.getenv("CLOUDINARY_API_SECRET"));

        return new Cloudinary(config);
    }
}
