package com.example.lineapibackend.flexMessages;

import com.example.lineapibackend.flexMessages.blocks.BodyBlock;
import com.example.lineapibackend.flexMessages.blocks.HeroBlock;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import java.util.Collections;
import java.util.function.Supplier;

public class CheckInSuccessFlexMessageSupplier implements Supplier<FlexMessage>, BodyBlock<Box>, HeroBlock<Image> {
    @Override
    public FlexMessage get() {
        final Image heroBlock = createHeroBlock();
        final Box bodyBlock = createBodyBlock();
        final Bubble bubble =
                Bubble.builder()
                .hero(heroBlock)
                .body(bodyBlock)
                .build();
        return new FlexMessage("Check-In success", bubble);
    }

    public Image createHeroBlock() {
        return Image
                .builder()
                .url("https://firebasestorage.googleapis.com/v0/b/aoop-project-d1add.appspot.com/o/RoomType%2FHomeless-09.png?alt=media&token=2e70ccf0-d8df-4944-8d62-8ad598e87bca")
                .size(Image.ImageSize.FULL_WIDTH)
                .aspectMode(Image.ImageAspectMode.Cover)
                .aspectRatio(Image.ImageAspectRatio.R1_51TO1)
                .build();
    }

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
