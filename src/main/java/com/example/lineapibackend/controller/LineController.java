package com.example.lineapibackend.controller;

import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.entity.User;
import com.example.lineapibackend.flexMessages.BubbleFactory;
import com.example.lineapibackend.quickReply.DatetimePickerSupplier;
import com.example.lineapibackend.service.Booking.BookingService;
import com.example.lineapibackend.service.Line.LineService;
import com.example.lineapibackend.service.Room.RoomService;
import com.example.lineapibackend.service.User.UserService;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@LineMessageHandler
public class LineController {

    private Logger logger = LoggerFactory.getLogger(LineController.class);
    private static final String TAG = "LineController: ";

    private final BubbleFactory bubbleFactory;

    private final LineService lineService;

    private final UserService userService;

    private final RoomService roomService;

    private final BookingService bookingService;

    private HashMap<String, HashMap<String, String>> bookingDateCache = new HashMap<>();

    @Autowired
    public LineController(UserService userService, RoomService roomService, BookingService bookingService, LineService lineService, BubbleFactory bubbleFactory) {
        this.userService = userService;
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.lineService = lineService;
        this.bubbleFactory = bubbleFactory;
    }

    @EventMapping
    public void handleTextMessage(MessageEvent<TextMessageContent> event) throws IllegalAccessException {
        logger.debug(TAG + ": " + event.toString());
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    @EventMapping
    public void handleFollow(FollowEvent event) {
        String replyToken = event.getReplyToken();
        String userId = event.getSource().getUserId();
        if (userId != null) {
            lineService.getProfile(userId)
                    .whenComplete((profile, throwable) -> {
                        if (throwable != null) {
                            lineService.replyText(replyToken, throwable.getMessage());
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
                                        lineService.reply(replyToken, Arrays.asList(
                                                new TextMessage("Display name: " + userDetail.getName()),
                                                new TextMessage("User ID: " + userDetail.getUserId()),
                                                new ImageMessage(userDetail.getPictureUrl(), userDetail.getPictureUrl())
                                        )));
                    });
        }
    }

    @EventMapping
    public void handlePostbackAction(PostbackEvent event) throws ParseException, IllegalAccessException {
        logger.debug(TAG + lineService.splitQuery(event.getPostbackContent().getData()));
        logger.debug(TAG + event.getPostbackContent().getParams());

        Map<String, String> queryString = lineService.splitQuery(event.getPostbackContent().getData());
        final String userId = event.getSource().getUserId();
        String action = queryString.get("action");


        switch (action) {

            case "perform-check-in": {
//                TODO: Edit this part
                lineService.push(userId, FlexMessage.builder()
                        .altText("Check In Success")
                        .contents(bubbleFactory.getBubble("check-in-success"))
                        .build());

                userService.findById(userId)
                        .flatMap(user -> {
                            user.setCheckInStatus(true);
                            return Mono.just(user);
                        })
                        .flatMap(user -> userService.updateUser(user, userId))
                        .subscribe();

                bookingService.findByUserId(userId)
                        .flatMap(booking -> {
                            booking.setCheckInStatus(true);
                            booking.getBookedRoom().setAvailability(false);
                            bookingService.updateBooking(booking, booking.getId());
                            return roomService.updateRoom(booking.getBookedRoom(), booking.getBookedRoom().getId());
                        })
                        .subscribe();

                return;
            }

//            DONE
            case "cancel-booking": {
                String bookingId = queryString.get("bookingId");
                bookingService.delete(bookingId).subscribe();
                lineService.push(userId, FlexMessage.builder()
                        .altText("Cancel Success")
                        .contents(bubbleFactory.getBubble("cancel-booking-success"))
                        .build());
                break;
            }
//            DONE
            case "submit-check-in-date": {
                String date = event.getPostbackContent().getParams().get("date");
                HashMap<String, String> checkInDate = new HashMap<>();
                checkInDate.put("check-in-date", date);
                this.bookingDateCache.put(userId, checkInDate);
                lineService.push(userId, new TextMessage("Check-in: " + date));
                lineService.push(userId, new DatetimePickerSupplier().getCheckOutQuickReply());
                return;
            }

            case "submit-check-out-date": {
                String date = event.getPostbackContent().getParams().get("date");
                lineService.push(userId, new TextMessage("Check-out: " + date));
                this.bookingDateCache.get(userId).put("check-out-date", date);

                lineService.push(userId, new TextMessage("Loading Room List ..."));

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
                        .distinct(Room::getType)
                        .flatMap(room -> {
                            try {
                                return Flux.just(bubbleFactory.getRoomDetailBubble("room-detail", room));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            return null;
                        })
                        .collectList()
                        .subscribe(roomBubbleList -> {
                            assert roomBubbleList != null;
                            if (!roomBubbleList.isEmpty()) {
                                lineService.push(event.getSource().getUserId(),
                                        new FlexMessage("Room List",
                                                Carousel
                                                        .builder()
                                                        .contents(roomBubbleList)
                                                        .build()
                                        ));
                            } else {
//                                TODO: NoRoomAvailable FlexMessage
                                try {
                                    lineService.push(event.getSource().getUserId(), FlexMessage.builder()
                                            .altText("No Booking")
                                            .contents(bubbleFactory.getBubble("no-booking"))
                                            .build());
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
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
//                            TODO: Supplier
                            .subscribe(booking -> {
                                try {
                                    lineService.push(userId, FlexMessage.builder()
                                            .altText("Booking Summary")
                                            .contents(bubbleFactory.getBookingDetailedBubble("summary", booking))
                                            .build());
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            });
                }


                break;
            }
            default:
                lineService.push(userId, new TextMessage("This the end"));
        }


    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) throws IllegalAccessException {
        String text = content.getText();
        String userId = event.getSource().getUserId();

        logger.debug(TAG + ": Got text message from %s : %s", replyToken, text);

        switch (text) {

            case "test": {
                lineService.reply(replyToken, FlexMessage.builder()
                        .altText("No Booking")
                        .contents(bubbleFactory.getBubble("no-booking"))
                        .build());
                return;
            }

            case "success": {
                logger.debug(TAG + ": check-in-success");
//                TODO: Supplier
                lineService.reply(replyToken, FlexMessage.builder()
                        .altText("Check In Success")
                        .contents(bubbleFactory.getBubble("check-in-success"))
                        .build()
                );
                break;
            }

            case "Check In": {
                logger.debug(TAG + "CHECK_IN_DEBUG");
                lineService.replyText(replyToken, "Loading your bookings ...");

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
                        .flatMap(booking -> {
                            try {
                                return Flux.just(bubbleFactory.getBookingDetailedBubble("check-in-detail", booking));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .collectList()
                        .subscribe(bookingList -> {
                            if (!bookingList.isEmpty()) {
                                lineService.push(userId,
                                        new FlexMessage("Booking List fo check-in",
                                                Carousel
                                                        .builder()
                                                        .contents(bookingList)
                                                        .build()));

                            } else {
                                try {
                                    lineService.push(userId, FlexMessage.builder()
                                            .altText("No Booking")
                                            .contents(bubbleFactory.getBubble("no-booking"))
                                            .build());
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                return;
            }

            case "Check Out": {
                logger.debug(TAG + "CHECK_OUT_DEBUG: ");

                lineService.replyText(replyToken, "Performing check out ...");

                User currentUser = userService.findById(userId).block();
                if (currentUser != null && !currentUser.isCheckInStatus()) {
                    lineService.push(userId, new TextMessage("Something went wrong !!"));
                    return;
                }


                bookingService.findByUserId(userId)
                        .filter(Booking::getCheckInStatus)
                        .subscribe(booking -> {
                            logger.debug("ACTIVE_BOOKINGS: " + booking);
                            if (booking != null) {
                                bookingService.delete(booking.getId()).subscribe();
                            }
                        });


                userService.findById(userId)
                        .subscribe(user -> {
                            user.setCheckInStatus(false);
                            userService.updateUser(user, userId)
                                    .subscribe();
                        });

                lineService.push(userId, new TextMessage("Check out success"));
                logger.debug(TAG + "CHECK_OUT_DEBUG: " + "SUCCESS");
                return;
            }

            case "Book a room": {
                logger.debug(TAG + ": Book a room");
                lineService.push(event.getSource().getUserId(), new DatetimePickerSupplier().getCheckInQuickReply());
                return;
            }

//            DONE: Manage Booking
            case "Manage Booking": {
                logger.debug(TAG + ": Manage Booking");

                lineService.replyText(replyToken, "Loading your booking list ...");

                bookingService.findByUserId(userId)
//                        TODO: Supplier
//                        .flatMap(booking -> Flux.just(new RoomManagingDetailBubbleSupplier(booking).get()))
                        .flatMap(booking -> {
                            try {
                                logger.debug(TAG + "BOOKING: " + booking);
                                return Flux.just(bubbleFactory.getBookingDetailedBubble("booking-detail", booking));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .collectList()
                        .subscribe(bookingList -> {

                            if (!bookingList.isEmpty()) {
                                logger.debug(TAG + "BOOKING_LIST: " + bookingList);
                                lineService.push(userId,
                                        new FlexMessage("Manage Booking",
                                                Carousel
                                                        .builder()
                                                        .contents(bookingList)
                                                        .build()));

                            } else {
                                try {
                                    lineService.push(userId, FlexMessage.builder()
                                            .altText("No Booking")
                                            .contents(bubbleFactory.getBubble("no-booking"))
                                            .build());
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                return;
            }
//            TODO: Default message specification
            default:
                logger.debug(TAG + ": Return echo message %s : %s", replyToken, text);
                lineService.replyText(replyToken, "Please use richmenu.");
        }
    }


}