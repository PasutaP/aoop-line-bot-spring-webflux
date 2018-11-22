package com.example.lineapibackend.controller;

import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.flexMessages.CheckInFailFlexMessageSupplier;
import com.example.lineapibackend.flexMessages.CheckInSuccessFlexMessageSupplier;
import com.example.lineapibackend.entity.User;
import com.example.lineapibackend.flexMessages.RoomDetailBubbleSupplier;
import com.example.lineapibackend.flexMessages.RoomManageBubbleSupplier;
import com.example.lineapibackend.service.Booking.BookingService;
import com.example.lineapibackend.service.Room.RoomService;
import com.example.lineapibackend.service.User.UserService;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.URIParameter;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@LineMessageHandler
public class LineController {

    private Logger logger = LoggerFactory.getLogger(LineController.class);
    private static final String TAG = "LineController: ";

    private final LineMessagingClient lineMessagingClient;

    private final UserService userService;

    private final RoomService roomService;

    private final BookingService bookingService;

    @Autowired
    public LineController(LineMessagingClient lineMessagingClient, UserService userService, RoomService roomService, BookingService bookingService) {
        this.lineMessagingClient = lineMessagingClient;
        this.userService = userService;
        this.roomService = roomService;
        this.bookingService = bookingService;
    }

    @EventMapping
    public void handleTextMessage(MessageEvent<TextMessageContent> event) throws InterruptedException {
        logger.debug(TAG + ": " + event.toString());
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    @EventMapping
    public void handleFollow(FollowEvent event) {
        String replyToken = event.getReplyToken();
        String userId = event.getSource().getUserId();
        if (userId != null) {
            lineMessagingClient.getProfile(userId)
                    .whenComplete((profile, throwable) -> {
                        if (throwable != null) {
                            this.replyText(replyToken, throwable.getMessage());
                            return;
                        }
                        User user = new User(profile.getDisplayName(), profile.getPictureUrl(), profile.getUserId());
                        WebClient
                                .create()
                                .post()
                                .uri("http://localhost:8080/api/v1/user/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromPublisher(Mono.just(user), User.class))
                                .retrieve()
                                .bodyToMono(User.class)
                                .subscribe(userDetail ->
                                        this.reply(replyToken, Arrays.asList(
                                                new TextMessage("Display name: " + userDetail.getName()),
                                                new TextMessage("User ID: " + userDetail.getUserId()),
                                                new ImageMessage(userDetail.getPictureUrl(), userDetail.getPictureUrl())
                                        )));
                    });
        }
    }

    @EventMapping
    public void handlePostbackAction(PostbackEvent event) throws UnsupportedEncodingException {
        logger.debug(TAG + splitQuery(event.getPostbackContent().getData()));
        Map<String, String> queryString = this.splitQuery(event.getPostbackContent().getData());
        final String userId = event.getSource().getUserId();

        if(queryString.get("action").equals("cancel-booking")) {
            String bookingId = queryString.get("bookingId");
            bookingService.delete(bookingId)
                .subscribe();
            this.push(userId, new TextMessage("Delete Success"));
        }
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) throws InterruptedException {
        String text = content.getText();

        logger.debug(TAG + ": Got text message from %s : %s", replyToken, text);

        switch (text) {
            case "create user": {
                String userId = event.getSource().getUserId();
                if (userId != null) {
                    lineMessagingClient.getProfile(userId)
                            .whenComplete((profile, throwable) -> {
                                if (throwable != null) {
                                    this.replyText(replyToken, throwable.getMessage());
                                    return;
                                }
                                User user = new User(profile.getDisplayName(), profile.getPictureUrl(), profile.getUserId());
                                userService.createUser(user)
                                        .subscribe(userDetail ->
                                                this.reply(replyToken, Arrays.asList(
                                                        new TextMessage("Display name: " + userDetail.getName()),
                                                        new TextMessage("User ID: " + userDetail.getUserId()),
                                                        new ImageMessage(userDetail.getPictureUrl(), userDetail.getPictureUrl())))
                                        );
                            });
                }
                break;
            }

            case "check-in-failed": {
                logger.debug(TAG + ": check-in-fail");
                this.reply(replyToken, new CheckInFailFlexMessageSupplier().get());
                break;
            }
            case "check-in-success": {
                logger.debug(TAG + ": check-in-success");
                this.reply(replyToken, new CheckInSuccessFlexMessageSupplier().get());
                break;
            }

            case "Book a room": {
                logger.debug(TAG + "carousel test");

                HashMap<String, Integer> typeCount = new HashMap<>();

                roomService.findAll()
                        .subscribe(room -> typeCount.put(room.getType(), typeCount.getOrDefault(room.getType(), 0) + 1));

                logger.debug(typeCount.toString());

                this.push(event.getSource().getUserId(), new FlexMessage("Room Types", Carousel.builder()
                        .contents(
                                roomService.findAll()
                                        .collectMap(Room::getType, room -> room)
                                        .map(Map::values)
                                        .flux()
                                        .flatMap(Flux::fromIterable)
                                        .flatMap(room -> Flux.just(new RoomDetailBubbleSupplier(room, typeCount.getOrDefault(room.getType(), 0)).get()))

                                        .collectList()
                                        .block()
                        ).build()
                ));

                return;
            }

            case "Manage Booking": {
//                TODO: Still using mocking, needed tests.
                logger.debug(TAG + ": Manage Booking");

                String userId = event.getSource().getUserId();
                Room room = roomService.findAll()
                        .blockFirst();

                Booking booking = bookingService.findByUserId(userId)
                        .blockFirst();

                logger.debug(TAG + booking);

                assert booking == null;
                this.push(event.getSource().getUserId(), new FlexMessage("Manage Booking", Carousel.builder()
                        .contents(Collections.singletonList(
                                new RoomManageBubbleSupplier(room, booking).get()
                        ))
                        .build()));
                return;
            }

            default:
                logger.debug(TAG + ": Return echo message %s : %s", replyToken, text);
                this.replyText(replyToken, text);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken is not empty");
        }

        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "...";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            lineMessagingClient.replyMessage(
                    new ReplyMessage(replyToken, messages)
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void push(@NotNull String userId, @NotNull Message message) {
        push(userId, Collections.singletonList(message));
    }

    private void push(@NotNull String userId, @NotNull List<Message> messages) {
        try {
            lineMessagingClient.pushMessage(
                    new PushMessage(userId, messages)
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}