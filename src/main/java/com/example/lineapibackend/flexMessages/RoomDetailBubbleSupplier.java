package com.example.lineapibackend.flexMessages;

import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.entity.roomDetail.RoomDetail;
import com.example.lineapibackend.flexMessages.blocks.BodyBlock;
import com.example.lineapibackend.flexMessages.blocks.FooterBlock;
import com.example.lineapibackend.flexMessages.blocks.HeroBlock;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.*;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.unit.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.function.Supplier;

public class RoomDetailBubbleSupplier implements Supplier<Bubble>, HeroBlock<Image>, BodyBlock<Box>, FooterBlock<Box> {

    private RoomDetail room;
    private Integer remainingRooms;
    private Date startDate;
    private Date endDate;

    public RoomDetailBubbleSupplier(RoomDetail room, Integer remainingRooms) {
        this.room = room;
        this.remainingRooms = remainingRooms;
    }

    @Override
    public Bubble get() {
//        TODO: Constructor refactoring
        final Image heroBlock = createHeroBlock();
        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock();
        return Bubble.builder()
                .hero(heroBlock)
                .body(bodyBlock)
                .footer(footerBlock)
                .build();
    }

    public Image createHeroBlock() {
        return Image.builder()
                .url(this.room.getRoomImageUrl())
                .size(Image.ImageSize.FULL_WIDTH)
                .aspectMode(Image.ImageAspectMode.Cover)
                .aspectRatio(Image.ImageAspectRatio.R20TO13)
                .build();
    }

    public Box createBodyBlock() {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .flex(0)
                .spacing(FlexMarginSize.NONE)
                .contents(Arrays.asList(
                        Text.builder()
                                .text(this.room.getRoomType())
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
                                .build(),
                        Text.builder()
                                .text(String.format("%s room left!", this.remainingRooms.toString())) //Remaining Room
                                .size(FlexFontSize.SM)
                                .align(FlexAlign.END)
                                .color("#DF8D5F")
                                .build()
                ))
                .build();
    }

    public Box createFooterBlock() {
        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .spacing(FlexMarginSize.SM)
                .contents(Collections.singletonList(
                        Button.builder()
                                .action(PostbackAction.builder()
                                        .data(String.format("action=reserve&roomType=%s", room.getRoomType()))
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
