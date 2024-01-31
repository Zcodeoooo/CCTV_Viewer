<p align="center"> <img src="https://github.com/Eanya-Tonic/CCTV_Viewer/blob/master/app/src/main/res/drawable/logo.png" style="width:200px;" /> </p>  <h1 align="center">电视浏览器</h1>  <p align="center">一个电视机顶盒及Android TV收看中国中央电视台直播的浏览器 </p>


## 下载安装包
https://github.com/Zcodeoooo/CCTV_Viewer/releases/
![img.png](img.png)

## 方案选择
    感谢https://github.com/lizongying/my-tv项目的对于央视频vip接口的支持,作者本人的账号开通的vip造福大众,另外由于该项目本身缺少关键解密文件(其实已经有了扒代码调用解密算法的方式取得ckey参数),还有由于项目本身来自于逆向js算法导致的不稳定性(鉴于最近Pandora项目的归档的教训),本人并不借鉴于此
    另外查看了sekiro方案(多设备注入js,提供负载均衡的服务器接口获取数据),可以达到各个方面的均衡,例如兼容性安卓版本 稳定性 速度,可以达到商业性的标准,但是涉及服务器相关,本人也爱莫能助,有心人可以参考下

## 注意
    由于目前精力有限,无法支持对于指定播放地址的模块化协作脚本定制化开发,有其他问题请及时反馈

##  兼容性说明    
    受WebView 第三方页面限制目前只能在安卓4.4及以上系统正常上运行
    已静态集成arm64和armeabi架构 x5内核 
    (安卓7.0及以下已测试geckoview内核及Crosswalk内核都会播放失败),其他方案请考虑tvbox
    安卓7.0以上系统无需添加x5内核,可以去除,以提升运行速度及占用大小
## 已更新功能
    今日节目单切换(遥控器菜单键呼出)
    直播进度后退前进(遥控器左右键)
    央视频vip源添加(目前发现问题是cctv特定频道有版权限制,央视频则没有)
    


## 计划更新
    下版本计划添加
    频道选择

最后感谢chatgpt的大力支持,让我3天学会Android入门

## 目前可看频道 
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

    央视频
            "CCTV4K",
            "CCTV1",
            "CCTV2",
            "CCTV3",
            "CCTV4",
            "CCTV5",
            "CCTV5+",
            "CCTV6",
            "CCTV7",
            "CCTV8",
            "CCTV9",
            "CCTV10",
            "CCTV11",
            "CCTV12",
            "CCTV13",
            "CCTV14",
            "CCTV15",
            "CCTV16-HD",
            "CCTV16(4K）",
            "CCTV17",
            "CGTN",
            "CGTN法语频道",
            "CGTN俄语频道",
            "CGTN阿拉伯语频道",
            "CGTN西班牙语频道",
            "CGTN外语纪录频道",
            "CCTV风云剧场频道",
            "CCTV第一剧场频道",
            "CCTV怀旧剧场频道",
            "CCTV世界地理频道",
            "CCTV风云音乐频道",
            "CCTV兵器科技频道",
            "CCTV高尔夫·网球频道",
            "CCTV女性时尚频道",
            "CCTV央视文化精品频道",
            "CCTV央视台球频道",
            "CCTV电视指南频道",
            "CCTV卫生健康频道",

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
