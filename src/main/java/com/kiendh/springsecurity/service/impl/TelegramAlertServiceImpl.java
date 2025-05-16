package com.kiendh.springsecurity.service.impl;

import com.kiendh.springsecurity.dto.enums.AlertType;
import com.kiendh.springsecurity.dto.response.AlertDto;
import com.kiendh.springsecurity.service.TelegramAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelegramAlertServiceImpl implements TelegramAlertService {

    @Qualifier("telegramRestTemplate")
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    public static final String TELEGRAM_SEND_MESSAGE_API = "https://api.telegram.org/bot{botToken}/sendMessage";
    public static final String TELEGRAM_GET_UPDATES_API = "https://api.telegram.org/bot{botToken}/getUpdates";

    @Value("${alert.telegram.bot-token}")
    private String botToken;

    private final Map<String, LocalDateTime> LAST_SEND_TIME = new ConcurrentHashMap<>();
    private static final String KEY_CHAT = "TELEGRAM_ALERT_CHAT_IDS";
    private static final String KEY_LAST_OFFSET = "TELEGRAM_ALERT_LAST_OFFSET";

    @Override
    @Async
    public void sendMessage(AlertDto alert) {
        if (AlertType.INFO.equals(alert.getAlertType())) {
            return;
        }

        if (LAST_SEND_TIME.containsKey(alert.getTitle()) && LAST_SEND_TIME.get(alert.getTitle()) != null
            && LAST_SEND_TIME.get(alert.getTitle()).plusSeconds(60).isAfter(LocalDateTime.now())) {
            return;
        }

        String chatIds = redisTemplate.opsForValue().get(KEY_CHAT);

        if (chatIds == null || chatIds.isEmpty()) {
            return;
        }

        for (String chatId : chatIds.split(";")) {
            if (chatId.isEmpty()) {
                continue;
            }
            Map<String, Object> body = new HashMap<>();
            body.put("text", String.format("""
                            <b>### %s ###</b>
                            <b>%s</b>
                            <b>Thời gian:</b> %s
                            <b>TraceId:</b> <code>%s</code>
                            <b>Mã khách hàng:</b> <code>%s</code>
                            <b>Tên khách hàng:</b> <code>%s</code>
                            <b>Nội dung:</b>
                            <pre><code>%s</code></pre>
                            """,
                    alert.getAlertType(), alert.getTitle(), alert.getTimestamp(), alert.getTraceId(),
                    alert.getUsername(), alert.getFullName(), alert.getContent().trim())
            );

            body.put("chat_id", chatId);
            body.put("parse_mode", "html");
            restTemplate.postForEntity(TELEGRAM_SEND_MESSAGE_API, body, String.class, botToken);
        }

        LAST_SEND_TIME.put(alert.getTitle(), LocalDateTime.now());
    }

    @Override
//    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void reloadReceivers() {
        String lastOffset = redisTemplate.opsForValue().get(KEY_LAST_OFFSET);
        String chatIdString = redisTemplate.opsForValue().get(KEY_CHAT);
        Set<String> chatIds = StringUtils.hasText(chatIdString) ?
                Arrays.stream(chatIdString.split(";")).collect(Collectors.toSet()) :
                new HashSet<>();

        URI uri = UriComponentsBuilder.fromUriString(TELEGRAM_GET_UPDATES_API)
                .queryParam("offset", lastOffset).build(botToken);

        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);

        if (response != null && Boolean.TRUE.equals(response.get("ok"))) {
            var results = (List<Map<String, Object>>) response.get("result");

            for (Map<String, Object> update : results) {
                long updateId = ((Number) update.get("update_id")).longValue();
                var message = (Map<String, Object>) update.get("message");
                if (message != null) {
                    var chat = (Map<String, Object>) message.get("chat");
                    if (chat != null) {
                        String chatId = String.valueOf(chat.get("id"));
                        chatIds.add(chatId);
                    }
                }
                redisTemplate.opsForValue().set(KEY_LAST_OFFSET, String.valueOf(updateId + 1));
            }
        }

        redisTemplate.opsForValue().set(KEY_CHAT, String.join(";", chatIds));
    }

    /** Response của api getUpdates telegram
     * {
     *     "ok": true,
     *     "result": [
     *         {
     *             "update_id": 687887332,
     *             "message": {
     *                 "message_id": 20,
     *                 "from": {
     *                     "id": 5926018037,
     *                     "is_bot": false,
     *                     "first_name": "Đỗ Hồng",
     *                     "last_name": "Kiên",
     *                     "username": "kiendooo",
     *                     "language_code": "en"
     *                 },
     *                 "chat": {
     *                     "id": 5926018037,
     *                     "first_name": "Đỗ Hồng",
     *                     "last_name": "Kiên",
     *                     "username": "kiendooo",
     *                     "type": "private"
     *                 },
     *                 "date": 1746520528,
     *                 "text": "/start",
     *                 "entities": [
     *                     {
     *                         "offset": 0,
     *                         "length": 6,
     *                         "type": "bot_command"
     *                     }
     *                 ]
     *             }
     *         }
     *     ]
     * }
     **/
}
