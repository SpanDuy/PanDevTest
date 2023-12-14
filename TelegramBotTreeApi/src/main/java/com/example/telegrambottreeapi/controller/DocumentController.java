package com.example.telegrambottreeapi.controller;

import com.example.telegrambottreeapi.service.CategoryService;
import com.example.telegrambottreeapi.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Component
@AllArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    public InputFile handleDownloadCommand(Long chatId, String[] commandArgs) {
        return documentService.getRootCategoriesExel(chatId);
    }
}
