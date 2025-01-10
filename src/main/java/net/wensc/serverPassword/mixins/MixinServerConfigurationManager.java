package net.wensc.serverPassword.mixins;

import net.minecraft.ChatMessageComponent;
import net.minecraft.EnumChatFormatting;
import net.minecraft.ServerConfigurationManager;
import net.minecraft.ServerPlayer;
import net.wensc.serverPassword.api.CustomServerPlayer;
import net.wensc.serverPassword.api.PasswordStatus;
import net.wensc.serverPassword.util.PasswordManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerConfigurationManager.class})
public class MixinServerConfigurationManager {
    @Inject(method = {"playerLoggedIn(Lnet/minecraft/ServerPlayer;)V"}, at = {@At("HEAD")})
    public void playerLoggedIn(ServerPlayer serverPlayer, CallbackInfo callbackInfo) {
        PasswordManager passwordManager = ((CustomServerPlayer) serverPlayer.getAsPlayer()).getPasswordManager();
        if (passwordManager.isPasswordSet()) {
            serverPlayer.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("此账号已注册，请20S内输入密码登录：/login 密码").setColor(EnumChatFormatting.DARK_PURPLE));
            passwordManager.setPasswordStatus(PasswordStatus.IPDIFFERENT);
        } else {
            serverPlayer.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("请设置初始密码：/setpwd 密码").setColor(EnumChatFormatting.DARK_PURPLE));
        }
    }
}
