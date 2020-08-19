package net.fabricmc.fabric.impl.biomes;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import net.fabricmc.fabric.api.biomes.v1.Climate;

/**
 * Climate related utility methods and fields for internal use only!
 */
public class InternalClimateUtils {
	private static final List<Climate> REVERSE_CLIMATE = new ArrayList<>();

	public static void addClimate(Climate climate, int id) {
		Preconditions.checkArgument(id == REVERSE_CLIMATE.size(), "Given climate id already exists");
		Preconditions.checkArgument(climate != null, "Climate is null");
		REVERSE_CLIMATE.add(climate);
	}
}
