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

import net.ymate.framework.commons.ReentrantLockHelper;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.wechat.IAccountProvider;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.base.WxAccount;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/24 上午1:18
 * @version 1.0
 */
public class DefaultAccountProvider implements IAccountProvider {

    private static final Map<String, WxAccount> __CACHES = new ConcurrentHashMap<String, WxAccount>();

    private static final Map<String, String> __TOKEN_CACHES = new ConcurrentHashMap<String, String>();

    private IWechat __owner;

    private WxAccount __defaultAccount;

    @Override
    public void init(IWechat owner) throws Exception {
        __owner = owner;
    }

    @Override
    public void destroy() throws Exception {
    }

    public IWechat getOwner() {
        return __owner;
    }

    @Override
    public void registerAccount(WxAccount account) {
        ReentrantLock _locker = ReentrantLockHelper.DEFAULT.getLocker(account.getId());
        _locker.lock();
        try {
            __doRegisterAccount(account);
        } finally {
            if (_locker.isLocked()) {
                _locker.unlock();
            }
        }
    }

    protected void __doRegisterAccount(WxAccount account) {
        __CACHES.put(account.getId(), account);
        __TOKEN_CACHES.put(account.getToken(), account.getId());
        //
        if (BlurObject.bind(account.getAttribute(DEFAULT_ACCOUNT)).toBooleanValue()) {
            __defaultAccount = account;
        }
    }

    @Override
    public WxAccount unregisterAccount(String accountId) {
        ReentrantLock _locker = ReentrantLockHelper.DEFAULT.getLocker(accountId);
        _locker.lock();
        try {
            return __doUnregisterAccount(accountId);
        } finally {
            if (_locker.isLocked()) {
                _locker.unlock();
            }
        }
    }

    protected WxAccount __doUnregisterAccount(String accountId) {
        WxAccount _account = __CACHES.remove(accountId);
        __TOKEN_CACHES.remove(_account.getToken());
        //
        if (BlurObject.bind(_account.getAttribute(DEFAULT_ACCOUNT)).toBooleanValue()) {
            __defaultAccount = null;
        }
        return _account;
    }

    @Override
    public Collection<String> getAccountIds() {
        return __CACHES.keySet();
    }

    @Override
    public boolean hasAccount(String accountId) {
        if (StringUtils.equals(accountId, DEFAULT_ACCOUNT)) {
            return __defaultAccount != null;
        }
        return __CACHES.containsKey(accountId);
    }

    @Override
    public WxAccount getAccount(String accountId) {
        if (StringUtils.equals(accountId, DEFAULT_ACCOUNT)) {
            return __defaultAccount;
        }
        return __CACHES.get(accountId);
    }

    @Override
    public WxAccount getAccountByToken(String token) {
        if (__defaultAccount != null && StringUtils.equals(__defaultAccount.getToken(), token)) {
            return __defaultAccount;
        }
        String _accountId = __TOKEN_CACHES.get(token);
        if (StringUtils.isNotBlank(_accountId)) {
            return getAccount(_accountId);
        }
        return null;
    }
}
