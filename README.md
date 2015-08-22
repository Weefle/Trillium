# *Trillium*

[![Build Status](https://travis-ci.org/TeamTrillium/Trillium.svg)](https://travis-ci.org/TeamTrillium/Trillium)

![logo](http://i.imgur.com/4UePdLH.png)

http://gettrillium.net/

http://www.spigotmc.org/resources/trillium.3882/

## DESCRIPTION

Trillium is a new kind of "essentials". It is the premium essentials.


This Minecraft plugin is a massive collection of commands, options, and handlers that can turn any vanilla server into a fully equipped server with all the tools necessary to break the barriers of your normal Minecraft server.

Trillium boasts to be the best "essentials" type plugin out there; it aspires to be unique, original, and very aesthetically pleasing as well as filled to the brim with fully customizable and very rich features that will turn your generic vanilla server into a wonder playground of options to modify your server the way you want.

Trillium has no unnecessary code that degrades server performance. The original Essentials plugin is fairly over-bloated with useless features that negate the original idea of having only essential features. Trillium, on the other hand, keeps its word by having more features than essentials but only essential ones with more features than essentials ever provided. And on a finishing note, are you not bored of that generic yellow-ish color scheme found on nearly every single server that runs Essentials?

## API

```xml
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>

        <dependency>
            <groupId>net.gettrillium.</groupId>
            <artifactId>Trillium</artifactId>
            <version>0.4.0</version>
        </dependency>
```

You can use the Utils class for *a lot* of neat utilities used throughout the plugin.
`https://github.com/TeamTrillium/Trillium/blob/master/src/main/java/net/gettrillium/trillium/api/Utils.java`

Trillium's API package is located here:
`https://github.com/TeamTrillium/Trillium/tree/master/src/main/java/net/gettrillium/trillium/api`
You can control most of every aspect in that package and it'll control the plugin as you please.

The way Trillium's commands and events are handled are with "Modules". Each module is pretty much a category.
You can use both events and commands in the same class and you do not have to register them in onEnable, Trillium handles that for you.
Furthermore, You don't have to have a commands section in your plugin.yml because Trillium handles that as well.

This all works if you use Trillium's module system.

Example of a class with a command and an event:

```java
package net.gettrillium.trillium.modules;
   
   *import net.gettrillium.trillium.api.TrilliumAPI;
   import net.gettrillium.trillium.api.TrilliumModule;*
   < ... more imports ... >
   
   // Your class needs to extend TrilliumModule
   // You can access "getConfig" directly and pretty much everything else.
   public class AbilityModule extends TrilliumModule {
   
    @Command(name = "Fly",
            command = "fly",
            description = "SOAR THROUGH THE AIR LIKE A MAJESTIC BUTTERFLY!",
            usage = "/fly",
            permissions = {Ability.FLY, Ability.FLY_OTHER})
    public void fly(CommandSender cs, String[] args) {
           // Needs to be the exact same as the command name. Write it ONLY if you want to make the command more synced with everything.
           String cmd = "fly";
           
           // <insert normal command code here>
           if (args.length == 0) {
               if (!(cs instanceof Player)) {
                   
                   // Trillium's message system.
                   // TrilliumAPI.getName(cmd) is from the string above, it's only for stuff like this. Makes your code neat. Not necessary though.
                   // Also prevents future typos.
                   // If the new Message is an Error, you can use given errors from the Error class.
                   // If not then: new Message(Mood.BAD, TrilliumAPI.getName(cmd), "Uh oh, you aint da console bro").to(cs);
                   // the ".to" is always necessary. Never forget it.
                   new Message(TrilliumAPI.getName(cmd), Error.CONSOLE_NOT_ALLOWED).to(cs);
                   return;
               }
   
               // Access everything the TrilliumPlayer contains.
               // Which is most of the stuff handled by trillium like /home.
               TrilliumPlayer player = player(cs.getName());
   
               // Ability.Fly is just a Placeholder permission. It makes the code simpler, you can just use a String permission.
               if (player.hasPermission(Ability.FLY) || player.hasPermission(Ability.FLY_OTHER)) {
               
                   // If you want to access regular player methods but only have the TrilliumPlayer object, then
                   // use "trilliumplay.getProxy().<regular player methods here>"
                   if (player.isFlying()) {
                      // Example of normal error message without the Error enums.
                       new Message(Mood.BAD, TrilliumAPI.getName(cmd), "You are no longer in fly mode.").to(player);
                   } else {
                       new Message(Mood.GOOD, TrilliumAPI.getName(cmd), "You are now in fly mode.").to(player);
                   }
                   player.setFlying(!player.isFlying());
               } else {
                   new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[0]).to(player);
               }
           } else {
               if (cs.hasPermission(Ability.FLY_OTHER)) {
                   TrilliumPlayer target = player(args[0]);
                   if (target != null) {
                       if (target.isFlying()) {
                           new Message(Mood.BAD, TrilliumAPI.getName(cmd), Pallete.HIGHLIGHT.getColor()
                                   + target.getName()
                                   + Pallete.MAJOR.getColor()
                                   + " is no longer in fly mode.").to(cs);
                           new Message(Mood.BAD, TrilliumAPI.getName(cmd), Pallete.HIGHLIGHT.getColor()
                                   + cs.getName()
                                   + Pallete.MAJOR.getColor()
                                   + " removed you from fly mode.").to(target);
   
                           target.setFlying(false);
                       } else {
                           new Message(Mood.GOOD, TrilliumAPI.getName(cmd), Pallete.HIGHLIGHT.getColor()
                                   + target.getName()
                                   + Pallete.MAJOR.getColor()
                                   + " is now in fly mode.").to(cs);
                           new Message(Mood.GOOD, TrilliumAPI.getName(cmd), Pallete.HIGHLIGHT.getColor()
                                   + cs.getName()
                                   + Pallete.MAJOR.getColor()
                                   + " put you in fly mode.").to(target);
   
                           target.setFlying(true);
                       }
                   } else {
                       new Message(TrilliumAPI.getName(cmd), Error.INVALID_PLAYER, args[0]).to(cs);
                   }
               } else {
                   new Message(TrilliumAPI.getName(cmd), Error.NO_PERMISSION, TrilliumAPI.getPermissions(cmd)[1]).to(cs);
               }
           }
       }
   

       // A regular event handler.
       @EventHandler
       public void onDamage(EntityDamageEvent event) {
           if (event.getEntity() instanceof Player) {
               TrilliumPlayer player = player((Player) event.getEntity());
               if (player.isGod() || player.isShadowBanned()) {
                   event.setCancelled(true);
               }
               if (player.isVanished() && getConfig().getBoolean(Configuration.Ability.GOD)) {
                   event.setCancelled(true);
               }
           }
       }
   }
```

Once you finish writing your module class, register it onEnable by doing the following:

`TrilliumAPI.registerModule(new ExampleClass());`

now leave the rest to Trillium. Literally.


## LICENSE

This plugin falls under the LGPL V3 license.
                          
https://github.com/TeamTrillium/Trillium/blob/master/LICENSE.txt

TL;DR for the lazy: `https://tldrlegal.com/license/gnu-lesser-general-public-license-v3-(lgpl-3)`
