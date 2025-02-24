# **SimpleDeathChest**
### Simple, Efficient, and maybe reliable Paper plugin.

## How does it work?
1. When a player dies, a chest is created in their place.
2. All of their items are deposited onto the chest.
3. That's it!

## Some interesting features:
- All items fit in the chest! 
  - As there's an special inventory that has 45 total slots, no items will be lost!
- No chest farming over here... 
  - Death chests are not dropped when broken
- Hold up, is it empty?
  - A death chest cannot be broken unless it's empty
- Am I too far down?
  - If for some reason a player dies because of the void, the chest will still appear, in the last layer of layer (Why would they break bedrock to kill themselves?) or if in the end in the layer before the void

## Usage:
Don't worry, it's somewhat simple.

Place the .jar file installed in the plugins folder inside your Minecraft server, and start it up!

Now, if everything went well, you should get a the following messages:

```
[HH:MM:SS INFO]: [DeathChest] Enabling DeathChest (current plugin version)
[HH:MM:SS INFO]: [DeathChest] Death Chest restore complete
[HH:MM:SS INFO]: [DeathChest] I might be working
```

And once you stop the server...

```
[HH:MM:SS INFO]: [DeathChest] Disabling DeathChest (current plugin version)
[HH:MM:SS WARN]: [DeathChest] I'm def NOT working rn
```

#

Some of the initial commits were made at 2am, sorry about the language used.

> [!CAUTION]
> This is my first plugin made for minecraft, there might be errors, bugs, and maybe an occasional crash in you server, it has not been fully tested.

[paper]: https://papermc.io
