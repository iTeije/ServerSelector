# ServerSelector
Here you can find all necessary information to set up the plugin and your menus.

## Menus
You could create new menus and redirect to them from any other menu you already had.

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
Note: a status object will be having its own color codes in a future release which you could edit via the /ss admin messages menu.
### Items
```
material - the material of the item (e.g. redstone_block or brown_wool)
display_name - the name of the item
slot - the slot where the item has to be placed (starting at 0)
```
#### Action types ("action_type")
```
QUEUE - queue someone for a server
SEND - send someone to a server without queueing (not recommended if dealing with loads of players)
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
#### Future features
- Permission (only load a menu item whenever the player has the given permission)
- Queue
- More efficient sockets and bungee messaging