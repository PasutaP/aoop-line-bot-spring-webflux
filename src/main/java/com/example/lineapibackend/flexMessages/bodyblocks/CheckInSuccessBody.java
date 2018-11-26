package com.example.lineapibackend.flexMessages.bodyblocks;

import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;

import java.util.Collections;

@BodyBlockImplementation(value = "check-in-success-body")
public class CheckInSuccessBody implements BodyBlock<Box> {

    @Override
    public Box createBodyBlock() {
        return Box
                .builder()
                .layout(FlexLayout.VERTICAL)
                .contents(Collections.singletonList(
                        Text.builder()
                                .text("Check-in Successful")
                                .size(FlexFontSize.XL)
                                .align(FlexAlign.START)
                                .weight(Text.TextWeight.BOLD)
                                .build()
                ))
                .build();
    }
}
