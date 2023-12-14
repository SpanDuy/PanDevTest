package com.example.telegrambottreeapi.controller;

import com.example.telegrambottreeapi.config.BotConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
public class BotController extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CategoryController categoryController;
    private final DocumentController documentController;

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
                case "/viewTree" -> sendMessage(chatId, categoryController.handleViewTreeCommand(chatId, commandArgs));
                case "/addElement" -> sendMessage(chatId, categoryController.handleAddElementCommand(chatId, commandArgs));
                case "/removeElement" -> sendMessage(chatId, categoryController.handleRemoveElementCommand(chatId, commandArgs));
                case "/help" -> sendMessage(chatId, categoryController.handleHelpCommand(chatId, commandArgs));

                case "/download" -> sendDocument(chatId, documentController.handleDownloadCommand(chatId, commandArgs));

                default -> sendMessage(chatId, "Такой команды нет(");
            }
        }
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendDocument(Long chatId, InputFile file) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(file);
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
