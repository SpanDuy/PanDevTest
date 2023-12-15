package com.example.telegrambottreeapi.command.commandImpl;

import com.example.telegrambottreeapi.command.Command;
import com.example.telegrambottreeapi.exception.ExceptionResponse;
import com.example.telegrambottreeapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class CommandRemoveElement implements Command<String> {
    private final CategoryService categoryService;

    @Override
    public String execute(Message message) {
        String[] commandArgs = message.getText().split(" ");

        try {
            categoryService.deleteCategory(message.getChatId(), commandArgs[1]);
            return "Удалено";
        } catch (ExceptionResponse e) {
            return e.getMessage();
        }
    }
}
