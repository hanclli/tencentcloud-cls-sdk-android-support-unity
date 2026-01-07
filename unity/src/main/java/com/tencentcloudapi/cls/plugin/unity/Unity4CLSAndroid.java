package com.tencentcloudapi.cls.plugin.unity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.tencentcloudapi.cls.android.ClsConfigOptions;
import com.tencentcloudapi.cls.android.ClsDataAPI;
import com.tencentcloudapi.cls.android.Credential;
import com.tencentcloudapi.cls.android.plugin.INetworkDiagnosisPlugin;
import com.tencentcloudapi.cls.plugin.network_diagnosis.CLSNetworkDiagnosis;
import com.tencentcloudapi.cls.plugin.network_diagnosis.INetworkDiagnosis;
import com.tencentcloudapi.cls.plugin.network_diagnosis.NetworkDiagnosisPlugin;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.SSLContext;


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
        singletonInit(activity, options);
        hasInit.set(true);
    }

    public static SSLContext getSSLContext(Context context) throws NoSuchAlgorithmException {
        return SSLContext.getDefault();
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

    public static void singletonInit(Context context, ClsConfigOptions clsConfigOptions) {
        clsConfigOptions.enableLog(true);
        clsConfigOptions.addTag("cls_android", "2.0.0");
        ClsDataAPI.startWithConfigOptions(context, clsConfigOptions);
        // 添加插件，自定义插件上报CLS内容
        INetworkDiagnosisPlugin clsNetDiagnosisPlugin = new NetworkDiagnosisPlugin();
        clsNetDiagnosisPlugin.addCustomField("test", "tag");
        clsNetDiagnosisPlugin.setAppCredentialToken("");
        ClsDataAPI.sharedInstance(context).
                addPlugin(clsNetDiagnosisPlugin).
                startPlugin(context);
    }

    public static void tcpPing(INetworkDiagnosis.TcpPingRequest request, INetworkDiagnosis.Callback callback) {
        CLSNetworkDiagnosis.getInstance().tcpPing(request, callback);
    }

    public static void ping(INetworkDiagnosis.PingRequest request, INetworkDiagnosis.Callback callback) {
        CLSNetworkDiagnosis.getInstance().ping(request, callback);
    }

    public static void http(INetworkDiagnosis.HttpRequest request, INetworkDiagnosis.Callback callback) throws NoSuchAlgorithmException {
        final Activity activity = getCurrentActivity();
        if (null == activity) {
            return;
        }
        request.credential = new INetworkDiagnosis.HttpCredential(getSSLContext(activity), null);
        CLSNetworkDiagnosis.getInstance().http(request, callback);
    }

    public static void dns(INetworkDiagnosis.DnsRequest request, INetworkDiagnosis.Callback callback) {
        CLSNetworkDiagnosis.getInstance().dns(request, callback);
    }

    public static void mtr(INetworkDiagnosis.MtrRequest request, INetworkDiagnosis.Callback callback) {
        CLSNetworkDiagnosis.getInstance().mtr(request, callback);
    }
}