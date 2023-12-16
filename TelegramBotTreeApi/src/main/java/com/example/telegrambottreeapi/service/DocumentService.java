package com.example.telegrambottreeapi.service;

import com.example.telegrambottreeapi.exception.ExceptionResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.IOException;

public interface DocumentService {
    InputFile getRootCategoriesExel(Long chatId);
    void addExelCategories(Long chatId, InputFile inputFile) throws ExceptionResponse, IOException, InvalidFormatException;
}
