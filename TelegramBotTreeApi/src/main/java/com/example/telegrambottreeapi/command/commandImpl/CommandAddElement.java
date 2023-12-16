package com.example.telegrambottreeapi.command.commandImpl;

import com.example.telegrambottreeapi.command.Command;
import com.example.telegrambottreeapi.exception.ExceptionResponse;
import com.example.telegrambottreeapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/*
        Command add element:
        Add new root if entered only one category.
        Add new child if entered only two categories,
         where first category is child and the second is parent.
*/
@RequiredArgsConstructor
public class CommandAddElement implements Command<String> {
    /*
        Service to work with categories.
     */
    private final CategoryService categoryService;

    /*
        Executing command:
        message - message of user with payload
     */
    @Override
    public String execute(Message message) {
        String[] commandArgs = message.getText().split(" ");

        if (commandArgs.length == 2) {
            /*
                Add root
             */
            String rootName = commandArgs[1];
            return addRootElement(message.getChatId(), rootName);
        } else if (commandArgs.length == 3) {
            /*
                Add child
             */
            String parentName = commandArgs[1];
            String childName = commandArgs[2];
            return addNewElement(message.getChatId(), parentName, childName);
        } else {
            /*
                Not found command
             */
            return "Неизвестное количество аргументов";
        }
    }

    /*
        Add root element:
        id - chatId
        name - name of category
     */
    private String addRootElement(Long id, String name) {
        try {
            categoryService.saveCategory(id, name);
            return "Добавлена корневая категория";
        } catch (ExceptionResponse e) {
            return e.getMessage();
        }
    }

    /*
        Add child element:
        id - chatId
        parentName - name of parent category
        childName - name of new child category
     */
    public String addNewElement(Long id, String parentName, String childName) {
        try {
            categoryService.saveCategory(id, parentName, childName);
            return "Добавлен дочерний элемент";
        } catch (ExceptionResponse e) {
            return e.getMessage();
        }
    }
}
