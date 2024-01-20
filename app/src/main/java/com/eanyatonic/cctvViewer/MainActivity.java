package com.eanyatonic.cctvViewer;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eanyatonic.cctvViewer.bean.EpgInfo;
import com.eanyatonic.cctvViewer.tools.FileTool;
import com.eanyatonic.cctvViewer.tools.FileUtils;
import com.eanyatonic.cctvViewer.tools.RestartAppUtil;
import com.eanyatonic.cctvViewer.tools.SysTool;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public FileTool filetool;
    private RecyclerView recyclerView;
    private View mCustomView;
    private FrameLayout mFrameLayout;
    private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;
    private com.tencent.smtt.sdk.WebView webView; // 导入 X5 WebView
    private com.tencent.smtt.sdk.WebView cctvFinishedView;

    private String aach;
    private ChannelAdapter channelAdapter;
    private final String[] liveUrls = {
            "https://tv.cctv.com/live/cctv1/",
            "https://tv.cctv.com/live/cctv2/",
            "https://tv.cctv.com/live/cctv3/",
            "https://tv.cctv.com/live/cctv4/",
            "https://tv.cctv.com/live/cctv5/",
            "https://tv.cctv.com/live/cctv6/",
            "https://tv.cctv.com/live/cctv7/",
            "https://tv.cctv.com/live/cctv8/",
            "https://tv.cctv.com/live/cctvjilu",
            "https://tv.cctv.com/live/cctv10/",
            "https://tv.cctv.com/live/cctv11/",
            "https://tv.cctv.com/live/cctv12/",
            "https://tv.cctv.com/live/cctv13/",
            "https://tv.cctv.com/live/cctvchild",
            "https://tv.cctv.com/live/cctv15/",
            "https://tv.cctv.com/live/cctv16/",
            "https://tv.cctv.com/live/cctv17/",
            "https://tv.cctv.com/live/cctv5plus/",
            "https://tv.cctv.com/live/cctveurope",
            "https://tv.cctv.com/live/cctvamerica/",
            "https://www.yangshipin.cn/#/tv/home?pid=600001859",
            "https://www.yangshipin.cn/#/tv/home?pid=600002309",
            "https://www.yangshipin.cn/#/tv/home?pid=600002521",
            "https://www.yangshipin.cn/#/tv/home?pid=600002483",
            "https://www.yangshipin.cn/#/tv/home?pid=600002520",
            "https://www.yangshipin.cn/#/tv/home?pid=600002475",
            "https://www.yangshipin.cn/#/tv/home?pid=600002508",
            "https://www.yangshipin.cn/#/tv/home?pid=600002485",
            "https://www.yangshipin.cn/#/tv/home?pid=600002509",
            "https://www.yangshipin.cn/#/tv/home?pid=600002498",
            "https://www.yangshipin.cn/#/tv/home?pid=600002506",
            "https://www.yangshipin.cn/#/tv/home?pid=600002531",
            "https://www.yangshipin.cn/#/tv/home?pid=600002481",
            "https://www.yangshipin.cn/#/tv/home?pid=600002516",
            "https://www.yangshipin.cn/#/tv/home?pid=600002525",
            "https://www.yangshipin.cn/#/tv/home?pid=600002484",
            "https://www.yangshipin.cn/#/tv/home?pid=600002490",
            "https://www.yangshipin.cn/#/tv/home?pid=600002503",
            "https://www.yangshipin.cn/#/tv/home?pid=600002505",
            "https://www.yangshipin.cn/#/tv/home?pid=600002532",
            "https://www.yangshipin.cn/#/tv/home?pid=600002493",
            "https://www.yangshipin.cn/#/tv/home?pid=600002513",
    };


    /**
     * for (let index = 0; index < document.querySelector(".tv-main-con-r-list-left").parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.__vue__._data.tabB.length; index++) {
     * console.log(document.querySelector(".tv-main-con-r-list-left").parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.__vue__._data.tabB[index].pid)
     * }
     */

    private String[] channelNames = {
            "CCTV-1 综合",
            "CCTV-2 财经",
            "CCTV-3 综艺",
            "CCTV-4 中文国际",
            "CCTV-5 体育",
            "CCTV-6 电影",
            "CCTV-7 军事农业",
            "CCTV-8 电视剧",
            "CCTV-9 纪录",
            "CCTV-10 科教",
            "CCTV-11 戏曲",
            "CCTV-12 社会与法",
            "CCTV-13 新闻",
            "CCTV-14 少儿",
            "CCTV-15 音乐",
            "CCTV-16 奥林匹克",
            "CCTV-17 农业农村",
            "CCTV-5+ 体育赛事",
            "CCTV 欧洲",
            "CCTV 美国",
            "CCTV1",
            "北京卫视",
            "江苏卫视",
            "东方卫视",
            "浙江卫视",
            "湖南卫视",
            "湖北卫视",
            "广东卫视",
            "广西卫视",
            "黑龙江卫视",
            "海南卫视",
            "重庆卫视",
            "深圳卫视",
            "四川卫视",
            "河南卫视",
            "福建东南卫视",
            "贵州卫视",
            "江西卫视",
            "辽宁卫视",
            "安徽卫视",
            "河北卫视",
            "山东卫视"
    };


    private int currentLiveIndex;

    private static final String PREF_NAME = "MyPreferences";
    private static final String PREF_KEY_LIVE_INDEX = "currentLiveIndex";

    private boolean doubleBackToExitPressedOnce = false;

    private final StringBuilder digitBuffer = new StringBuilder(); // 用于缓存按下的数字键
    private static final long DIGIT_TIMEOUT = 3000; // 超时时间（毫秒）

    private TextView inputTextView; // 用于显示正在输入的数字的 TextView

    // 初始化透明的View
    private View loadingOverlay;

    // 频道显示view
    private TextView overlayTextView;

    private String info = "";

    public List<EpgInfo> epgList = new ArrayList<>();
    public List<EpgInfo> epgYSPList = new ArrayList<>();

    private String liveType;


    @JavascriptInterface
    public void setCctvEpgInfo(String result) throws JSONException {
        epgList.clear();
        JSONArray jsonArray = new JSONArray(result);
        for (int i = 0; i < jsonArray.length(); i++) {
            EpgInfo epgInfo = new EpgInfo(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("name"));
            epgList.add(epgInfo);
            Log.d("setCctvEpgInfo", epgInfo.getName() + epgInfo.getId());
        }

    }

    @JavascriptInterface
    public void setYanShiPingEpgInfo(String result) throws JSONException {
        epgYSPList.clear();
        epgYSPList.add(new EpgInfo("document.querySelector(\".tv-main-con-r-list > div > div:nth-child(2)\").click()","CCTV1"));
        JSONArray jsonArray = new JSONArray(result);
        for (int i = 0; i < jsonArray.length(); i++) {
            EpgInfo epgInfo = new EpgInfo(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("name"));
            epgYSPList.add(epgInfo);
            Log.d("setYanShiPingEpgInfo",epgInfo.getName());
        }

        Log.d("setYanShiPingEpgInfo", epgYSPList.size()+"x");
    }

    private String yanShiPingCurrentChannel;
    @JavascriptInterface
    public void setYanShiPingCurrentChannel(String result) {
        yanShiPingCurrentChannel = result;
    }

    // document.querySelector(".tv-main-con-r-list-left-imgb.tvSelect").innerText


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFrameLayout = findViewById(R.id.flVideoContainer);
        // X5WebView初始化
        initX5WebView();
        Toast.makeText(MainActivity.this, "x5内核状态" + QbSdk.canLoadX5(getApplicationContext()) + "内核架构" + SysTool.showSysAach() + "  如果x5内核状态为true,无法播放请重启应用", Toast.LENGTH_SHORT).show();
        // 初始化 WebView
        webView = findViewById(R.id.webView);
        // 初始化显示正在输入的数字的 TextView
        inputTextView = findViewById(R.id.inputTextView);
        // 初始化 loadingOverlay
        loadingOverlay = findViewById(R.id.loadingOverlay);
        // 初始化 overlayTextView
        overlayTextView = findViewById(R.id.overlayTextView);

        // 加载上次保存的位置
        loadLastLiveIndex();
        // 添加自动化调试
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true);
//            Log.d("remote debug", "远程调试");
//        }
        // 配置 WebView 设置
