package com.example.lineapibackend.flexMessages;

import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;

import java.util.Arrays;
import java.util.function.Supplier;

public class CheckInFailFlexMessageSupplier implements Supplier<FlexMessage> {
    @Override
    public FlexMessage get() {
        final Box bodyBlock = createBodyBlock();
        final Bubble bubble =
                Bubble.builder()
                        .body(bodyBlock)
                        .build();
        return new FlexMessage("Check-In Fail", bubble);
    }

    private Box createBodyBlock() {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(Arrays.asList(
                        Text.builder()
                                .text("Check-in Failed")
                                .size(FlexFontSize.XL)
                                .align(FlexAlign.START)
                                .weight(Text.TextWeight.BOLD)
                                .color("#DF8D5F")
                                .build(),
                        Text.builder()
                                .text("The check in process is avaiable only on your reservation date.")
                                .size(FlexFontSize.Md)
                                .wrap(true)
                                .build()
                ))
                .build();
    }

}
