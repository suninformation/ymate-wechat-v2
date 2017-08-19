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
package net.ymate.wechat.web.controller;

import net.ymate.framework.commons.ParamUtils;
import net.ymate.framework.core.util.WebUtils;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.PathVariable;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.util.CookieHelper;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.Wechat;
import net.ymate.wechat.WechatApiCaller;
import net.ymate.wechat.WechatUserStatus;
import net.ymate.wechat.base.WxSnsAccessToken;
import net.ymate.wechat.base.WxSnsUser;
import net.ymate.wechat.web.Constants;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/23 上午4:19
 * @version 1.0
 */
@Controller
@RequestMapping("/wechat/oauth")
public class OAuthController {

    @RequestMapping("/{account_id}")
    public IView __doOAuthGET(@PathVariable(Constants.ACCOUNT_ID) String accountId,
                              @RequestParam(defaultValue = IWechat.OAuthScope.SNSAPI_BASE) String scope,
                              @RequestParam(Constants.REDIRECT_URI) String redirectUri) throws Exception {
        if (StringUtils.isNotBlank(redirectUri)
                && Wechat.get().getModuleCfg().getAccountProvider().hasAccount(accountId)) {
            WechatApiCaller _caller = WechatApiCaller.create(accountId);
            //
            boolean _tokenGetFlag = false;
            //
            WechatUserStatus _wxUserStatus = Wechat.get().currentUserStatus(_caller.getAccount().getId());
            if (_wxUserStatus != null) {
                if (_wxUserStatus.refreshOauthIfNeed()) {
                    Wechat.get().saveOrUpdateUserStatus(_wxUserStatus);
                }
                if (_wxUserStatus.checkOauthScope(scope)) {
                    _tokenGetFlag = true;
                }
            }
            if (!_tokenGetFlag) {
                String _redirectUri = _caller.getAccount().getRedirectUri();
                if (StringUtils.isBlank(_redirectUri)) {
                    _redirectUri = String.format("%s?redirect_uri=%s", WebUtils.buildURL(WebContext.getRequest(), String.format("/wechat/oauth/%s/redirect", accountId), true), redirectUri);
                }
                String _state = ParamUtils.createNonceStr();
                CookieHelper.bind().allowUseHttpOnly().setCookie(Constants.WX_STATE, _state);
                return View.redirectView(_caller.oauth().codeUrl(scope, _state, _redirectUri));
            }
            return View.redirectView(redirectUri);
        }
        return HttpStatusView.bind(HttpServletResponse.SC_BAD_REQUEST);
    }

    @RequestMapping("/{account_id}/redirect")
    public IView __doRedirectGET(@PathVariable(Constants.ACCOUNT_ID) String accountId,
                                 @RequestParam String code,
                                 @RequestParam String state,
                                 @RequestParam(Constants.REDIRECT_URI) String redirectUri) throws Exception {
        if (StringUtils.isNotBlank(redirectUri) && StringUtils.isNotBlank(code)
                && Wechat.get().getModuleCfg().getAccountProvider().hasAccount(accountId)) {
            if (StringUtils.isNotBlank(state) && StringUtils.equals(state, CookieHelper.bind().getCookie(Constants.WX_STATE).toStringValue())) {
                WechatApiCaller _caller = WechatApiCaller.create(accountId);
                //
                WxSnsAccessToken _token = _caller.oauth().accessToken(code);
                if (_token != null && _token.isOK()) {
                    if (StringUtils.equals(_token.getScope(), IWechat.OAuthScope.SNSAPI_USERINFO)) {
                        WxSnsUser _snsUser = _caller.oauth().userInfo(_token.getAccessToken().getToken(), _token.getOpenId());
                        if (_snsUser != null && _snsUser.isOK()) {
                            Wechat.get().saveOrUpdateUserInfo(_caller.getAccount().getId(), _snsUser, _token);
                        }
                    }
                    //
                    Wechat.get().saveOrUpdateUserStatus(WechatUserStatus.create(Wechat.get().buildWxUid(_caller.getAccount().getId(), _token.getOpenId()), _caller.getAccount().getId(), _token));
                    //
                    return View.redirectView(redirectUri);
                }
            }
        }
        return HttpStatusView.bind(HttpServletResponse.SC_BAD_REQUEST);
    }
}
