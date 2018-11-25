package com.example.lineapibackend.controller;

import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.entity.roomDetail.RoomDetailFactory;
import com.example.lineapibackend.flexMessages.*;
import com.example.lineapibackend.entity.User;
import com.example.lineapibackend.flexMessages.RoomManagingDetail.RoomManagingDetailBubbleSupplier;
import com.example.lineapibackend.quickReply.DatetimePickerSupplier;
import com.example.lineapibackend.service.Booking.BookingService;
import com.example.lineapibackend.service.Room.RoomService;
import com.example.lineapibackend.service.User.UserService;
import com.example.lineapibackend.utils.RoomTypes;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
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
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private HashMap<String, HashMap<String, String>> bookingDateCache = new HashMap<>();

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
    public void handlePostbackAction(PostbackEvent event) throws UnsupportedEncodingException, ParseException {
        logger.debug(TAG + splitQuery(event.getPostbackContent().getData()));
        logger.debug(TAG + event.getPostbackContent().getParams());

        Map<String, String> queryString = this.splitQuery(event.getPostbackContent().getData());
        final String userId = event.getSource().getUserId();
        String action = queryString.get("action");


        switch (action) {

            case "perform-check-in": {
                String bookingId = queryString.get("bookingId");
                this.push(userId, new CheckInSuccessFlexMessageSupplier().get());

                userService.findById(userId)
                        .flatMap(user -> {
                            user.setCheckInStatus(true);
                            return Mono.just(user);
                        })
                        .flatMap(user -> userService.updateUser(user, userId))
                        .flatMap(user -> {
                            logger.debug(TAG + user);
                            return Mono.just("Done");
                        })
                        .subscribe();

                bookingService.findByUserId(userId)
                        .flatMap(booking -> {
                            booking.setCheckInStatus(true);
                            logger.debug(TAG + booking);
                            return bookingService.updateBooking(booking, booking.getId());
                        })
                        .subscribe(newBooking -> logger.debug(TAG + "NEW_BOOKING: " + newBooking));


                logger.debug(TAG + "CHECK_IN_DEBUG: " + "Success");
                return;
            }

//            DONE
            case "cancel-booking": {
                String bookingId = queryString.get("bookingId");
                bookingService.delete(bookingId)
                        .subscribe();
                this.push(userId, new CancellationSuccessFlexMessageSupplier().get());
                break;
            }
//            DONE
            case "submit-check-in-date": {
                String date = event.getPostbackContent().getParams().get("date");
                HashMap<String, String> checkInDate = new HashMap<>();
                checkInDate.put("check-in-date", date);
                this.bookingDateCache.put(userId, checkInDate);
                this.push(userId, new TextMessage("Check-in: " + date));
                this.push(userId, new DatetimePickerSupplier().getCheckOutQuickReply());
                return;
            }

            case "submit-check-out-date": {
                String date = event.getPostbackContent().getParams().get("date");
                this.push(userId, new TextMessage("Check-out: " + date));
                this.bookingDateCache.get(userId).put("check-out-date", date);

                this.push(userId, new TextMessage("Loading Room List ..."));

                HashMap<String, Integer> typeCount = new HashMap<>();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = dateFormat.parse(bookingDateCache.get(userId).get("check-in-date"));
                Date endDate = dateFormat.parse(bookingDateCache.get(userId).get("check-out-date"));


//                TODO: Bug fixing needed.
                List<Room> roomBookedInRange = bookingService.findAll()
                        .filter(booking -> !(booking.getCheckInDate().before(endDate) && booking.getCheckOutDate().before(startDate)))
                        .flatMap(booking -> Flux.just(booking.getBookedRoom()))
                        .collectList()
                        .block();

                roomService.findRoomsNotInRoomList(roomBookedInRange)
                        .flatMap(room -> {
                            typeCount.put(room.getType(), typeCount.getOrDefault(room.getType(), 0) + 1);
                            return Flux.just(room);
                        })
                        .blockLast();

//                DONE: Display FlexMessages from availble room(s)
                Flux.fromArray(RoomTypes.values())
                        .flatMap(roomTypes -> Flux.just(RoomDetailFactory.getRoomDetail(roomTypes.toString())))
                        .flatMap(roomDetail -> Flux.just(new RoomDetailBubbleSupplier(roomDetail, typeCount.getOrDefault(roomDetail.getRoomType(), 0)).get()))
                        .collectList()
                        .subscribe(roomBubbleList -> {
                            if (!roomBubbleList.isEmpty()) {
                                this.push(event.getSource().getUserId(),
                                        new FlexMessage("Room List",
                                                Carousel
                                                        .builder()
                                                        .contents(roomBubbleList)
                                                        .build()
                                        ));
                            } else {

//                                TODO: NoRoomAvailable FlexMessage
                                this.push(event.getSource().getUserId(), new NoBookingFlexMessageSupplier().get());
                            }
                        });
                return;
            }
            case "reserve": {
                logger.debug(TAG + "RESERVE: " + event);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = dateFormat.parse(bookingDateCache.get(userId).get("check-in-date"));
                Date endDate = dateFormat.parse(bookingDateCache.get(userId).get("check-out-date"));
//                Booking newBooking = new Booking(startDate, endDate, )
                Room room = roomService.findFirstByType(queryString.get("roomType")).block();

                if (room != null) {
                    logger.debug(TAG + "ROOMTYPE_DEBUG: " + room);
                    bookingService.createBooking(new Booking(startDate, endDate, room, userId))
                            .subscribe(booking -> this.push(userId, new SummaryFlexMessageSupplier(booking).get()));
                }

                break;
            }
            default:
                this.push(userId, new TextMessage("This the end"));
        }


    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) throws InterruptedException {
        String text = content.getText();
        String userId = event.getSource().getUserId();

        logger.debug(TAG + ": Got text message from %s : %s", replyToken, text);

        switch (text) {

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

            case "Check In": {
                logger.debug(TAG + "CHECK_IN_DEBUG");
                this.replyText(replyToken, "Loading your bookings ...");

                GregorianCalendar lowerBound = new GregorianCalendar();
                lowerBound.set(Calendar.HOUR, 0);
                lowerBound.set(Calendar.SECOND, 0);
                lowerBound.add(Calendar.DATE, -1);

                GregorianCalendar upperBound = new GregorianCalendar();
                upperBound.set(Calendar.HOUR, 0);
                upperBound.set(Calendar.SECOND, 0);
                upperBound.add(Calendar.DATE, 1);

                bookingService.findByUserId(userId)
                        .filter(booking -> booking.getCheckInDate().after(lowerBound.getTime()) && booking.getCheckInDate().before(upperBound.getTime()))
                        .flatMap(booking -> Flux.just(new CheckInManagingDetailBubbleSupplier(booking).get()))
                        .collectList()
                        .subscribe(bookingList -> {
                            if (!bookingList.isEmpty()) {
                                this.push(userId,
                                        new FlexMessage("Booking List fo check-in",
                                                Carousel
                                                        .builder()
                                                        .contents(bookingList)
                                                        .build()));

                            } else {
                                this.push(userId, new NoBookingFlexMessageSupplier().get());
                            }
                        });
                return;
            }

            case "Check Out": {
                logger.debug(TAG + "CHECK_OUT_DEBUG: ");

                this.replyText(replyToken, "Performing check out ...");

                User currentUser = userService.findById(userId).block();
                if (currentUser != null && !currentUser.isCheckInStatus()) {
                    this.push(userId, new TextMessage("Something went wrong !!"));
                    return;
                }


                bookingService.findByUserId(userId)
                        .filter(Booking::getCheckInStatus)
                        .subscribe(booking -> {
                            logger.debug("ACTIVE_BOOKINGS: " + booking);
                            if (booking != null) {
                                bookingService.delete(booking.getId()).subscribe();
                            } else {
                                this.push(userId, new CheckInFailFlexMessageSupplier().get());
                            }
                        });


                userService.findById(userId)
                        .subscribe(user -> {
                            user.setCheckInStatus(false);
                            userService.updateUser(user, userId)
                                    .subscribe();
                        });

                this.push(userId, new TextMessage("Check out success"));
                logger.debug(TAG + "CHECK_OUT_DEBUG: " + "SUCCESS");
                return;
            }

            case "Book a room": {
                logger.debug(TAG + ": Book a room");

                this.push(event.getSource().getUserId(), new DatetimePickerSupplier().getCheckInQuickReply());
                return;
            }

//            DONE: Manage Booking
            case "Manage Booking": {
                logger.debug(TAG + ": Manage Booking");

                this.replyText(replyToken, "Loading your booking list ...");
//                String userId = event.getSource().getUserId();

                bookingService.findByUserId(userId)
                        .flatMap(booking -> Flux.just(new RoomManagingDetailBubbleSupplier(booking).get()))
                        .collectList()
                        .subscribe(bookingList -> {
                            if (!bookingList.isEmpty()) {
                                this.push(userId,
                                        new FlexMessage("Manage Booking",
                                                Carousel
                                                        .builder()
                                                        .contents(bookingList)
                                                        .build()));

                            } else {
                                this.push(userId, new NoBookingFlexMessageSupplier().get());
                            }
                        });
                return;
            }
//            TODO: Default message specification
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