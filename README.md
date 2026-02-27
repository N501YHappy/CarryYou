# CarryYou - 把玩家抱起来?

![Minecraft](https://img.shields.io/badge/Minecraft-1.16+-brightgreen)

一个允许玩家抓取和携带其他实体（包括玩家）的Minecraft插件

~~猜您想找:蛋仔派对~~
## 安装方法 (还用说嘛？)

1. 下载**最新版本**的 `CarryYou.jar` 文件
2. 将文件放入服务器的 `plugins` 文件夹
3. 重启服务器或使用 `/reload` 命令
4. 插件会自动生成配置文件

[CarryYou.jar点击下载](https://www.minebbs.com/resources/carryyou.15388/download)

## 使用方法

### 抓取实体
1. **潜行**（按住Shift键）
2. **看向**想要抓取的生物或玩家
3. **按下交换主副手键**（默认是 `F` 键）
4. 成功抓取后，实体会出现在你前方

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

## 配置文件 (config.yml)

插件首次运行时会自动生成配置文件。以下是默认配置和说明：

```yaml
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

```


### 技术要求
- **Minecraft版本**: 1.16+ （不清楚）
- **Java版本**: 8+
## 贡献指南
1. 遇到bug不要憋着不说！
2. 有什么主意在issues说，或者通过邮件n501yhappy@outlook.com或者qq:3029340076
## 许可证

~~我都抄袭蛋仔派对了要什么许可证？~~

![bstats](https://bstats.org/signatures/bukkit/CarryYou.svg)
