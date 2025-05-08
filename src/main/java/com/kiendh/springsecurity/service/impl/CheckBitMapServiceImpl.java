package com.kiendh.springsecurity.service.impl;

import com.kiendh.springsecurity.service.CheckBitMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.zip.CRC32;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckBitMapServiceImpl implements CheckBitMapService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String EMAIL_BITMAP_KEY = "user_email";
    private static final String USERNAME_BITMAP_KEY = "user_username";

    @Override
    public boolean isEmailExists(String email) {
        CRC32 crc32 = new CRC32();
        crc32.update(email.getBytes());
        long hash = crc32.getValue();

        log.info("Hash value for email {}: {}", email, hash);
        return Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(EMAIL_BITMAP_KEY, hash));
    }

    @Override
    public void addEmail(String email) {
        CRC32 crc32 = new CRC32();
        crc32.update(email.getBytes());
        long hash = crc32.getValue();

        log.info("Adding email {} with hash value: {}", email, hash);
        redisTemplate.opsForValue().setBit(EMAIL_BITMAP_KEY, hash, true);

        Long countOfEmailExists = redisTemplate.opsForValue().size(EMAIL_BITMAP_KEY);
        log.info("Count of email exists: {}", countOfEmailExists);
    }

    @Override
    public boolean isUsernameExists(String username) {
        CRC32 crc32 = new CRC32();
        crc32.update(username.getBytes());

        long hash = crc32.getValue();
        return Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(USERNAME_BITMAP_KEY, hash));
    }

    @Override
    public void addUsername(String username) {
        CRC32 crc32 = new CRC32();
        crc32.update(username.getBytes());
        long hash = crc32.getValue();

        log.info("Adding username {} with hash value: {}", username, hash);
        redisTemplate.opsForValue().setBit(USERNAME_BITMAP_KEY, hash, true);
    }
}
