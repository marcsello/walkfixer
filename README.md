# WalkFixer
**Plugin for Spigot 1.12**

This plugin is to fix the infamous bug on spigot servers where players can't walk only jump

This bug occours because somehow the server sets the players walkspeed to zero.
This plugin checks this value upon player join (if this feature enabled), and resets it.
If auto-fix disabled the /wfix command can be used to fix the player's own problem, or someone elses


## Configuration:
```
attempt-autofix:
  If this enabled, the server checks if the players walkspeed is broken upon their join, and automatically tries to fix it
  if this set to false the players can still use /wfix
  Default: false


zero-bad-only: 
  The plugin only considers zero-walkspeed as bad value (intented behaviour) 
  If this is set to false, the plugin fixes everything that is not the default walk speed (not recommended)
  Default: true

tell-target: 
  Notify the player that they just got fixed by the plugin
  Default: true

target-message: 
  The text of that notification
  Default: "Hold on! Your walk speed is being fixed."
```

## Permissions:
```
  walkfixer.wfix.others:
    description: Allows forcefully fixing someones walk speed (that does not involve the player who issued the command!)
    default: op
    
  walkfixer.wfix.self:
    description: Allow players fixing walkspeed for themselves
    default: true
    
  walkfixer.wfix.allow_good:
    description: Forces a fix even if the plugin assumes that it's not necessary
    default: op
 ```
 
 There seems to be other solutions to this problem as well:  
 https://www.spigotmc.org/threads/i-cant-move-only-jump.246386/
 
## Get the plugin
Prebuilt jar files will be aviliable... I think
