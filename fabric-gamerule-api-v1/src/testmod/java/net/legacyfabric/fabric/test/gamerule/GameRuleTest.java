package net.legacyfabric.fabric.test.gamerule;

import net.minecraft.world.GameRuleManager;

import net.fabricmc.api.ModInitializer;

import net.legacyfabric.fabric.api.gamerule.v1.GameRuleRegistrationCallback;

public class GameRuleTest implements ModInitializer {
	@Override
	public void onInitialize() {
		GameRuleRegistrationCallback.EVENT.register(gameRuleManager -> {
			gameRuleManager.addGameRule("someNumberGameRule", "1", GameRuleManager.ValueType.NUMERICAL_VALUE);
			gameRuleManager.addGameRule("someBooleanGameRule", "false", GameRuleManager.ValueType.BOOLEAN_VALUE);
			gameRuleManager.addGameRule("someStrGameRule", "foo", GameRuleManager.ValueType.ANY_VALUE);
		});
	}
}
