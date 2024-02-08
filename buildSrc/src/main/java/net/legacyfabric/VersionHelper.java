package net.legacyfabric;

import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

public class VersionHelper {
	public static SemanticVersion parseVersion(String version) {
		return MCVersionLookup.parse(version);
	}

	public static VersionPredicate parsePredicate(String predicate) {
        try {
            return VersionPredicate.parse(predicate);
        } catch (VersionParsingException e) {
            throw new RuntimeException(e);
        }
    }

	public static boolean needUserProperties(String stringVersion) {
		SemanticVersion version = parseVersion(stringVersion);

		VersionPredicate predicate = parsePredicate(">=1.7 <=1.8");

		return predicate.test(version);
	}
}
