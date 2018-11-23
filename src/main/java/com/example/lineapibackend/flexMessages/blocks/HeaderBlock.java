package com.example.lineapibackend.flexMessages.blocks;

import com.linecorp.bot.model.message.flex.component.FlexComponent;

public interface HeaderBlock<T extends FlexComponent>  {
    T createHeaderblock();
}
