package com.example.lineapibackend.flexMessages.bodyblocks.bodystrategies;


import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.flexMessages.bodyblocks.BodyBlock;
import com.example.lineapibackend.flexMessages.bodyblocks.BodyBlockImplementation;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Icon;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@BodyBlockImplementation(value = "booking-detail-body")
public class BookingDetailBody implements BodyBlock<Box> {

    private Room room;
    private Booking booking;

    @Override
    public Box createBodyBlock() {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .spacing(FlexMarginSize.NONE)
                .contents(Arrays.asList(
                        Text.builder()
                                .text(this.room.getType())
                                .size(FlexFontSize.XL)
                                .weight(Text.TextWeight.BOLD)
                                .build(),
                        Box.builder()
                                .layout(FlexLayout.BASELINE)
                                .spacing(FlexMarginSize.SM)
                                .contents(Arrays.asList(
                                        Icon.builder()
                                                .url("https://firebasestorage.googleapis.com/v0/b/aoop-project-d1add.appspot.com/o/RoomType%2Fuser-01.png?alt=media&token=464c9859-3df8-45e8-948f-6b73e8d29060")
                                                .margin(FlexMarginSize.NONE)
                                                .size(FlexFontSize.XL)
                                                .build(),
                                        Text.builder()
                                                .text(String.format("%d", this.room.getSleeps()))
                                                .flex(0)
                                                .margin(FlexMarginSize.MD)
                                                .align(FlexAlign.END)
                                                .wrap(false)
                                                .build()
                                ))
                                .build(),
                        Text.builder()
                                .text(String.format("THB %.2f", this.room.getPrice()))
                                .size(FlexFontSize.LG)
                                .build(),
                        Box.builder()
                                .layout(FlexLayout.BASELINE)
                                .contents(Arrays.asList(
                                        Text.builder()
                                                .text("CHECK IN")
                                                .size(FlexFontSize.SM)
                                                .color("#AAAAAA")
                                                .wrap(true)
                                                .build(),
                                        Text.builder()
                                                .text(new SimpleDateFormat("dd.MM.yyyy").format(this.booking.getCheckInDate()))
                                                .flex(1)
                                                .margin(FlexMarginSize.NONE)
                                                .size(FlexFontSize.LG)
                                                .align(FlexAlign.START)
                                                .wrap(true)
                                                .build()
                                ))
                                .build(),
                        Box.builder()
                                .layout(FlexLayout.BASELINE)
                                .contents(Arrays.asList(
                                        Text.builder()
                                                .text("CHECK OUT")
                                                .size(FlexFontSize.SM)
                                                .color("#AAAAAA")
                                                .build(),
                                        Text.builder()
                                                .text(new SimpleDateFormat("dd.MM.yyyy").format(this.booking.getCheckOutDate()))
                                                .size(FlexFontSize.LG)
                                                .color("#000000")
                                                .build()
                                ))
                                .build()

                ))
                .build();
    }

    public Box createBodyBlock(Booking booking) {
        this.booking = booking;
        this.room = booking.getBookedRoom();
        return this.createBodyBlock();
    }

}
