package com.example.lineapibackend.flexMessages.heroblocks;

import com.linecorp.bot.model.message.flex.component.Image;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@HeroBlockImplementation(value = "check-in-success-hero")
public class CheckInSuccessHero implements HeroBlock<Image> {

    @Override
    public Image createHeroBlock() {
        return Image
                .builder()
                .url("https://firebasestorage.googleapis.com/v0/b/aoop-project-d1add.appspot.com/o/RoomType%2FHomeless-09.png?alt=media&token=2e70ccf0-d8df-4944-8d62-8ad598e87bca")
                .size(Image.ImageSize.FULL_WIDTH)
                .aspectMode(Image.ImageAspectMode.Cover)
                .aspectRatio(Image.ImageAspectRatio.R1_51TO1)
                .build();
    }
}
