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
package net.ymate.wechat.web.event;

import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.wechat.Wechat;
import net.ymate.wechat.WechatApiCaller;
import net.ymate.wechat.WechatEvent;
import net.ymate.wechat.base.WxUser;
import net.ymate.wechat.message.event.SubscribeEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/11 上午1:46
 * @version 1.0
 */
public class SubscribeEventListener implements IEventListener<WechatEvent> {

    private static final Log _LOG = LogFactory.getLog(SubscribeEventListener.class);

    @Override
    public boolean handle(final WechatEvent context) {
        switch (context.getEventName()) {
            case SUBSCRIBE:
                try {
                    SubscribeEvent _subscribe = (SubscribeEvent) context.getSource();
                    WxUser _info = WechatApiCaller.create(_subscribe.getToUserName()).user().userInfo(_subscribe.getFromUserName());
                    if (_info != null && _info.isOK()) {
                        Wechat.get().saveOrUpdateUserInfo(_subscribe.getToUserName(), _info);
                    }
                } catch (Exception e) {
                    _LOG.warn("", RuntimeUtils.unwrapThrow(e));
                }
                break;
        }
        return false;
    }
}
