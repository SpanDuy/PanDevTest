package com.example.telegrambottreeapi.command.commandImpl;

import com.example.telegrambottreeapi.command.Command;
import org.telegram.telegrambots.meta.api.objects.Message;

/*
        Default command:
        This command returns when user enter
        not existing command
 */
public class CommandDefault implements Command<String> {
    /*
        Executing command:
        message - message of user with payload
        returns string with not existing command
     */
    @Override
    public String execute(Message message) {
        return "Команды " + message.getText() + " нет";
    }
}
