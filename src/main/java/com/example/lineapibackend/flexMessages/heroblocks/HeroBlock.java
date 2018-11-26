package com.example.lineapibackend.flexMessages.heroblocks;

import com.linecorp.bot.model.message.flex.component.FlexComponent;

public interface HeroBlock<T extends FlexComponent> {
    T createHeroBlock();
}
