# CarryYou - Pick Up Players

<div align="center">
  <a href="./README.md">简体中文</a> ｜
  <a href="./README_en.md">English</a>
</div>

<br>

A Minecraft mod that allows players to **grab and carry** other entities (including other players).


## Installation Instructions (Need I say more?)

1. Download the **latest version** of the `CarryYou.jar` file
2. Place the file in the server’s `plugins` folder
3. Restart the server or use the `/reload` command
4. The mod will automatically generate a configuration file

[Click here to download CarryYou.jar](https://pluginversion.n501yhappy.top/carryyou/download)

## How to Use

### Picking Up Entities
1. **Sneak** (hold down the Shift key)
2. **Look at** the mob or player you want to pick up
3. **Press the primary/secondary weapon swap key** (default is `F`) or **press the drop item key** (default is `Q`; can be changed in the configuration)
4. After a successful capture, the entity will sit on your head

> **Cooldown**: You must wait for the cooldown to expire between captures (can be set in the configuration); administrators are not subject to this restriction

### Dropping an Entity

1. **Gently Set Down**: Press the grab key again (while in stealth mode)
2. **Throw It High**: Left-click/Right-click

## Preview Image
![cxk](https://gh-proxy.org/https://github.com/N501YHappy/CarryYou/blob/main/Preview/img.png)
![nya](https://gh-proxy.org/https://github.com/N501YHappy/CarryYou/blob/main/Preview/img2.png)

### Special Creature Effects
- **Pick up a chicken**: Automatically gain a slow-fall effect to prevent fall damage
- **Pick up a Creeper**: The Creeper won’t explode—it’s safe and worry-free
- The above features can be toggled on or off in the configuration file (`fun.with_chicken` / `fun.with_creeper`)

### Breakout Mechanism (Note: This is not Identity V)
If you are grabbed by another player, you can:
1. **Rapidly click the left mouse button** to attempt to break free
2. Reach the required CPS (clicks per second) to successfully break free
3. You cannot break free if you have the Weakness debuff

## Permissions and Commands

### Commands
| Command | Alias | Description | Permission | Default |
|------|------|------|------|------|
| `/carryyou` | `/cy reload` | Reload plugin configuration | `carryyou.reload` | OP |

### Permission Nodes
| Permission Node                 | Description          | Default   |
|----------------------|-------------|------|
| `carryyou.reload`    | Allows reloading plugin configuration  | OP   |
| `carryyou.uncarried` | Players with this permission cannot be carried | None |
| `carryyou.unbreak`   | Players with this permission cannot escape     | None |
| `carryyou.can`       | Players with this permission can carry others    | Everyone |

# Hybrid
## WorldGuard
By calling the WorldGuard API and registering the `carriable` flag, you can set whether players can be carried within a territory.
## Residence
By calling the Residence API and registering the `carriable` flag, you can set whether players can be carried within a territory.
## Dominion
By calling the Dominion API and registering the PriFlag `carriable`, you can set whether players can be carried within a territory.

## Configuration File (config.yml)

The plugin automatically generates a configuration file when it runs for the first time. Below are the default settings and descriptions:

```yaml
# config.yml
# config.yml

# Plugin prompt prefix
prefix: “&7[&aCarry&bYou&7] ”

# CPS (clicks per second) required to escape
# Default: 6.0 means you need to click 6 times per second to break free

needed_cps: 6.0

# Pickup cooldown (milliseconds); administrators are not subject to this limit
# Default: 1000 means a 1-second interval between picks
cooldown: 1000

# Progress bar settings
progress_bar:
  # Progress bar length (number of characters)
  length: 20

  # Symbol on the left side of the progress bar
  left: “&7[”

  # Symbol on the right side of the progress bar
  right: “&7]”

  # Hollow heart (uncompleted portion)
  empty: “&c♡”

  # Solid heart (completed portion)
  filled: “&c♥”
deny_worlds: # Lifting is prohibited in these worlds! But admins can do whatever they want.
  - “world_the_end”

deny_entities: # Lifting is prohibited for these entities! But if you're an admin...
  - “ender_dragon”

# Fun feature settings
fun:
  # Apply a slow-descent effect when picking up a chicken
  with_chicken: true
  # Creepers won’t explode when picked up
  with_creeper: true

# Throw power settings
throw_power:
  # Throw power when using SHIFT + F
  drop: 0.1
  # Throw power when left-clicking/attacking
  attack: 0.9
  # Throw power when right-clicking
  interact: 0.4

# Trigger settings
trigger:
  # true = triggered by SHIFT+F, false = triggered by SHIFT+Q
  shift_f_q: true
  # true = must have empty hands to trigger, false = no restriction
  empty: true

```

## Message Configuration (messages.yml)

The mod automatically generates a message configuration file on first run; you can customize all mod messages.

```yaml
# messages.yml

# Escape/Break-free related messages
break:
  weakness: “&cYou are currently in a weakened state!”
  unbreak: “&cYou can't break free right now... Just hang in there for a bit~”
  break_free_subtitle: “&eQuickly click the left mouse button to break free!”
  progress_bar_broken: “&7(&cIt’s broken!&7)”

# Messages related to carrying
carry:
  cooldown: “&cYou’ll have to wait another %s seconds” # %s is the number of seconds displayed, not milliseconds!
  world_deny: “&cThis world doesn’t allow you to carry it...”
  no_permission: “&cYou're too small. Wait until you grow up a little more, and then it'll be willing to snuggle up in your arms.”
  worldguard_deny: “&cLittle Guard tells me this is someone else's territory! You can't do that!”
  residence_deny: “&cThe residence admin won't let you do that.”
  dominion_deny: "&cNo way! This is someone else’s territory!"
  entity_deny: “&cYou can’t pick it up!”
  player_uncarried: “&cYou can’t pick it up!”

# Command-related messages
command:
  no_permission: “&cYou don’t have permission to use this command!”
  reload_success: “&aThe configuration file has been reloaded!”
  reload_error: “&cAn error occurred while reloading the configuration file: ”
```

Supports `&` color codes. After making changes, use `/carryyou reload` to reload.

### Technical Requirements
- **Minecraft Version**: 1.16+ (unconfirmed)
- **Java Version**: 8+
## Contribution Guidelines
1. Don’t keep bugs to yourself!
2. Share your ideas in the issues section: 或者通过邮件n501yhappy@outlook.com或者qq:3029340076

> A Few Notes
I know my mod is pretty rough, but please let me know if you find any bugs, qwq
github上面交issues,发邮件n501yhappy@outlook.com, QQ 3029340076, etc.—please definitely let me know if you find any bugs QAQ
Also, if it doesn’t work on your server… just let me know which server you’re using, and I’ll adapt it for you as soon as possible.
Anyway, just speak up with any suggestions—I won’t kill you (

![bstats](https://bstats.org/signatures/bukkit/CarryYou.svg)


Translated with DeepL.com (free version)