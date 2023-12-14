package com.example.telegrambottreeapi.controller;

import com.example.telegrambottreeapi.config.BotConfig;
import com.example.telegrambottreeapi.entity.Category;
import com.example.telegrambottreeapi.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

            switch (commandArgs[0]){
                case "/viewTree":
                    handleViewTreeCommand(message, commandArgs);
                    break;
                case "/addElement":
                    handleAddElementCommand(message, commandArgs);
                    break;
                case "/removeElement":
                    handleRemoveElementCommand(message, commandArgs);
                    break;
                default:
                    sendMessage(chatId, "Hello, World!");
            }
        }
    }

    private void handleViewTreeCommand(Message message, String[] commandArgs) {
        sendMessage(message.getChatId(), categoryService.getRootCategories());
    }

    private void handleAddElementCommand(Message message, String[] commandArgs) {
        if (commandArgs.length == 2) {
            String rootName = commandArgs[1];
            addRootElement(rootName);
            sendMessage(message.getChatId(), "Added root");
        }
        if (commandArgs.length == 3) {
            String parentName = commandArgs[1];
            String childName = commandArgs[2];
            addNewElement(parentName, childName);
            sendMessage(message.getChatId(), "Added child");
        }
    }

    private void handleRemoveElementCommand(Message message, String[] commandArgs) {
        categoryService.deleteCategory(commandArgs[1]);
    }

    private void addRootElement(String name) {
        categoryService.saveCategory(name);
    }

    private void addNewElement(String parentName, String childName) {
        categoryService.saveCategory(parentName, childName);
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
}
