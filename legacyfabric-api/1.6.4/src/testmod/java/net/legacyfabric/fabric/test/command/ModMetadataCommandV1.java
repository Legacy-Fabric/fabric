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

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.text.ChatMessage;
import net.minecraft.util.Formatting;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ContactInformation;

public class ModMetadataCommandV1 extends AbstractCommand {
	@Override
	public String getCommandName() {
		return "modmetadatav1";
	}

	@Override
	public String getUsageTranslationKey(CommandSource source) {
		return "modmetadatav1";
	}

	@Override
	public void execute(CommandSource commandSource, String[] args) throws CommandException {
		if (args.length > 0) {
			Optional<ModContainer> optionalModContainer = FabricLoader.getInstance().getModContainer(args[0]);

			if (optionalModContainer.isPresent()) {
				ModContainer container = optionalModContainer.get();

				ChatMessage builder = ChatMessage.createTextMessage("");
				builder.addText("Mod Name: ".concat(container.getMetadata().getName()).concat("\n"));
				builder.addText("Description: ".concat(container.getMetadata().getDescription()).concat("\n"));
				ContactInformation contact = container.getMetadata().getContact();

				if (contact.get("issues").isPresent()) {
					ChatMessage issueText = ChatMessage.createTextMessage("");
					issueText.addText("Issues: ");
					ChatMessage issueUrl = ChatMessage.createTextMessage(contact.get("issues").get());
					issueText.addUsing(issueUrl);
					issueText.addText("\n");
					builder.addUsing(issueText);
				}

				if (contact.get("sources").isPresent()) {
					ChatMessage sourcesText = ChatMessage.createTextMessage("");
					sourcesText.addText("Sources: ");
					ChatMessage sourcesUrl = ChatMessage.createTextMessage(contact.get("sources").get());
					sourcesText.addUsing(sourcesUrl);
					sourcesText.addText("\n");
					builder.addUsing(sourcesText);
				}

				builder.addText("Metadata Type: ".concat(container.getMetadata().getType()).concat("\n"));
				commandSource.method_5505(builder);
			} else {
				ChatMessage builder = ChatMessage.createTextMessage("Couldn't find Mod container for mod id '" + args[0] + "'");
				builder.setColor(Formatting.RED);
				commandSource.method_5505(builder);
			}
		}
	}

	@Override
	public int compareTo(@NotNull Object o) {
		return super.compareTo((Command) o);
	}
}
