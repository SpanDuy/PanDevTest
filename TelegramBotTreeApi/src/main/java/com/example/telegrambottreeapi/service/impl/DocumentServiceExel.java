package com.example.telegrambottreeapi.service.impl;

import com.example.telegrambottreeapi.entity.Category;
import com.example.telegrambottreeapi.repository.CategoryRepository;
import com.example.telegrambottreeapi.service.DocumentService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class DocumentServiceExel implements DocumentService {
    private final CategoryRepository categoryRepository;

    @Override
    public InputFile getRootCategoriesExel(Long chatId) {
        List<Category> categories = categoryRepository.findByParentIsNullAndChatId(chatId);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Category Tree");

        int rowNum = 0;

        for (Category category : categories) {
            rowNum += buildCategoryTree(sheet, category, rowNum, 0);
        }

        File excelFile = new File("workbook.xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(excelFile)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new InputFile(excelFile, "exel.xlsx");
    }

    private static int buildCategoryTree(Sheet sheet, Category category, int rowNum, int depth) {
        Row row = sheet.createRow(rowNum);

        Cell cell = row.createCell(depth);
        cell.setCellValue(category.getName());

        List<Category> childCategories = category.getCategoryChildes();
        int childRowNum = rowNum + 1;
        for (Category childCategory : childCategories) {
            childRowNum = buildCategoryTree(sheet, childCategory, childRowNum, depth + 1);
        }
        return childRowNum;
    }
}
