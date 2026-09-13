package net.legacyfabric.fabric.test.registry;

import net.minecraft.entity.living.mob.monster.CreeperEntity;
import net.minecraft.world.World;

public class TestCreeperEntity extends CreeperEntity {
	public TestCreeperEntity(World world) {
		super(world);
	}

	@Override
	public void tick() {
		if (this.isAlive()) {
			if (this.hasStatusEffect(RegistryTest.EFFECT)) {
				this.setIgnited();
			}
		}

		super.tick();
	}
}
