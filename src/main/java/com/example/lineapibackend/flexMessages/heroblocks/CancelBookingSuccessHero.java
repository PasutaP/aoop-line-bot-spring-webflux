package com.example.lineapibackend.flexMessages.heroblocks;

import com.linecorp.bot.model.message.flex.component.Image;

@HeroBlockImplementation(value = "cancel-booking-success-hero")
public class CancelBookingSuccessHero implements HeroBlock<Image> {
    @Override
    public Image createHeroBlock() {
        return Image.builder()
                .url("https://firebasestorage.googleapis.com/v0/b/aoop-project-d1add.appspot.com/o/Resources%2FHomeless-14.png?alt=media&token=4b28c657-d931-40b1-8008-8ce919ee8d61")
                .aspectMode(Image.ImageAspectMode.Cover)
                .aspectRatio(Image.ImageAspectRatio.R1_51TO1)
                .size(Image.ImageSize.FULL_WIDTH)
                .build();
    }
}
