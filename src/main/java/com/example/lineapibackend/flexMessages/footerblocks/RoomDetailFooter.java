package com.example.lineapibackend.flexMessages.footerblocks;

import com.example.lineapibackend.entity.Room;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;

import java.util.Collections;

@FooterBlockImplementation(value = "room-detail-footer")
public class RoomDetailFooter implements FooterBlock<Box> {

    private Room room;

    public Box createFooterBlock(Room room) {
        this.room = room;
        return this.createFooterBlock();
    }

    @Override
    public Box createFooterBlock() {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .spacing(FlexMarginSize.SM)
                .contents(Collections.singletonList(
                        Button.builder()
                                .action(PostbackAction.builder()
                                        .data(String.format("action=reserve&roomType=%s", room.getType()))
                                        .label("Reservation")
                                        .build()
                                )
                                .flex(2)
                                .color("#DF8D5F")
                                .style(Button.ButtonStyle.PRIMARY)
                                .build()
                ))
                .build();
    }
}
