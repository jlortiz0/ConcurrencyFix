package org.jlortiz.ConcurrencyFix.mixin;

import net.minecraft.advancement.criterion.AbstractCriterion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Mixin(AbstractCriterion.class)
public class CriterionMixin {
	private final Lock l = new ReentrantLock();
	@Inject(at = @At("HEAD"), method = "beginTrackingCondition")
	private void beginLock(CallbackInfo info) {
		l.lock();
	}

	@Inject(at = @At("RETURN"), method = "beginTrackingCondition")
	private void beginUnlock(CallbackInfo info) {
		l.unlock();
	}

	@Inject(at = @At("HEAD"), method = "trigger")
	private void triggerLock(CallbackInfo info) {
		l.lock();
	}

	@Inject(at = @At("RETURN"), method = "trigger")
	private void triggerUnlock(CallbackInfo info) {
		l.unlock();
	}

	@Inject(at = @At("HEAD"), method = "endTracking")
	private void endLock(CallbackInfo info) {
		l.lock();
	}

	@Inject(at = @At("RETURN"), method = "endTracking")
	private void endUnlock(CallbackInfo info) {
		l.unlock();
	}
}
