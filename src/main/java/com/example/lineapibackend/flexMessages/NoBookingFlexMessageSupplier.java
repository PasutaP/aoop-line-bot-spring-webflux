package com.example.lineapibackend.flexMessages;

import com.example.lineapibackend.flexMessages.blocks.BodyBlock;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;


import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;

public class NoBookingFlexMessageSupplier implements Supplier<FlexMessage>, BodyBlock<Box> {
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

    @Override
    public FlexMessage get() {
        Bubble bubble = Bubble.builder()
                .body(createBodyBlock())
                .build();
        return new FlexMessage("No booking", bubble);
    }
}
