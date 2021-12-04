/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.legacyrewoven.impl.command.lib.sponge.args;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.ArgumentParseException;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.CommandArgs;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.CommandContext;
import io.github.legacyrewoven.api.command.v2.lib.sponge.args.CommandElement;
import io.github.legacyrewoven.api.permission.v1.PermissibleCommandSource;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

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
				return SPECIAL_TOKENS.stream().filter((input) -> input.startsWith(finalArg.get())).collect(ImmutableList.toImmutableList());
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
