package com.penta.flutterhybridart;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.Platform;
import com.idlefish.flutterboost.Utils;
import com.idlefish.flutterboost.interfaces.INativeRouter;

import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        INativeRouter router = (context, url, urlParams, requestCode, exts) -> {
            String assembleUrl = Utils.assembleUrl(url, urlParams);
            PageRouter.openPageByUrl(context, assembleUrl, urlParams);
        };

        FlutterBoost.BoostLifecycleListener boostLifecycleListener = new FlutterBoost.BoostLifecycleListener() {

            @Override
            public void beforeCreateEngine() {
                Log.e("pentaArt","beforeCreateEngine()");
            }

            @Override
            public void onEngineCreated() {
                Log.e("pentaArt","onEngineCreated()");


                //TODO:这个插件注册是干什么用的
                FlutterBoost.instance().engineProvider().getPlugins().add(new FlutterPlugin() {
                    @Override
                    public void onAttachedToEngine(FlutterPluginBinding binding) {

                    }

                    @Override
                    public void onDetachedFromEngine(FlutterPluginBinding binding) {

                    }
                });

                GeneratedPluginRegistrant.registerWith(FlutterBoost.instance().engineProvider());

                BinaryMessenger messenger = FlutterBoost.instance().engineProvider().getDartExecutor();
                initMainMethod(messenger);
            }

            @Override
            public void onPluginsRegistered() {
                Log.e("pentaArt","onPluginsRegistered()");

            }

            @Override
            public void onEngineDestroy() {
                Log.e("pentaArt","onEngineDestroy()");
            }

        };

        //
        // AndroidManifest.xml 中必须要添加 flutterEmbedding 版本设置
        //
        //   <meta-data android:name="flutterEmbedding"
        //               android:value="2">
        //    </meta-data>
        // GeneratedPluginRegistrant 会自动生成 新的插件方式　
        //
        // 插件注册方式请使用
        // FlutterBoost.instance().engineProvider().getPlugins().add(new FlutterPlugin());
        // GeneratedPluginRegistrant.registerWith()，是在engine 创建后马上执行，放射形式调用
        //



        Platform platform = new FlutterBoost
                .ConfigBuilder(this, router)
                .isDebug(true)
                .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
                .renderMode(FlutterView.RenderMode.texture)
                .lifecycleListener(boostLifecycleListener)
                .build();
        FlutterBoost.instance().init(platform);
    }


    private void initMainMethod(BinaryMessenger messenger) {
        MethodChannel mMainChannel = new MethodChannel(messenger, "penta.com:FlutterHybridArt");
        mMainChannel.setMethodCallHandler((call, result) -> {


            switch (call.method) {//根据方法名进行处理
                case "foo":
                    handleToast(this, call);//具体处理
                    break;
                default:
                    result.notImplemented();
            }

        });
    }


    public static void handleToast(Context context, MethodCall methodCall) {
        String msg = methodCall.argument("msg");
//        int type = methodCall.argument("type");
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
