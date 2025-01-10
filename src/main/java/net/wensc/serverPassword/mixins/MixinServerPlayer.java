package net.wensc.serverPassword.mixins;

import net.minecraft.*;
import net.wensc.serverPassword.api.IServerPlayer;
import net.wensc.serverPassword.util.LogWriter;
import net.wensc.serverPassword.util.PasswordManager;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ServerPlayer.class})
public abstract class MixinServerPlayer extends EntityPlayer implements IServerPlayer {
    @Unique
    private PasswordManager passwordManager = new PasswordManager(ReflectHelper.dyCast(this));

    @Override
    public PasswordManager svpwd$getPasswordManager() {
        return this.passwordManager;
    }

    public MixinServerPlayer(World par1World, String par2Str) {
        super(par1World, par2Str);
    }

    @Inject(method = {"travelToDimension(I)V"}, at = {@At("HEAD")}, cancellable = true)
    public void preventTravelToDimension(int par1, CallbackInfo callbackInfo) {
        if (this.passwordManager.preventActions()) {
            callbackInfo.cancel();
        } else {
            LogWriter.writeLog(this.getEntityName(), "正在跨世界传送,坐标：" + LogWriter.getDimName(this.worldObj.getDimensionId()) + "(" + (int) this.posX + "," + (int) this.posY + "," + (int) this.posZ + "),目标世界：" + LogWriter.getDimName(par1));
        }
    }

    @Inject(method = {"setPositionAndUpdate(DDD)V"}, at = {@At("HEAD")}, cancellable = true)
    public void preventPositionUpdate(double par1, double par3, double par5, CallbackInfo callbackInfo) {
        if (this.passwordManager.preventActions()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = {"attackEntityFrom(Lnet/minecraft/Damage;)Lnet/minecraft/EntityDamageResult;"}, at = {@At("HEAD")}, cancellable = true)
    public void attackEntityFrom(Damage damage, CallbackInfoReturnable<EntityDamageResult> callbackInfoReturnable) {
        if (this.passwordManager.preventActions()) {
            callbackInfoReturnable.setReturnValue(null);
            callbackInfoReturnable.cancel();
        }
    }

    @Inject(method = {"clonePlayer(Lnet/minecraft/EntityPlayer;Z)V"}, at = {@At("RETURN")})
    public void injectClonePlayer(EntityPlayer par1EntityPlayer, boolean par2, CallbackInfo callbackInfo) {
        this.passwordManager.cloneFrom(((IServerPlayer) par1EntityPlayer).svpwd$getPasswordManager());
    }

    @Inject(method = {"onUpdate()V"}, at = {@At("HEAD")}, cancellable = true)
    public void injectBeforeUpdate(CallbackInfo callbackInfo) {
        if (!this.passwordManager.onUpdate()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = {"readEntityFromNBT(Lnet/minecraft/NBTTagCompound;)V"}, at = {@At("RETURN")})
    public void injectReadNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo callbackInfo) {
        if (par1NBTTagCompound.hasKey("password")) {
            this.passwordManager.setPassword(par1NBTTagCompound.getString("password"));
        }
    }

    @Inject(method = {"writeEntityToNBT(Lnet/minecraft/NBTTagCompound;)V"}, at = {@At("RETURN")})
    public void injectWriteNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo callbackInfo) {
        par1NBTTagCompound.setString("password", this.passwordManager.getPassword());
    }
}
