1.0.0:
- Prevent shaped crafting recipes if the solar panel has upgrades on it. Simply put the solar panel in the crafting grid first to pull out the upgrades.
- Added custom recipe serializer "solarfluxkeepupgrades:crafting_shaped" which will prevent solar panel upgrades from being lost.
- Added custom recipe serializer "solarfluxkeepupgrades:crafting_shapeless" which pulls out all items that were on the panel. If the panel had 5 upgrades and the inventory was used to pull out upgrades, then you'll need to do it once more to pull out the last upgrade.
- Added integration to draconic evolution solar panels with forge:conditional and "solarfluxkeepupgrades:fusion_crafting" recipe serializer which only loads when draconicevolution is present