package xyz.n501yhappy.carryyou;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class VersionCheck {
    private final OkHttpClient client = new OkHttpClient();
    private final String VERSION_URL = "https://pluginversion.n501yhappy.top/carryyou/latest";
    private Plugin plugin;
    public VersionCheck(Plugin _plugin){
        this.plugin = _plugin;
    }
    private boolean compare(String a, String b) { // 版本A 是否大于 版本B
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
    private String getLastVer(){
        Request request = new Request.Builder()
                .url(VERSION_URL)
                .build();
        String response_body;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                plugin.getLogger().warning("请求失败: " + response.code());
                return null;
            }
            response_body = response.body().string();
        } catch (IOException e) {
            plugin.getLogger().warning("更新信息请求失败");
            return null;
        }
        String result = null;
        JSONObject mResult = JSON.parseObject(response_body);
        result = mResult.getString("latest_version");
        return result;
    }
    public void checkVersion(){
        String current_ver = plugin.getDescription().getVersion();
        String latest_ver = getLastVer();
        if (latest_ver == null) {
            plugin.getLogger().info("无法检查更新，跳过版本检测");
            return;
        }
        if (compare(latest_ver, current_ver)) {
            plugin.getLogger().warning("你的Carryyou插件版本落后啦！最新版是" + latest_ver + "，请到github或相关渠道更新qwq，可能会修复一些bug什么的");
        } else {
            plugin.getLogger().info("Carryyou是最新版本呢！ (" + current_ver + ")");
        }
    }
}
