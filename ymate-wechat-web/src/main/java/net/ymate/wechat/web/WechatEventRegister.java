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
package net.ymate.wechat.web;

import net.ymate.platform.core.event.Events;
import net.ymate.platform.core.event.IEventRegister;
import net.ymate.platform.core.event.annotation.EventRegister;
import net.ymate.wechat.WechatEvent;
import net.ymate.wechat.web.event.*;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/17 上午10:37
 * @version 1.0
 */
@EventRegister
public class WechatEventRegister implements IEventRegister {

    @Override
    public void register(Events events) throws Exception {
        events.registerListener(Events.MODE.ASYNC, WechatEvent.class, new MessageEventListener())
                .registerListener(Events.MODE.ASYNC, WechatEvent.class, new LocationEventListener())
                .registerListener(Events.MODE.ASYNC, WechatEvent.class, new SubscribeEventListener())
                .registerListener(Events.MODE.ASYNC, WechatEvent.class, new UnsubscribeEventListener())
                .registerListener(Events.MODE.ASYNC, WechatEvent.class, new ScanEventListener());
    }
}
