package com.example.telegrambottreeapi.repository;

import com.example.telegrambottreeapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
        Repository with categories
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /*
            Find all roots categories
            Out: List with root categories
     */
    List<Category> findByParentIsNull();
    /*
            Find roots categories of chat
            In: ID if chat
            Out: List with root categories of chat
     */
    List<Category> findByParentIsNullAndChatId(Long chatId);
    /*
            Find by parent id
            In: ID of parent
            Out: List of child categories
     */
    List<Category> findByParentId(Long parentId);
    /*
            Find category by name
            In: Name of category
            Out: Found category
     */
    Category findByName(String name);
    /*
            Find category of chat
            In: Name of category
                ID of chat with this category
            Out: Found category
     */
    Category findByNameAndChatId(String name, Long id);
}