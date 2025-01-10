package net.wensc.serverPassword.util;

import net.minecraft.ServerPlayer;
import net.wensc.serverPassword.api.PasswordStatus;

public class PasswordManager {
    private final ServerPlayer serverPlayer;
    private String password = "";

    private PasswordStatus passwordStatus;
    private int passwordTimer;

    public PasswordManager(ServerPlayer serverPlayer) {
        this.serverPlayer = serverPlayer;
    }

    public boolean onUpdate() {
        if (this.passwordStatus == PasswordStatus.IPDIFFERENT) {
            this.passwordTimer = 400;
            this.passwordStatus = PasswordStatus.IPDIFFWAIT;
        }
        if (this.passwordTimer > 0 && this.passwordStatus == PasswordStatus.IPDIFFWAIT) {
            this.passwordTimer--;
            if (this.passwordTimer <= 0) {
                this.serverPlayer.playerNetServerHandler.kickPlayerFromServer("输入密码超时");
            }
        }
        return this.passwordStatus == PasswordStatus.SAMEPWD;
    }

    public boolean isPasswordSet() {
        return !this.password.isEmpty();
    }

    public void setPasswordStatus(PasswordStatus passwordStatus) {
        this.passwordStatus = passwordStatus;
    }

    public PasswordStatus getPasswordStatus() {
        return this.passwordStatus;
    }

    public boolean preventActions() {
        return this.passwordStatus != PasswordStatus.SAMEPWD;
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
        this.passwordTimer = old.passwordTimer;
    }

}
