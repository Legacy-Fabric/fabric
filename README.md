# Legacy Fabric API

Essential hooks for modding with Fabric.

Fabric API is the library for essential hooks and interoperability mechanisms for Fabric mods, such as:
- Exposing functionality that is useful but difficult to access for many mods such as particles, biomes and dimensions
- Adding events, hooks and APIs to improve interopability between mods.

For support, consider joining the [Legacy Fabric discord server](https://legacyfabric.net/discord) or opening an issue.

## Modules available per minecraft version

✅ = Working as expected\
⚠ = Working but has some issues.\
? = Not sure if it was tested or working correctly\
❌ = Not working at all, likely crashing

|                      | 1.7.10 | 1.8 | 1.8.9 | 1.9.4 | 1.10.2 | 1.11.2 | 1.12.2 | 1.13.2 |
|----------------------|--------|-----|-------|-------|--------|--------|--------|--------|
| api-base             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ✅      |
| command-api-v1       | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| command-api-v2       | ✅      | ✅   | ✅     | ⚠     | ⚠      | ⚠      | ⚠      | ❌      |
| crash-report-info-v1 | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ✅      |
| entity-events-v1     | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| gamerule-api-v1      | ?      | ?   | ?     | ?     | ?      | ?      | ?      | ?      |
| item-groups-v1       | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| keybindings-api-v1   | ⚠ #132 | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ?      |
| lifecycle-events-v1  | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| logger-api-v1        | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ✅      |
| networking-api-v1    | ?      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| permissions-api-v1   | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| registry-sync-api-v1 | ❌      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| rendering-api-v1     | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| resource-loader-v1   | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
