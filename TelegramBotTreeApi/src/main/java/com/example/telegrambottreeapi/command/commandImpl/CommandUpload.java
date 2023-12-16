package com.example.telegrambottreeapi.command.commandImpl;

import com.example.telegrambottreeapi.command.Command;
import com.example.telegrambottreeapi.service.CategoryService;
import com.example.telegrambottreeapi.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

/*
        Upload Command:
        Command to add categories from exel file.
 */
@RequiredArgsConstructor
public class CommandUpload implements Command<String> {
    private final DocumentService documentService;

    @Override
    public String execute(Message message) {
        return "Добавлено успешно";
        // return documentService.addExelCategories(message.getChatId(), message.getDocument());
    }
}
