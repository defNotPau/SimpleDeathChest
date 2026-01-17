# Translating
## So you know another language and you want to help :D?
You've come to the right place, and I'm here to help you through the process of translating this *beautiful* plugin. <br>
First things first, hello, I'm very excited for you to be part of this plugin. I love foreign languages, and would love for you to help out here. <br>
If you want to help with the translation you'd make a:
 - lang.yml on `src/resources/lang/` **This is the important one**
    - the "lang" would be the language code, like:
        - `en` for English
        - `es` for Spanish
        - `ru` for Russian
        - `de` for German
        - the full list you can find in [Wikipedia](https://en.wikipedia.org/wiki/List_of_ISO_639_language_codes) just take into account the "set 1" field

And now allow me to explain the format ;) <br>
`.yml` or YAML files all have a similar structure, fields and values. <br>
for this application, I'll break down for you the format.
```yaml
list:
  nonPlayer: "Only players can use this command."
  noDeathchests: "You have no active death chests."
  start: "the death-chests found are at:"
  unknown: "unknown owner/time"
  ago: "ago"
  yours: "yours"
  usage: "Usage"
```
`list` is just the container, referenced as `list.(element)` <br>
And in this case it means these are the translations for the `/deathchest list` command <br>
and its children:
 - `nonPlayer` -> Fallback saying that nothing besides players might use the command
 - `noDeathchests` -> Fallback that says that the player has no death chests of their own
 - `start` -> starting message saying that the player's death chests "are found at:" as leaving the response open
 - `unknown` -> remark if the chest's time or ownership is unknown
 - `ago` -> when you say "That happend x time ago" you have that word like ago/elapsed, that's it
 - `yours` -> second person possesive determinant/pronoun
 - `usage` -> way or method to use