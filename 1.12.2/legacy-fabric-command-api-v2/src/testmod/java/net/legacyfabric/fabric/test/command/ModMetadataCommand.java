/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
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

import java.util.Objects;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;

import net.fabricmc.loader.api.FabricLoader;
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
						.arguments(GenericArguments.mod(new LiteralText("modid")))
						.executor(ModMetadataCommand::execute)
						.build(),
				"modmetadata"
		);
	}

	private static final boolean useStyle = !Objects.equals(FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString(), "1.8");

	private static CommandResult execute(PermissibleCommandSource source, CommandContext ctx) throws CommandException {
		ModContainer container = ctx.<ModContainer>getOne("modid").orElseThrow(() -> new CommandException(new LiteralText("mod not found")));
		LiteralText builder = new LiteralText("");
		builder.append("Mod Name: ".concat(container.getMetadata().getName()).concat("\n"));
		builder.append("Description: ".concat(container.getMetadata().getDescription()).concat("\n"));
		ContactInformation contact = container.getMetadata().getContact();

		if (contact.get("issues").isPresent()) {
			LiteralText issueText = new LiteralText("");
			issueText.append("Issues: ");
			LiteralText issueUrl = new LiteralText(contact.get("issues").get());
			if (useStyle) issueUrl.setStyle(issueText.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, issueUrl.asString())));
			issueText.append(issueUrl);
			issueText.append("\n");
			builder.append(issueText);
		}

		if (contact.get("sources").isPresent()) {
			LiteralText sourcesText = new LiteralText("");
			sourcesText.append("Sources: ");
			LiteralText sourcesUrl = new LiteralText(contact.get("sources").get());
			if (useStyle) sourcesUrl.setStyle(sourcesText.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, sourcesUrl.asString())));
			sourcesText.append(sourcesUrl);
			sourcesText.append("\n");
			builder.append(sourcesText);
		}

		builder.append("Metadata Type: ".concat(container.getMetadata().getType()).concat("\n"));
		source.sendMessage(builder);
		return CommandResult.success();
	}
}
