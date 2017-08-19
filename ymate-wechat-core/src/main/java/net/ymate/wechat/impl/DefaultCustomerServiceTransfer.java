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
package net.ymate.wechat.impl;

import net.ymate.wechat.ICustomerServiceTransfer;
import net.ymate.wechat.message.InMessage;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/15 下午5:53
 * @version 1.0
 */
public class DefaultCustomerServiceTransfer implements ICustomerServiceTransfer {

    @Override
    public boolean messageFilter(InMessage message) {
        boolean _filtered = false;
        if (message != null) {
            switch (message.getMsgType()) {
                case TEXT:
                case IMAGE:
                case VOICE:
                case VIDEO:
                case SHORT_VIDEO:
                case LOCATION:
                case LINK:
                    _filtered = true;
                    break;
            }
        }
        return _filtered;
    }

    @Override
    public String buildTransInfo(InMessage message) {
        return null;
    }
}
