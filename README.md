# CarryYou - 把玩家抱起来

![Minecraft](https://img.shields.io/badge/Minecraft-1.16+-brightgreen)

一个允许玩家抓取和携带其他实体（包括玩家）的Minecraft插件

## 安装方法 (还用说嘛？)

1. 下载**最新版本**的 `CarryYou.jar` 文件
2. 将文件放入服务器的 `plugins` 文件夹
3. 重启服务器或使用 `/reload` 命令
4. 插件会自动生成配置文件

[CarryYou.jar点击下载](https://pluginversion.n501yhappy.icu/carryyou/download)

## 使用方法

### 抓取实体
1. **潜行**（按住Shift键）
2. **看向**想要抓取的生物或玩家
3. **按下交换主副手键**（默认是 `F` 键）
4. 成功抓取后，实体会坐在你头上

### 放下实体

1. **轻拿轻放**：再次按下交换主副手键（潜行状态下）
2. **大力飞天**：左键/右键

## 预览图
![cxk](https://gh-proxy.org/https://github.com/N501YHappy/CarryYou/blob/main/Preview/img.png)
![nya](https://gh-proxy.org/https://github.com/N501YHappy/CarryYou/blob/main/Preview/img2.png)

### 挣脱机制 (注意，这并不是第五人格)
如果你被其他玩家抓取，可以：
1. **快速点击左键** 尝试挣脱
2. 达到要求的CPS（每秒点击次数）即可成功挣脱
3. 如果有虚弱buff就不能挣脱了

## 权限和命令

### 命令
| 命令 | 别名 | 描述 | 权限 | 默认 |
|------|------|------|------|------|
| `/carryyou` | `/cy reload` | 重新加载插件配置 | `carryyou.reload` | OP |

### 权限节点
| 权限节点                 | 描述          | 默认   |
|----------------------|-------------|------|
| `carryyou.reload`    | 允许重新加载插件配置  | OP   |
| `carryyou.uncarried` | 有这个权限的人抱不起来 | 谁都没有 |
| `carryyou.unbreak`   | 有这个的跑不掉     | 谁都没有 |
| `carryyou.can`       | 有这个可以抱别人    | 人人有份 |

# 杂交
## WorldGuard
通过调用WorldGuard的API,注册Flag `carriable` 可设置领地内是否抓举
## Residence
通过调用Residence的API,注册Flag `carriable` 可设置领地内是否抓举
## Dominion
通过调用Dominion的API,注册PriFlag `carriable` 可设置领地内是否抓举

## 配置文件 (config.yml)

插件首次运行时会自动生成配置文件。以下是默认配置和说明：

```yaml
# config.yml
# config.yml

# 插件提示前缀
prefix: "&7[&aCarry&bYou&7] "

# 逃脱所需要的CPS（每秒点击次数）
# 默认值: 6.0 表示需要每秒点击6次才能挣脱

needed_cps: 6.0

# 进度条设置
progress_bar:
  # 进度条长度（字符数）
  length: 20

  # 进度条左边符号
  left: "&7["

  # 进度条右边符号
  right: "&7]"

  # 空心的心形（未完成部分）
  empty: "&c♡"

  # 实心的心形（已完成部分）
  filled: "&c♥"
deny_worlds: #这些世界禁止抓举！ 但是管理员干什么都可以哦
  - "world_the_end"

deny_entities: #这些生物禁止抓举！ 但是如果是管理员的话...
  - "ender_dragon"

# 丢出力度设置
throw_power:
  # SHIFT + F键丢出的力度
  drop: 0.1
  # 左键/攻击丢出的力度
  attack: 0.9
  # 右键丢出的力度
  interact: 0.4

```

## 消息配置 (messages.yml)

插件首次运行时会自动生成消息配置文件，你可以自定义所有插件消息。

```yaml
# messages.yml

# 逃离/挣脱相关消息
break:
  weakness: "&c你现在处于虚弱状态！"
  unbreak: "&c你现在不能挣脱哦...忍一会吧~"
  break_free_subtitle: "&e快速点击左键挣脱！"
  progress_bar_broken: "&7(&c坏掉了！&7)"

# 抓取相关消息
carry:
  world_deny: "&c当前世界不允许你抱它..."
  no_permission: "&c你太小啦，等你再长大一点点，它才愿意钻到你怀里哦"
  worldguard_deny: "&c小guard告诉我这是别人的领地！你不可以这样！"
  residence_deny: "&cres管理员不让你这么做哦"
  dominion_deny: "&c不行！这是别人的地盘！"
  entity_deny: "&c你不能抱它！"
  player_uncarried: "&c你不能抱它！"

# 命令相关消息
command:
  no_permission: "&c你没有权限使用此命令！"
  reload_success: "&a配置文件已重新加载！"
  reload_error: "&c重新加载配置文件时出错: "
```

支持 `&` 颜色代码。修改后使用 `/carryyou reload` 重新加载。

### 技术要求
- **Minecraft版本**: 1.16+ （不清楚）
- **Java版本**: 8+
## 贡献指南
1. 遇到bug不要憋着不说！
2. 有什么主意在issues说，或者通过邮件n501yhappy@outlook.com或者qq:3029340076

>一些小事情
我知道我插件写的很烂，但是如果有bug可以给我说吗qwq
github上面交issues,发邮件n501yhappy@outlook.com,QQ3029340076 什么的，有bug一定告诉我QAQ
还有就是你服务器如果运行不了的话...你把服务端告诉我我会尽快给你做适配的
总之有什么建议直接说就行了，我不会杀死你的(

![bstats](https://bstats.org/signatures/bukkit/CarryYou.svg)
