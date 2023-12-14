package com.example.telegrambottreeapi.service.impl;

import com.example.telegrambottreeapi.entity.Category;
import com.example.telegrambottreeapi.exception.ExceptionResponse;
import com.example.telegrambottreeapi.repository.CategoryRepository;
import com.example.telegrambottreeapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public String getRootCategories(Long chatId) {
        return categoryRepository.findByParentIsNullAndChatId(chatId).toString();
    }

    @Override
    public Category saveCategory(Long chatId, String name) throws ExceptionResponse {
        if (categoryRepository.findByNameAndChatId(name, chatId) != null) {
            throw new ExceptionResponse("Категория с таким названием уже есть");
        }
        Category category = Category.builder().chatId(chatId).name(name).build();
        return categoryRepository.save(category);
    }

    @Override
    public Category saveCategory(Long chatId, String parentCategoryName, String childCategoryName) throws ExceptionResponse {
        Category parentCategory = categoryRepository.findByName(parentCategoryName);
        if (parentCategory == null) {
            throw new ExceptionResponse("Родительской категории с таким названием нет");
        }
        if (categoryRepository.findByNameAndChatId(childCategoryName, chatId) != null) {
            throw new ExceptionResponse("Категория с таким названием уже есть");
        }
        Category childCategory = Category.builder()
                .chatId(chatId)
                .name(childCategoryName)
                .parent(parentCategory)
                .build();
        parentCategory.getCategoryChildes().add(childCategory);
        return categoryRepository.save(childCategory);
    }

    @Override
    public void deleteCategory(Long chatId, String categoryName) throws ExceptionResponse {
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new ExceptionResponse("Категории с таким названием нет");
        }

        category.setParent(null);
        categoryRepository.save(category);

        categoryRepository.delete(category);
    }
}
