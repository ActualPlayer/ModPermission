# ModPermissions
Block the use of mods using permissions 

## Permissions

* `modpermissions.blacklist.{mod id}.*` - Block all use of a mod
* `modpermissions.blacklist.{mod id}.craft` - Block all crafting of the mod's items and blocks
* `modpermissions.blacklist.{mod id}.pickup` - Blocks picking up of a mod's items
* `modpermissions.blacklist.{mod id}.interact` - Block all use of a mod's item and blocks, this includes placing BUT NOT BREAKING blocks - a mod's blocks can be broken even with the permission. This is to prevent griefing by placing blocks near a user's house or location that he can not break. What the mod WILL do instead is prevent the item from dropping when the block is broken by a player with the permission. These two things can be changed in the config.

## Config

* `blacklistItemPickup` - When a player does not have permission to a mod, also disable picking up of items on the floor.
* `informationMessage` - What message to send to the user when he attempts to use a mod that he does not have permission to use. Leave this empty to disable messaging. %m is the mod id
* `allowBreakingOfBlocks` - Allows breaking of blocks even with the permission to prevent griefing.
* `allowItemsToDrop` - If allowBreakingOfBlocks is true, this will also drop the item to the ground. The user will not be able to pick up the item if he has the modpermissions.blacklist.{mod id}.pickup permission"
