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
package net.ymate.wechat.web.support;

import net.ymate.platform.webmvc.IRequestContext;
import net.ymate.platform.webmvc.IRequestProcessor;
import net.ymate.platform.webmvc.IWebMvc;
import net.ymate.platform.webmvc.RequestMeta;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.wechat.Wechat;
import net.ymate.wechat.base.WxAccount;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午4:25
 * @version 1.0
 */
public class WechatRequestProcessor implements IRequestProcessor {

    public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
        List<String> _params = new ArrayList<String>();
        _params.add(token);
        _params.add(timestamp);
        _params.add(nonce);
        Collections.sort(_params, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return DigestUtils.sha1Hex(_params.get(0) + _params.get(1) + _params.get(2)).equals(signature);
    }

    @Override
    public Map<String, Object> processRequestParams(IWebMvc owner, RequestMeta requestMeta) throws Exception {
        IRequestContext _reqContext = WebContext.getRequestContext();
        String _accountId = StringUtils.substringAfterLast(_reqContext.getRequestMapping(), "/");
        String _suffix = _reqContext.getSuffix();
        if (StringUtils.isNotBlank(_suffix) && StringUtils.endsWith(_accountId, _suffix)) {
            _accountId = StringUtils.substringBefore(_accountId, _suffix);
        }
        WxAccount _account = Wechat.get().getAccount(_accountId);
        if (_account != null) {
            HttpServletRequest _request = WebContext.getRequest();
            //
            String _timestamp = _request.getParameter("timestamp");
            String _nonce = _request.getParameter("nonce");
            String _signature = _request.getParameter("signature");
            //
            if (checkSignature(_account.getToken(), _signature, _timestamp, _nonce)) {
                String _content = IOUtils.toString(_request.getInputStream(), _request.getCharacterEncoding());
                //
                Map<String, Object> _params = new HashMap<String, Object>();
                _params.put("protocol", StringUtils.trimToNull(_content));
                _params.put("account", _account);
                _params.put("openId", _request.getParameter("openid"));
                //
                return _params;
            }
        }
        return Collections.emptyMap();
    }
}
