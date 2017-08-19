/*
 * Copyright 2007-2017 the original author or authors.
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

import net.ymate.wechat.base.*;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/9 下午1:58
 * @version 1.0
 */
public interface IDataStorageAdapter {

    /**
     * 初始化
     *
     * @param owner 所属模块对象
     * @throws Exception 可能产生的任何异常
     */
    void init(IWechat owner) throws Exception;

    /**
     * 销毁
     *
     * @throws Exception 可能产生的任何异常
     */
    void destroy() throws Exception;

    // ------

    WxAccessToken loadAccessToken(String accountId);

    void saveOrUpdateAccessToken(String accountId, WxAccessToken.Result accessToken);

    WxJsApiTicket loadJsApiTicket(String accountId);

    void saveOrUpdateJsApiTicket(String accountId, WxJsApiTicket.Result jsApiTicket);

    // ------

    /**
     * @param accountId 微信帐号ID
     * @return 返回当前登录用户的微信用户状态对象
     */
    WechatUserStatus currentUserStatus(String accountId);

    /**
     * @param accountId 微信帐号ID
     * @param wxUid     微信用户记录主键值
     * @return 返回指定openid的微信用户状态对象
     */
    WechatUserStatus loadUserStatus(String accountId, String wxUid);

    /**
     * 存储或更新微信用户状态数据
     *
     * @param userStatus 用户状态对象
     */
    void saveOrUpdateUserStatus(WechatUserStatus userStatus);

    // ------

    /**
     * @param accountId 微信帐号ID
     * @param openId    微信用户openid
     * @return 构建用于持久化微信用户记录主键值
     */
    String buildWxUid(String accountId, String openId);

    /**
     * 存储微信用户相关数据
     *
     * @param accountId      微信帐号ID
     * @param snsUser        微信用户信息对象, 可能为空
     * @param snsAccessToken 微信用户令牌对象
     */
    void saveOrUpdateUserInfo(String accountId, WxSnsUser snsUser, WxSnsAccessToken snsAccessToken);

    void saveOrUpdateUserInfo(String accountId, WxUser user);
}
