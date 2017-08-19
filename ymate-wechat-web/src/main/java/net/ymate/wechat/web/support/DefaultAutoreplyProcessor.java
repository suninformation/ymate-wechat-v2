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
package net.ymate.wechat.web.support;

import net.ymate.wechat.IAutoreplyProcessor;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.message.OutMessage;
import net.ymate.wechat.message.event.MenuEvent;
import net.ymate.wechat.message.event.SubscribeEvent;
import net.ymate.wechat.message.in.InLocationMessage;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/17 上午11:19
 * @version 1.0
 */
public class DefaultAutoreplyProcessor implements IAutoreplyProcessor {

    private IWechat __owner;

    @Override
    public void init(IWechat owner) throws Exception {
        __owner = owner;
    }

    @Override
    public void destroy() throws Exception {
    }

    public IWechat getOwner() {
        return __owner;
    }

    @Override
    public OutMessage onSubscribe(SubscribeEvent event) throws Exception {
        return null;
    }

    @Override
    public OutMessage onMenu(MenuEvent event) throws Exception {
        return null;
    }

    @Override
    public OutMessage onLocation(InLocationMessage location) throws Exception {
        return null;
    }

    @Override
    public OutMessage onKeywords(String accountId, String toUserName, String keywords) throws Exception {
        return null;
    }
}
