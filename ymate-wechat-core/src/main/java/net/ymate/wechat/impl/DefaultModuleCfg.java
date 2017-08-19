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
package net.ymate.wechat.impl;

import net.ymate.platform.core.YMP;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.wechat.*;
import net.ymate.wechat.base.WxAccount;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午1:56
 * @version 1.0
 */
public class DefaultModuleCfg implements IWechatModuleCfg {

    private IAccountProvider __accountProvider;

    private ITokenCacheAdapter __tokenCacheAdapter;

    private IMessageProcessor __messageProcessor;

    private IAutoreplyProcessor __autoreplyProcessor;

    private IDataStorageAdapter __dataStorageAdapter;

    private ICustomerServiceTransfer __customerServiceTransfer;

    public DefaultModuleCfg(YMP owner) {
        Map<String, String> _moduleCfgs = owner.getConfig().getModuleConfigs(IWechat.MODULE_NAME);
        //
        __accountProvider = ClassUtils.impl(_moduleCfgs.get("account_provider_class"), IAccountProvider.class, getClass());
        if (__accountProvider == null) {
            __accountProvider = new DefaultAccountProvider();
            __accountProvider.registerAccount(WxAccount.create(_moduleCfgs).addAttribute("default", "true"));
        }
        //
        __tokenCacheAdapter = ClassUtils.impl(_moduleCfgs.get("token_cache_adapter_class"), ITokenCacheAdapter.class, getClass());
        if (__tokenCacheAdapter == null) {
            __tokenCacheAdapter = new DefaultTokenCacheAdapter();
        }
        //
        __messageProcessor = ClassUtils.impl(_moduleCfgs.get("message_processor_class"), IMessageProcessor.class, getClass());
        if (__messageProcessor == null) {
            __messageProcessor = new DefaultMessageProcessor();
        }
        //
        __autoreplyProcessor = ClassUtils.impl(_moduleCfgs.get("autoreply_processor_class"), IAutoreplyProcessor.class, getClass());
        //
        __dataStorageAdapter = ClassUtils.impl(_moduleCfgs.get("data_storage_adapter_class"), IDataStorageAdapter.class, getClass());
        //
        __customerServiceTransfer = ClassUtils.impl(_moduleCfgs.get("customer_service_transfer_class"), ICustomerServiceTransfer.class, getClass());
    }

    @Override
    public IAccountProvider getAccountProvider() {
        return __accountProvider;
    }

    @Override
    public ITokenCacheAdapter getTokenCacheAdapter() {
        return __tokenCacheAdapter;
    }

    @Override
    public IMessageProcessor getMessageProcessor() {
        return __messageProcessor;
    }

    @Override
    public IAutoreplyProcessor getAutoreplyProcessor() {
        return __autoreplyProcessor;
    }

    @Override
    public IDataStorageAdapter getDataStorageAdapter() {
        return __dataStorageAdapter;
    }

    @Override
    public ICustomerServiceTransfer getCustomerServiceTransfer() {
        return __customerServiceTransfer;
    }
}