package com.example.lineapibackend.flexMessages;

import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.flexMessages.bodyblocks.*;
import com.example.lineapibackend.flexMessages.bodyblocks.bodystrategies.BookingDetailBody;
import com.example.lineapibackend.flexMessages.bodyblocks.bodystrategies.CheckInDetailBody;
import com.example.lineapibackend.flexMessages.bodyblocks.bodystrategies.RoomDetailBody;
import com.example.lineapibackend.flexMessages.bodyblocks.bodystrategies.SummaryBody;
import com.example.lineapibackend.flexMessages.footerblocks.footerstrategies.BookingDetailFooter;
import com.example.lineapibackend.flexMessages.footerblocks.footerstrategies.CheckInDetailFooter;
import com.example.lineapibackend.flexMessages.footerblocks.FooterBlockStrategyFactory;
import com.example.lineapibackend.flexMessages.footerblocks.footerstrategies.RoomDetailFooter;
import com.example.lineapibackend.flexMessages.heroblocks.HeroBlockStrategyFactory;
import com.example.lineapibackend.flexMessages.heroblocks.herostrategies.RoomImageHero;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.container.Bubble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BubbleFactory {

    private final HeroBlockStrategyFactory heroBlockStrategyFactory;

    private final BodyBlockStrategyFactory bodyBlockStrategyFactory;

    private final FooterBlockStrategyFactory footerBlockStrategyFactory;

    @Autowired
    private BubbleFactory(HeroBlockStrategyFactory heroBlockStrategyFactory, BodyBlockStrategyFactory bodyBlockStrategyFactory, FooterBlockStrategyFactory footerBlockStrategyFactory) {
        this.heroBlockStrategyFactory = heroBlockStrategyFactory;
        this.bodyBlockStrategyFactory = bodyBlockStrategyFactory;
        this.footerBlockStrategyFactory = footerBlockStrategyFactory;
    }

    public Bubble getBubble(String strategy) throws IllegalAccessException {
        if (strategy == null) {
            return null;
        } else if (strategy.equals("check-in-success") || strategy.equals("cancel-booking-success")) {
            return Bubble.builder()
                    .hero((Image) heroBlockStrategyFactory.getImplementation(strategy + "-hero").createHeroBlock())
                    .body((Box) bodyBlockStrategyFactory.getImplementation(strategy + "-body").createBodyBlock())
                    .build();
        } else if (strategy.equals("no-booking")) {
            return Bubble.builder()
                    .body((Box) bodyBlockStrategyFactory.getImplementation(strategy + "-body").createBodyBlock())
                    .build();
        }

        return null;
    }

    public Bubble getBookingDetailedBubble(String strategy, Booking booking) throws IllegalAccessException {
        if (booking == null || strategy == null) {
            return null;
        } else if (strategy.equals("booking-detail")) {
            return Bubble.builder()
                    .hero(((RoomImageHero) heroBlockStrategyFactory.getImplementation("room-image-hero")).createHeroBlock(booking.getBookedRoom()))
                    .body(((BookingDetailBody) bodyBlockStrategyFactory.getImplementation(strategy + "-body")).createBodyBlock(booking))
                    .footer(((BookingDetailFooter) footerBlockStrategyFactory.getImplementation(strategy + "-footer")).createFooterBlock(booking))
                    .build();
        } else if (strategy.equals("summary")) {
            return Bubble.builder()
                    .body(((SummaryBody) bodyBlockStrategyFactory.getImplementation(strategy + "-body")).createBodyBlock(booking))
                    .build();
        } else if (strategy.equals("check-in-detail")) {
            return Bubble.builder()
                    .hero(((RoomImageHero) heroBlockStrategyFactory.getImplementation("room-image-hero")).createHeroBlock(booking.getBookedRoom()))
                    .body(((CheckInDetailBody) bodyBlockStrategyFactory.getImplementation(strategy + "-body")).createBodyBlock(booking))
                    .footer(((CheckInDetailFooter) footerBlockStrategyFactory.getImplementation(strategy + "-footer")).createFooterBlock(booking))
                    .build();
        }
        return null;
    }

    public Bubble getRoomDetailBubble(String strategy, Room room) throws IllegalAccessException {
        if (strategy == null || room == null) {
            return null;
        } else if (strategy.equals("room-detail")) {
            return Bubble.builder()
                    .hero(((RoomImageHero) heroBlockStrategyFactory.getImplementation("room-image-hero")).createHeroBlock(room))
                    .body(((RoomDetailBody) bodyBlockStrategyFactory.getImplementation(strategy + "-body")).createBodyBlock(room))
                    .footer(((RoomDetailFooter) footerBlockStrategyFactory.getImplementation(strategy + "-footer")).createFooterBlock(room))
                    .build();
        }

        return null;
    }
}
