package org.jlortiz.ConcurrencyFix.mixin;

import net.minecraft.advancement.criterion.AbstractCriterion;
import org.jlortiz.ConcurrencyFix.ConcurrencyFixLock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractCriterion.class)
public class CriterionMixin {
	private ConcurrencyFixLock l;
	@Inject(at = @At("RETURN"), method="<init>")
	private void initLock(CallbackInfo info) {
		this.l = new ConcurrencyFixLock();
	}

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

	@Inject(at = @At("HEAD"), method = "endTrackingCondition")
	private void endLock2(CallbackInfo info) {
		l.lock();
	}

	@Inject(at = @At("RETURN"), method = "endTrackingCondition")
	private void endUnlock2(CallbackInfo info) {
		l.unlock();
	}
}
