# Legacy Fabric API

Essential hooks for modding with Fabric.

Fabric API is the library for essential hooks and interoperability mechanisms for Fabric mods, such as:
- Exposing functionality that is useful but difficult to access for many mods such as particles, biomes and dimensions
- Adding events, hooks and APIs to improve interoperability between mods.

For support, consider joining the [Legacy Fabric discord server](https://legacyfabric.net/discord) or opening an issue.

## Modules available and tested by minecraft version
✅ = Working as expected.\
⚠ = Working but has some issues.\
? = Not tested, might not work as intended.\
❌ = Not ported or probably not working.

|                      | 1.3.2 | 1.4.7 | 1.5.2 | 1.6.4                         | 1.7.10 | 1.8 | 1.8.9 | 1.9.4 | 1.10.2 | 1.11.2 | 1.12.2 | 1.13.2 |
|----------------------|--------|--------|--------|-------------------------------|--------|-----|-------|-------|--------|--------|--------|--------|
| api-base             | ✅                            | ✅                            | ✅                            | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ✅      |
| biome-api-v1         | ❌                            | ❌                            | ❌                            | ❌                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| block-api-v1         | ❌                            | ❌                            | ❌                            | ❌                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| block-entity-api-v1  | ❌                            | ❌                            | ❌                            | ❌                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| command-api-v1       | ❌                            | ❌                            | ❌                            | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| command-api-v2       | ❌                            | ❌                            | ❌                            | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| crash-report-info-v1 | ❌                            | ❌                            | ❌                            | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ✅      |
| effect-api-v1        | ❌                            | ❌                            | ❌                            | ❌                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| enchantment-api-v1  | ❌                             | ❌                            | ❌                            | ❌                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| entity-api-v1        | ❌                            | ❌                            | ❌                            | ❌                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| entity-events-v1     | ❌                            | ❌                            | ❌                            | ❌                             | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| gamerule-api-v1      | ❌                            | ?                              | ?                              | ?                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ?      |
| item-api-v1          | ❌                            | ❌                             | ❌                            | ❌                            | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| item-groups-v1       | ✅                            | ✅                            | ✅                            | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| keybindings-api-v1   | ❌                            | ❌                            | ❌                            | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| lifecycle-events-v1  | ❌                            | ❌                            | ❌                            | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| logger-api-v1        | ✅                            | ✅                            | ✅                            | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ✅      |
| networking-api-v1    | ❌                            | ❌                            | ❌                            | ❌                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| permissions-api-v1   | ❌                            | ❌                            | ❌                            | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| registry-sync-api-v1 | ❌ (Depends on networking-api) | ❌ (Depends on networking-api) | ❌ (Depends on networking-api) | ❌ (Depends on networking-api) | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| registry-sync-api-v2 | ❌ (Depends on networking-api) | ❌ (Depends on networking-api) | ❌ (Depends on networking-api) | ❌ (Depends on networking-api) | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| rendering-api-v1     | ❌                            | ❌                             | ❌                             | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
| resource-loader-v1   | ❌                            | ❌                             | ❌                             | ✅                             | ✅      | ✅   | ✅     | ✅     | ✅      | ✅      | ✅      | ❌      |
