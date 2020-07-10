package net.fabricmc.fabric.impl.network;

public final class PacketDebugOptions {
	public static final boolean DISABLE_BUFFER_RELEASES = System.getProperty("fabric.networking.broken.disableBufferReleases", "false").equalsIgnoreCase("true");
}
