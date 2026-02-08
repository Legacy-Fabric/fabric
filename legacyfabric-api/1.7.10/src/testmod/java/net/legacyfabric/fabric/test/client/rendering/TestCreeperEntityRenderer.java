/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.test.client.rendering;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.model.Model;
import net.minecraft.client.render.model.entity.CreeperModel;
import net.minecraft.client.resource.Identifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.mob.MobEntity;
import net.minecraft.util.math.MathHelper;

import net.legacyfabric.fabric.test.registry.RegistryTest;

public class TestCreeperEntityRenderer extends MobRenderer {
	private static final Identifier field_6472 = new Identifier("textures/entity/creeper/creeper_armor.png");
	private static final Identifier TEXTURE = new Identifier("legacy-fabric-api", "textures/entity/creeper/creeper.png");
	private Model field_2086 = new CreeperModel(2.0F);

	public TestCreeperEntityRenderer() {
		super(new CreeperModel(), 0.5F);
	}

	@Override
	public void render(Entity entity, double x, double y, double z, float yaw, float tickDelta) {
		super.render((MobEntity) entity, x, y, z, yaw, tickDelta);
	}

	protected void applyScale(LivingEntity entity, float f) {
		RegistryTest.TestCreeperEntity creeperEntity = (RegistryTest.TestCreeperEntity) entity;
		float var3 = creeperEntity.getFuse(f);
		float var4 = 1.0F + MathHelper.sin(var3 * 100.0F) * var3 * 0.01F;

		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}

		var3 *= var3;
		var3 *= var3;
		float var5 = (1.0F + var3 * 0.4F) * var4;
		float var6 = (1.0F + var3 * 0.1F) / var4;
		GL11.glScalef(var5, var6, var5);
	}

	protected int getOverlayColor(LivingEntity entity, float f, float g) {
		RegistryTest.TestCreeperEntity creeperEntity = (RegistryTest.TestCreeperEntity) entity;
		float var4 = creeperEntity.getFuse(g);

		if ((int) (var4 * 10.0F) % 2 == 0) {
			return 0;
		} else {
			int var5 = (int) (var4 * 0.2F * 255.0F);

			if (var5 < 0) {
				var5 = 0;
			}

			if (var5 > 255) {
				var5 = 255;
			}

			short var6 = 255;
			short var7 = 255;
			short var8 = 255;
			return var5 << 24 | var6 << 16 | var7 << 8 | var8;
		}
	}

	protected int bindTexture(LivingEntity entity, int i, float f) {
		RegistryTest.TestCreeperEntity creeperEntity = (RegistryTest.TestCreeperEntity) entity;

		if (creeperEntity.isCharged()) {
			GL11.glDepthMask(!creeperEntity.isInvisible());

			if (i == 1) {
				float var4 = (float) creeperEntity.ticks + f;
				this.bindTexture(field_6472);
				GL11.glMatrixMode(5890);
				GL11.glLoadIdentity();
				float var5 = var4 * 0.01F;
				float var6 = var4 * 0.01F;
				GL11.glTranslatef(var5, var6, 0.0F);
				this.setDecorationModel(this.field_2086);
				GL11.glMatrixMode(5888);
				GL11.glEnable(3042);
				float var7 = 0.5F;
				GL11.glColor4f(var7, var7, var7, 1.0F);
				GL11.glDisable(2896);
				GL11.glBlendFunc(1, 1);
				return 1;
			}

			if (i == 2) {
				GL11.glMatrixMode(5890);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(5888);
				GL11.glEnable(2896);
				GL11.glDisable(3042);
			}
		}

		return -1;
	}

	protected int bindDecorationTexture(LivingEntity creeperEntity, int i, float f) {
		return -1;
	}

	@Override
	protected Identifier getTextureLocation(Entity entity) {
		return TEXTURE;
	}
}
