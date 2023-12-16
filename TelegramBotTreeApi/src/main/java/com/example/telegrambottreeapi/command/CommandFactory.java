package com.example.telegrambottreeapi.command;

import com.example.telegrambottreeapi.command.commandImpl.*;
import com.example.telegrambottreeapi.service.CategoryService;
import com.example.telegrambottreeapi.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/*
        Command Factory:
        Returns the command corresponding to the message.
 */
@Component
@RequiredArgsConstructor
public class CommandFactory {
    private final CategoryService categoryService;
    private final DocumentService documentService;

    public Command<?> createCommand(Message message) {
        String commandText = message.getText();
        String[] commandArgs = message.getText().split(" ");

        if ("/viewTree".equals(commandArgs[0])) {
            return new CommandViewTree(categoryService);
        } else if ("/addElement".equals(commandArgs[0])) {
            return new CommandAddElement(categoryService);
        } else if ("/removeElement".equals(commandArgs[0])) {
            return new CommandRemoveElement(categoryService);
        } else if ("/help".equals(commandArgs[0])) {
            return new CommandHelp(categoryService);
        } else if ("/download".equals(commandArgs[0])) {
            return new CommandDownload(documentService);
        } else {
            return new CommandDefault();
        }
    }
}
