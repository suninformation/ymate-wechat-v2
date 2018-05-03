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
package net.ymate.wechat.web.intercept;

import net.ymate.framework.core.util.WebUtils;
import net.ymate.framework.webmvc.support.UserSessionBean;
import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.WechatUserStatus;
import net.ymate.wechat.web.Constants;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/8 下午2:27
 * @version 1.0
 */
public class SnsUserAuthCheckInterceptor implements IInterceptor {

    @Override
    public Object intercept(InterceptContext context) throws Exception {
        IView _returnView = null;
        // 是否需要检查AccessToken状态, 默认为false
        boolean _checkStatus = BlurObject.bind(context.getContextParams().get(Constants.CHECK_STATE)).toBooleanValue();
        // 是否使用snsapi_base, 默认为true
        boolean _baseScope = BlurObject.bind(StringUtils.defaultIfBlank(context.getContextParams().get(Constants.OAUTH_BASE_SCOPE), "true")).toBooleanValue();
        //
        boolean _flag = false;
        UserSessionBean _sessionBean = UserSessionBean.current();
        if (_sessionBean == null) {
            _flag = true;
        } else {
            WechatUserStatus _wxUserStatus = _sessionBean.getAttribute(WechatUserStatus.class.getName());
            // OPENID是否存在?
            if (_wxUserStatus == null || StringUtils.isBlank(_wxUserStatus.openId())) {
                _flag = true;
            } else if (_checkStatus && !_wxUserStatus.checkStatus()) {
                _flag = true;
            } else if (!_baseScope && !_wxUserStatus.checkOauthScope(IWechat.OAuthScope.SNSAPI_USERINFO)) {
                _flag = true;
            }
        }
        if (_flag) {
            // 尝试分别从Context、Request、Session和Application中获取AccountId
            String _accountId = WebContext.getContext().getAttribute(Constants.ACCOUNT_ID);
            if (StringUtils.isBlank(_accountId)) {
                _accountId = StringUtils.trimToNull(WebContext.getContext().getParameterToString(Constants.ACCOUNT_ID));
                if (_accountId == null) {
                    _accountId = StringUtils.trimToNull(WebContext.getContext().getSessionAttributeToString(Constants.ACCOUNT_ID));
                    if (_accountId == null) {
                        _accountId = WebContext.getContext().getApplicationAttributeToString(Constants.ACCOUNT_ID);
                    }
                }
            }
            // 若不存在则回应HTTP状态为400
            if (_accountId != null) {
                // 自定义的跳转地址
                String _redirectUri = context.getContextParams().get(Constants.OAUTH_REDIRECT_URI);
                if (StringUtils.isBlank(_redirectUri)) {
                    // 拼装当前请求URL
                    _redirectUri = WebUtils.buildURL(WebContext.getRequest(), WebContext.getRequestContext().getRequestMapping(), true).concat("?").concat(WebContext.getRequest().getQueryString());
                } else if (!StringUtils.startsWithIgnoreCase(_redirectUri, "http")) {
                    _redirectUri = WebUtils.buildURL(WebContext.getRequest(), _redirectUri, true);
                }
                return View.redirectView(WebUtils.buildURL(WebContext.getRequest(), "/wechat/oauth/".concat(_accountId), true))
                        .addAttribute("scope", _baseScope ? IWechat.OAuthScope.SNSAPI_BASE : IWechat.OAuthScope.SNSAPI_USERINFO)
                        .addAttribute(Constants.REDIRECT_URI, _redirectUri);
            } else {
                _returnView = View.httpStatusView(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        return _returnView;
    }
}
