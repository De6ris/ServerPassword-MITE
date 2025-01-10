package net.wensc.serverPassword.mixins;

import net.minecraft.NetServerHandler;
import net.minecraft.Packet85SimpleSignal;
import net.minecraft.ServerPlayer;
import net.wensc.serverPassword.api.IServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetServerHandler.class})
public class MixinNetServerHandler {
    @Shadow
    public ServerPlayer playerEntity;

    @Inject(method = {"handleSimpleSignal(Lnet/minecraft/Packet85SimpleSignal;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/ServerPlayer;dropOneItem(Z)Lnet/minecraft/EntityItem;")}, cancellable = true)
    public void preventDrop(Packet85SimpleSignal packet, CallbackInfo callbackInfo) {
        if (((IServerPlayer) this.playerEntity.getAsPlayer()).svpwd$getPasswordManager().preventActions()) {
            callbackInfo.cancel();
        }
    }
}
