package com.example.telegrambottreeapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Category> categoryChildes;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n {\n");
        buildCategoryTree(sb, this, 0);
        sb.append(" }\n");
        return sb.toString();
    }

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
