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
QUEUE - server name to queue the player for
SEND - server name to send the player to
MENU - menu name (has to be the same as the JSON file name of the specific menu)
```

#### Permissions ("permission")
You could add a permission element to a menu item, so it will only load whenever the player has the permission. 
