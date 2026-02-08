/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.Formatting;
import net.minecraft.text.Text;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ContactInformation;

public class ModMetadataCommandV1 extends AbstractCommand {
	@Override
	public String getName() {
		return "modmetadatav1";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "modmetadatav1";
	}

	@Override
	public void run(CommandSource commandSource, String[] args) throws CommandException {
		if (args.length > 0) {
			Optional<ModContainer> optionalModContainer = FabricLoader.getInstance().getModContainer(args[0]);

			if (optionalModContainer.isPresent()) {
				ModContainer container = optionalModContainer.get();

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
				commandSource.sendMessage(builder);
			} else {
				Text builder = Text.literal("Couldn't find Mod container for mod id '" + args[0] + "'");
				builder.setColor(Formatting.RED);
				commandSource.sendMessage(builder);
			}
		}
	}

	@Override
	public int compareTo(@NotNull Command o) {
		return super.compareTo((Command) o);
	}
}
