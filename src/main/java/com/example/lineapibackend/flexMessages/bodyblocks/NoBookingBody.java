package com.example.lineapibackend.flexMessages.bodyblocks;

import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;

import java.util.Collections;


@BodyBlockImplementation(value = "no-booking-body")
public class NoBookingBody implements BodyBlock<Box> {

    @Override
    public Box createBodyBlock() {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(Collections.singletonList(
                        Text.builder()
                                .text("You haven't booked any room yet")
                                .size(FlexFontSize.XL)
                                .align(FlexAlign.START)
                                .weight(Text.TextWeight.BOLD)
                                .color("#DF8D5F")
                                .wrap(true)
                                .build()
                ))
                .build();
    }
}
