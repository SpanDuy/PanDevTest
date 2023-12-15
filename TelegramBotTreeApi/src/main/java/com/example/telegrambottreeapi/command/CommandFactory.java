package com.example.telegrambottreeapi.command;

import com.example.telegrambottreeapi.command.commandImpl.*;
import com.example.telegrambottreeapi.service.CategoryService;
import com.example.telegrambottreeapi.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class CommandFactory {
    private final CategoryService categoryService;
    private final DocumentService documentService;

    public Command<?> createCommand(Message message) {
        String commandText = message.getText();

        if ("/viewTree".equals(commandText)) {
            return new CommandViewTree(categoryService);
        } else if ("/addElement".equals(commandText)) {
            return new CommandAddElement(categoryService);
        } else if ("/removeElement".equals(commandText)) {
            return new CommandRemoveElement(categoryService);
        } else if ("/help".equals(commandText)) {
            return new CommandHelp(categoryService);
        } else if ("/download".equals(commandText)) {
            return new CommandDownload(documentService);
        } else {
            return null;
        }
    }
}
