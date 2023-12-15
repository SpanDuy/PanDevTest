package com.example.telegrambottreeapi.command;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface Command<T> {
    T execute(Message message);
}
