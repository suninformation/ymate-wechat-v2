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
import net.ymate.wechat.base.WxMsgSend;
import net.ymate.wechat.base.WxResult;
import net.ymate.wechat.message.OutMessage;
import net.ymate.wechat.message.TemplateMessage;

import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/9 下午6:07
 * @version 1.0
 */
public interface IMessageApi {

    String MESSAGE_CUSTOM_SEND = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

    String MESSAGE_TEMPLATE_SEND = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    String MASS_SEND_ALL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=";

    String MASS_SEND_BY_OPENID = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";

    String MASS_PREVIEW = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=";

    String MASS_DELETE = "https://api.weixin.qq.com//cgi-bin/message/mass/delete?access_token=";

    String MASS_GET = "https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=";

    //

    WxResult customSend(OutMessage message);

    WxMsgSend templateSend(TemplateMessage message);

    //

    WxMsgSend massSend(String clientmsgid, String tagId, boolean toAll, IWechat.MessageType msgType, String mediaIdOrContent, int sendIgnoreReprint);

    WxMsgSend massSend(String tagId, boolean toAll, IWechat.MessageType msgType, String mediaIdOrContent);

    WxMsgSend massSend(String clientmsgid, List<String> openIds, IWechat.MessageType msgType, String mediaIdOrContent, int sendIgnoreReprint);

    WxMsgSend massSend(List<String> openIds, IWechat.MessageType msgType, String mediaIdOrContent);

    WxMsgSend massPreview(String openId, IWechat.MessageType msgType, String mediaIdOrContent);

    WxResult massDelete(String msgId);

    WxMsgSend massGet(String msgId);
}
