package com.example.telegrambottreeapi.controller;

import com.example.telegrambottreeapi.command.Command;
import com.example.telegrambottreeapi.command.CommandFactory;
import com.example.telegrambottreeapi.config.BotConfig;
import com.example.telegrambottreeapi.sender.Sender;
import com.example.telegrambottreeapi.sender.SenderFactory;
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
    private final DocumentController documentController;
    private final CommandFactory commandFactory;
    private final SenderFactory senderFactory;

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

            Command<?> command = commandFactory.createCommand(message);
            Object sendingData = command.execute(message);

            if (sendingData.getClass().equals(String.class)) {
                sendMessage(message.getChatId(), (String) sendingData);
            } else if (sendingData.getClass().equals(InputFile.class)) {
                sendDocument(message.getChatId(), (InputFile) sendingData);
            } else {
                throw new IllegalStateException("Unexpected value: " + sendingData.getClass());
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
