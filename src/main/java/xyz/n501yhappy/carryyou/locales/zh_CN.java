package xyz.n501yhappy.carryyou.locales;

public class zh_CN implements MessageInfo {
    @Override public String onlyPlayer() { return MessageInfo.color("这个命令只能由玩家执行！！"); }
    @Override public String reloadSuccess() { return MessageInfo.color("&a配置文件已重新加载！"); }
    @Override public String reloadError() { return MessageInfo.color("&c重新加载配置文件时出错: "); }
    @Override public String reloadErrorLog() { return MessageInfo.color("&c插件重载错误！"); }
    @Override public String anyError() { return MessageInfo.color("&c出错了！"); }
    @Override public String checkRequestError(int statusCode) { return MessageInfo.color(String.format("版本检查请求失败: %d", statusCode)); }
    @Override public String checkError(String message) { return MessageInfo.color(String.format("版本检查失败: %s", message)); }
    @Override public String checkSkipped() { return MessageInfo.color("无法检查更新，跳过版本检测"); }
    @Override public String updateAvailable(String latestVersion) { return MessageInfo.color(String.format("你的Carryyou插件版本落后啦！最新版是%s，请到github或相关渠道更新qwq，可能会修复一些bug什么的", latestVersion)); }
    @Override public String upToDate(String currentVersion) { return MessageInfo.color(String.format("Carryyou是最新版本呢！ (%s)", currentVersion)); }
}
