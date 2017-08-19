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

import net.ymate.wechat.base.WxAutoreply;
import net.ymate.wechat.base.WxQRCode;
import net.ymate.wechat.base.WxShortUrl;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/9 下午5:52
 * @version 1.0
 */
public interface ICommonApi {

    String WX_GET_CALLBACK_IP = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=";

    String QRCODE_CREATE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";

    String QRCODE_SHOW = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

    String SHORT_URL = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=";

    String AUTOREPLY_GET_INFO = "https://api.weixin.qq.com/cgi-bin/get_current_autoreply_info?access_token=";

    //

    String[] callbackIp();

    WxQRCode qrCodeCreate(int sceneId, int expireSeconds);

    WxQRCode qrCodeCreate(int sceneId);

    WxQRCode qrCodeCreate(String sceneStr, int expireSeconds);

    WxQRCode qrCodeCreate(String sceneStr);

    String qrCodeShow(String ticket);

    WxShortUrl shortUrl(String longUrl);

    WxAutoreply autoreplyGetInfo();
}
