package com.example.lineapibackend.quickReply;

import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.quickreply.QuickReply;
import com.linecorp.bot.model.message.quickreply.QuickReplyItem;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class DatetimePickerSupplier {

    public Message getCheckInQuickReply() {
        final List<QuickReplyItem> quickReplyItems = Collections.singletonList(
                QuickReplyItem.builder()
                        .action(new DatetimePickerAction(
                                "check-in date",
                                "action=submit-check-in-date",
                                "date",
                                new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "",
                                "",
                                new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ""
                        ))
                        .build()
        );

        return TextMessage.builder()
                .quickReply(QuickReply.items(quickReplyItems))
                .text("Please select you check-in date")
                .build();
    }

    public Message getCheckOutQuickReply() {
        final List<QuickReplyItem> quickReplyItems = Collections.singletonList(
                QuickReplyItem.builder()
                        .action(new DatetimePickerAction(
                                "check-out date",
                                "action=submit-check-out-date",
                                "date",
                                new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "",
                                "",
                                new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ""
                        ))
                        .build()
        );

        return TextMessage.builder()
                .quickReply(QuickReply.items(quickReplyItems))
                .text("Please select you check-out date")
                .build();
    }
}
