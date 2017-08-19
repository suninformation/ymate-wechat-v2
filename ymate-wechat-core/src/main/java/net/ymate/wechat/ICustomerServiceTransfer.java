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
package net.ymate.wechat;

import net.ymate.wechat.message.InMessage;

/**
 * 客服消息转发器接口
 *
 * @author 刘镇 (suninformation@163.com) on 2017/8/15 下午4:40
 * @version 1.0
 */
public interface ICustomerServiceTransfer {

    /**
     * @param message 当前接收到的消息数据对象
     * @return 返回是否执行消息转发，true-需要, false-不需要
     */
    boolean messageFilter(InMessage message);

    /**
     * @param message 当前接收到的消息数据对象
     * @return 判断当前消息数据并返回指定客户账号，默认返回空(表示不将消息转发至指定客服)
     */
    String buildTransInfo(InMessage message);
}
