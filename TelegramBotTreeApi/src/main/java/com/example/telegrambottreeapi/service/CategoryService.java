package com.example.telegrambottreeapi.service;

import com.example.telegrambottreeapi.entity.Category;
import com.example.telegrambottreeapi.exception.ExceptionResponse;

import java.util.List;

public interface CategoryService {
    List<Category> getRootCategories(Long chatId);
    Category saveCategory(Long chatId, String parentCategory) throws ExceptionResponse;
    Category saveCategory(Long chatId, String parentCategory, String category) throws ExceptionResponse;
    void deleteCategory(Long chatId, String categoryName) throws ExceptionResponse;
}
