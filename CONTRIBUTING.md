# Yes, please do contribute

mainly, if you're going to do anything, if possible refrain from __changing main mechanics__

As for what would be good...
 - anything compatibility-related
 - any fixes in terms of bad code or optimization
 - fixing a bug
 - fixing anything that is an issue in your own server :)
 - and... (see below)

Pull requests won't be accepted right away, but they'll be checked... <br> regularly? no.

# Translating
## So you know another language and you want to help :D?
You've come to the right place, and I'm here to help you through the process of translating this *beautiful* plugin. <br>
First things first, hello, I'm very excited for you to be part of this plugin. I love languages, and would love for you to help out here. <br>
If you want to help with the translation you'd make a:
- lang.yml on `src/resources/lang/` **This is the important one**
    - the "lang" would be the language code, like:
        - `en` for English
        - `es` for Spanish
        - `ru` for Russian
        - `de` for German
        - `fr` for French
        - and so on, the full list you can find over at [Wikipedia](https://en.wikipedia.org/wiki/List_of_ISO_639_language_codes), just take into account the "set 1" field

And now allow me to explain the format ;) <br>
`.yml` or YAML files all have a similar structure, fields and values. <br>
for this application, it'd look something like this, let me break it down.
```yaml
list:
  nonPlayer: "Only players can use this command."
  noDeathchests: "You have no active death chests."
  start: "the death-chests found are at:"
  unknown: "unknown owner/time"
  ago: "ago"
  yours: "yours"
  usage: "Usage"

time:
  minute: "minute"
  minutes: "minutes"

  hour: "hour"
  hours: "hours"

  day: "day"
  days: "days"
```
`list` is just the container, referenced as `list.(element)` <br>
And in this case it means these are the translations for the `/deathchest list` command <br>
and its children:
- `nonPlayer` -> Fallback saying that nothing besides players might use the command
- `noDeathchests` -> Fallback that says that the player has no death chests of their own
- `start` -> starting message saying that the player's death chests "are found at:" as leaving the response open
- `unknown` -> remark if the chest's time or ownership is unknown
- `ago` -> when you say "That happened x time ago" you have that word like ago/elapsed, that's it
- `yours` -> second person possessive determinant/pronoun
- `usage` -> way or method to use


`time` is the section of all that has anything to do with the translation of time (1.5.4) <br>
to be clear, if there's not a  way to say the plural, just keep it as the singular one
- `minute` -> way to say "One **minute**"
- `minutes` -> now the plural way
- `hour` -> for "One **hour**"
- `hours` -> multiple hours?
- `day` -> "One **day**"
- `days` -> multiple days?

<br>
Thank you so much for doing this :D