package com.example.telegrambottreeapi.command;

import org.telegram.telegrambots.meta.api.objects.Message;

/*
        Interface Command:
        All commands implements this interface.
 */
public interface Command<T> {
    T execute(Message message);
}
