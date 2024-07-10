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

package net.legacyfabric.fabric.test.command;

import java.util.Optional;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ContactInformation;

import net.minecraft.server.MinecraftServer;

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
	public void method_3279(MinecraftServer minecraftServer, CommandSource commandSource, String[] args) throws CommandException {
		if (args.length > 0) {
			Optional<ModContainer> optionalModContainer = FabricLoader.getInstance().getModContainer(args[0]);

			if (optionalModContainer.isPresent()) {
				ModContainer container = optionalModContainer.get();

				StringBuilder builder = new StringBuilder();
				builder.append("Mod Name: ".concat(container.getMetadata().getName()).concat("\n"));
				builder.append("Description: ".concat(container.getMetadata().getDescription()).concat("\n"));
				ContactInformation contact = container.getMetadata().getContact();

				if (contact.get("issues").isPresent()) {
					StringBuilder issueText = new StringBuilder("");
					issueText.append("Issues: ");
					StringBuilder issueUrl = new StringBuilder(contact.get("issues").get());
					issueText.append(issueUrl);
					issueText.append("\n");
					builder.append(issueText);
				}

				if (contact.get("sources").isPresent()) {
					StringBuilder sourcesText = new StringBuilder("");
					sourcesText.append("Sources: ");
					StringBuilder sourcesUrl = new StringBuilder(contact.get("sources").get());
					sourcesText.append(sourcesUrl);
					sourcesText.append("\n");
					builder.append(sourcesText);
				}

				builder.append("Metadata Type: ".concat(container.getMetadata().getType()).concat("\n"));
				CommandV1Test.LOGGER.info(builder.toString());
			} else {
				CommandV1Test.LOGGER.error("Couldn't find Mod container for mod id '" + args[0] + "'");
			}
		}
	}
}
