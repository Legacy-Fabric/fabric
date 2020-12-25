package net.legacyfabric.fabric.impl.fluid.volume;

import java.awt.Color;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import net.legacyfabric.fabric.api.fluid.volume.v1.settings.DensityUnits;
import net.legacyfabric.fabric.api.fluid.volume.v1.settings.FluidSettings;
import net.legacyfabric.fabric.api.fluid.volume.v1.settings.TemperatureUnits;

public class ImmutableFluidSettings implements FluidSettings {
	private final Color color;
	private final int luminance;
	private final int temperature;
	private final float density;

	ImmutableFluidSettings(Color color, int luminance, int temperature, float density) {
		this.color = color;
		this.luminance = luminance;
		this.temperature = temperature;
		this.density = density;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public int getLuminance() {
		return this.luminance;
	}

	@Override
	public int getTemperature() {
		return this.temperature;
	}

	@Override
	public float getDensity() {
		return this.density;
	}

	@Override
	public String toString() {
		return "ImmutableFluidSettings{" + "color=" + this.color +
				", luminance=" + this.luminance +
				", temperature=" + this.temperature +
				", density=" + this.density +
				'}';
	}

	public static class Builder implements FluidSettings.Builder {
		private Color color;
		private int luminance = -1;
		private int temperature = TemperatureUnits.ROOM_TEMPERATURE;
		private float density = DensityUnits.WATER;

		@Override
		public FluidSettings.Builder color(Color color) {
			this.color = color;
			return this;
		}

		@Override
		public FluidSettings.Builder luminance(int luminance) {
			this.luminance = luminance;
			return this;
		}

		@Override
		public FluidSettings.Builder temperature(int temperature) {
			this.temperature = temperature;
			return this;
		}

		@Override
		public FluidSettings.Builder density(float density) {
			this.density = density;
			return this;
		}

		@Override
		public FluidSettings.Builder color(Supplier<Color> colorSupplier) {
			this.color = colorSupplier.get();
			return this;
		}

		@Override
		public FluidSettings.Builder luminance(IntSupplier luminanceSupplier) {
			this.luminance = luminanceSupplier.getAsInt();
			return this;
		}

		@Override
		public FluidSettings.Builder temperature(IntSupplier temperatureSupplier) {
			this.temperature =temperatureSupplier.getAsInt();
			return this;
		}

		@Override
		public FluidSettings.Builder density(Supplier<Float> densitySupplier) {
			this.density = densitySupplier.get();
			return this;
		}

		@Override
		public FluidSettings build() {
			if (this.color == null) {
				throw new IllegalStateException("Missing color!");
			}
			if (this.luminance == -1) {
				throw new IllegalStateException("Missing luminance!");
			}
			return new ImmutableFluidSettings(this.color, this.luminance, this.temperature, this.density);
		}

		@Override
		public String toString() {
			return "ImmutableFluidSettings.Builder{" + "color=" + this.color +
					", luminance=" + this.luminance +
					", temperature=" + this.temperature +
					", density=" + this.density +
					'}';
		}
	}
}
