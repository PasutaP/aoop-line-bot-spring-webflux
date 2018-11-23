package com.example.lineapibackend.flexMessages.blocks;

import com.linecorp.bot.model.message.flex.component.FlexComponent;

public interface HeroBlock<T extends FlexComponent> {
    T createHeroBlock();
}
