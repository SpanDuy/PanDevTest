package com.example.telegrambottreeapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/*
        Category entity
 */
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
            ID of chat with user who send this category
     */
    private Long chatId;

    /*
            Name of category
     */
    private String name;

    /*
            Parent category.
            Root category have null.
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    /*
            List with child categories
     */
    @OneToMany(mappedBy = "parent",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Category> categoryChildes;

    /*
            Overriding for structure view
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n {\n");
        buildCategoryTree(sb, this, 0);
        sb.append(" }\n");
        return sb.toString();
    }

    /*
            Making of structure format things to tabulation.
     */
    private void buildCategoryTree(StringBuilder response, Category category, int depth) {
        for (int i = 0; i < depth; i++) {
            response.append("       "); // Добавляем отступы для визуального отображения уровня вложенности
        }
        response.append("- ").append(category.getName()).append("\n");

        List<Category> childCategories = category.getCategoryChildes();
        for (Category childCategory : childCategories) {
            buildCategoryTree(response, childCategory, depth + 1);
        }
    }
}
