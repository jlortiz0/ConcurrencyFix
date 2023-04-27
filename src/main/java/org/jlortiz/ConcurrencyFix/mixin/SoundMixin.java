package org.jlortiz.ConcurrencyFix.mixin;

import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
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
	private <V> boolean playAdd(List<V> instance, V e) {
		l.lock();
		instance.add(e);
		l.unlock();
		return true;
	}

	@Redirect(at=@At(value="INVOKE", target="Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), method="play(Lnet/minecraft/client/sound/SoundInstance;)V")
	private <K, V> V playPut(Map<K,V> map, K key, V value) {
		l.lock();
		V prev = map.put(key, value);
		l.unlock();
		return prev;
	}

	@Inject(at = @At("HEAD"), method = "stopAll()V")
	private void stopAllLock(CallbackInfo info) {
		l.lock();
	}

	@Inject(at = @At("RETURN"), method = "stopAll()V")
	private void stopAllUnlock(CallbackInfo info) {
		l.unlock();
	}

	@Inject(at = @At("HEAD"), method = "stop(Lnet/minecraft/client/sound/SoundInstance;)V")
	private void stopLock(CallbackInfo info) { l.lock(); }

	@Inject(at = @At("RETURN"), method = "stop(Lnet/minecraft/client/sound/SoundInstance;)V")
	private void stopUnlock(CallbackInfo info) { l.unlock(); }
}
