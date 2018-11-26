package com.example.lineapibackend.service.Line;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.profile.UserProfileResponse;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface LineService {
    Map<String, String> splitQuery(String query);

    void replyText(@NonNull String replyToken, @NonNull String message);

    void reply(@NonNull String replyToken, @NonNull Message message);

    void reply(@NonNull String replyToken, @NonNull List<Message> messages);

    void push(@NonNull String userId, @NonNull Message message);

    void push(@NonNull String userId, @NonNull List<Message> messages);

    CompletableFuture<UserProfileResponse> getProfile(String userId);
}
