package xyz.n501yhappy.carryyou.locales;

public class en_US implements MessageInfo {
    @Override public String onlyPlayer() { return MessageInfo.color("This command can only be used by players!"); }
    @Override public String reloadSuccess() { return MessageInfo.color("&aConfig reloaded!"); }
    @Override public String reloadError() { return MessageInfo.color("&cError reloading config: "); }
    @Override public String reloadErrorLog() { return MessageInfo.color("&cError reloading plugin!"); }
    @Override public String anyError() { return MessageInfo.color("&cUh oh... something went wrong"); }
    @Override public String checkRequestError(int statusCode) { return MessageInfo.color(String.format("Version check request failed: %d", statusCode)); }
    @Override public String checkError(String message) { return MessageInfo.color(String.format("Version check failed: %s", message)); }
    @Override public String checkSkipped() { return MessageInfo.color("Unable to check for updates, skipping version check"); }
    @Override public String updateAvailable(String latestVersion) { return MessageInfo.color(String.format("Your CarryYou plugin is outdated! The latest version is %s, update it on GitHub or related channels, it may fix some bugs", latestVersion)); }
    @Override public String upToDate(String currentVersion) { return MessageInfo.color(String.format("CarryYou is up to date! (%s)", currentVersion)); }

    @Override
    public String findSoftDepends() {
        return MessageInfo.color("&aFound");
    }

    @Override
    public String enable() {
        return MessageInfo.color("&aPlugin Enabled");
    }

    @Override
    public String disable() {
        return MessageInfo.color("&cPlugin Disabled");
    }

    @Override
    public String[] FlagInfo() {
        return new String[]{"Can carry","can carry other entities up"};
    }
}
