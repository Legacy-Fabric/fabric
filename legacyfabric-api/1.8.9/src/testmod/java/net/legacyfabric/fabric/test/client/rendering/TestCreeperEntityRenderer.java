/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import net.legacyfabric.fabric.test.registry.RegistryTest;

public class TestCreeperEntityRenderer extends MobEntityRenderer<RegistryTest.TestCreeperEntity> {
	private static final Identifier TEXTURE = new Identifier("legacy-fabric-api", "textures/entity/creeper/creeper.png");

	public TestCreeperEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new CreeperEntityModel(), 0.5F);
//		this.addFeature(new CreeperLightningFeatureRenderer(this));
	}

	protected void scale(RegistryTest.TestCreeperEntity creeperEntity, float f) {
		float g = creeperEntity.getClientFuseTime(f);
		float h = 1.0F + MathHelper.sin(g * 100.0F) * g * 0.01F;
		g = MathHelper.clamp(g, 0.0F, 1.0F);
		g *= g;
		g *= g;
		float i = (1.0F + g * 0.4F) * h;
		float j = (1.0F + g * 0.1F) / h;
		GlStateManager.scale(i, j, i);
	}

	protected int method_5776(RegistryTest.TestCreeperEntity creeperEntity, float f, float g) {
		float h = creeperEntity.getClientFuseTime(g);
		if ((int)(h * 10.0F) % 2 == 0) {
			return 0;
		} else {
			int i = (int)(h * 0.2F * 255.0F);
			i = MathHelper.clamp(i, 0, 255);
			return i << 24 | 822083583;
		}
	}

	protected Identifier getTexture(RegistryTest.TestCreeperEntity creeperEntity) {
		return TEXTURE;
	}
}
