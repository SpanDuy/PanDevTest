package com.example.telegrambottreeapi.service;

import org.telegram.telegrambots.meta.api.objects.InputFile;

public interface DocumentService {
    InputFile getRootCategoriesExel(Long chatId);
}
