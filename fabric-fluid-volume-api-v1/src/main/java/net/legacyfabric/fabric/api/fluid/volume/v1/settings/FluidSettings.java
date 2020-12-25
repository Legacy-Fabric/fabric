package net.legacyfabric.fabric.api.fluid.volume.v1.settings;

import java.awt.Color;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import net.legacyfabric.fabric.impl.fluid.volume.ImmutableFluidSettings;

/**
 * Stores basic settings about a fluid.
 * This exists because fluid tags don't.
 */
public interface FluidSettings {
	/**
	 * @return The color of the fluid
	 */
	Color getColor();

	/**
	 * @return The amount of light, between 0 and 15, that the fluid emits
	 */
	int getLuminance();

	/**
	 * @return The temperature of the fluid, in celsius
	 */
	int getTemperature();

	/**
	 * @return The density of the fluid, in tonnes per cubic metre. For reference, the density of water is 1.
	 */
	float getDensity();

	/**
	 * Converts this to a builder
	 * @return A {@link Builder} instance, containing the data in this
	 */
	default Builder toBuilder() {
		return builder().color(this::getColor).temperature(this::getTemperature).luminance(this::getLuminance).density(this::getDensity);
	}

	/**
	 * Creates a new {@link Builder}
	 * @return a builder
	 */
	static Builder builder() {
		return new ImmutableFluidSettings.Builder();
	}

	/**
	 * Helper class for creating {@link FluidSettings}
	 */
	interface Builder {
		/**
		 * Sets the fluid's color
		 * @return this
		 */
		Builder color(Color color);

		/**
		 * Sets the fluid's luminance
		 * @return this
		 */
		Builder luminance(int luminance);

		/**
		 * Sets the fluid's temperature
		 * @return this
		 */
		Builder temperature(int temperature);

		/**
		 * Sets the fluid's density
		 * @return this
		 */
		Builder density(float density);

		/**
		 * Sets the fluid's color
		 * @return this
		 */
		Builder color(Supplier<Color> colorSupplier);

		/**
		 * Sets the fluid's luminance
		 * @return this
		 */
		Builder luminance(IntSupplier luminanceSupplier);

		/**
		 * Sets the fluid's temperature
		 * @return this
		 */
		Builder temperature(IntSupplier temperatureSupplier);

		/**
		 * Sets the fluid's density
		 * @return this
		 */
		Builder density(Supplier<Float> densitySupplier);

		/**
		 * Converts this builder to a {@link FluidSettings} instance
		 * @return A {@link FluidSettings} instance, containing the data in this builder
		 */
		FluidSettings build();
	}
}
