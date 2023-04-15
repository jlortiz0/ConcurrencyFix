package org.jlortiz.ConcurrencyFix.mixin;

import net.minecraft.client.sound.SoundSystem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

	@Inject(at = @At(value = "FIELD", target="Lnet/minecraft/client/sound/SoundSystem;tickingSounds:Ljava/util/List;", opcode = Opcodes.GETFIELD), method = "play(Lnet/minecraft/client/sound/SoundInstance;)V")
	private void playLock(CallbackInfo info) {
		l.lock();
	}

	@Inject(at = @At("TAIL"), method = "play(Lnet/minecraft/client/sound/SoundInstance;)V")
	private void playUnlock(CallbackInfo info) {
		l.unlock();
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
