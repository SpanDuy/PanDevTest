package com.example.telegrambottreeapi.controller;

import com.example.telegrambottreeapi.config.BotConfig;
import com.example.telegrambottreeapi.entity.Category;
import com.example.telegrambottreeapi.exception.ExceptionResponse;
import com.example.telegrambottreeapi.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    public String handleHelpCommand(Long id, String[] commandArgs) {
        String[] commands = new String[] {
                "/help - вывести все команды",
                "/viewTree - вывести все деревья запросов",
                "/addElement <название элемента> - создать корневой элемент",
                "/addElement <родительский элемент> <дочерний элемент> - добавить дочерний элемент родительскому",
                "/removeElement <название элемента> - удаляет элемент и все его дочерние"
        };
        return String.join("\n", commands);
    }

    public String handleViewTreeCommand(Long id, String[] commandArgs) {
        return categoryService.getRootCategories(id);
    }

    public String handleAddElementCommand(Long id, String[] commandArgs) {
        if (commandArgs.length == 2) {
            String rootName = commandArgs[1];
            return addRootElement(id, rootName);
        }
        if (commandArgs.length == 3) {
            String parentName = commandArgs[1];
            String childName = commandArgs[2];
            return addNewElement(id, parentName, childName);
        } else {
            return "Неизвестное количество аргументов";
        }
    }

    public String handleRemoveElementCommand(Long id, String[] commandArgs) {
        try {
            categoryService.deleteCategory(id, commandArgs[1]);
            return "Удалено";
        } catch (ExceptionResponse e) {
            return e.getMessage();
        }
    }

    public String addRootElement(Long id, String name) {
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
