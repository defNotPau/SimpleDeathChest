# **SimpleDeathChest**
### Simple, Efficient, and maybe reliable Minecraft plugin.
![Java 21](https://img.shields.io/badge/language-Java%2021-9115ff.svg?style=flat-square)

## How does it work?
1. When a player dies, a chest is created in their place.
2. All of their items are deposited onto the chest.
3. That's it!

## Some interesting features:
- All items fit in the chest! 
  - As there's a special inventory that has 45 total slots, so no items will be lost!
- No chest farming over here... 
  - Death chests are not dropped when broken
- Hold up, is it empty?
  - A death chest cannot be broken unless it's empty
- Am I too far down?
  - When dies by the void in the end, the chest will still appear, but in y = 1

 ## Usage:
Don't worry, it's somewhat simple.

Place the .jar file installed in the plugins folder inside your Minecraft server, and start it up!

Now, if everything went well, you should get the following logs:

```
[HH:MM:SS INFO]: [DeathChest] Enabling DeathChest v1.0-SNAPSHOT
[HH:MM:SS INFO]: [DeathChest] I might be working
```

And once you stop the server...

```
[HH:MM:SS WARN]: [DeathChest] I'm def NOT working rn
```

#

Some of the initial commits were made at 2am, sorry about the language used.

> [!CAUTION]
> This is my first plugin made for minecraft, there might be errors, bugs, and maybe an occasional crash in you server, it has not been fully tested.

[paper]: https://papermc.io
