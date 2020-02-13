package com.superexercisebook.justchat.command;

import com.superexercisebook.justchat.GlobalState;
import com.superexercisebook.justchat.Justchat;
import com.superexercisebook.justchat.config.Settings;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class ReloadCommand implements CommandExecutor {
    private Justchat plugin;

    public ReloadCommand(Justchat justchat) {
        plugin = justchat;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        GlobalState.config = new Settings();
        GlobalState.client.updateConfig();
        return CommandResult.success();
    }
}
