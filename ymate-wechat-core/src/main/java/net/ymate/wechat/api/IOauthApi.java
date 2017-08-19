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
package net.ymate.wechat.api;

import net.ymate.wechat.IWechat;
import net.ymate.wechat.base.WxResult;
import net.ymate.wechat.base.WxSnsAccessToken;
import net.ymate.wechat.base.WxSnsUser;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/9 下午6:01
 * @version 1.0
 */
public interface IOauthApi {

    String OAUTH_GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?";

    String OAUTH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    String OAUTH_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    String OAUTH_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=";

    String OAUTH_AUTH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/auth?access_token=";

    //

    String codeUrl(String scope, String state, String redirectUri);

    WxSnsAccessToken accessToken(String code);

    WxSnsAccessToken refreshToken(String refreshToken);

    WxSnsUser userInfo(String oauthAccessToken, String openid);

    WxSnsUser userInfo(String oauthAccessToken, String openid, IWechat.LangType lang);

    WxResult authAccessToken(String oauthAccessToken, String openid);
}
