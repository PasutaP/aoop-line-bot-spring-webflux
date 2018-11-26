package com.example.lineapibackend.flexMessages.bodyblocks;
import com.linecorp.bot.model.message.flex.component.FlexComponent;

public interface BodyBlock<T extends FlexComponent> {
    T createBodyBlock();
}
