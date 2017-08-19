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

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import net.ymate.framework.commons.ParamUtils;
import net.ymate.framework.commons.XPathHelper;
import net.ymate.platform.core.util.DateTimeUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.webmvc.annotation.*;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import net.ymate.platform.webmvc.view.impl.TextView;
import net.ymate.wechat.IMessageProcessor;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.Wechat;
import net.ymate.wechat.base.WxAccount;
import net.ymate.wechat.message.OutMessage;
import net.ymate.wechat.message.event.*;
import net.ymate.wechat.message.in.*;
import net.ymate.wechat.web.Constants;
import net.ymate.wechat.web.support.WechatRequestProcessor;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

/**
 * 微信服务接入统一入口
 *
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午3:56
 * @version 1.0
 */
@Controller
@RequestMapping("/wechat/service")
public class ServiceController {

    @RequestMapping("/{account_id}")
    public IView __doServiceGET(@PathVariable(Constants.ACCOUNT_ID) String accountId,
                                @RequestParam String signature,
                                @RequestParam String timestamp,
                                @RequestParam String nonce,
                                @RequestParam String echostr) throws Exception {

        if (StringUtils.isBlank(accountId)
                || StringUtils.isBlank(signature)
                || StringUtils.isBlank(timestamp)
                || StringUtils.isBlank(nonce)
                || StringUtils.isBlank(echostr)) {
            echostr = "";
        } else {
            WxAccount _account = Wechat.get().getModuleCfg().getAccountProvider().getAccount(accountId);
            if (_account == null || !WechatRequestProcessor.checkSignature(_account.getToken(), signature, timestamp, nonce)) {
                echostr = "";
            }
        }
        return new TextView(echostr);
    }

    private String __doParseCryptMsg(WXBizMsgCrypt msgCrypt, XPathHelper helper) throws XPathExpressionException, AesException {
        return msgCrypt.decryptMsg(helper.getStringValue("//MsgSignature"), helper.getStringValue("//TimeStamp"), helper.getStringValue("//Nonce"), helper.getStringValue("//Encrypt"));
    }

    @RequestMapping(value = "/{account_id}", method = Type.HttpMethod.POST)
    @RequestProcessor(WechatRequestProcessor.class)
    public IView __doServicePOST(WxAccount account, String protocol, String openId) throws Exception {
        //
        if (account != null && StringUtils.isNotBlank(protocol)) {
            // 解析消息报文
            XPathHelper _helper = new XPathHelper(protocol);
            WXBizMsgCrypt _msgCrypt = null;
            if (account.isMsgEncrypted()) {
                _msgCrypt = new WXBizMsgCrypt(account.getToken(), account.getAppAesKey(), account.getAppId());
                try {
                    protocol = __doParseCryptMsg(_msgCrypt, _helper);
                } catch (AesException e) {
                    if (StringUtils.isBlank(account.getLastAppAesKey())) {
                        throw e;
                    }
                    _msgCrypt = new WXBizMsgCrypt(account.getToken(), account.getLastAppAesKey(), account.getAppId());
                    protocol = __doParseCryptMsg(_msgCrypt, _helper);
                }
                _helper = new XPathHelper(protocol);
            }
            //
            IMessageProcessor _messageProcessor = Wechat.get().getModuleCfg().getMessageProcessor();
            OutMessage _responseMsg = null;
            try {
                _responseMsg = _messageProcessor.onMessageReceived(protocol);
                if (_responseMsg == null) {
                    String _toUserName = _helper.getStringValue("//ToUserName");
                    String _fromUserName = _helper.getStringValue("//FromUserName");
                    if (StringUtils.equals(account.getId(), _toUserName) && StringUtils.equals(openId, _fromUserName)) {
                        Integer _createTime = _helper.getNumberValue("//CreateTime").intValue();
                        //
                        IWechat.MessageType _messageType = IWechat.MessageType.parse(_helper.getStringValue("//MsgType"));
                        if (_messageType != null) {
                            if (IWechat.MessageType.EVENT.equals(_messageType)) {
                                IWechat.EventType _eventType = IWechat.EventType.parse(_helper.getStringValue("//Event"));
                                if (_eventType != null) {
                                    switch (_eventType) {
                                        case SUBSCRIBE:
                                            _responseMsg = _messageProcessor.onSubscribeEvent(new SubscribeEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                            break;
                                        case UNSUBSCRIBE:
                                            _messageProcessor.onUnsubscribeEvent(new UnsubscribeEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                            break;
                                        case MENU_CLICK:
                                        case MENU_VIEW:
                                        case MENU_LOCATION_SELECT:
                                        case MENU_MEDIA_ID:
                                        case MENU_PIC_PHOTO_OR_ALBUM:
                                        case MENU_PIC_SYSPHOTO:
                                        case MENU_PIC_WEIXIN:
                                        case MENU_SCANCODE_PUSH:
                                        case MENU_SCANCODE_WAITMSG:
                                            _responseMsg = _messageProcessor.onMenuEvent(new MenuEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                            break;
                                        case SCAN:
                                            _responseMsg = _messageProcessor.onScanEvent(new ScanEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                            break;
                                        case LOCATION:
                                            _responseMsg = _messageProcessor.onLocationEvent(new LocationEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                            break;
                                        case MASS_SEND_JOB_FINISH:
                                            _messageProcessor.onMassJobEvent(new MassJobEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                            break;
                                        case TEMPLATE_SEND_JOB_FINISH:
                                            _messageProcessor.onTemplateJobEvent(new TemplateJobEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                            break;
                                        case USER_ENTER_TEMPSESSION:
                                            _messageProcessor.onUserEntrySessionEvent(new UserEntrySessionEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                            break;
                                        default:
                                            _responseMsg = _messageProcessor.onUnknownMessage(protocol);
                                    }
                                }
                            } else {
                                String _msgId = _helper.getStringValue("//MsgId");
                                switch (_messageType) {
                                    case TEXT:
                                        _responseMsg = _messageProcessor.onTextMessage(new InTextMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                        break;
                                    case IMAGE:
                                        _responseMsg = _messageProcessor.onImageMessage(new InImageMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                        break;
                                    case VOICE:
                                        _responseMsg = _messageProcessor.onVoiceMessage(new InVoiceMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                        break;
                                    case VIDEO:
                                        _responseMsg = _messageProcessor.onVideoMessage(new InVideoMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                        break;
                                    case SHORT_VIDEO:
                                        _responseMsg = _messageProcessor.onShortVideoMessage(new InShortVideoMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                        break;
                                    case LOCATION:
                                        _responseMsg = _messageProcessor.onLocationMessage(new InLocationMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                        break;
                                    case LINK:
                                        _responseMsg = _messageProcessor.onLinkMessage(new InLinkMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                        break;
                                    default:
                                        _responseMsg = _messageProcessor.onUnknownMessage(protocol);
                                }
                            }
                        } else {
                            _responseMsg = _messageProcessor.onUnknownMessage(protocol);
                        }
                    }
                }
            } catch (Throwable e) {
                _messageProcessor.onExceptionCaught(RuntimeUtils.unwrapThrow(e));
            }
            String _responseContent = _responseMsg != null ? _responseMsg.toXML() : null;
            if (_responseContent != null && account.isMsgEncrypted() && _msgCrypt != null) {
                _responseContent = _msgCrypt.encryptMsg(_responseContent, DateTimeUtils.currentTimeUTC() + "", ParamUtils.createNonceStr());
            }
            return new TextView(StringUtils.trimToEmpty(_responseContent), "text/xml");
        }
        return HttpStatusView.bind(HttpServletResponse.SC_BAD_REQUEST);
    }
}
