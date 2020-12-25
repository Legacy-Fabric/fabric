package net.legacyfabric.fabric.api.fluid.volume.v1;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

import net.legacyfabric.fabric.api.fluid.volume.v1.settings.FluidSettings;

public class FluidType {
	private static final Registry<Identifier, FluidType> FLUID_TYPES = new SimpleRegistry<>();

	protected final Identifier id;
	protected final FluidBlock fluidBlock;
	protected final FluidSettings settings;

	private FluidType(Identifier id, FluidBlock fluidBlock, FluidSettings settings) {
		this.id = id;
		this.fluidBlock = fluidBlock;
		this.settings = settings;
	}

	public Identifier getId() {
		return this.id;
	}

	public FluidSettings getSettings() {
		return this.settings;
	}

	@Override
	public String toString() {
		return "FluidType{" + "id=" + this.id +
				", fluidBlock=" + this.fluidBlock.getTranslatedName() +
				", settings=" + this.settings +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof FluidType)) {
			return false;
		}

		FluidType fluidType = (FluidType) o;
		return Objects.equals(this.id, fluidType.id) && Objects.equals(this.fluidBlock, fluidType.fluidBlock) && Objects.equals(this.settings, fluidType.settings);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.fluidBlock, this.settings);
	}

	public static Optional<FluidType> get(Identifier id) {
		return Optional.ofNullable(FLUID_TYPES.get(id));
	}

	public static FluidType getOrCreate(FluidBlock block, @Nullable FluidSettings settings) {
		Identifier id = Block.REGISTRY.getIdentifier(block);
		Preconditions.checkNotNull(id, "block was not registered");
		return get(id).orElseGet(() -> new FluidType(id, block, Objects.requireNonNull(settings)));
	}
}
