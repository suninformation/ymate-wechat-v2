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
package net.ymate.wechat.base;

import net.ymate.platform.core.lang.BlurObject;
import net.ymate.wechat.IWechat;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/23 下午6:44
 * @version 1.0
 */
public class WxAccount implements Serializable {

    private static final Log _LOG = LogFactory.getLog(WxAccount.class);

    // 微信公众帐号原始ID
    private String id;

    // 第三方应用唯一凭证
    private String appId;

    // 第三方应用唯一凭证密钥
    private String appSecret;

    // 消息加密密钥由43位字符组成，可随机修改，字符范围为A-Z，a-z，0-9
    private String appAesKey;

    // 上一次使用的消息加密密钥（备用）
    private String lastAppAesKey;

    // 是否采用消息加密
    private boolean isMsgEncrypted;

    // OAuth授权后重定向的URL地址
    private String redirectUri;

    // 公众号类型, 默认值: IWechat.AccountType.UNKNOW
    private IWechat.AccountType type;

    // 是否已认证
    private boolean isVerified;

    /**
     * 微信接入唯一标识, 默认值: wechat
     */
    private String token;

    // 自定义扩展属性映射
    private Map<String, String> attributes = new HashMap<String, String>();

    public static WxAccount create(Map<String, String> moduleCfgs) {
        WxAccount _account = new WxAccount(moduleCfgs.get("account_id"),
                moduleCfgs.get("app_id"),
                moduleCfgs.get("app_secret"),
                moduleCfgs.get("app_aes_key"),
                moduleCfgs.get("last_app_aes_key"),
                moduleCfgs.get("redirect_uri"), IWechat.AccountType.parse(BlurObject.bind(moduleCfgs.get("type")).toIntValue()),
                BlurObject.bind(moduleCfgs.get("is_verfied")).toBooleanValue(),
                moduleCfgs.get("token"), BlurObject.bind(moduleCfgs.get("is_msg_encrypted")).toBooleanValue());
        //
        for (Map.Entry<String, String> _item : moduleCfgs.entrySet()) {
            if (_item.getKey().startsWith("params.")) {
                String _key = StringUtils.substringAfter(_item.getKey(), "params.");
                _account.addAttribute(_key, _item.getValue());
            }
        }
        return _account;
    }

    public WxAccount(String id, String appId, String appSecret, String appAesKey, String lastAppAesKey, String redirectUri, IWechat.AccountType type, boolean isVerfied, String token, boolean isMsgEncrypted) {
        if (StringUtils.isBlank(id)) {
            throw new NullArgumentException("id");
        }
        if (StringUtils.isBlank(appId)) {
            throw new NullArgumentException("appId");
        }
        if (StringUtils.isBlank(appSecret)) {
            throw new NullArgumentException("appSecret");
        }
        //
        this.id = id;
        this.appId = appId;
        this.appSecret = appSecret;
        this.appAesKey = appAesKey;
        this.lastAppAesKey = lastAppAesKey;
        this.redirectUri = redirectUri;
        this.type = type == null ? IWechat.AccountType.UNKNOW : type;
        this.isVerified = isVerfied;
        this.token = StringUtils.defaultIfBlank(token, IWechat.MODULE_NAME);
        this.isMsgEncrypted = isMsgEncrypted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppAesKey() {
        return appAesKey;
    }

    public void setAppAesKey(String appAesKey) {
        this.appAesKey = appAesKey;
    }

    public String getLastAppAesKey() {
        return lastAppAesKey;
    }

    public void setLastAppAesKey(String lastAppAesKey) {
        this.lastAppAesKey = lastAppAesKey;
    }

    public boolean isMsgEncrypted() {
        return isMsgEncrypted;
    }

    public void setMsgEncrypted(boolean msgEncrypted) {
        isMsgEncrypted = msgEncrypted;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public IWechat.AccountType getType() {
        return type;
    }

    public void setType(IWechat.AccountType type) {
        this.type = type;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public WxAccount addAttribute(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }
}
