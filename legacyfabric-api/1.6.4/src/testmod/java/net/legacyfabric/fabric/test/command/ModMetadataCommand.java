/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

package net.legacyfabric.fabric.test.command;

import net.minecraft.text.Text;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ContactInformation;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandManager;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.GenericArguments;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class ModMetadataCommand {
	public static void register(CommandManager manager) {
		manager.register(
				CommandSpec.builder()
						.arguments(GenericArguments.mod(Text.literal("modid")))
						.executor(ModMetadataCommand::execute)
						.build(),
				"modmetadata"
		);
	}

	private static CommandResult execute(PermissibleCommandSource source, CommandContext ctx) throws CommandException {
		ModContainer container = ctx.<ModContainer>getOne("modid").orElseThrow(() -> new CommandException(Text.literal("Couldn't find Mod container")));
		Text builder = Text.literal("");
		builder.appendLiteral("Mod Name: ".concat(container.getMetadata().getName()).concat("\n"));
		builder.appendLiteral("Description: ".concat(container.getMetadata().getDescription()).concat("\n"));
		ContactInformation contact = container.getMetadata().getContact();

		if (contact.get("issues").isPresent()) {
			Text issueText = Text.literal("");
			issueText.appendLiteral("Issues: ");
			Text issueUrl = Text.literal(contact.get("issues").get());
			issueText.append(issueUrl);
			issueText.appendLiteral("\n");
			builder.append(issueText);
		}

		if (contact.get("sources").isPresent()) {
			Text sourcesText = Text.literal("");
			sourcesText.appendLiteral("Sources: ");
			Text sourcesUrl = Text.literal(contact.get("sources").get());
			sourcesText.append(sourcesUrl);
			sourcesText.appendLiteral("\n");
			builder.append(sourcesText);
		}

		builder.appendLiteral("Metadata Type: ".concat(container.getMetadata().getType()).concat("\n"));
		source.sendMessage(builder);
		return CommandResult.success();
	}
}