//        filetool = new FileTool(this);
//        String backwardScript =filetool.readFileContent("js/backwardScript.js");
//
//        String forwardScript =filetool.readFileContent("js/forwardScript.js");
//
//        String cctvOpenScript =filetool.readFileContent("js/getEpgScript.js");
        webView.addJavascriptInterface(this, "bridge");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        // 添加自动播放视频
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        webSettings.setUseWideViewPort(true);
        // 启用 JavaScript 自动点击功能
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setMixedContentMode(WebSettings.LOAD_NORMAL);


        // 设置 WebViewClient 和 WebChromeClient
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 忽略 SSL 错误
            }

            // 设置 WebViewClient，监听页面加载完成事件
            @Override
            public void onPageFinished(WebView view, String url) {
                cctvFinishedView = view;

                // 页面加载完成后执行 JavaScript 脚本
                // 清空info
                info = "";
                if (url.contains("cctv")) {
                    liveType = "cctv";

                    // 获取节目预告和当前节目
                    view.evaluateJavascript("document.querySelector('#jiemu > li.cur.act').innerText", value -> {
                        // 处理获取到的元素值
                        if (!value.equals("null") && !value.isEmpty()) {
                            String elementValueNow = value.replace("\"", ""); // 去掉可能的引号
                            info += elementValueNow + "\n";
                        }
                    });
                    view.evaluateJavascript("document.querySelector('#jiemu > li:nth-child(4)').innerText", value -> {
                        // 处理获取到的元素值
                        if (!value.equals("null") && !value.isEmpty()) {
                            String elementValueNext = value.replace("\"", ""); // 去掉可能的引号
                            info += elementValueNext;
                        }
                    });

                    view.evaluateJavascript("""
                            {
                                function sleep(ms) {
                                    return new Promise(resolve => setTimeout(resolve, ms));
                                }
                                
                                let interval = setInterval(async function () {
                                    console.log('页面加载完成！');
                                    // 休眠 1000 毫秒（1秒）
                                    await sleep(1000);
                                    // 休眠 50 毫秒
                                    await sleep(50);
                                    console.log('获取节目单');
                                    var epg_list = [
                                        { name: "", id: "" },
                                    ];
                                
                                    var epg_child = document.querySelector("#epg_player").childNodes;
                                    for (let index = 0; index < epg_child.length; index++) {
                                        if (index % 2 == 1) {
                                            epg_list.push({ name: epg_child[index].innerText, id: epg_child[index].id });
                                        }
                                    }
                                    let filteredArray = epg_list.map(obj => {
                                        Object.keys(obj).forEach(key => obj[key] === '' || obj[key] === null ? delete obj[key] : '');
                                        return obj;
                                    }).filter(obj => Object.keys(obj).length > 0);
                                    console.log(JSON.stringify(filteredArray));
                                    clearInterval(interval);
                                    bridge.setCctvEpgInfo(JSON.stringify(filteredArray));
                                }, 3000);
                            }
                            """, null);

                    view.evaluateJavascript("""
                            {
                                        function sleep(ms) {
                                            return new Promise(resolve => setTimeout(resolve, ms));
                                        }
                                    
                                        // 页面加载完成后执行 JavaScript 脚本
                                        let interval = setInterval(async function executeScript() {
                                            console.log('页面加载完成！');
                                    
                                            // 休眠 1000 毫秒（1秒）
                                            await sleep(1000);
                                    
                                            // 休眠 50 毫秒
                                            await sleep(50);
                                    
                                            console.log('点击分辨率按钮');
                                            if(document.querySelector('#resolution_item_720_player')===null){
                                                var elem = document.querySelector("#resolution_item_480_player")
                                                elem.click();
                                            }else{
                                                var elem = document.querySelector('#resolution_item_720_player');
                                                elem.click();
                                            }
                                            
                                            // 休眠 50 毫秒
                                            await sleep(50);
                                    
                                            console.log('设置音量并点击音量按钮');
                                            var btn = document.querySelector('#player_sound_btn_player');
                                            btn.setAttribute('volume', 100);
                                            btn.click();
                                            btn.click();
                                            btn.click();
                                    
                                            // 休眠 50 毫秒
                                            await sleep(50);
                                    
                                            console.log('点击全屏按钮');
                                            var fullscreenBtn = document.querySelector('#player_pagefullscreen_player');
                                            fullscreenBtn.click();
                                            clearInterval(interval);
                                        }, 3000);
                            }
                            """, null);


                    new Handler().postDelayed(() -> {
                        loadingOverlay.setVisibility(View.GONE);
                        showOverlay(channelNames[currentLiveIndex] + "\n" + info);
                        channelAdapter.notifyDataSetChanged();
                    }, 3000);

                } else if (url.contains("yangshipin")) {
                    liveType = "yangshipin";
                    //当前频道

                    view.evaluateJavascript("""
                            {
                                function sleep(ms) {
                                    return new Promise(resolve => setTimeout(resolve, ms));
                                }

                                // 页面加载完成后执行 JavaScript 脚本
                                let interval = setInterval(async function () {
                                    await sleep(1050);
                                    var cur;
                                    if(document.querySelector('.tv-main-con-r-list-left-imgb.tvSelect')===null){
                                        // cctv频道当前频道查询
                                       cur = document.querySelector(".tv-main-con-r-list-left-imga.tvSelect").innerText.replace(/[\\s\\n]/g, '')
                                    }else{
                                       // 地方频道当前频道查询
                                       cur = document.querySelector('.tv-main-con-r-list-left-imgb.tvSelect').innerText.replace(/[\\s\\n]/g, '')
                                    }
                                    console.log(cur)
                                    bridge.setYanShiPingCurrentChannel(cur)
                                    clearInterval(interval)
                                }, 3000);
                            }
                            """,null);


                    // 获取频道列表
                    view.evaluateJavascript("""
                            {
                                function sleep(ms) {
                                    return new Promise(resolve => setTimeout(resolve, ms));
                                }

                                // 页面加载完成后执行 JavaScript 脚本
                                let interval = setInterval(async function () {
                                    var epgInfoList = [];
                                    // 休眠 1000 毫秒（1秒）
                                    await sleep(1050);
                                    var epgList = document.querySelector(".tv-main-con-r-list > div").childNodes
                                
                                    for (let index = 0; index < epgList.length; index++) {
                                        const element = epgList[index];
                                        if(element.className==="oveerflow-1 tv-main-con-r-list-left-imgb"||element.className==="oveerflow-1 tv-main-con-r-list-left-imgb tvSelect"){
                                            var epgInfo = {
                                                "id":'document.querySelector(".tv-main-con-r-list > div").childNodes['+index+']'+'.click()',
                                                "name":document.querySelector(".tv-main-con-r-list > div").childNodes[index].innerText.replace(/\\n/g, '')
                                            }
                                            epgInfoList.push(epgInfo)
                                        }
                                
                                    }

                                    if(epgList.length>0){
                                        clearInterval(interval)
                                        bridge.setYanShiPingEpgInfo(JSON.stringify(epgInfoList))
                                    }
                                }, 3000);
                            }
                            """, null);

                    view.evaluateJavascript("""
                            {
                                function sleep(ms) {
                                    return new Promise(resolve => setTimeout(resolve, ms));
                                }
                                let interval = setInterval(async function () {
                                    await sleep(1050);
                                    console.log('设置音量并点击音量按钮');
                                    document.querySelector(".container").__vue__.volume = 1
                                    await sleep(50);
                                    document.querySelector(".container").__vue__.changeVolume()
                                    await sleep(50);
                                    document.querySelector(".container").__vue__.changeVolume()
                                    await sleep(50);
                                    console.log('点击全屏按钮');
                                    document.querySelector(".videoFull").click()
                                    clearInterval(interval);
                                }, 3000);
                            }
                            """, null);
                }

            }


        });
        // 设置 WebView 客户端
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
                Log.d("onShowCustomView", "onShowCustomView");
                super.onShowCustomView(view, callback);
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                mCustomView = view;
                mFrameLayout.addView(mCustomView);
                mCustomViewCallback = callback;
                webView.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            @Override
            public void onHideCustomView() {
                Log.d("onHideCustomView", "onHideCustomView");
                webView.setVisibility(View.VISIBLE);
                if (mCustomView == null) {
                    return;
                }
                mCustomView.setVisibility(View.GONE);
                mFrameLayout.removeView(mCustomView);
                mCustomViewCallback.onCustomViewHidden();
                mCustomView = null;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                super.onHideCustomView();
            }


        });


        // 禁用缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);

        // 在 Android TV 上，需要禁用焦点自动导航
        webView.setFocusable(false);


        // 加载初始网页
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(null);
        epgList.add(new EpgInfo("暂无节目单", "暂无节目单"));
        channelAdapter = new ChannelAdapter(epgList, recyclerView, webView);
        recyclerView.setAdapter(channelAdapter);

        loadLiveUrl();

    }


    private void initX5WebView() {
        // 搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
//        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//            @Override
//            public void onViewInitFinished(boolean arg0) {
//                // x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                Log.d("X5CORE", "onViewInitFinished is " + arg0);
//            }
//
//            @Override
//            public void onCoreInitFinished() {
//
//            }
//        };
//        // x5内核初始化接口
//        QbSdk.initX5Environment(this, cb);

        // 在调用TBS初始化、创建WebView之前进行如下配置2
        boolean canLoadX5 = QbSdk.canLoadX5(getApplicationContext());
        if (!canLoadX5) {
            tbsInstall();
        }

    }

    private void tbsInstall() {
        HashMap<String, Object> map = new HashMap<>(2);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        aach = SysTool.showSysAach();
        if (aach.contains("arm64")) {
            FileUtils.copyAssets(getApplicationContext(), "046279_arm64v8a_x5.tbs.apk", FileUtils.getTBSFileDir(getApplicationContext()).getPath() + "/046279_arm64v8a_x5.tbs.apk");
            QbSdk.reset(getApplicationContext());
            QbSdk.installLocalTbsCore(getApplicationContext(), 46279, FileUtils.getTBSFileDir(getApplicationContext()).getPath() + "/046279_arm64v8a_x5.tbs.apk");
        } else if (aach.contains("armeabi")) {
            FileUtils.copyAssets(getApplicationContext(), "046914_armeabi_x5.tbs.apk", FileUtils.getTBSFileDir(getApplicationContext()).getPath() + "/046914_armeabi_x5.tbs.apk");
            QbSdk.reset(getApplicationContext());
            QbSdk.installLocalTbsCore(getApplicationContext(), 46914, FileUtils.getTBSFileDir(getApplicationContext()).getPath() + "/046914_armeabi_x5.tbs.apk");
        }

        Log.e("canloadX5", "canLoadX5: " + QbSdk.canLoadX5(getApplicationContext()) + "|TbsVersion:" + QbSdk.getTbsVersion(getApplicationContext()));
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!recyclerView.hasFocus() && event.getAction() == KeyEvent.ACTION_DOWN && liveType.equals("cctv")) {
            cctvFinishedView.evaluateJavascript(
                    """
                                 function simulate(element, eventName) {
                                     var options = extend(defaultOptions, arguments[2] || {});
                                     var oEvent, eventType = null;
                                 
                                     for (var name in eventMatchers) {
                                         if (eventMatchers[name].test(eventName)) {
                                             eventType = name;
                                             break;
                                         }
                                     }
                                 
                                     if (!eventType) throw new SyntaxError('Only HTMLEvents and MouseEvents interfaces are supported');
                                 
                                     if (document.createEvent) {
                                         oEvent = document.createEvent(eventType);
                                         if (eventType == 'HTMLEvents') {
                                             oEvent.initEvent(eventName, options.bubbles, options.cancelable);
                                         } else {
                                             oEvent.initMouseEvent(eventName, options.bubbles, options.cancelable, document.defaultView, options.button, options.pointerX, options.pointerY, options.pointerX, options.pointerY, options.ctrlKey, options.altKey, options.shiftKey, options.metaKey, options.button, element);
                                         }
                                         element.dispatchEvent(oEvent);
                                     } else {
                                         options.clientX = options.pointerX;
                                         options.clientY = options.pointerY;
                                         var evt = document.createEventObject();
                                         oEvent = extend(evt, options);
                                         element.fireEvent('on' + eventName, oEvent);
                                     }
                                     return element;
                                 }
                                 
                                 function extend(destination, source) {
                                     for (var property in source) destination[property] = source[property];
                                     return destination;
                                 }
                                 
                                 var eventMatchers = {
                                     'HTMLEvents': /^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,
                                     'MouseEvents': /^(?:click|dblclick|mouse(?:down|up|over|move|out))$/
                                 }
                                 var defaultOptions = {
                                     pointerX: 0,
                                     pointerY: 0,
                                     button: 0,
                                     ctrlKey: false,
                                     altKey: false,
                                     shiftKey: false,
                                     metaKey: false,
                                     bubbles: true,
                                     cancelable: true
                                 }
                                 
                                 function triggerMouseEvent (node, eventType) {
                                     var clickEvent = document.createEvent ('MouseEvents');
                                     clickEvent.initEvent (eventType, true, true);
                                     node.dispatchEvent (clickEvent);
                                 };
                                 
                                 async function mouseDragStart(node) {
                                     console.log("Starting drag...");
                                     triggerMouseEvent(node, "mousedown")
                                 }
                                 
                                 
                                 var progress = null;
                                 // 200 -1000
                                 async function mouseDragEnd(node,x,y){
                                     console.log("Ending drag...");
                                     await sleep(500)
                                     simulate(node, "mousemove",{pointerX:  x-30, pointerY: y})
                                     await sleep(500)
                                     simulate(node, "mouseup" , {pointerX:  x-30, pointerY: y})
                                 
                                 }
                                 function sleep(ms) {
                                     return new Promise(resolve => setTimeout(resolve, ms));
                                 }
                                 
                                 async function playback(offset){
                                     document.querySelector('#play_or_plause_player').click();
                                     await sleep(500);
                                     const targetElement = document.querySelector("#timeshift_pointer_player")
                                     const xy = document.querySelector("#timeshift_pointer_player").getClientRects()[0]
                                     console.log(xy)
                                     mouseDragStart(targetElement);
                                     mouseDragEnd(targetElement,xy.x+offset,xy.y);
                                 };
                            """
                    , null);
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT || event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    // 执行上一个直播地址的操作
                    navigateToPreviousLive();
                    return true;  // 返回 true 表示事件已处理，不传递给 WebView
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                    // 执行下一个直播地址的操作
                    navigateToNextLive();
                    return true;  // 返回 true 表示事件已处理，不传递给 WebView
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                    // 执行暂停操作
                    simulateTouch(webView, 0.5f, 0.5f);
                    return true;  // 返回 true 表示事件已处理，不传递给 WebView
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                    cctvFinishedView.evaluateJavascript(
                            """
                                     {playback(-30);}
                                    """
                            , null);
                    return true;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    cctvFinishedView.evaluateJavascript(
                            """
                                       {playback(60);}
                                    """, null);
                    return true;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.requestFocus();
                    return false;  // 返回 true 表示事件已处理，不传递给 WebView
                }
                return true;  // 返回 true 表示事件已处理，不传递给 WebView
            } else if (event.getKeyCode() >= KeyEvent.KEYCODE_0 && event.getKeyCode() <= KeyEvent.KEYCODE_9) {
                int numericKey = event.getKeyCode() - KeyEvent.KEYCODE_0;

                // 将按下的数字键追加到缓冲区
                digitBuffer.append(numericKey);

                // 使用 Handler 来在超时后处理输入的数字
                new Handler().postDelayed(this::handleNumericInput, DIGIT_TIMEOUT);

                // 更新显示正在输入的数字的 TextView
                updateInputTextView();

                return true;  // 事件已处理，不传递给 WebView
            }
        } else if (!recyclerView.hasFocus() && event.getAction() == KeyEvent.ACTION_DOWN && liveType.equals("yangshipin")) {
            // 换台无法使用loadUrl vue hash模式害死人=.= 可惜不能改服务端代码,只能写js了
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT || event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    navigateVueToPreviousLive();
                    return true;  // 返回 true 表示事件已处理，不传递给 WebView
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                    navigateVueToNextLive();
                    return true;  // 返回 true 表示事件已处理，不传递给 WebView
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                    simulateTouch(webView, 0.5f, 0.5f);
                    return true;  // 返回 true 表示事件已处理，不传递给 WebView
                }
            } else if (event.getKeyCode() >= KeyEvent.KEYCODE_0 && event.getKeyCode() <= KeyEvent.KEYCODE_9) {
                int numericKey = event.getKeyCode() - KeyEvent.KEYCODE_0;
                digitBuffer.append(numericKey);
                new Handler().postDelayed(this::handleNumericInput, DIGIT_TIMEOUT);
                updateInputTextView();
                return true;  // 事件已处理，不传递给 WebView
            }
        }
        return super.dispatchKeyEvent(event);  // 如果不处理，调用父类的方法继续传递事件
    }

    private void handleNumericInput() {
        // 将缓冲区中的数字转换为整数
        if (digitBuffer.length() > 0) {
            int numericValue = Integer.parseInt(digitBuffer.toString());

            // 检查数字是否在有效范围内
            if (numericValue > 0 && numericValue <= liveUrls.length) {
                currentLiveIndex = numericValue - 1;
                loadLiveUrl();
                saveCurrentLiveIndex(); // 保存当前位置
            }

            // 重置缓冲区
            digitBuffer.setLength(0);

            // 取消显示正在输入的数字
            inputTextView.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateInputTextView() {
        // 在 TextView 中显示当前正在输入的数字
        inputTextView.setVisibility(View.VISIBLE);
        inputTextView.setText("换台：" + digitBuffer.toString());
    }

    private void loadLastLiveIndex() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        currentLiveIndex = preferences.getInt(PREF_KEY_LIVE_INDEX, 0); // 默认值为0
        loadLiveUrl(); // 加载上次保存的位置的直播地址
    }

    private void saveCurrentLiveIndex() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_KEY_LIVE_INDEX, currentLiveIndex);
        editor.apply();
    }


    private void loadLiveUrl() {
        Log.d("currentLiveIndex", currentLiveIndex + "currentLiveIndex" + liveUrls.length);
        if (currentLiveIndex >= 0 && currentLiveIndex < liveUrls.length) {
            // 显示加载的View
//            loadingOverlay.setVisibility(View.VISIBLE);
            webView.setInitialScale(getMinimumScale());

            //  webView.reload(); 不能解决#地址问题

            webView.loadUrl(liveUrls[currentLiveIndex]);
        }
    }



    private int getCCTVHeadOffset(){
        for (int i = 0; i < liveUrls.length; i++) {
            if (liveUrls[i].contains("cctv.com")){
                return i;
            }
        }
        return 0;
    }

    private int getCCTVTailOffset(){
        for (int i = liveUrls.length - 1; i >= 0; i--) {
            if (liveUrls[i].contains("cctv.com")){
                return i;
            }
        }
        return liveUrls.length-1;
    }

    private void navigateVueToPreviousLive() {
        Log.d("getCCTVTailOffset()",yanShiPingCurrentChannel+"xx"+epgYSPList.get(0).getName());
        if (yanShiPingCurrentChannel!=null&&yanShiPingCurrentChannel.equals(epgYSPList.get(0).getName())){
            Log.d("getCCTVTailOffset()",getCCTVTailOffset()+"xx");
            navigateToPreviousLive(getCCTVTailOffset());
        }else
        if (yanShiPingCurrentChannel!=null&&epgYSPList!=null&&epgYSPList.size()>0){
            for (int i = 0; i < epgYSPList.size(); i++) {
                if (yanShiPingCurrentChannel.equals(epgYSPList.get(i).getName())){
                    cctvFinishedView.evaluateJavascript(epgYSPList.get(i-1).getId(),null);
                    currentLiveIndex = getCCTVTailOffset() + i-1;
                    saveCurrentLiveIndex(); // 保存当前位置
                }
            }
        }
    }


    private void navigateVueToNextLive() {
        if (yanShiPingCurrentChannel!=null&&yanShiPingCurrentChannel.equals(epgYSPList.get(epgYSPList.size()-1).getName())){
            navigateToNextLive(getCCTVHeadOffset());
        }else
        if (yanShiPingCurrentChannel!=null&&epgYSPList!=null&&epgYSPList.size()>0){
            for (int i = 0; i < epgYSPList.size(); i++) {
                if (yanShiPingCurrentChannel.equals(epgYSPList.get(i).getName())){
                    cctvFinishedView.evaluateJavascript(epgYSPList.get(i+1).getId(),null);
                    currentLiveIndex = getCCTVTailOffset() + i+1;
                    saveCurrentLiveIndex(); // 保存当前位置
                }
            }
        }

    }


    private void navigateToPreviousLive() {
        currentLiveIndex = (currentLiveIndex - 1 + liveUrls.length) % liveUrls.length;
        loadLiveUrl();
        saveCurrentLiveIndex(); // 保存当前位置
    }

    private void navigateToNextLive() {
        currentLiveIndex = (currentLiveIndex + 1) % liveUrls.length;
        loadLiveUrl();
        saveCurrentLiveIndex(); // 保存当前位置
    }

    private void navigateToPreviousLive(int index) {
        currentLiveIndex = index;
        loadLiveUrl();
        saveCurrentLiveIndex(); // 保存当前位置
    }

    private void navigateToNextLive(int index) {
        currentLiveIndex = index;
        loadLiveUrl();
        saveCurrentLiveIndex(); // 保存当前位置
    }

    private int getMinimumScale() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // 计算缩放比例，使用 double 类型进行计算
        double scale = Math.min((double) screenWidth / 3840.0, (double) screenHeight / 2160.0) * 100;

        // 四舍五入并转为整数
        return (int) Math.round(scale);
    }

    // 在需要模拟触摸的地方调用该方法
    public void simulateTouch(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;

        // 构造 ACTION_DOWN 事件
        MotionEvent downEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        view.dispatchTouchEvent(downEvent);

        // 构造 ACTION_UP 事件
        MotionEvent upEvent = MotionEvent.obtain(downTime, eventTime + 100, MotionEvent.ACTION_UP, x, y, 0);
        view.dispatchTouchEvent(upEvent);

        // 释放事件对象
        downEvent.recycle();
        upEvent.recycle();
    }

    @Override
    public void onBackPressed() {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }


        // 如果两秒内再次按返回键，则退出应用
    }

    private void showOverlay(String channelInfo) {
        // 设置覆盖层内容
        overlayTextView.setText(channelInfo);

        findViewById(R.id.overlayTextView).setVisibility(View.VISIBLE);

        // 使用 Handler 延时隐藏覆盖层
        new Handler().postDelayed(() -> {
            findViewById(R.id.overlayTextView).setVisibility(View.GONE);
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        // 在销毁活动时，释放 WebView 资源
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }


//    @Override
//    public void onItemClick(View view, int position) {
//        Log.d("onItemClick", position + "xxxxxxx");
//        EpgInfo channel = epgList.get(position);
//        Log.d("cctvFinishedView",cctvFinishedView.toString());
//        cctvFinishedView.evaluateJavascript("async function xx(){document.querySelector('#play_or_plause_player').click();await sleep(3000);"
//                        + "document.querySelector('#" + channel.getId() + "')" + ".click();"+"}"+"xx()"
//                , null);
//    }


}


