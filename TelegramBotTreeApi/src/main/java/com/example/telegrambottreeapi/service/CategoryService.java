package com.example.telegrambottreeapi.service;

import com.example.telegrambottreeapi.entity.Category;

import java.util.List;

public interface CategoryService {
    String getRootCategories();
    Category saveCategory(String parentCategory);
    Category saveCategory(String parentCategory, String category);
    void deleteCategory(String categoryName);
}
