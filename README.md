# **SimpleDeathChest**
### Simple, Efficient, and hopefully reliable way to safekeep player's items after their death.

## How does it work?
1. When a player dies, a chest is created in their place.
2. All of their items are deposited into the chest.
3. That's it!

## Some interesting features:
- All items fit in the chest! 
  - As there's an special inventory fitting the total number of items, no items will be lost!
- No chest farming over here... 
  - Death chests are not dropped when broken
- Hold up, is it empty?
  - A death chest cannot be broken unless it's empty
- Am I too far down?
  - If for some reason a player dies below the world y-limit downwards the chest will spawn on the lowest layer before the **_void_**
- Hold on... where's my chest??????
  - `/deathchest list` will allow you to see all your chests' coordinates in the world ;)
- **Configurability**: Not everyone wants the same features in their server, so...
  - `explosion_proof` - Maybe you prefer the chest being prone to explosions
  - `player_breakable` - Can players break a deathchest?
  - `items_drop_when_broken` - will items drop when a chest is broken? (by a player)
  - `items_drop_when_exploded` - Uhh... isn't it clear? (wheather items will drop when a chest is exploded)
  - `name_on_chest` - do you want the chest to display who's its _creator_?

### And Translations have arrived :)
- Maybe your server's players don't speak english, so you'd want them to be able to understand some information, right?
- Therefore, in the configuration as for version `1.5.2` the setting `general.language` has become available
- You just put one of the "currently available" language codes in the field, and you're set :)
- Currently available languages:
  - Español (Spanish) `"es"`
  - English (uhhh...) `"en"`
  - Nederlands (Dutch) `"nl"`

## Usage:
Don't worry, it's simple.

Place the .jar file installed in the plugins folder inside your Minecraft server's plugins folder, and start it up!

Now, if everything went well, you should get a the following messages:

```
[HH:MM:SS INFO]: [DeathChest] Enabling DeathChest v(plugin version)
[HH:MM:SS INFO]: [DeathChest] Death Chest restore complete
[HH:MM:SS INFO]: [DeathChest] I might be working
```

And once you stop the server...

```
[HH:MM:SS INFO]: [DeathChest] Disabling DeathChest v(plugin version)
[HH:MM:SS WARN]: [DeathChest] I'm def NOT working rn
```

if you want to configure something, check in your `plugins` folder for a folder called `DeathChest`, then `config.yml` is where you can change the settings ;) 

## Just a friendly reminder...
I'm lazy, very much. I've found bugs I CANNOT fathom why they exist, please go to the Issues and report ANY problem you have. Though, most importantly: _MAYBE_ the plugin has only been tested in 1.21.8 bc I can't be bothered to test it through all versions so... You might be the first one to try the plugin on a minecraft version :D

### AI usage:
I'm not directly opposed to the usage of AI in the world, yet human creativity is something I deeply love. <br>
Aside from the initial versions of the plugin (pre 1.0 versions, unreleased) there has never been any usage of AI on my part. I hope if you're interested in contributing in any way you do it without using AI, I'd love it. 

Some of the initial commits were made at 2am, sorry about the language used.

> [!CAUTION]
> This is my first plugin made for minecraft, there might be errors, bugs, and maybe an occasional crash in you server, it has not been fully tested.

[paper]: https://papermc.io
