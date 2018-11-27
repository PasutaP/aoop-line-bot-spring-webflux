package com.example.lineapibackend.flexMessages.footerblocks.footerstrategies;

import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.flexMessages.footerblocks.FooterBlock;
import com.example.lineapibackend.flexMessages.footerblocks.FooterBlockImplementation;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;

import java.util.Collections;


@FooterBlockImplementation(value = "check-in-detail-footer")
public class CheckInDetailFooter implements FooterBlock<Box> {

    private Booking booking;

    @Override
    public Box createFooterBlock() {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(Collections.singletonList(
                        Button.builder()
                                .action(PostbackAction.builder()
                                        .label("Check In")
                                        .data(String.format("action=perform-check-in&userId=%s&bookingId=%s", booking.getBookedByUserId(), booking.getId()))
                                        .build())
                                .color("#DF8D5F")
                                .style(Button.ButtonStyle.PRIMARY)
                                .build()
                ))
                .build();
    }

    public Box createFooterBlock(Booking booking) {
        this.booking = booking;
        return this.createFooterBlock();
    }
}
