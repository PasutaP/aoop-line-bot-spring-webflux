package com.example.lineapibackend.flexMessages.blocks;

import com.linecorp.bot.model.message.flex.component.FlexComponent;

public interface FooterBlock<T extends FlexComponent> {
    T createFooterBlock();
}

