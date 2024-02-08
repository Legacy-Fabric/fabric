/*
 * Copyright 2016 FabricMC
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
package net.legacyfabric;

import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCVersionLookup {
	private static final Pattern VERSION_PATTERN = Pattern.compile(
		"0\\.\\d+(\\.\\d+)?a?(_\\d+)?|" // match classic versions first: 0.1.2a_34
			+ "\\d+\\.\\d+(\\.\\d+)?(-pre\\d+| Pre-[Rr]elease \\d+)?|" // modern non-snapshot: 1.2, 1.2.3, optional -preN or " Pre-Release N" suffix
			+ "\\d+\\.\\d+(\\.\\d+)?(-rc\\d+| [Rr]elease Candidate \\d+)?|" // 1.16+ Release Candidate
			+ "\\d+w\\d+[a-z]|" // modern snapshot: 12w34a
			+ "[a-c]\\d\\.\\d+(\\.\\d+)?[a-z]?(_\\d+)?[a-z]?|" // alpha/beta a1.2.3_45
			+ "(Alpha|Beta) v?\\d+\\.\\d+(\\.\\d+)?[a-z]?(_\\d+)?[a-z]?|" // long alpha/beta names: Alpha v1.2.3_45
			+ "Inf?dev (0\\.31 )?\\d+(-\\d+)?|" // long indev/infdev names: Infdev 12345678-9
			+ "(rd|inf)-\\d+|" // early rd-123, inf-123
			+ "1\\.RV-Pre1|3D Shareware v1\\.34|23w13a_or_b|" // odd exceptions
			+ "(.*[Ee]xperimental [Ss]napshot )(\\d+)" // Experimental versions.
	);
	private static final Pattern RELEASE_PATTERN = Pattern.compile("\\d+\\.\\d+(\\.\\d+)?");
	private static final Pattern PRE_RELEASE_PATTERN = Pattern.compile(".+(?:-pre| Pre-[Rr]elease )(\\d+)");
	private static final Pattern RELEASE_CANDIDATE_PATTERN = Pattern.compile(".+(?:-rc| [Rr]elease Candidate )(\\d+)");
	private static final Pattern SNAPSHOT_PATTERN = Pattern.compile("(?:Snapshot )?(\\d+)w0?(0|[1-9]\\d*)([a-z])");
	private static final Pattern EXPERIMENTAL_PATTERN = Pattern.compile("(?:.*[Ee]xperimental [Ss]napshot )(\\d+)");
	private static final Pattern BETA_PATTERN = Pattern.compile("(?:b|Beta v?)1\\.(\\d+(\\.\\d+)?[a-z]?(_\\d+)?[a-z]?)");
	private static final Pattern ALPHA_PATTERN = Pattern.compile("(?:a|Alpha v?)[01]\\.(\\d+(\\.\\d+)?[a-z]?(_\\d+)?[a-z]?)");
	private static final Pattern INDEV_PATTERN = Pattern.compile("(?:inf-|Inf?dev )(?:0\\.31 )?(\\d+(-\\d+)?)");

	protected static SemanticVersion parse(String version) {
        try {
            return SemanticVersion.parse(
                normalizeVersion(version, getRelease(version))
            );
        } catch (VersionParsingException e) {
            throw new RuntimeException(e);
        }
    }

	protected static String getRelease(String version) {
		if (RELEASE_PATTERN.matcher(version).matches()) return version;

		assert isProbableVersion(version);

		int pos = version.indexOf("-pre");
		if (pos >= 0) return version.substring(0, pos);

		pos = version.indexOf(" Pre-Release ");
		if (pos >= 0) return version.substring(0, pos);

		pos = version.indexOf(" Pre-release ");
		if (pos >= 0) return version.substring(0, pos);

		pos = version.indexOf(" Release Candidate ");
		if (pos >= 0) return version.substring(0, pos);

		Matcher matcher = SNAPSHOT_PATTERN.matcher(version);

		if (matcher.matches()) {
			int year = Integer.parseInt(matcher.group(1));
			int week = Integer.parseInt(matcher.group(2));

			if (year == 23 && week >= 51 || year >= 24) {
				return "1.20.5";
			} else if (year == 23 && week >= 40 && week <= 46) {
				return "1.20.3";
			} else if (year == 23 && week >= 31 && week <= 35) {
				return "1.20.2";
			} else if (year == 23 && week >= 12 && week <= 18) {
				return "1.20";
			} else if (year == 23 && week <= 7) {
				return "1.19.4";
			} else if (year == 22 && week >= 42) {
				return "1.19.3";
			} else if (year == 22 && week == 24) {
				return "1.19.1";
			} else if (year == 22 && week >= 11 && week <= 19) {
				return "1.19";
			} else if (year == 22 && week >= 3 && week <= 7) {
				return "1.18.2";
			} else if (year == 21 && week >= 37 && week <= 44) {
				return "1.18";
			} else if (year == 20 && week >= 45 || year == 21 && week <= 20) {
				return "1.17";
			} else if (year == 20 && week >= 27 && week <= 30) {
				return "1.16.2";
			} else if (year == 20 && week >= 6 && week <= 22) {
				return "1.16";
			} else if (year == 19 && week >= 34) {
				return "1.15";
			} else if (year == 18 && week >= 43 || year == 19 && week <= 14) {
				return "1.14";
			} else if (year == 18 && week >= 30 && week <= 33) {
				return "1.13.1";
			} else if (year == 17 && week >= 43 || year == 18 && week <= 22) {
				return "1.13";
			} else if (year == 17 && week == 31) {
				return "1.12.1";
			} else if (year == 17 && week >= 6 && week <= 18) {
				return "1.12";
			} else if (year == 16 && week == 50) {
				return "1.11.1";
			} else if (year == 16 && week >= 32 && week <= 44) {
				return "1.11";
			} else if (year == 16 && week >= 20 && week <= 21) {
				return "1.10";
			} else if (year == 16 && week >= 14 && week <= 15) {
				return "1.9.3";
			} else if (year == 15 && week >= 31 || year == 16 && week <= 7) {
				return "1.9";
			} else if (year == 14 && week >= 2 && week <= 34) {
				return "1.8";
			} else if (year == 13 && week >= 47 && week <= 49) {
				return "1.7.4";
			} else if (year == 13 && week >= 36 && week <= 43) {
				return "1.7.2";
			} else if (year == 13 && week >= 16 && week <= 26) {
				return "1.6";
			} else if (year == 13 && week >= 11 && week <= 12) {
				return "1.5.1";
			} else if (year == 13 && week >= 1 && week <= 10) {
				return "1.5";
			} else if (year == 12 && week >= 49 && week <= 50) {
				return "1.4.6";
			} else if (year == 12 && week >= 32 && week <= 42) {
				return "1.4.2";
			} else if (year == 12 && week >= 15 && week <= 30) {
				return "1.3.1";
			} else if (year == 12 && week >= 3 && week <= 8) {
				return "1.2.1";
			} else if (year == 11 && week >= 47 || year == 12 && week <= 1) {
				return "1.1";
			}
		}

		return null;
	}

	private static boolean isProbableVersion(String str) {
		return VERSION_PATTERN.matcher(str).matches();
	}

	/**
	 * Returns the probable version contained in the given string, or null if the string doesn't contain a version.
	 */
	private static String findProbableVersion(String str) {
		Matcher matcher = VERSION_PATTERN.matcher(str);

		if (matcher.find()) {
			return matcher.group();
		} else {
			return null;
		}
	}

	/**
	 * Convert an arbitrary MC version into semver-like release-preRelease form.
	 *
	 * <p>MC Snapshot -> alpha, MC Pre-Release -> rc.
	 */
	protected static String normalizeVersion(String name, String release) {
		if (release == null || name.equals(release)) {
			String ret = normalizeSpecialVersion(name);
			return ret != null ? ret : normalizeVersion(name);
		}

		Matcher matcher;

		if ((matcher = EXPERIMENTAL_PATTERN.matcher(name)).matches()) {
			return String.format("%s-Experimental.%s", release, matcher.group(1));
		} else if (name.startsWith(release)) {
			matcher = RELEASE_CANDIDATE_PATTERN.matcher(name);

			if (matcher.matches()) {
				String rcBuild = matcher.group(1);

				// This is a hack to fake 1.16's new release candidates to follow on from the 8 pre releases.
				if (release.equals("1.16")) {
					int build = Integer.parseInt(rcBuild);
					rcBuild = Integer.toString(8 + build);
				}

				name = String.format("rc.%s", rcBuild);
			} else {
				matcher = PRE_RELEASE_PATTERN.matcher(name);

				if (matcher.matches()) {
					boolean legacyVersion;

					try {
						legacyVersion = VersionPredicate.parse("<=1.16").test(SemanticVersion.parse(release));
					} catch (VersionParsingException e) {
						throw new RuntimeException("Failed to parse version: " + release);
					}

					// Mark pre-releases as 'beta' versions, except for version 1.16 and before, where they are 'rc'
					if (legacyVersion) {
						name = String.format("rc.%s", matcher.group(1));
					} else {
						name = String.format("beta.%s", matcher.group(1));
					}
				} else {
					String ret = normalizeSpecialVersion(name);
					if (ret != null) return ret;
				}
			}
		} else if ((matcher = SNAPSHOT_PATTERN.matcher(name)).matches()) {
			name = String.format("alpha.%s.%s.%s", matcher.group(1), matcher.group(2), matcher.group(3));
		} else {
			// Try short-circuiting special versions which are complete on their own
			String ret = normalizeSpecialVersion(name);
			if (ret != null) return ret;

			name = normalizeVersion(name);
		}

		return String.format("%s-%s", release, name);
	}

	private static String normalizeVersion(String version) {
		// old version normalization scheme
		// do this before the main part of normalization as we can get crazy strings like "Indev 0.31 12345678-9"
		Matcher matcher;

		if ((matcher = BETA_PATTERN.matcher(version)).matches()) { // beta 1.2.3: 1.0.0-beta.2.3
			version = "1.0.0-beta." + matcher.group(1);
		} else if ((matcher = ALPHA_PATTERN.matcher(version)).matches()) { // alpha 1.2.3: 1.0.0-alpha.2.3
			version = "1.0.0-alpha." + matcher.group(1);
		} else if ((matcher = INDEV_PATTERN.matcher(version)).matches()) { // indev/infdev 12345678: 0.31.12345678
			version = "0.31." + matcher.group(1);
		} else if (version.startsWith("c0.")) { // classic: unchanged, except remove prefix
			version = version.substring(1);
		} else if (version.startsWith("rd-")) { // pre-classic
			version = version.substring("rd-".length());
			if ("20090515".equals(version)) version = "150000"; // account for a weird exception to the pre-classic versioning scheme
			version = "0.0.0-rd." + version;
		}

		StringBuilder ret = new StringBuilder(version.length() + 5);
		boolean lastIsDigit = false;
		boolean lastIsLeadingZero = false;
		boolean lastIsSeparator = false;

		for (int i = 0, max = version.length(); i < max; i++) {
			char c = version.charAt(i);

			if (c >= '0' && c <= '9') {
				if (i > 0 && !lastIsDigit && !lastIsSeparator) { // no separator between non-number and number, add one
					ret.append('.');
				} else if (lastIsDigit && lastIsLeadingZero) { // leading zero in output -> strip
					ret.setLength(ret.length() - 1);
				}

				lastIsLeadingZero = c == '0' && (!lastIsDigit || lastIsLeadingZero); // leading or continued leading zero(es)
				lastIsSeparator = false;
				lastIsDigit = true;
			} else if (c == '.' || c == '-') { // keep . and - separators
				if (lastIsSeparator) continue;

				lastIsSeparator = true;
				lastIsDigit = false;
			} else if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) { // replace remaining non-alphanumeric with .
				if (lastIsSeparator) continue;

				c = '.';
				lastIsSeparator = true;
				lastIsDigit = false;
			} else { // keep other characters (alpha)
				if (lastIsDigit) ret.append('.'); // no separator between number and non-number, add one

				lastIsSeparator = false;
				lastIsDigit = false;
			}

			ret.append(c);
		}

		// strip leading and trailing .

		int start = 0;
		while (start < ret.length() && ret.charAt(start) == '.') start++;

		int end = ret.length();
		while (end > start && ret.charAt(end - 1) == '.') end--;

		return ret.substring(start, end);
	}

	private static String normalizeSpecialVersion(String version) {
		switch (version) {
			case "13w12~":
				// A pair of debug snapshots immediately before 1.5.1-pre
				return "1.5.1-alpha.13.12.a";

			case "15w14a":
				// The Love and Hugs Update, forked from 1.8.3
				return "1.8.4-alpha.15.14.a+loveandhugs";

			case "1.RV-Pre1":
				// The Trendy Update, probably forked from 1.9.2 (although the protocol/data versions immediately follow 1.9.1-pre3)
				return "1.9.2-rv+trendy";

			case "3D Shareware v1.34":
				// Minecraft 3D, forked from 19w13b
				return "1.14-alpha.19.13.shareware";

			case "20w14~":
				// The Ultimate Content update, forked from 20w13b
				return "1.16-alpha.20.13.inf"; // Not to be confused with the actual 20w14a

			case "1.14.3 - Combat Test":
				// The first Combat Test, forked from 1.14.3 Pre-Release 4
				return "1.14.3-rc.4.combat.1";

			case "Combat Test 2":
				// The second Combat Test, forked from 1.14.4
				return "1.14.5-combat.2";

			case "Combat Test 3":
				// The third Combat Test, forked from 1.14.4
				return "1.14.5-combat.3";

			case "Combat Test 4":
				// The fourth Combat Test, forked from 1.15 Pre-release 3
				return "1.15-rc.3.combat.4";

			case "Combat Test 5":
				// The fifth Combat Test, forked from 1.15.2 Pre-release 2
				return "1.15.2-rc.2.combat.5";

			case "Combat Test 6":
				// The sixth Combat Test, forked from 1.16.2 Pre-release 3
				return "1.16.2-beta.3.combat.6";

			case "Combat Test 7":
				// Private testing Combat Test 7, forked from 1.16.2
				return "1.16.3-combat.7";

			case "1.16_combat-2":
				// Private testing Combat Test 7b, forked from 1.16.2
				return "1.16.3-combat.7.b";

			case "1.16_combat-3":
				// The seventh Combat Test 7c, forked from 1.16.2
				return "1.16.3-combat.7.c";

			case "1.16_combat-4":
				// Private testing Combat Test 8(a?), forked from 1.16.2
				return "1.16.3-combat.8";

			case "1.16_combat-5":
				// The eighth Combat Test 8b, forked from 1.16.2
				return "1.16.3-combat.8.b";

			case "1.16_combat-6":
				// The ninth Combat Test 8c, forked from 1.16.2
				return "1.16.3-combat.8.c";

			case "2point0_red":
				// 2.0 update version red, forked from 1.5.1
				return "1.5.2-red";

			case "2point0_purple":
				// 2.0 update version purple, forked from 1.5.1
				return "1.5.2-purple";

			case "2point0_blue":
				// 2.0 update version blue, forked from 1.5.1
				return "1.5.2-blue";

			case "23w13a_or_b":
				// Minecraft 23w13a_or_b, forked from 23w13a
				return "1.20-alpha.23.13.ab";

			default:
				return null; //Don't recognise the version
		}
	}
}
