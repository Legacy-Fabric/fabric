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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.args.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableList;

class SpaceSplitInputTokenizer implements InputTokenizer {
	public static final SpaceSplitInputTokenizer INSTANCE = new SpaceSplitInputTokenizer();
	private static final Pattern SPACE_REGEX = Pattern.compile("^[ ]*$");

	private SpaceSplitInputTokenizer() {
	}

	@Override
	public List<SingleArg> tokenize(String arguments, boolean lenient) {
		if (SPACE_REGEX.matcher(arguments).matches()) {
			return ImmutableList.of();
		}

		List<SingleArg> ret = new ArrayList<>();
		int lastIndex = 0;
		int spaceIndex;

		while ((spaceIndex = arguments.indexOf(" ")) != -1) {
			if (spaceIndex != 0) {
				ret.add(new SingleArg(arguments.substring(0, spaceIndex), lastIndex, lastIndex + spaceIndex));
				arguments = arguments.substring(spaceIndex);
			} else {
				arguments = arguments.substring(1);
			}

			lastIndex += spaceIndex + 1;
		}

		ret.add(new SingleArg(arguments, lastIndex, lastIndex + arguments.length()));
		return ret;
	}
}
