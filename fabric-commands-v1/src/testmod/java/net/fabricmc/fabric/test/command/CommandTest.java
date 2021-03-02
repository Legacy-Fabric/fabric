/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

package net.fabricmc.fabric.test.command;

import static net.fabricmc.fabric.api.command.v1.CommandManager.literal;

import net.minecraft.text.LiteralText;

import net.fabricmc.fabric.api.command.v1.DispatcherRegistrationCallback;

public interface CommandTest {
	static void initialize() {
		DispatcherRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(literal("crab")
					.then(literal("crab")
							.then(literal("crab")
									.executes(context -> {
										context.getSource().sendFeedback(new LiteralText("Triple crab"));
										return 0;
									}))
							.executes(context -> {
								context.getSource().sendFeedback(new LiteralText("Double crab"));
								return 2;
							}))
					.executes(context -> {
						context.getSource().sendFeedback(new LiteralText("Crab"));
						return 1;
					}));

			if (dedicated) {
				dispatcher.register(literal("dedicated")
						.then(literal("crab")
								.then(literal("crab")
										.then(literal("crab")
												.executes(context -> {
													context.getSource().sendFeedback(new LiteralText("Dedicated triple crab"));
													return 0;
												}))
										.executes(context -> {
											context.getSource().sendFeedback(new LiteralText("Dedicated double crab"));
											return 2;
										}))
								.executes(context -> {
									context.getSource().sendFeedback(new LiteralText("Dedicated crab"));
									return 1;
								})));
			} else {
				dispatcher.register(literal("client")
						.then(literal("crab")
								.then(literal("crab")
										.then(literal("crab")
												.executes(context -> {
													context.getSource().sendFeedback(new LiteralText("Client triple crab"));
													return 0;
												}))
										.executes(context -> {
											context.getSource().sendFeedback(new LiteralText("Client double crab"));
											return 2;
										}))
								.executes(context -> {
									context.getSource().sendFeedback(new LiteralText("Client crab"));
									return 1;
								})));
			}
		});
	}
}
