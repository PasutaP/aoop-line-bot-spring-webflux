# LINE chatbot for hotel services

## Introduction
This project is a LINE chatbot messaging server which allows users to do operations with a hotel.

## Covered hotel operations
* **Room Booking**: An allowance for users in order to do booking for an associated hotel.
* **Checking-In**: Checking-In service with a booking in the available period.
* **Checking-Out**: Checking-Out from the previously checked-in booking.
* **Booking Management**: An allowance to manage bookings which are booked.

## Technology Stack
* Spring Framework
* Spring Webflux
* MongoDB
* [LINE Bot SDK for Spring](https://github.com/line/line-bot-sdk-java)

## Controllers
* [Line Contoller](https://github.com/arut-ji/aoop-line-bot-spring-webflux/blob/master/src/main/java/com/example/lineapibackend/controller/LineController.java): This controller provides API endpoint for webhook requests from LINE Messaging API.
* [Booking Controller](https://github.com/arut-ji/aoop-line-bot-spring-webflux/blob/master/src/main/java/com/example/lineapibackend/controller/BookingController.java): This controller provides API endpoints for dealing with bookings.
* [User Controller](https://github.com/arut-ji/aoop-line-bot-spring-webflux/blob/master/src/main/java/com/example/lineapibackend/controller/UserController.java): This controller provides API endpoints for dealing with User.
* [Room Controller](https://github.com/arut-ji/aoop-line-bot-spring-webflux/blob/master/src/main/java/com/example/lineapibackend/controller/RoomController.java): This controller provides API endpoints for dealing with Room.

## Authors
* **Pasuta Paopun** (Rich Menus and Flex Messages designer)
* **Arut Jinadit** (Software Architecture designer, Developer)
