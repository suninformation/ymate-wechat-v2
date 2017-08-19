### ymate-wechat-v2

> 基于YMP框架实现的微信公众平台服务接入模块

#### Maven包依赖

    <dependency>
        <groupId>net.ymate.wechat</groupId>
        <artifactId>ymate-wechat-core</artifactId>
        <version>2.0.0</version>
    </dependency>

#### 模块配置参数说明

    #-------------------------------------
    # wechat 模块初始化参数
    #-------------------------------------
    
    # 微信公众账号信息提供者接口实现类, 若未提供则使用默认配置
    ymp.configs.wechat.account_provider_class=
    
    # Token缓存适配器接口实现类, 若未提供则使用默认配置
    ymp.configs.wechat.token_cache_adapter_class=
    
    # 消息处理器, 可选参数, 若未提供则使用默认配置
    ymp.configs.wechat.message_processor_class=
    
    # 消息自动回复处理器接口实现类, 可选参数, 默认值为空
    ymp.configs.wechat.autoreply_processor_class=
    
    # 数据存储适配器接口实现类, 可选参数, 默认值为空
    ymp.configs.wechat.data_storage_adapter_class=
    
    # 客服消息转发器接口实现类, 可选参数, 默认值为空
    ymp.configs.wechat.customer_service_transfer_class=
    
    #-------------------------------------
    # 默认微信账户配置参数
    #-------------------------------------
    
    # 微信公众帐号原始ID, 必须
    ymp.configs.wechat.account_id=
    
    # 第三方应用唯一凭证, 必须
    ymp.configs.wechat.app_id=
    
    # 第三方应用唯一凭证密钥, 必须
    ymp.configs.wechat.app_secret=
    
    # 是否采用消息加密, 默认值为false
    ymp.configs.wechat.is_msg_encrypted=
    
    # 消息加密密钥由43位字符组成，可随机修改，字符范围为A-Z，a-z，0-9
    ymp.configs.wechat.app_aes_key=
    
    # 上一次使用的消息加密密钥（备用）
    ymp.configs.wechat.last_app_aes_key=
    
    # OAuth授权后重定向的URL地址, 可选参数
    ymp.configs.wechat.redirect_uri=
    
    # 公众号类型, 默认值为0, (0-未知, 1-服务号, 2-订阅号, 3-小程序)
    ymp.configs.wechat.type=
    
    # 是否已认证, 默认值为false（备用）
    ymp.configs.wechat.is_verfied=
    
    # 微信接入唯一标识, 默认值为wechat
    ymp.configs.wechat.token=
    
    # 自定义扩展属性
    ymp.configs.wechat.params.xxx=

#### 示例代码：

#### One More Thing

YMP不仅提供便捷的Web及其它Java项目的快速开发体验，也将不断提供更多丰富的项目实践经验。

感兴趣的小伙伴儿们可以加入 官方QQ群480374360，一起交流学习，帮助YMP成长！

了解更多有关YMP框架的内容，请访问官网：http://www.ymate.net/