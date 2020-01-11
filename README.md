## BY_APP_TEMPLATE

参照[MVVM-Rhine](https://github.com/qingmei2/MVVM-Rhine)打造的，
适合博也公司项目的MVVM快速开发模板，精简了di及其他一些小部分。
致力于让业务逻辑更清晰有条理。
**特别适用于**类似ebuy这种进行层层加密的网络请求方式。
推荐Kotlin开发语言。Kotlin已经官宣了，还不用起来？本模板只适用于AndroidX！

## 模块介绍及依赖关系展示

1. core(核心模块，各种基础的东西在这里面)：
    - retrofit相关，转换器适配器等
    - Gson数据转换库
    - AutoDispose防止RxJava内存泄漏（此模块没有引入RxJava相关）
    - 依赖了RxJava相关

2. by_network(博也网络请求模块，适合ebuy这种网络请求方式)：
    - 依赖了core模块

## 快速开始

此模板项目分为`core`模块和`by_network`模块。<br/>
如果项目没有用到像ebuy这种层层加密网络请求方式，那么在主工程项目中common模块即可。<br/>
如果用到像ebuy这种网络请求方式那么仅仅只需要引入by_network模块即可。

**模块初始化：**

`core`和`by_network`模块只能二选一引入

1. 如果只引入了`core`模块，在自定义的Application中的onCreate方法加入以下代码：

    ```kotlin
    
    CoreManager.init(this)
    ```
    
    你还可以继续配置`core`网络框架，在上面的代码加上下面的代码：
    
    ```kotlin
    
    configNetWork(baseUrl = "https://music.ghpym.com", writeTimeOut = 10)
    ```
    
    上面代码只展示了其中一些自定义参数，还有一些其他的自定义参数请参考实体类NetWorkConfig
    
    还可以继续配置Log打印框架，在上面的代码加上下面的代码：
    
    ```kotlin
    
    configCLog(isEnableLog = true, showThreadInfo = false)
    ```
    
    上面的代码只展示了配置其中一部分参数，更多参数请查看configCLog方法详情
    
2. 如果只引入了`by_network`模块，在自定义的Application中的onCreate方法中先按照上面第1.步骤所示
进行CoreManager的配置，然后添加以下代码：
    ```kotlin
    
    ByNetWorkManager.init()
    ```

    另外，setConvert方法未展示，其作用是自定义参数加密解密的转换器，如果未设置将使用默认的转换器，其相关加密解密方法参照ebuy项目。如果需要自己设计实现，请参考`by_network`模块中的SampleConvert。

**防止RxJava内存泄漏：**

`core`模块引入了AutoDispose框架，在生命周期结束而自动断开与RxJava的连接只需一行代码：
1. Activity中，必须继承自`core`模块中的CommonActivity，在RxJava流中添加如下Kotlin代码：
    ```kotlin
    
    autoDisposable(scopeProvider)
    ```
2. Fragment中，必须继承自`core`模块中的CommonFragment，在RxJava流中添加如下Kotlin代码：
    ```kotlin
    
    autoDisposable(scopeProvider)
    ```
3. ViewModel中，必须继承自`core`模块中的CommonViewModel，在RxJava流中添加如下Kotlin代码：
    ```kotlin
    
    autoDisposable(this)
    ```
**`by_network`模块配置**

服务器公共参数配置文件位于`by_network`模块config包中，
NetWorkConfig.kt文件为配置文件，已经添加相关注释，请自行更改。

**`by_network`使用方式**
重新改造，使用动态代理的方式，让你可以像使用Retrofit一样使用`by_network`
1. 先使用interface声明网络请求接口：
    ```kotlin
    
    interface EbuyService {
        // 接口方法只能返回Flowable或者Observable
        @Type("BY_Config_version")
        fun getVersionData(@Param("appid") appid: String): Flowable<VersionData>
    }
    ```
2. 使用ByNet.get().create()方法创建出Service对象，然后便可以进行网络请求：
    ```kotlin
    
    ByNet.get().create(EbuyService::class.java)
                .getVersionData("by565fa4facdb191")
                .map { Result.success(it) }
                .onErrorReturn { Result.failure(it) }
    ```
3. 注解介绍：

    @Type：接口type，声明ebuy这种网络请求方式的接口类型（或者说是请求地址）
    
    @Param：接口中需要传的参数名
    
    @Params：接口中需要传的参数的键值对组，被其修饰的参数必须是HashMap<String, Object>类型，
    用途：例如参数过多导致方法过长，如果不想全部写在接口方法中就可以使用此注解
    
    @ApiVersion：给接口指定apiVersion的值，一般这个值是字符串类型100，但是不保证后期所有接口都是100所以才有了此注解

**其他亮点请自行阅读app模块下源码，一些设计保证了条理清晰，请遵循他们！**

## 辅助工具

快速生成模板代码插件：
[BY_APP_TEMPLATE-Template](https://github.com/cyixlq/BY_APP_TEMPLATE-Template)

使用此插件可以快速生成**Kotlin**模板代码，增加键盘寿命，减少无意义的时间消耗，
争取早点下班。通过此插件生成的模板代码已经实现了下方中的开发规范，
以及实现了此项目模板的设计理念，保证条理性。使用方法请参见该项目的README。
**暂不支持Java模板代码生成！**

## 开发规范

开发规范已在demo中进行了展示，添加了相关注释，在此还是重复强调几点：

1. 切记千万不要自行创建Intent进行Activity的跳转，强制每个Activity创建时必须提供一个launch方法，
在方法参数中添加跳转所需要携带的值。此举是为了规范界面间的传值。
launch方法的实现参照TestActivity。

2. 创建Fragment请不要在所需要的地方直接进行实例化，反例：
    ```kotlin
    
    val testFragment = TestFragment()
    ```
    应当使用要创建的Fragment自身提供静态instance方法，正例：
    ```kotlin
    
    class TestFragment: Fragment() {
        companion object {
            fun instance(title: String): TestFragment {
                val bundle = Bundle().apply {
                    putString("title", title)
                }
                return TestFragment().apply {
                    arguments = bundle
                }
            }
        }
    }

    // 在需要实例化的地方调用instance方法
    val testFragment = TestFragment.instance("test")
    ```
3. 强制所有依赖必须添加到config.gradle，此举是为了统一所有依赖版本。具体做法请参考demo！

4. 请等待后续添加

## 更新日志
### 2020.01.08
   - core库中BaseActivity、BaseFragment和BaseViewModel分别改名为
   CommonActivity、CommonFragment、CommonViewModel，原因是开发者自己可能还需
   要自定义一层Base，他们更喜欢用Base这个名称开头
   - 以源代码的方式引入RxWeaver — 一款全局错误处理的库，原库地址以及使用方法:
   [点我跳转](https://github.com/qingmei2/RxWeaver)
   - 升级GradlePlugin版本，kotlin-gradle-plugin版本