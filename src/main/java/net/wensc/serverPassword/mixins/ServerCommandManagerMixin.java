package net.wensc.serverPassword.mixins;

import net.minecraft.CommandHandler;
import net.minecraft.ServerCommandManager;
import net.wensc.serverPassword.util.CommandLogin;
import net.wensc.serverPassword.util.CommandSetPassword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommandManager.class)
public class ServerCommandManagerMixin extends CommandHandler {
    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void injectInit(CallbackInfo callbackInfo) {
        registerCommand(new CommandSetPassword());
        registerCommand(new CommandLogin());
    }
}
