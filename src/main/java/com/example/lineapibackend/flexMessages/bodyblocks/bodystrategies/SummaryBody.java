package com.example.lineapibackend.flexMessages.bodyblocks.bodystrategies;

import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.flexMessages.bodyblocks.BodyBlock;
import com.example.lineapibackend.flexMessages.bodyblocks.BodyBlockImplementation;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@BodyBlockImplementation(value = "summary-body")
public class SummaryBody implements BodyBlock<Box> {

    private Room room;
    private Booking booking;

    public Box createBodyBlock(Booking booking) {
        this.booking = booking;
        this.room = booking.getBookedRoom();
        return this.createBodyBlock();
    }

    @Override
    public Box createBodyBlock() {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .spacing(FlexMarginSize.NONE)
                .contents(Arrays.asList(
                        Text.builder()
                                .text("You have just booked!")
                                .size(FlexFontSize.LG)
                                .color("#DF8B5F")
                                .weight(Text.TextWeight.BOLD)
                                .build(),
                        Text.builder()
                                .text(this.room.getType())
                                .size(FlexFontSize.XL)
                                .weight(Text.TextWeight.BOLD)
                                .build(),
                        Text.builder()
                                .text(String.format("THB %.2f", this.room.getPrice()))
                                .size(FlexFontSize.LG)
                                .build(),
                        Separator.builder()
                                .margin(FlexMarginSize.MD)
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
                                .build(),
                        Separator.builder()
                                .margin(FlexMarginSize.MD)
                                .build()
                ))
                .build();
    }
}
