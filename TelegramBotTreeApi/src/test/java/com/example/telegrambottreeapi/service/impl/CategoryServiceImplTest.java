package com.example.telegrambottreeapi.service.impl;

import com.example.telegrambottreeapi.entity.Category;
import com.example.telegrambottreeapi.exception.ExceptionResponse;
import com.example.telegrambottreeapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void getRootCategoriesTest() {
        Long chatId = 1L;
        String name = "root";
        Category category = new Category(1L, chatId, name, null, new ArrayList<>());
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryRepository.findByParentIsNullAndChatId(chatId)).thenReturn(categories);

        assertEquals(categories, categoryService.getRootCategories(chatId));
    }

    @Test
    public void saveCategory_rootSuccessful() throws ExceptionResponse {
        Long chatId = 1L;
        String parentCategoryName = "ParentCategory";
        String childCategoryName = "ChildCategory";

        // Mock the behavior of categoryRepository
        Category parentCategory = new Category(1L, chatId, parentCategoryName, null, new ArrayList<>());
        Category childCategory = new Category(1L, chatId, childCategoryName, null, new ArrayList<>());
        childCategory.setParent(parentCategory);
        when(categoryRepository.findByNameAndChatId(parentCategoryName, chatId)).thenReturn(parentCategory);
        when(categoryRepository.findByNameAndChatId(childCategoryName, chatId)).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(childCategory);

        // Call the method under test
        Category savedCategory = categoryService.saveCategory(chatId, parentCategoryName, childCategoryName);

        // Log the savedCategory and other relevant information
        System.out.println("Saved Category: " + savedCategory);

        // Verify interactions and assertions
        verify(categoryRepository).findByNameAndChatId(parentCategoryName, chatId);
        verify(categoryRepository).findByNameAndChatId(childCategoryName, chatId);
        verify(categoryRepository).save(any(Category.class));

        assertNotNull(savedCategory); // Вот здесь происходит ошибка
        assertEquals(childCategoryName, savedCategory.getName());
        assertEquals(parentCategory, savedCategory.getParent());
        assertEquals(chatId, savedCategory.getChatId());
    }

    @Test
    public void saveCategory_ParentCategoryNotFound() {
        Long chatId = 1L;
        String parentCategoryName = "NonexistentParent";
        String childCategoryName = "ChildCategory";

        // Mock the behavior of categoryRepository
        when(categoryRepository.findByNameAndChatId(parentCategoryName, chatId)).thenReturn(null);

        // Call the method under test and expect an ExceptionResponse
        ExceptionResponse exceptionResponse = assertThrows(ExceptionResponse.class,
                () -> categoryService.saveCategory(chatId, parentCategoryName, childCategoryName));

        // Verify interactions and assertions
        verify(categoryRepository).findByNameAndChatId(parentCategoryName, chatId);
        verify(categoryRepository, never()).findByNameAndChatId(childCategoryName, chatId);
        verify(categoryRepository, never()).save(any(Category.class));

        assertEquals("Родительской категории с таким названием нет", exceptionResponse.getMessage());
    }

    @Test
    public void saveCategory_ChildCategoryAlreadyExists() {
        Long chatId = 1L;
        String parentCategoryName = "ParentCategory";
        String childCategoryName = "ExistingChildCategory";

        // Mock the behavior of categoryRepository
        Category parentCategory = new Category(1L, chatId, parentCategoryName, null, new ArrayList<>());
        when(categoryRepository.findByNameAndChatId(parentCategoryName, chatId)).thenReturn(parentCategory);
        when(categoryRepository.findByNameAndChatId(childCategoryName, chatId)).thenReturn(new Category());

        // Call the method under test and expect an ExceptionResponse
        ExceptionResponse exceptionResponse = assertThrows(ExceptionResponse.class,
                () -> categoryService.saveCategory(chatId, parentCategoryName, childCategoryName));

        // Verify interactions and assertions
        verify(categoryRepository).findByNameAndChatId(parentCategoryName, chatId);
        verify(categoryRepository).findByNameAndChatId(childCategoryName, chatId);
        verify(categoryRepository, never()).save(any(Category.class));

        assertEquals("Категория с таким названием уже есть", exceptionResponse.getMessage());
    }

    @Test
    public void saveCategory_Successful() throws ExceptionResponse {
        Long chatId = 1L;
        String categoryName = "NewCategory";

        Category parentCategory = new Category(1L, chatId, categoryName, null, new ArrayList<>());

        // Mock the behavior of categoryRepository
        when(categoryRepository.findByNameAndChatId(categoryName, chatId)).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(parentCategory);

        // Call the method under test
        Category savedCategory = categoryService.saveCategory(chatId, categoryName);

        // Verify interactions and assertions
        verify(categoryRepository).findByNameAndChatId(categoryName, chatId);
        verify(categoryRepository).save(any(Category.class));

        assertNotNull(savedCategory);
        assertEquals(chatId, savedCategory.getChatId());
        assertEquals(categoryName, savedCategory.getName());
    }

    @Test
    public void saveCategory_CategoryAlreadyExists() {
        Long chatId = 1L;
        String categoryName = "ExistingCategory";

        // Mock the behavior of categoryRepository
        when(categoryRepository.findByNameAndChatId(categoryName, chatId)).thenReturn(new Category());

        // Call the method under test and expect an ExceptionResponse
        ExceptionResponse exceptionResponse = assertThrows(ExceptionResponse.class,
                () -> categoryService.saveCategory(chatId, categoryName));

        // Verify interactions and assertions
        verify(categoryRepository).findByNameAndChatId(categoryName, chatId);
        verify(categoryRepository, never()).save(any(Category.class));

        assertEquals("Категория с таким названием уже есть", exceptionResponse.getMessage());
    }

    @Test
    public void deleteCategory_Successful() throws ExceptionResponse {
        Long chatId = 1L;
        String categoryName = "ExistingCategory";

        // Mock the behavior of categoryRepository
        Category category = new Category(1L, chatId, categoryName, null, new ArrayList<>());
        when(categoryRepository.findByNameAndChatId(categoryName, chatId)).thenReturn(category);

        // Call the method under test
        categoryService.deleteCategory(chatId, categoryName);

        // Verify interactions and assertions
        verify(categoryRepository).findByNameAndChatId(categoryName, chatId);
        verify(categoryRepository).save(category);
        verify(categoryRepository).delete(category);
    }

    @Test
    public void deleteCategory_CategoryNotFound() {
        Long chatId = 1L;
        String categoryName = "NonexistentCategory";

        // Mock the behavior of categoryRepository
        when(categoryRepository.findByNameAndChatId(categoryName, chatId)).thenReturn(null);

        // Call the method under test and expect an ExceptionResponse
        ExceptionResponse exceptionResponse = assertThrows(ExceptionResponse.class,
                () -> categoryService.deleteCategory(chatId, categoryName));

        // Verify interactions and assertions
        verify(categoryRepository).findByNameAndChatId(categoryName, chatId);
        verify(categoryRepository, never()).save(any(Category.class));
        verify(categoryRepository, never()).delete(any(Category.class));

        assertEquals("Категории с таким названием нет", exceptionResponse.getMessage());
    }
}
