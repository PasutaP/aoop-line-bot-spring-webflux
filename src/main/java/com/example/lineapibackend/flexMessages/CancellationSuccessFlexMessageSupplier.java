package com.example.lineapibackend.flexMessages;

import com.example.lineapibackend.flexMessages.blocks.BodyBlock;
import com.example.lineapibackend.flexMessages.blocks.HeroBlock;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;

import java.util.Collections;
import java.util.function.Supplier;

public class CancellationSuccessFlexMessageSupplier implements Supplier<FlexMessage>, HeroBlock<Image>, BodyBlock<Box> {
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

    @Override
    public Image createHeroBlock() {
        return Image.builder()
                .url("https://firebasestorage.googleapis.com/v0/b/aoop-project-d1add.appspot.com/o/Resources%2FHomeless-14.png?alt=media&token=4b28c657-d931-40b1-8008-8ce919ee8d61")
                .aspectMode(Image.ImageAspectMode.Cover)
                .aspectRatio(Image.ImageAspectRatio.R1_51TO1)
                .size(Image.ImageSize.FULL_WIDTH)
                .build();
    }

    @Override
    public FlexMessage get() {
        Bubble bubble = Bubble.builder()
                .body(createBodyBlock())
                .hero(createHeroBlock())
                .build();

        return new FlexMessage("Cancellation Success", bubble);
    }
}
