package com.superexercisebook.justchat.command;

import com.superexercisebook.justchat.Justchat;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandRegister {


    public CommandRegister(Justchat justchat) {

        ReloadCommand reloadCommand = new ReloadCommand(justchat);
        CommandSpec reloadCommandSpec = CommandSpec.builder()
                .description(Text.of("Justchat"))
                .permission("justchat.admin.reload")
                .executor(reloadCommand)
                .build();

        MainCommand mainCommand = new MainCommand(justchat);
        CommandSpec mainCommandSpec = CommandSpec.builder()
                .description(Text.of("Justchat"))
                .permission("justchat.user")
                .executor(mainCommand)
                .child(reloadCommandSpec, "jc")
                .build();

        Sponge.getCommandManager().register(justchat, mainCommandSpec, "justchat");

    }
}
