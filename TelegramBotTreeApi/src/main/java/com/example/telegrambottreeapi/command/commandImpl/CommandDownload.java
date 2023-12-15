package com.example.telegrambottreeapi.command.commandImpl;

import com.example.telegrambottreeapi.command.Command;
import com.example.telegrambottreeapi.service.CategoryService;
import com.example.telegrambottreeapi.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class CommandDownload implements Command<InputFile> {
    private final DocumentService documentService;

    @Override
    public InputFile execute(Message message) {
        return documentService.getRootCategoriesExel(message.getChatId());
    }
}
