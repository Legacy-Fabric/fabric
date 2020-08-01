package net.fabricmc.fabric.impl.armor;

public enum EquipmentSlot {
	HEAD(0),
	CHEST(1),
	LEGS(2),
	FEET(3);

	private final int slotId;

	EquipmentSlot(int slotId) {
		this.slotId = slotId;
	}

	public int getSlotId() {
		return slotId;
	}

	public static EquipmentSlot getById(int slotId) {
		return EquipmentSlot.values()[slotId];
	}
}
