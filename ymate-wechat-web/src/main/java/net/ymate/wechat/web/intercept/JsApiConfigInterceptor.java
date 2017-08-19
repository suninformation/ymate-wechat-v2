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
package net.ymate.wechat.web.intercept;

import com.alibaba.fastjson.JSONObject;
import net.ymate.framework.commons.ParamUtils;
import net.ymate.framework.core.util.WebUtils;
import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.util.DateTimeUtils;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import net.ymate.wechat.Wechat;
import net.ymate.wechat.base.WxAccount;
import net.ymate.wechat.web.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/18 上午11:31
 * @version 1.0
 */
public class JsApiConfigInterceptor implements IInterceptor {

    @Override
    public Object intercept(InterceptContext context) throws Exception {
        HttpServletRequest _request = WebContext.getRequest();
        String _paramName = StringUtils.defaultIfBlank(context.getContextParams().get(Constants.ACCOUNT_ID), Constants.ACCOUNT_ID);
        String _accountId = WebContext.getContext().getAttribute(_paramName);
        if (StringUtils.isBlank(_accountId)) {
            _accountId = _request.getParameter(_accountId);
        }
        if (StringUtils.isNotBlank(_accountId)) {
            WxAccount _account = Wechat.get().getAccount(_accountId);
            if (_account != null) {
                String _queryStr = StringUtils.trimToNull(_request.getQueryString());
                String _currURL = WebUtils.buildURL(_request, WebContext.getRequestContext().getRequestMapping() + (_queryStr == null ? "" : "?" + _queryStr), true);
                //
                _request.setAttribute(Constants.ATTR_JSAPI_CONFIG, __buildJsApiConfig(_account, StringUtils.substringBefore(_currURL, "#"), DateTimeUtils.currentTimeUTC(), ParamUtils.createNonceStr()));
                //
                return null;
            }
        }
        return HttpStatusView.bind(HttpServletResponse.SC_BAD_REQUEST);
    }

    private JSONObject __buildJsApiConfig(WxAccount account, String url, long timestamp, String noncestr) {
        String _signature = "jsapi_ticket=" + Wechat.get().getModuleCfg().getTokenCacheAdapter().getJsApiTicket(account).getTicket() + "&" + "noncestr=" + noncestr + "&" + "timeStamp=" + timestamp + "&" + "url=" + url;
        _signature = DigestUtils.sha1Hex(_signature);
        //
        JSONObject _json = new JSONObject();
        _json.put("appId", account.getAppId());
        _json.put("timestamp", timestamp);
        _json.put("nonceStr", noncestr);
        _json.put("signature", _signature);
        //
        return _json;
    }
}
