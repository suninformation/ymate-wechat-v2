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

import net.ymate.wechat.base.WxSnsAccessToken;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/5 下午5:15
 * @version 1.0
 */
public class WechatUserStatus implements Serializable {

    /**
     * 微信用户记录ID
     */
    private String id;

    /**
     * 微信用户openid
     */
    private String openId;

    /**
     * 微信用户unionid
     */
    private String unionId;

    /**
     * 微信帐号ID
     */
    private String accountId;

    //
    private String oauthAccessToken;

    private String oauthRefreshToken;

    private long oauthExpiredTime;

    private String oauthScope;

    private long lastModifyTime;

    public static WechatUserStatus create(String id, String accountId) {
        return new WechatUserStatus()
                .id(id)
                .accountId(accountId)
                .lastModifyTime(System.currentTimeMillis());
    }

    public static WechatUserStatus create(String id, String accountId, WxSnsAccessToken snsAccessToken) {
        return new WechatUserStatus()
                .id(id)
                .openId(snsAccessToken.getOpenId())
                .unionId(snsAccessToken.getUnionId())
                .accountId(accountId)
                .oauthAccessToken(snsAccessToken.getAccessToken().getToken())
                .oauthRefreshToken(snsAccessToken.getRefreshToken())
                .oauthExpiredTime(snsAccessToken.getAccessToken().getExpiredTime())
                .oauthScope(snsAccessToken.getScope())
                .lastModifyTime(System.currentTimeMillis());
    }

    public String id() {
        return id;
    }

    public WechatUserStatus id(String id) {
        this.id = id;
        return this;
    }

    public String openId() {
        return openId;
    }

    public WechatUserStatus openId(String openId) {
        this.openId = openId;
        return this;
    }

    public String unionId() {
        return unionId;
    }

    public WechatUserStatus unionId(String unionId) {
        this.unionId = unionId;
        return this;
    }

    public String accountId() {
        return accountId;
    }

    public WechatUserStatus accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String oauthAccessToken() {
        return oauthAccessToken;
    }

    public WechatUserStatus oauthAccessToken(String oauthAccessToken) {
        this.oauthAccessToken = oauthAccessToken;
        return this;
    }

    public String oauthRefreshToken() {
        return oauthRefreshToken;
    }

    public WechatUserStatus oauthRefreshToken(String oauthRefreshToken) {
        this.oauthRefreshToken = oauthRefreshToken;
        return this;
    }

    public long oauthExpiredTime() {
        return oauthExpiredTime;
    }

    public WechatUserStatus oauthExpiredTime(long oauthExpiredTime) {
        this.oauthExpiredTime = oauthExpiredTime;
        return this;
    }

    public String oauthScope() {
        return oauthScope;
    }

    public WechatUserStatus oauthScope(String oauthScope) {
        this.oauthScope = oauthScope;
        return this;
    }

    public long lastModifyTime() {
        return lastModifyTime;
    }

    public WechatUserStatus lastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
        return this;
    }

    public boolean checkStatus() {
        return StringUtils.isNotBlank(this.oauthAccessToken) && System.currentTimeMillis() < this.oauthExpiredTime;
    }

    public boolean checkOauthScope(String needOauthScope) {
        return StringUtils.contains(this.oauthScope, needOauthScope);
    }

    /**
     * @return 若返回true则表示已执行刷新操作
     */
    public boolean refreshOauthIfNeed() {
        if (!checkStatus()) {
            WxSnsAccessToken _token = WechatApiCaller.create(this.accountId).oauth().refreshToken(this.oauthRefreshToken);
            if (_token != null && _token.isOK()) {
                this.oauthRefreshToken(_token.getAccessToken().getToken())
                        .oauthExpiredTime(_token.getAccessToken().getExpiredTime())
                        .oauthRefreshToken(_token.getRefreshToken())
                        .oauthScope(_token.getScope())
                        .unionId(_token.getUnionId())
                        .lastModifyTime(System.currentTimeMillis());
                return true;
            }
        }
        return false;
    }
}
