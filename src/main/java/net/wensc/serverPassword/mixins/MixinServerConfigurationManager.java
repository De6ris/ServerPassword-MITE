package net.wensc.serverPassword.mixins;

import net.minecraft.ServerConfigurationManager;
import net.minecraft.ServerPlayer;
import net.wensc.serverPassword.api.IServerPlayer;
import net.wensc.serverPassword.util.PasswordManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerConfigurationManager.class})
public class MixinServerConfigurationManager {
    @Inject(method = {"playerLoggedIn(Lnet/minecraft/ServerPlayer;)V"}, at = {@At("HEAD")})
    public void playerLoggedIn(ServerPlayer serverPlayer, CallbackInfo callbackInfo) {
        PasswordManager passwordManager = ((IServerPlayer) serverPlayer.getAsPlayer()).svpwd$getPasswordManager();
        passwordManager.onLogIn();
    }
}
