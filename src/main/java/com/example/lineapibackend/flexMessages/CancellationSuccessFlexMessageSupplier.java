package com.example.lineapibackend.flexMessages;

import com.example.lineapibackend.flexMessages.blocks.BodyBlock;
import com.example.lineapibackend.flexMessages.blocks.HeroBlock;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;

import java.util.function.Supplier;

public class CancellationSuccessFlexMessageSupplier implements Supplier<FlexMessage>, HeroBlock<Image>, BodyBlock<Text> {
    @Override
    public Text createBodyBlock() {
        return Text.builder()
                .text("Cancellation Successful")
                .size(FlexFontSize.XL)
                .weight(Text.TextWeight.BOLD)
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
        return null;
    }
}
