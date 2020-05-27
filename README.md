# ServerSelector
Here you can find all necessary information to set up the plugin and your menus.

## Setup
Note: the native version of this plugin is **1.15.2**, other modern versions and legacy versions will be supported in a future release.
1. Download the plugin from the spigot page (link soon).
2. Put the .jar file in in...
- Servers you want to get data from (status, current players, max players).
- Servers you want to use the selector on.
- Your BungeeCord server
3. Start/restart your servers.
4. Open plugin folder > menus, here you can find all your menus, for more information about menus, scroll down to the **menus** section. 
5. You're practically ready to go, a few things to note:
- Edit your plugin settings via the config.yml file and use **/ss admin reload** to reload the plugin files. The config.yml won't be updated across all servers so you have to do that one by one.
- Only edit messages via the ingame menu (**/ss admin messages**) since that will update it across all active servers.


## Menus
You can find your menus in your plugin folder > menus, the Main.json file is required since the selector item will always use that file (not to mention you could always edit it).  You can create other (.json) files to create other menus, these are automatically loaded and you just have to redirect to it from other menus by using the filename as 'action' argument.

### Main
```
title - the inventory name
chat_name - the name of the inventory in chat messages
size - amount of slots (rows * 9)
```
#### Lore ("lore")
You can put information about an item (e.g. server or menu) in the lore. These are all replacements you could use in the lore of a server item:
```
{status} - displays the status of the server (OFFLINE, WHITELISTED, ONLINE)
{current_players} - displays the amount of players connected to that server
{max_players} - displays the player capacity
{queue} - displays the amount of players who are currently in the queue
```
An example of a lore for a server item:
```
"lore": [
"&7This is a example lore with server information",
"",
"&b&lStatus: {status}",
"&b&lPlayers: &3{current_players}/{max_players}",
"&b&lQueue: &3{queue}"
]
```
Note: a status object will be having its own color codes in a future release which you could edit via the **/ss admin messages** menu.
### Items
```
material - the material of the item (e.g. redstone_block or brown_wool)
display_name - the name of the item
slot - the slot where the item has to be placed (starting at 0)
```
#### Action types ("action_type")
```
QUEUE - queue for a server
SEND - send the player to a server without queueing (not recommended if dealing with loads of players)

MENU - open another menu
CLOSE - close current menu

NONE - does nothing but displaying an item
```

#### Action info ("action")
Please note everything is case-sensitive, and that action types which are not described below will ignore
this JSON element.
```
QUEUE - server name to queue the player for (not available yet)
SEND - server name to send the player to
MENU - menu name (has to be the same as the JSON file name of the specific menu)
```
## Future features
- Permission (only load a menu item whenever the player has the given permission)
- Queue
- More efficient use of sockets