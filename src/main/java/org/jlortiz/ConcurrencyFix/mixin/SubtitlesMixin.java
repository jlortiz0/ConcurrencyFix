package org.jlortiz.ConcurrencyFix.mixin;

import net.minecraft.client.gui.hud.SubtitlesHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Mixin(SubtitlesHud.class)
public class SubtitlesMixin {
    private final Lock l = new ReentrantLock();

    @Inject(method = "render", at=@At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;defaultBlendFunc()V"))
    private void renderLock(CallbackInfo ci) {
        l.lock();
    }

    @Inject(method = "render", at=@At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V"))
    private void renderUnlock(CallbackInfo ci) {
        l.unlock();
    }

    @Inject(method = "onSoundPlayed", at=@At("HEAD"))
    private void onPlayedLock(CallbackInfo ci) {
        l.lock();
    }

    @Inject(method = "onSoundPlayed", at=@At("RETURN"))
    private void onPlayedUnlock(CallbackInfo ci) {
        l.unlock();
    }
}
