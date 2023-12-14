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
public class CategoryController extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final CategoryService categoryService;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            Message message = update.getMessage();
            String[] commandArgs = message.getText().split(" ");
            long chatId = update.getMessage().getChatId();

            switch (commandArgs[0]) {
                case "/viewTree" -> handleViewTreeCommand(message, commandArgs);
                case "/addElement" -> handleAddElementCommand(message, commandArgs);
                case "/removeElement" -> handleRemoveElementCommand(message, commandArgs);
                case "/help" -> handleHelpCommand(message, commandArgs);
                default -> sendMessage(chatId, "Такой команды нет(");
            }
        }
    }

    private void handleHelpCommand(Message message, String[] commandArgs) {
        String[] commands = new String[] {
                "/help - вывести все команды",
                "/viewTree - вывести все деревья запросов",
                "/addElement <название элемента> - создать корневой элемент",
                "/addElement <родительский элемент> <дочерний элемент> - добавить дочерний элемент родительскому",
                "/removeElement <название элемента> - удаляет элемент и все его дочерние"
        };
        String formattedMessage = String.join("\n", commands);
        sendMessage(message.getChatId(), formattedMessage);
    }

    private void handleViewTreeCommand(Message message, String[] commandArgs) {
        sendMessage(message.getChatId(), categoryService.getRootCategories(message.getChatId()));
    }

    private void handleAddElementCommand(Message message, String[] commandArgs) {
        if (commandArgs.length == 2) {
            String rootName = commandArgs[1];
            addRootElement(message.getChatId(), rootName);
        }
        if (commandArgs.length == 3) {
            String parentName = commandArgs[1];
            String childName = commandArgs[2];
            addNewElement(message.getChatId(), parentName, childName);
        } else {
            sendMessage(message.getChatId(), "Неизвестное количество аргументов");
        }
    }

    private void handleRemoveElementCommand(Message message, String[] commandArgs) {
        try {
            categoryService.deleteCategory(message.getChatId(), commandArgs[1]);
        } catch (ExceptionResponse e) {
            sendMessage(message.getChatId(), e.getMessage());
        }
    }

    private void addRootElement(Long id, String name) {
        try {
            categoryService.saveCategory(id, name);
            sendMessage(id, "Добавлен дочерний элемент");
        } catch (ExceptionResponse e) {
            sendMessage(id, e.getMessage());
        }
    }

    private void addNewElement(Long id, String parentName, String childName) {
        try {
            categoryService.saveCategory(id, parentName, childName);
            sendMessage(id, "Добавлена корневая категория");
        } catch (ExceptionResponse e) {
            sendMessage(id, e.getMessage());
        }
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("SOME OTHER ERROR");
        }
    }
}
