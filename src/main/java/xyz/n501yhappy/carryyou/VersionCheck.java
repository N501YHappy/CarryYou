package xyz.n501yhappy.carryyou;

import org.bukkit.plugin.Plugin;
import xyz.n501yhappy.carryyou.locales.MessageInfo;

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
                plugin.getLogger().warning(MessageInfo.current().checkRequestError(response.statusCode()));
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
            plugin.getLogger().warning(MessageInfo.current().checkError(e.getMessage()));
            return null;
        }
    }

    public void checkVersion() {
        String currentVer = plugin.getDescription().getVersion();
        String latestVer = getLastVer();
        if (latestVer == null) {
            plugin.getLogger().info(MessageInfo.current().checkSkipped());
            return;
        }
        if (compare(latestVer, currentVer)) {
            plugin.getLogger().warning(MessageInfo.current().updateAvailable(latestVer));
        } else {
            plugin.getLogger().info(MessageInfo.current().upToDate(currentVer));
        }
    }
}
