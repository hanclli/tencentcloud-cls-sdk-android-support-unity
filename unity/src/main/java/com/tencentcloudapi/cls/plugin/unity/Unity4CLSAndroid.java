package com.tencentcloudapi.cls.plugin.unity;

import android.app.Activity;
import android.util.Log;

import com.tencentcloudapi.cls.android.CLSLog;
import com.tencentcloudapi.cls.android.ClsConfigOptions;
import com.tencentcloudapi.cls.android.ClsDataAPI;
import com.tencentcloudapi.cls.android.Credential;
import com.tencentcloudapi.cls.android.plugin.AbstractPlugin;
import com.tencentcloudapi.cls.plugin.network_diagnosis.CLSNetDiagnosis;
import com.tencentcloudapi.cls.plugin.network_diagnosis.CLSNetDiagnosisPlugin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class Unity4CLSAndroid {
    private static final AtomicBoolean hasInit = new AtomicBoolean(false);
    public static void initialize(ClsConfigOptions options) {
        if (hasInit.get()) {
            return;
        }
        final Activity activity = getCurrentActivity();
        if (null == activity) {
            return;
        }

        ClsConfigOptions clsConfigOptions = new ClsConfigOptions(
                options.getEndpoint(),
                options.getTopicId(),
                new Credential(options.getCredential().getSecretId(), options.getCredential().getSecretKey()));

        ClsDataAPI.startWithConfigOptions(activity.getApplicationContext(), clsConfigOptions);
        // 添加插件，自定义插件上报CLS内容
        AbstractPlugin clsNetDiagnosisPlugin = new CLSNetDiagnosisPlugin();
        ClsDataAPI.sharedInstance(activity.getApplicationContext()).
                addPlugin(clsNetDiagnosisPlugin).
                startPlugin(activity.getApplicationContext());
        hasInit.set(true);
    }

    private static Activity getCurrentActivity() {
        try {
            Object object = Reflection.getStaticField("com.unity3d.player.UnityPlayer", "currentActivity", null);
            if (object instanceof Activity) {return (Activity)object;}
        } catch (Exception exception) {
            Log.w("SLSAndroidAgent", "Failed to get the current activity from UnityPlayer");
            exception.printStackTrace();
        }
        return null;
    }

    public static void tcpPing(String domain, int port, CLSNetDiagnosis.Output output,
                               CLSNetDiagnosis.Callback callback, Map<String, String> customField) {
        Map<String, String> customFiled = new LinkedHashMap<>();
        customFiled.put("cls","custom field");
        CLSNetDiagnosis.getInstance().tcpPing("www.tencentcloud.com", 80, new CLSNetDiagnosis.Output(){
            @Override
            public void write(String line) {
                System.out.println(line);
            }
        }, new CLSNetDiagnosis.Callback() {
            @Override
            public void onComplete(String result) {
                // result为探测结果，JSON格式。
                CLSLog.d("TraceRoute", String.format("traceRoute result: %s", result));
            }
        }, customFiled);
    }
}