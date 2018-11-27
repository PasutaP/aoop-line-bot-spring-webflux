package com.example.lineapibackend.flexMessages.bodyblocks.bodystrategies;

import com.example.lineapibackend.flexMessages.bodyblocks.BodyBlock;
import com.example.lineapibackend.flexMessages.bodyblocks.BodyBlockImplementation;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;

import java.util.Collections;

@BodyBlockImplementation(value = "cancel-booking-success-body")
public class CancelBookingSuccessBody implements BodyBlock<Box> {

    @Override
    public Box createBodyBlock() {
        return Box.builder()
                .layout(FlexLayout.BASELINE)
                .contents(Collections.singletonList(
                        Text.builder()
                                .text("Cancellation Successful")
                                .size(FlexFontSize.XL)
                                .weight(Text.TextWeight.BOLD)
                                .build()
                ))
                .build();
    }
}
