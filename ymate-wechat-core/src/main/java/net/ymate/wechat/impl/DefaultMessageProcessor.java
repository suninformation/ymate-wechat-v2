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
package net.ymate.wechat.impl;

import net.ymate.platform.core.event.EventContext;
import net.ymate.platform.core.event.Events;
import net.ymate.wechat.*;
import net.ymate.wechat.message.InMessage;
import net.ymate.wechat.message.OutMessage;
import net.ymate.wechat.message.event.*;
import net.ymate.wechat.message.in.*;
import net.ymate.wechat.message.out.OutTransferMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 下午11:23
 * @version 1.0
 */
public class DefaultMessageProcessor implements IMessageProcessor {

    private static final Log _LOG = LogFactory.getLog(DefaultMessageProcessor.class);

    private IWechat __owner;

    private Events __events;

    private boolean __devMode;

    private boolean __autoreply;

    private IAutoreplyProcessor __autoreplyProcessor;

    private boolean __transferred;

    private ICustomerServiceTransfer __customerServiceTransfer;

    protected IWechat getOwner() {
        return __owner;
    }

    protected boolean devMode() {
        return __devMode;
    }

    protected boolean autoreply() {
        return __autoreply;
    }

    protected boolean transferred() {
        return __transferred;
    }

    protected OutMessage __doWriteLog(InMessage message) {
        return null;
    }

    protected EventContext __doBuildEvent(WechatEvent.EVENT event, InMessage message) {
        return new WechatEvent(message, event);
    }

    protected OutMessage __doTransferIfNeed(InMessage message) {
        if (__transferred && __customerServiceTransfer.messageFilter(message)) {
            return new OutTransferMessage(message, __customerServiceTransfer.buildTransInfo(message));
        }
        return null;
    }

    @Override
    public void init(IWechat owner) throws Exception {
        __owner = owner;
        __events = __owner.getOwner().getEvents();
        __devMode = __owner.getOwner().getConfig().isDevelopMode();
        //
        __autoreplyProcessor = __owner.getModuleCfg().getAutoreplyProcessor();
        __autoreply = __autoreplyProcessor != null;
        //
        __customerServiceTransfer = __owner.getModuleCfg().getCustomerServiceTransfer();
        __transferred = __customerServiceTransfer != null;
    }

    @Override
    public void destroy() throws Exception {
    }

    @Override
    public OutMessage onMessageReceived(String protocol) throws Exception {
        if (__devMode) {
            _LOG.info(protocol);
        }
        return null;
    }

    @Override
    public void onExceptionCaught(Throwable cause) throws Exception {
        _LOG.error("", cause);
    }

    @Override
    public OutMessage onUnknownMessage(String protocol) throws Exception {
        _LOG.info(protocol);
        return null;
    }

    @Override
    public OutMessage onTextMessage(InTextMessage message) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.MSG_TEXT, message));
        //
        OutMessage _returnValue = __doTransferIfNeed(message);
        if (_returnValue == null && __autoreply) {
            _returnValue = __autoreplyProcessor.onKeywords(message.getToUserName(), message.getFromUserName(), message.getContent());
        }
        return _returnValue != null ? _returnValue : __doWriteLog(message);
    }

    @Override
    public OutMessage onImageMessage(InImageMessage message) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.MSG_IMAGE, message));
        //
        OutMessage _returnValue = __doTransferIfNeed(message);
        return _returnValue != null ? _returnValue : __doWriteLog(message);
    }

    @Override
    public OutMessage onVoiceMessage(InVoiceMessage message) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.MSG_VOICE, message));
        //
        OutMessage _returnValue = __doTransferIfNeed(message);
        if (_returnValue == null && __autoreply) {
            _returnValue = __autoreplyProcessor.onKeywords(message.getToUserName(), message.getFromUserName(), message.getRecognition());
        }
        return _returnValue != null ? _returnValue : __doWriteLog(message);
    }

    @Override
    public OutMessage onVideoMessage(InVideoMessage message) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.MSG_VIDEO, message));
        //
        OutMessage _returnValue = __doTransferIfNeed(message);
        return _returnValue != null ? _returnValue : __doWriteLog(message);
    }

    @Override
    public OutMessage onShortVideoMessage(InShortVideoMessage message) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.MSG_SHORT_VIDEO, message));
        OutMessage _returnValue = __doTransferIfNeed(message);
        return _returnValue != null ? _returnValue : __doWriteLog(message);
    }

    @Override
    public OutMessage onLocationMessage(InLocationMessage message) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.MSG_LOCATION, message));
        //
        OutMessage _returnValue = __doTransferIfNeed(message);
        if (_returnValue == null && __autoreply) {
            _returnValue = __autoreplyProcessor.onLocation(message);
        }
        return _returnValue != null ? _returnValue : __doWriteLog(message);
    }

    @Override
    public OutMessage onLinkMessage(InLinkMessage message) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.MSG_LINK, message));
        //
        OutMessage _returnValue = __doTransferIfNeed(message);
        return _returnValue != null ? _returnValue : __doWriteLog(message);
    }

    @Override
    public OutMessage onSubscribeEvent(SubscribeEvent event) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.SUBSCRIBE, event));
        if (__autoreply) {
            return __autoreplyProcessor.onSubscribe(event);
        }
        return __doWriteLog(event);
    }

    @Override
    public void onUnsubscribeEvent(UnsubscribeEvent event) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.UNSUBSCRIBE, event));
        __doWriteLog(event);
    }

    @Override
    public OutMessage onMenuEvent(MenuEvent event) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.MENU, event));
        if (__autoreply) {
            return __autoreplyProcessor.onMenu(event);
        }
        return __doWriteLog(event);
    }

    @Override
    public OutMessage onScanEvent(ScanEvent event) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.SCAN, event));
        return __doWriteLog(event);
    }

    @Override
    public OutMessage onLocationEvent(LocationEvent event) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.LOCATION, event));
        return __doWriteLog(event);
    }

    @Override
    public void onMassJobEvent(MassJobEvent event) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.MASS_SEND_JOB_FINISH, event));
        __doWriteLog(event);
    }

    @Override
    public void onTemplateJobEvent(TemplateJobEvent event) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.TEMPLATE_SEND_JOB_FINISH, event));
        __doWriteLog(event);
    }

    @Override
    public void onUserEntrySessionEvent(UserEntrySessionEvent event) throws Exception {
        __events.fireEvent(__doBuildEvent(WechatEvent.EVENT.USER_ENTRY_SESSION, event));
        __doWriteLog(event);
    }
}
