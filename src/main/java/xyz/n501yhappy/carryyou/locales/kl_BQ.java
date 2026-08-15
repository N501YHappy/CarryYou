package xyz.n501yhappy.carryyou.locales;

public class kl_BQ implements MessageInfo {
    @Override public String onlyPlayer() { return MessageInfo.color("你其实知道这个命令只能由玩家执行...对吧？捅死你喵！"); }
    @Override public String reloadSuccess() { return MessageInfo.color("&a配置文件已重新加载喵！"); }
    @Override public String reloadError() { return MessageInfo.color("&c唔……糟糕，我可能不行了: "); }
    @Override public String reloadErrorLog() { return MessageInfo.color("&cぐへへ、死んだンゴ"); }
    @Override public String anyError() { return MessageInfo.color("&c为什么没人来帮我呀……"); }
    @Override public String checkRequestError(int statusCode) { return MessageInfo.color(String.format("版本检查请求失败: %d", statusCode)); }
    @Override public String checkError(String message) { return MessageInfo.color(String.format("现在，我就是即将迫近的焚风！: %s", message)); }
    @Override public String checkSkipped() { return MessageInfo.color("无法检查更新，跳过版本检测"); }
    @Override public String updateAvailable(String latestVersion) { return MessageInfo.color(String.format("你的Carryyou插件版本落后啦！最新版是%s，请到github或相关渠道更新qwq，可能会修复一些bug什么的", latestVersion)); }
    @Override public String upToDate(String currentVersion) { return MessageInfo.color(String.format("Carryyou是最新版本呢！ (%s)", currentVersion)); }

    @Override
    public String findSoftDepends() {
        return "找到了喵！";
    }

    @Override
    public String WrongConfig() {
        return "配置文件编写错了喵，怎么搞的喵";
    }

    @Override
    public String[] FlagInfo() {
        return new String[]{"举高高","顾名思义，可以举高高"};
    }
}
