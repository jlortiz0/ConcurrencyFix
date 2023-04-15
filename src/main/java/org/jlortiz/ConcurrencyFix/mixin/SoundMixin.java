package org.jlortiz.ConcurrencyFix.mixin;

import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.TickableSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Mixin(SoundSystem.class)
public class SoundMixin {
	private final Lock l = new ReentrantLock();

	@Inject(at = @At("HEAD"), method = "tick()V")
	private void tickLock(CallbackInfo info) {
		l.lock();
	}

	@Inject(at = @At("RETURN"), method = "tick()V")
	private void tickUnlock(CallbackInfo info) {
		l.unlock();
	}

	@Redirect(at = @At(value = "INVOKE", target="Ljava/util/List;add(Ljava/lang/Object;)Z"), method = "play(Lnet/minecraft/client/sound/SoundInstance;)V")
	private boolean playAdd(List instance, Object e) {
		l.lock();
		((List<TickableSoundInstance>)instance).add((TickableSoundInstance)e);
		l.unlock();
		return true;
	}

	@Inject(at = @At("HEAD"), method = "stopAll()V")
	private void stopLock(CallbackInfo info) {
		l.lock();
	}

	@Inject(at = @At("RETURN"), method = "stopAll()V")
	private void stopUnlock(CallbackInfo info) {
		l.unlock();
	}
}
