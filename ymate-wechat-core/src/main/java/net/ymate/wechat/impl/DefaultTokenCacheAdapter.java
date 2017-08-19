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

import com.alibaba.fastjson.JSON;
import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.framework.commons.IHttpResponse;
import net.ymate.framework.commons.ReentrantLockHelper;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.wechat.IDataStorageAdapter;
import net.ymate.wechat.ITokenCacheAdapter;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.base.WxAccessToken;
import net.ymate.wechat.base.WxAccount;
import net.ymate.wechat.base.WxJsApiTicket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/26 上午3:32
 * @version 1.0
 */
public class DefaultTokenCacheAdapter implements ITokenCacheAdapter {

    private static final Log _LOG = LogFactory.getLog(DefaultTokenCacheAdapter.class);

    private static final String __WX_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    private static final String __WX_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=";

    private static final Map<String, WxAccessToken> __TOKEN_CACHES = new ConcurrentHashMap<String, WxAccessToken>();

    private static final Map<String, WxJsApiTicket> __TICKET_CACHES = new ConcurrentHashMap<String, WxJsApiTicket>();

    private IWechat __owner;

    @Override
    public void init(IWechat owner) throws Exception {
        __owner = owner;
    }

    @Override
    public void destroy() throws Exception {
    }

    @Override
    public WxAccessToken getAccessToken(WxAccount account) {
        WxAccessToken _token = __TOKEN_CACHES.get(account.getId());
        if (_token == null || _token.isExpired()) {
            ReentrantLock _locker = ReentrantLockHelper.DEFAULT.getLocker(account.getId().concat("_token"));
            _locker.lock();
            try {
                _token = __doTryGetAccessToken(account);
            } catch (Exception e) {
                try {
                    _token = __doTryGetAccessToken(account);
                } catch (Exception ex) {
                    _LOG.warn("", RuntimeUtils.unwrapThrow(ex));
                }
            } finally {
                if (_locker.isLocked()) {
                    _locker.unlock();
                }
            }
        }
        return _token;
    }

    private WxAccessToken __doTryGetAccessToken(WxAccount account) throws Exception {
        WxAccessToken _token = __TOKEN_CACHES.get(account.getId());
        if (_token == null || _token.isExpired()) {
            _token = __doGetAccessToken(account.getId());
            if (_token == null || _token.isExpired()) {
                WxAccessToken.Result _result = __doGetAccessToken(account.getAppId(), account.getAppSecret());
                if (_result != null) {
                    _token = _result.getAccessToken();
                    __doSaveOrUpdateAccessToken(account, _result);
                }
            }
        }
        return _token;
    }

    @Override
    public void saveOrUpdateAccessToken(WxAccount account, WxAccessToken.Result accessToken) {
        ReentrantLock _locker = ReentrantLockHelper.DEFAULT.getLocker(account.getId().concat("_token"));
        _locker.lock();
        try {
            __doSaveOrUpdateAccessToken(account, accessToken);
        } finally {
            if (_locker.isLocked()) {
                _locker.unlock();
            }
        }
    }

    @Override
    public WxAccessToken resetAccessToken(WxAccount account, WxAccessToken accessToken) {
        WxAccessToken _token = __TOKEN_CACHES.get(account.getId());
        ReentrantLock _locker = ReentrantLockHelper.DEFAULT.getLocker(account.getId().concat("_token"));
        _locker.lock();
        try {
            if (_token == null || _token.getExpiredTime() == accessToken.getExpiredTime()) {
                WxAccessToken.Result _result = __doGetAccessToken(account.getAppId(), account.getAppSecret());
                _token = _result.getAccessToken();
                //
                __doSaveOrUpdateAccessToken(account, _result);
            }
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        } finally {
            if (_locker.isLocked()) {
                _locker.unlock();
            }
        }
        return _token;
    }

    @Override
    public WxJsApiTicket getJsApiTicket(WxAccount account) {
        WxJsApiTicket _ticket = __TICKET_CACHES.get(account.getId());
        if (_ticket == null || _ticket.isExpired()) {
            WxAccessToken _token = this.getAccessToken(account);
            if (_token != null) {
                ReentrantLock _locker = ReentrantLockHelper.DEFAULT.getLocker(account.getId().concat("_ticket"));
                _locker.lock();
                try {
                    _ticket = __doTryGetJsApiTicket(account, _token);
                } catch (Exception e) {
                    try {
                        _ticket = __doTryGetJsApiTicket(account, _token);
                    } catch (Exception ex) {
                        _LOG.warn("", RuntimeUtils.unwrapThrow(ex));
                    }
                } finally {
                    if (_locker.isLocked()) {
                        _locker.unlock();
                    }
                }
            }
        }
        return _ticket;
    }

