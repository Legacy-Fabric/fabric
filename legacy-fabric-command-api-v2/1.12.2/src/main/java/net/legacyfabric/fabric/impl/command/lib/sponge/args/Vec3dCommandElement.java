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

package net.legacyfabric.fabric.impl.command.lib.sponge.args;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

/**
 * A {@link Vec3d} command element.
 *
 * <p>It has the following syntax:</p>
 *
 * <blockquote><pre> x,y,z
 * x y z.</pre></blockquote>
 *
 * <p>Each element can be relative to a location? so
 * <tt>parseRelativeDouble()</tt> -- relative is ~(num)</p>
 */
public class Vec3dCommandElement extends CommandElement {
	private static final ImmutableSet<String> SPECIAL_TOKENS = ImmutableSet.of("#target", "#me");

	public Vec3dCommandElement(@Nullable Text key) {
		super(key);
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		String xStr;
		String yStr;
		String zStr;
		xStr = args.next();

		if (xStr.contains(",")) {
			String[] split = xStr.split(",");

			if (split.length != 3) {
				throw args.createError(new LiteralText(String.format("Comma-separated location must have 3 elements, not %s", split.length)));
			}

			xStr = split[0];
			yStr = split[1];
			zStr = split[2];
		} else if (xStr.equalsIgnoreCase("#me")) {
			return source.getPos();
		} else {
			yStr = args.next();
			zStr = args.next();
		}

		double x = this.parseRelativeDouble(args, xStr, source.getPos().x);
		double y = this.parseRelativeDouble(args, yStr, source.getPos().y);
		double z = this.parseRelativeDouble(args, zStr, source.getPos().z);
		return new Vec3d(x, y, z);
	}

	@Override
	public List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		Optional<String> arg = args.nextIfPresent();

		// Traverse through the possible arguments. We can't really complete arbitrary integers
		if (arg.isPresent()) {
			if (arg.get().startsWith("#")) {
				Optional<String> finalArg = arg;
				return Collections.unmodifiableList(SPECIAL_TOKENS.stream().filter((input) -> input.startsWith(finalArg.get())).collect(Collectors.toList()));
			} else if (arg.get().contains(",") || !args.hasNext()) {
				return ImmutableList.of(arg.get());
			} else {
				arg = args.nextIfPresent();

				if (args.hasNext()) {
					return ImmutableList.of(args.nextIfPresent().get());
				}

				return ImmutableList.of(arg.get());
			}
		}

		return ImmutableList.of();
	}

	private double parseRelativeDouble(CommandArgs args, String arg, @Nullable Double relativeTo) throws ArgumentParseException {
		boolean relative = arg.startsWith("~");

		if (relative) {
			if (relativeTo == null) {
				throw args.createError(new LiteralText("Relative position specified but source does not have a position"));
			}

			arg = arg.substring(1);

			if (arg.isEmpty()) {
				return relativeTo;
			}
		}

		try {
			double ret = Double.parseDouble(arg);
			return relative ? ret + relativeTo : ret;
		} catch (NumberFormatException e) {
			throw args.createError(new LiteralText(String.format("Expected input %s to be a double, but was not", arg)));
		}
	}
}
