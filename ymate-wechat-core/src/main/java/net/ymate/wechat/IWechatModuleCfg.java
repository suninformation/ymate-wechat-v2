/*
 * Copyright 2007-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.wechat;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午1:52
 * @version 1.0
 */
public interface IWechatModuleCfg {

    /**
     * @return 微信公众账号信息提供者接口实现类, 若未提供则使用默认配置
     */
    IAccountProvider getAccountProvider();

    /**
     * @return Token缓存适配器接口实现类, 若未提供则使用默认配置
     */
    ITokenCacheAdapter getTokenCacheAdapter();

    /**
     * @return 消息处理器, 可选参数, 若未提供则使用默认配置
     */
    IMessageProcessor getMessageProcessor();

    /**
     * @return 消息自动回复处理器接口实现类, 可选参数, 默认值为空
     */
    IAutoreplyProcessor getAutoreplyProcessor();

    /**
     * @return 数据存储适配器接口实现类, 可选参数, 默认值为空
     */
    IDataStorageAdapter getDataStorageAdapter();

    /**
     * @return 客服消息转发器接口实现类, 可选参数, 默认值为空
     */
    ICustomerServiceTransfer getCustomerServiceTransfer();
}