package com.example.telegrambottreeapi.service.impl;

import com.example.telegrambottreeapi.entity.Category;
import com.example.telegrambottreeapi.exception.ExceptionResponse;
import com.example.telegrambottreeapi.repository.CategoryRepository;
import com.example.telegrambottreeapi.service.CategoryService;
import com.example.telegrambottreeapi.service.DocumentService;
import lombok.AllArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/*
        Service for working with documents
 */
@Service
@AllArgsConstructor
public class DocumentServiceExel implements DocumentService {
    private final CategoryService categoryService;

    /*
            Make Exel document with categories
     */
    @Override
    public InputFile getRootCategoriesExel(Long chatId) {
        List<Category> categories = categoryService.getRootCategories(chatId);
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

    /*
            Make structure format ot exel document
     */
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

    /*
            Save categories from exel document
     */
    @Override
    public void addExelCategories(Long chatId, InputFile inputFile) throws ExceptionResponse, IOException, InvalidFormatException {
        File excelFile = inputFile.getNewMediaFile();
        Workbook workbook = new XSSFWorkbook(excelFile);

        Sheet sheet = workbook.getSheetAt(0);

        for (int rowNum = 0; ; rowNum++) {
            Row row = sheet.createRow(rowNum);
            Cell cellParent = row.createCell(0);
            Cell cellChild = row.createCell(1);

            if (cellParent == null) {
                break;
            }
            if (cellChild == null) {
                categoryService.saveCategory(chatId, cellParent.getStringCellValue());
            } else {
                categoryService.saveCategory(chatId, cellParent.getStringCellValue(), cellChild.getStringCellValue());
            }
        }
    }
}
