package com.superexercisebook.justchat.command;

import com.superexercisebook.justchat.Justchat;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class MainCommand implements CommandExecutor {
    private Justchat plugin;

    public MainCommand(Justchat justchat) {
        plugin = justchat;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return CommandResult.success();
    }
}
