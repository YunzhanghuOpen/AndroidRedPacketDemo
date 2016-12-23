# AndroidRedPacketDemo
### Description

#### AndroidRedPacketDemo主要演示如何调用相关方法实现红包功能。主要功能如下：

* 红包SDK的集成方式（远程依赖aar）
* 获取签名参数
* 进入红包页面（单聊、群聊、小额随机红包）
* 进入转账页面
* 进入零钱页面
* 打开红包  

**具体实现可参考Demo中RedPacketUtil中的方法**


## How to add dependency red packet aar 

###in your top-level build file

```java
buildscript {
    repositories {
        maven { url "https://raw.githubusercontent.com/YunzhanghuOpen/redpacket-maven-repo/master/release" }
        jcenter()
    }
}
```
###in your module build file

```java
dependencies {
    compile 'com.yunzhanghu:redpacket:3.4.0@aar'
}
```







### ScreenShot
![image](https://cloud.githubusercontent.com/assets/3954285/21000890/37053ae0-bd58-11e6-8a2f-545e5f867600.png)
