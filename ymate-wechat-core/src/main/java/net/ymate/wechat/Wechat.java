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

import net.ymate.platform.core.Version;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.annotation.Module;
import net.ymate.wechat.base.WxAccount;
import net.ymate.wechat.base.WxSnsAccessToken;
import net.ymate.wechat.base.WxSnsUser;
import net.ymate.wechat.base.WxUser;
import net.ymate.wechat.impl.DefaultModuleCfg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午1:50
 * @version 1.0
 */
@Module
public class Wechat implements IModule, IWechat {

    private static final Log _LOG = LogFactory.getLog(Wechat.class);

    public static final Version VERSION = new Version(2, 0, 0, Wechat.class.getPackage().getImplementationVersion(), Version.VersionType.Alphal);

    private static volatile IWechat __instance;

    private YMP __owner;

    private IWechatModuleCfg __moduleCfg;

    private boolean __inited;

    public static IWechat get() {
        if (__instance == null) {
            synchronized (VERSION) {
                if (__instance == null) {
                    __instance = YMP.get().getModule(Wechat.class);
                }
            }
        }
        return __instance;
    }

    @Override
    public String getName() {
        return IWechat.MODULE_NAME;
    }

    @Override
    public void init(YMP owner) throws Exception {
        if (!__inited) {
            //
            _LOG.info("Initializing ymate-wechat-core-" + VERSION);
            //
            __owner = owner;
            __moduleCfg = new DefaultModuleCfg(owner);
            __owner.getEvents().registerEvent(WechatEvent.class);
            //
            __moduleCfg.getAccountProvider().init(this);
            __moduleCfg.getTokenCacheAdapter().init(this);
            __moduleCfg.getMessageProcessor().init(this);
            //
            if (__moduleCfg.getAutoreplyProcessor() != null) {
                __moduleCfg.getAutoreplyProcessor().init(this);
            }
            //
            __inited = true;
        }
    }

    @Override
    public boolean isInited() {
        return __inited;
    }

    @Override
    public WxAccount getAccount(String accountId) {
        return __moduleCfg.getAccountProvider().getAccount(accountId);
    }

    @Override
    public String getAccountAttribute(String accountId, String attrKey) {
        WxAccount _account = getAccount(accountId);
        if (_account != null) {
            return _account.getAttribute(attrKey);
        }
        return null;
    }

    @Override
    public WechatApiCaller createApiCaller(String accountId) {
        return WechatApiCaller.create(accountId);
    }

    @Override
    public WechatApiCaller createApiCaller(WxAccount account) {
        return WechatApiCaller.create(account);
    }

    @Override
    public WechatUserStatus currentUserStatus(String accountId) {
        if (__moduleCfg.getDataStorageAdapter() != null) {
            return __moduleCfg.getDataStorageAdapter().currentUserStatus(accountId);
        }
        return null;
    }

    @Override
    public WechatUserStatus loadUserStatus(String accountId, String wxUid) {
        if (__moduleCfg.getDataStorageAdapter() != null) {
            return __moduleCfg.getDataStorageAdapter().loadUserStatus(accountId, wxUid);
        }
        return null;
    }

    @Override
    public void saveOrUpdateUserStatus(WechatUserStatus userStatus) {
        if (__moduleCfg.getDataStorageAdapter() != null) {
            __moduleCfg.getDataStorageAdapter().saveOrUpdateUserStatus(userStatus);
        }
    }

    @Override
    public String buildWxUid(String accountId, String openId) {
        if (__moduleCfg.getDataStorageAdapter() != null) {
            return __moduleCfg.getDataStorageAdapter().buildWxUid(accountId, openId);
        }
        return null;
    }

    @Override
    public void saveOrUpdateUserInfo(String accountId, WxSnsUser snsUser, WxSnsAccessToken snsAccessToken) {
        if (__moduleCfg.getDataStorageAdapter() != null) {
            __moduleCfg.getDataStorageAdapter().saveOrUpdateUserInfo(accountId, snsUser, snsAccessToken);
        }
    }

    @Override
    public void saveOrUpdateUserInfo(String accountId, WxUser user) {
        if (__moduleCfg.getDataStorageAdapter() != null) {
            __moduleCfg.getDataStorageAdapter().saveOrUpdateUserInfo(accountId, user);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (__inited) {
            __inited = false;
            //
            if (__moduleCfg.getAutoreplyProcessor() != null) {
                __moduleCfg.getAutoreplyProcessor().destroy();
            }
            //
            __moduleCfg.getMessageProcessor().destroy();
            __moduleCfg.getTokenCacheAdapter().destroy();
            __moduleCfg.getAccountProvider().destroy();
            //
            __moduleCfg = null;
            __owner = null;
        }
    }

    @Override
    public YMP getOwner() {
        return __owner;
    }

    @Override
    public IWechatModuleCfg getModuleCfg() {
        return __moduleCfg;
    }
}
