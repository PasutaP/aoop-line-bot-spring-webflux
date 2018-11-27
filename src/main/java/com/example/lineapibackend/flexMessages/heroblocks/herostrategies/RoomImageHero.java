package com.example.lineapibackend.flexMessages.heroblocks.herostrategies;

import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.flexMessages.heroblocks.HeroBlock;
import com.example.lineapibackend.flexMessages.heroblocks.HeroBlockImplementation;
import com.linecorp.bot.model.message.flex.component.Image;

@HeroBlockImplementation(value = "room-image-hero")
public class RoomImageHero implements HeroBlock<Image> {

    private Room room;

    public Image createHeroBlock(Room room) {
        this.room = room;
        return this.createHeroBlock();
    }

    @Override
    public Image createHeroBlock() {
        return Image.builder()
                .url(this.room.getRoomImageUrl())
                .size(Image.ImageSize.FULL_WIDTH)
                .aspectMode(Image.ImageAspectMode.Cover)
                .aspectRatio(Image.ImageAspectRatio.R20TO13)
                .build();
    }
}
