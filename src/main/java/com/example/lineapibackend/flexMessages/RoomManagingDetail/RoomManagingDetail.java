package com.example.lineapibackend.flexMessages.RoomManagingDetail;

import com.example.lineapibackend.flexMessages.blocks.BodyBlock;
import com.example.lineapibackend.flexMessages.blocks.FooterBlock;
import com.example.lineapibackend.flexMessages.blocks.HeroBlock;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Image;

import java.util.function.Supplier;

public interface RoomManagingDetail extends BodyBlock<Box>, HeroBlock<Image>, FooterBlock<Box> {
}
