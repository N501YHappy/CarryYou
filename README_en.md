# CarryYou - Pick Up Players

<div align="center" style="font-size: 24px;">
  <a href="./README.md">简体中文  </a>
  <a href="./README_en.md">  English</a>
</div>

A Minecraft mod that allows players to grab and carry other entities (including other players)

![Minecraft](https://img.shields.io/badge/Minecraft-1.16+-brightgreen)

## Installation (Need I say more?)

1. Download the **latest version** of the `CarryYou.jar` file
2. Place the file in the server’s `plugins` folder
3. Restart the server or use the `/reload` command
4. The mod will automatically generate a configuration file

[Click here to download CarryYou.jar](https://pluginversion.n501yhappy.top/carryyou/download)

## How to Use

### Capturing Entities
1. **Stealth** (Hold down the Shift key)
2. **Face** the mob or player you want to capture
3. **Press the primary/secondary weapon swap key** (default is the `F` key)
4. After a successful capture, the entity will sit on your head

### Dropping the Entity

1. **Gently Place**: Press the main/off-hand swap key again (while in stealth mode)
2. **Throw with Force**: Left-click/Right-click

## Preview Image
![cxk](https://gh-proxy.org/https://github.com/N501YHappy/CarryYou/blob/main/Preview/img.png)
![nya](https://gh-proxy.org/https://github.com/N501YHappy/CarryYou/blob/main/Preview/img2.png)

### Breakout Mechanics (Note: This is not Identity V)
If you are grabbed by another player, you can:
1. **Rapidly click the left mouse button** to attempt to break free
2. Reach the required CPS (clicks per second) to successfully break free
3. You cannot break free if you have the Weakened debuff

## Permissions and Commands

### Commands
| Command | Alias | Description | Permission | Default |
|------|------|------|------|------|
| `/carryyou` | `/cy reload` | Reload plugin configuration | `carryyou.reload` | OP |

### Permission Nodes
| Permission Node                 | Description          | Default   |
|----------------------|-------------|------|
| `carryyou.reload`    | Allows reloading plugin configuration  | OP   |
| `carryyou.uncarried` | Players with this permission cannot be picked up | No one |
| `carryyou.unbreak`   | Players with this permission cannot escape     | No one |
| `carryyou.can`       | Players with this permission can pick up others    | Everyone |

# Hybrid
## WorldGuard
By calling the WorldGuard API and registering the `carriable` flag, you can set whether players can be picked up within a territory
## Residence
By calling the Residence API and registering the `carriable` flag, you can set whether players can be picked up within a territory
## Dominion
By calling the Dominion API and registering the `carriable` PriFlag, you can set whether players can be picked up within a territory

## Configuration File (config.yml)

The plugin automatically generates a configuration file upon first run. Below are the default settings and descriptions:

```yaml
# config.yml
# config.yml

# Plugin prompt prefix
prefix: “&7[&aCarry&bYou&7] ”

# CPS (clicks per second) required to escape
# Default: 6.0 indicates that 6 clicks per second are required to break free

needed_cps: 6.0

# Progress bar settings
progress_bar:
  # Progress bar length (number of characters)
  length: 20

  # Symbol on the left side of the progress bar
  left: “&7[”

  # Right progress bar symbol
  right: “&7]”

  # Hollow heart (uncompleted section)
  empty: “&c♡”

  # Solid heart (completed section)
  filled: “&c♥”
deny_worlds: # Lifting is prohibited in these worlds! But admins can do whatever they want
  - “world_the_end”

deny_entities: # Loading is prohibited in these worlds! But admins can do whatever they want...
  - “ender_dragon”

# Throw power settings
throw_power:
  # Throw power when using SHIFT + F
  drop: 0.1
  # Throw power when using left click/attack
  attack: 0.9
  # Throw power when right-clicking
  interact: 0.4

```

## Message Configuration (messages.yml)

The plugin automatically generates a message configuration file on first run. You can customize all plugin messages.

```yaml
# messages.yml

# Escape/Break Free Messages
break:
  weakness: “&cYou are now in a weakened state!”
  unbreak: “&cYou can't break free right now... Hang in there for a bit~”
  break_free_subtitle: “&eQuickly click the left mouse button to break free!”
  progress_bar_broken: “&7(&cIt's broken!&7)”

# Carry-related messages
carry:
  world_deny: “&cThe current world doesn't allow you to carry it...”
  no_permission: “&cYou're too small. Wait until you grow up a little more, and then it'll be willing to snuggle into your arms.”
  worldguard_deny: “&cLittle Guard told me this is someone else's territory! You can't do that!”
  residence_deny: “&cThe residence admin won't let you do that.”
  dominion_deny: "&cNo way! This is someone else's territory!"
  entity_deny: “&cYou can't pick it up!”
  player_uncarried: “&cYou can't pick it up!”

# Command-related messages
command:
  no_permission: “&cYou don't have permission to use this command!”
  reload_success: “&aConfiguration file reloaded!”
  reload_error: “&cError reloading the configuration file: ”
```

Supports `&` color codes. After making changes, use `/carryyou reload` to reload.

### Technical Requirements
- **Minecraft Version**: 1.16+ (Unconfirmed)
- **Java Version**: 8+
## Contribution Guidelines
1. Don’t keep bugs to yourself!
2. Share your ideas in the issues section: 或者通过邮件n501yhappy@outlook.com或者qq:3029340076

> A Few Notes
I know my mod is pretty rough, but please let me know if you find any bugs, okay?
let me know if you find any bugs QAQ
Also, if it doesn’t work on your server… just let me know which server you’re using, and I’ll adapt it for you as soon as possible.
Anyway, just speak up with any suggestions—I won’t kill you (

![bstats](https://bstats.org/signatures/bukkit/CarryYou.svg)


Translated with DeepL.com (free version)