    private WxJsApiTicket __doTryGetJsApiTicket(WxAccount account, WxAccessToken accessToken) throws Exception {
        WxJsApiTicket _ticket = __TICKET_CACHES.get(account.getId());
        if (_ticket == null || _ticket.isExpired()) {
            _ticket = __doGetJsApiTicket(account.getId());
            if (_ticket == null || _ticket.isExpired()) {
                WxJsApiTicket.Result _result = __doGetJsApiTicket(accessToken);
                if (_result != null) {
                    _ticket = _result.getTicket();
                    __doSaveOrUpdateJsApiTicket(account, _result);
                }
            }
        }
        return _ticket;
    }

    @Override
    public void saveOrUpdateJsApiTicket(WxAccount account, WxJsApiTicket.Result jsApiTicket) {
        ReentrantLock _locker = ReentrantLockHelper.DEFAULT.getLocker(account.getId().concat("_ticket"));
        _locker.lock();
        try {
            __doSaveOrUpdateJsApiTicket(account, jsApiTicket);
        } finally {
            if (_locker.isLocked()) {
                _locker.unlock();
            }
        }
    }

    @Override
    public WxJsApiTicket resetJsApiTicket(WxAccount account, WxJsApiTicket jsApiTicket) {
        WxJsApiTicket _ticket = __TICKET_CACHES.get(account.getId());
        ReentrantLock _locker = ReentrantLockHelper.DEFAULT.getLocker(account.getId().concat("_ticket"));
        _locker.lock();
        try {
            if (_ticket == null || _ticket.getExpiredTime() == jsApiTicket.getExpiredTime()) {
                WxJsApiTicket.Result _result = __doGetJsApiTicket(getAccessToken(account));
                _ticket = _result.getTicket();
                __doSaveOrUpdateJsApiTicket(account, _result);
            }
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        } finally {
            if (_locker.isLocked()) {
                _locker.unlock();
            }
        }
        return _ticket;
    }

    public IWechat getOwner() {
        return __owner;
    }

    protected WxAccessToken __doGetAccessToken(String accountId) throws Exception {
        IDataStorageAdapter _storageAdapter = __owner.getModuleCfg().getDataStorageAdapter();
        if (_storageAdapter != null) {
            return _storageAdapter.loadAccessToken(accountId);
        }
        return null;
    }

    protected WxAccessToken.Result __doGetAccessToken(String appId, String appSecret) throws Exception {
        IHttpResponse _response = HttpClientHelper.create().get(__WX_ACCESS_TOKEN.concat("&appid=").concat(appId).concat("&secret=").concat(appSecret));
        if (_response != null && _response.getStatusCode() == 200) {
            WxAccessToken.Result _result = new WxAccessToken.Result(JSON.parseObject(_response.getContent()));
            if (_result.isOK()) {
                return _result;
            }
        }
        return null;
    }

    protected void __doSaveOrUpdateAccessToken(WxAccount account, WxAccessToken.Result accessToken) {
        __TOKEN_CACHES.put(account.getId(), accessToken.getAccessToken());
        //
        IDataStorageAdapter _storageAdapter = __owner.getModuleCfg().getDataStorageAdapter();
        if (_storageAdapter != null) {
            _storageAdapter.saveOrUpdateAccessToken(account.getId(), accessToken);
        }
    }

    protected WxJsApiTicket __doGetJsApiTicket(String accountId) throws Exception {
        IDataStorageAdapter _storageAdapter = __owner.getModuleCfg().getDataStorageAdapter();
        if (_storageAdapter != null) {
            return _storageAdapter.loadJsApiTicket(accountId);
        }
        return null;
    }

    protected WxJsApiTicket.Result __doGetJsApiTicket(WxAccessToken accessToken) throws Exception {
        IHttpResponse _response = HttpClientHelper.create().get(__WX_JSAPI_TICKET.concat(accessToken.getToken()));
        if (_response != null && _response.getStatusCode() == 200) {
            WxJsApiTicket.Result _result = new WxJsApiTicket.Result(JSON.parseObject(_response.getContent()));
            if (_result.isOK()) {
                return _result;
            }
        }
        return null;
    }

    protected void __doSaveOrUpdateJsApiTicket(WxAccount account, WxJsApiTicket.Result jsApiTicket) {
        __TICKET_CACHES.put(account.getId(), jsApiTicket.getTicket());
        //
        IDataStorageAdapter _storageAdapter = __owner.getModuleCfg().getDataStorageAdapter();
        if (_storageAdapter != null) {
            _storageAdapter.saveOrUpdateJsApiTicket(account.getId(), jsApiTicket);
        }
    }
}
