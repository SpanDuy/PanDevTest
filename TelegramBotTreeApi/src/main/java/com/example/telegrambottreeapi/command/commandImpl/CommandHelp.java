package com.example.telegrambottreeapi.command.commandImpl;

import com.example.telegrambottreeapi.command.Command;
import com.example.telegrambottreeapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/*
        Command Help:
        Returns string with all existing commands
 */
@RequiredArgsConstructor
public class CommandHelp implements Command<String> {
    private final CategoryService categoryService;

    @Override
    public String execute(Message message) {
        String[] commands = new String[] {
                "/help - вывести все команды",
                "/viewTree - вывести все деревья запросов",
                "/addElement <название элемента> - создать корневой элемент",
                "/addElement <родительский элемент> <дочерний элемент> - добавить дочерний элемент родительскому",
                "/removeElement <название элемента> - удаляет элемент и все его дочерние",
                "/download - вывести все деревья запросов ввиде exel файла"
        };
        return String.join("\n", commands);
    }
}
