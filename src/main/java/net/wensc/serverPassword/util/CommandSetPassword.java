package net.wensc.serverPassword.util;

import net.minecraft.ChatMessageComponent;
import net.minecraft.CommandBase;
import net.minecraft.EnumChatFormatting;
import net.minecraft.ICommandSender;
import net.wensc.serverPassword.api.IServerPlayer;
import net.wensc.serverPassword.api.PasswordStatus;

public class CommandSetPassword extends CommandBase {
    @Override
    public String getCommandName() {
        return "setpwd";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.setpwd.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        String password = strings[0];
        PasswordManager passwordManager = ((IServerPlayer) CommandBase.getCommandSenderAsPlayer(iCommandSender).getAsPlayer()).svpwd$getPasswordManager();
        if (passwordManager.isPasswordSet() && passwordManager.preventActions()) {
            iCommandSender.sendChatToPlayer(ChatMessageComponent.createFromText("无权操作"));
            LogWriter.writeLog(iCommandSender.getCommandSenderName(), "尝试重置密码，密码：" + password);
        } else {
            passwordManager.setPassword(password);
            passwordManager.setPasswordStatus(PasswordStatus.SAME_PWD);
            iCommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("设置密码成功").setColor(EnumChatFormatting.AQUA));
            LogWriter.writeLog(iCommandSender.getCommandSenderName(), "设置密码成功，密码：" + password);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        return true;
    }
}
