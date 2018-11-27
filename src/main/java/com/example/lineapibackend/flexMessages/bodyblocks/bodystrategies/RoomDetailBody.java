package com.example.lineapibackend.flexMessages.bodyblocks.bodystrategies;

import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.flexMessages.bodyblocks.BodyBlock;
import com.example.lineapibackend.flexMessages.bodyblocks.BodyBlockImplementation;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Icon;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.unit.*;

import java.util.Arrays;


@BodyBlockImplementation(value = "room-detail-body")
public class RoomDetailBody implements BodyBlock<Box> {

    private Room room;

    @Override
    public Box createBodyBlock() {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .flex(0)
                .spacing(FlexMarginSize.NONE)
                .contents(Arrays.asList(
                        Text.builder()
                                .text(this.room.getType())
                                .margin(FlexMarginSize.NONE)
                                .size(FlexFontSize.XL)
                                .align(FlexAlign.START)
                                .gravity(FlexGravity.CENTER)
                                .weight(Text.TextWeight.BOLD)
                                .build(),
                        Box.builder()
                                .layout(FlexLayout.BASELINE)
                                .flex(1)
                                .margin(FlexMarginSize.NONE)
                                .contents(Arrays.asList(
                                        Icon.builder()
                                                .url("https://firebasestorage.googleapis.com/v0/b/aoop-project-d1add.appspot.com/o/RoomType%2Fuser-01.png?alt=media&token=464c9859-3df8-45e8-948f-6b73e8d29060")
                                                .margin(FlexMarginSize.NONE)
                                                .size(FlexFontSize.XL)
                                                .build(),
                                        Text.builder()
                                                .text(String.format("%d", this.room.getSleeps())) //Sleeps
                                                .flex(0)
                                                .margin(FlexMarginSize.MD)
                                                .weight(Text.TextWeight.REGULAR)
                                                .wrap(false)
                                                .build()
                                ))
                                .build(),
                        Text.builder()
                                .text(String.format("THB %.2f", this.room.getPrice())) //Room price
                                .flex(10)
                                .margin(FlexMarginSize.NONE)
                                .size(FlexFontSize.XL)
                                .align(FlexAlign.START)
                                .gravity(FlexGravity.BOTTOM)
                                .color("#000000")
                                .build()
                ))
                .build();
    }

    public Box createBodyBlock(Room room) {
        this.room = room;
        return this.createBodyBlock();
    }
}
