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

import net.ymate.wechat.base.WxAccount;

import java.util.Collection;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/23 下午5:58
 * @version 1.0
 */
public interface IAccountProvider {

    String DEFAULT_ACCOUNT = "default";

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

    //

    void registerAccount(WxAccount account);

    WxAccount unregisterAccount(String accountId);

    Collection<String> getAccountIds();

    boolean hasAccount(String accountId);

    WxAccount getAccount(String accountId);

    WxAccount getAccountByToken(String token);
}
