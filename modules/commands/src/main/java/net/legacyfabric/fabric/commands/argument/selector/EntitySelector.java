package net.legacyfabric.fabric.commands.argument.selector;

import net.minecraft.command.EntityNotFoundException;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public final class EntitySelector {

	private final List<Entity> entities = new ArrayList<>();

	public EntitySelector(Entity entity) {
		this.entities.add(entity);
	}

	public EntitySelector(List<Entity> entities) {
		this.entities.addAll(entities);
	}

	/**
	 * Retrieves a single entity.
	 *
	 * @throws EntityNotFoundException when there are no entities to return.
	 */
	public Entity getSingleEntity() throws EntityNotFoundException {
		try {
			return entities.get(0);
		} catch (Exception e) {
			throw new EntityNotFoundException();
		}
	}

	/**
	 * @throws EntityNotFoundException when there are no entities to return.
	 */
	public List<Entity> getEntities() throws EntityNotFoundException {
		return entities;
	}
}
