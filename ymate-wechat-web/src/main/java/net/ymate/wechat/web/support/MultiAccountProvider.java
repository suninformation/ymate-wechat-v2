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
package net.ymate.wechat.web.support;

import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.persistence.IResultSet;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.base.WxAccount;
import net.ymate.wechat.impl.DefaultAccountProvider;
import net.ymate.wechat.model.WechatAccount;
import net.ymate.wechat.web.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/18 下午3:14
 * @version 1.0
 */
public class MultiAccountProvider extends DefaultAccountProvider {
    @Override
    public void init(IWechat owner) throws Exception {
        super.init(owner);
        //
        IResultSet<WechatAccount> _wxAccounts = WechatAccount.builder().build().find();
        if (_wxAccounts != null && _wxAccounts.isResultsAvailable()) {
            for (WechatAccount _item : _wxAccounts.getResultData()) {
                WxAccount _account = new WxAccount(_item.getAccountId(),
                        _item.getAppId(),
                        _item.getAppSecret(),
                        _item.getAppAesKey(),
                        _item.getLastAppAesKey(),
                        _item.getRedirectUri(), IWechat.AccountType.parse(_item.getType()), BlurObject.bind(_item.getIsVerified()).toBooleanValue(),
                        _item.getToken(), BlurObject.bind(_item.getIsMsgEncrypted()).toBooleanValue()).addAttribute(Constants.ACCOUNT_ID, _item.getId());
                //
                if (BlurObject.bind(_item.getIsDefault()).toBooleanValue()) {
                    _account.addAttribute(DEFAULT_ACCOUNT, "true");
                }
                //
                __doRegisterAccount(_account);
            }
        } else {
            WxAccount _account = WxAccount.create(owner.getOwner().getConfig().getModuleConfigs(IWechat.MODULE_NAME)).addAttribute(DEFAULT_ACCOUNT, "true");
            long _currentTime = System.currentTimeMillis();
            WechatAccount _wxAccount = WechatAccount.builder()
                    .id(DigestUtils.md5Hex(_account.getId()))
                    .name(_account.getId())
                    .accountId(_account.getId())
                    .appId(_account.getAppId())
                    .appSecret(_account.getAppSecret())
                    .appAesKey(_account.getAppAesKey())
                    .lastAppAesKey(_account.getLastAppAesKey())
                    .redirectUri(_account.getRedirectUri())
                    .type(_account.getType().getType())
                    .isVerified(_account.isVerified() ? 1 : 0)
                    .token(_account.getToken())
                    .isMsgEncrypted(_account.isMsgEncrypted() ? 1 : 0)
                    .isDefault(1)
                    .siteId(StringUtils.defaultIfBlank(_account.getAttribute(Constants.SITE_ID), DEFAULT_ACCOUNT))
                    .createTime(_currentTime)
                    .lastModifyTime(_currentTime)
                    .build().saveOrUpdate();
            //
            __doRegisterAccount(_account.addAttribute(Constants.ACCOUNT_ID, _wxAccount.getId()));
        }
    }
}
