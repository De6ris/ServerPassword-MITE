package net.wensc.serverPassword.util;

import net.minecraft.ChatMessageComponent;
import net.minecraft.EnumChatFormatting;
import net.minecraft.ServerPlayer;
import net.wensc.serverPassword.api.PasswordStatus;

public class PasswordManager {
    private final ServerPlayer serverPlayer;
    private String password = "";
    private PasswordStatus passwordStatus;
    private int enterPwdTimer;
    private int setPwdTimer;

    public PasswordManager(ServerPlayer serverPlayer) {
        this.serverPlayer = serverPlayer;
    }

    public boolean onUpdate() {
        if (this.enterPwdTimer > 0 && this.waitingForPwd()) {
            if (this.enterPwdTimer % 100 == 0) {
                this.noticeEnterPwd();
            }
            this.enterPwdTimer--;
            if (this.enterPwdTimer <= 0) {
                this.serverPlayer.playerNetServerHandler.kickPlayerFromServer("输入密码超时");
            }
        }
        if (this.notSetPwd()) {
            if (this.setPwdTimer % 100 == 0) {
                this.noticeSetPwd();
            }
            this.setPwdTimer++;
        }
        return this.passwordStatus == PasswordStatus.SAME_PWD;
    }

    public boolean isPasswordSet() {
        return !this.password.isEmpty();
    }

    public void onLogIn() {
        if (this.isPasswordSet()) {
            this.enterPwdTimer = 400;
            this.passwordStatus = PasswordStatus.IP_DIFF_WAIT;
        } else {
            this.passwordStatus = PasswordStatus.NOT_SET_PWD;
        }
    }

    public void noticeSetPwd() {
        serverPlayer.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("请设置初始密码：/setpwd 密码").setColor(EnumChatFormatting.DARK_PURPLE));
    }

    public void noticeEnterPwd(){
        serverPlayer.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("此账号已注册，请20S内输入密码登录：/login 密码").setColor(EnumChatFormatting.DARK_PURPLE));
    }

    public boolean waitingForPwd() {
        return this.passwordStatus == PasswordStatus.IP_DIFF_WAIT;
    }

    public boolean notSetPwd() {
        return this.passwordStatus == PasswordStatus.NOT_SET_PWD;
    }

    public void setPasswordStatus(PasswordStatus passwordStatus) {
        this.passwordStatus = passwordStatus;
    }

    public boolean preventActions() {
        return !this.samePwd();
    }

    public boolean samePwd() {
        return this.passwordStatus == PasswordStatus.SAME_PWD;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean checkPassword(String test) {
        return this.password.equals(test);
    }

    public void cloneFrom(PasswordManager old) {
        this.password = old.password;
        this.passwordStatus = old.passwordStatus;
        this.enterPwdTimer = old.enterPwdTimer;
    }

}
