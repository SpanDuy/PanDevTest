package com.example.telegrambottreeapi.repository;

import com.example.telegrambottreeapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNull();
    List<Category> findByParentIsNullAndChatId(Long chatId);
    List<Category> findByParentId(Long parentId);
    Category findByName(String name);
    Category findByNameAndChatId(String name, Long id);
}