package com.example.telegrambottreeapi.command.commandImpl;

import com.example.telegrambottreeapi.command.Command;
import com.example.telegrambottreeapi.exception.ExceptionResponse;
import com.example.telegrambottreeapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class CommandAddElement implements Command<String> {
    private final CategoryService categoryService;

    @Override
    public String execute(Message message) {
        String[] commandArgs = message.getText().split(" ");

        if (commandArgs.length == 2) {
            String rootName = commandArgs[1];
            return addRootElement(message.getChatId(), rootName);
        }
        if (commandArgs.length == 3) {
            String parentName = commandArgs[1];
            String childName = commandArgs[2];
            return addNewElement(message.getChatId(), parentName, childName);
        } else {
            return "Неизвестное количество аргументов";
        }
    }

    private String addRootElement(Long id, String name) {
        try {
            categoryService.saveCategory(id, name);
            return "Добавлена корневая категория";
        } catch (ExceptionResponse e) {
            return e.getMessage();
        }
    }

    public String addNewElement(Long id, String parentName, String childName) {
        try {
            categoryService.saveCategory(id, parentName, childName);
            return "Добавлен дочерний элемент";
        } catch (ExceptionResponse e) {
            return e.getMessage();
        }
    }
}
