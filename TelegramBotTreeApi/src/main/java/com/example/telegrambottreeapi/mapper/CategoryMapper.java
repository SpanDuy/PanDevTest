package com.example.telegrambottreeapi.mapper;

import com.example.telegrambottreeapi.entity.Category;

public class CategoryMapper {
    public static Category mapToCategory(String name) {
        return Category.builder()
                .name(name)
                .build();
    }
}
