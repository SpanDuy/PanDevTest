package com.example.telegrambottreeapi.service.impl;

import com.example.telegrambottreeapi.entity.Category;
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
    public String getRootCategories() {
        return categoryRepository.findByParentIsNull().toString();
    }

    @Override
    public Category saveCategory(String name) {
        Category category = Category.builder().name(name).build();
        return categoryRepository.save(category);
    }

    @Override
    public Category saveCategory(String parentCategoryName, String childCategoryName) {
        Category parentCategory = categoryRepository.findByName(parentCategoryName);
        Category childCategory = Category.builder()
                .name(childCategoryName)
                .parent(parentCategory)
                .build();
        parentCategory.getCategoryChildes().add(childCategory);
        return categoryRepository.save(childCategory);
    }

    @Override
    public void deleteCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);

        // category.setParent(null);
        // categoryRepository.save(category);

        categoryRepository.delete(category);
    }
}
