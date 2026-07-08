package xyz.n501yhappy.carryyou;

import org.bukkit.plugin.Plugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class VersionCheck {
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private static final String VERSION_URL = "https://pluginversion.n501yhappy.top/carryyou/latest";
    private final Plugin plugin;

    public VersionCheck(Plugin plugin) {
        this.plugin = plugin;
    }

    private boolean compare(String a, String b) {
        if (a == null || b == null) return false;
        String[] result_A = a.split("\\.");
        String[] result_B = b.split("\\.");
        int maxLength = Math.max(result_A.length, result_B.length);

        for (int i = 0; i < maxLength; i++) {
            int numA = i < result_A.length ? Integer.parseInt(result_A[i]) : 0;
            int numB = i < result_B.length ? Integer.parseInt(result_B[i]) : 0;

            if (numA > numB) {
                return true;
            } else if (numA < numB) {
                return false;
            }
        }
        return false;
    }

    private String getLastVer() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VERSION_URL))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                plugin.getLogger().warning("版本检查请求失败: " + response.statusCode());
                return null;
            }
            String body = response.body();

            int key_idx = body.indexOf("\"latest_version\"");
            if (key_idx == -1) return null;
            int point_idx = body.indexOf(':', key_idx);
            int st = body.indexOf('"', point_idx);
            int ed = body.indexOf('"', st + 1);
            return body.substring(st + 1, ed);

        } catch (Exception e) {
            plugin.getLogger().warning("版本检查失败: " + e.getMessage());
            return null;
        }
    }

    public void checkVersion() {
        String currentVer = plugin.getDescription().getVersion();
        String latestVer = getLastVer();
        if (latestVer == null) {
            plugin.getLogger().info("无法检查更新，跳过版本检测");
            return;
        }
        if (compare(latestVer, currentVer)) {
            plugin.getLogger().warning("你的Carryyou插件版本落后啦！最新版是" + latestVer + "，请到github或相关渠道更新qwq，可能会修复一些bug什么的");
        } else {
            plugin.getLogger().info("Carryyou是最新版本呢！ (" + currentVer + ")");
        }
    }
}
