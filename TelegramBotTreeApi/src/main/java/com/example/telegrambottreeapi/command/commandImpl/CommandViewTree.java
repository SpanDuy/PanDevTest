package com.example.telegrambottreeapi.command.commandImpl;

import com.example.telegrambottreeapi.command.Command;
import com.example.telegrambottreeapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/*
        Command View Tree:
        Command to show tree of categories
        in structure view
 */
@RequiredArgsConstructor
public class CommandViewTree implements Command<String> {
    private final CategoryService categoryService;

    @Override
    public String execute(Message message) {
        return categoryService.getRootCategories(message.getChatId()).toString();
    }
}
