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
package net.ymate.wechat.message.out;

import com.alibaba.fastjson.JSONObject;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.message.InMessage;
import net.ymate.wechat.message.OutMessage;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/15 下午6:12
 * @version 1.0
 */
public class OutTransferMessage extends OutMessage {

    public OutTransferMessage(InMessage inMessage) {
        super(inMessage.getFromUserName(), inMessage.getToUserName(), IWechat.MessageType.TRANSFER_CUSTOMER_SERVICE);
    }

    public OutTransferMessage(InMessage inMessage, String kfAccount) {
        super(inMessage.getFromUserName(), inMessage.getToUserName(), IWechat.MessageType.TRANSFER_CUSTOMER_SERVICE);
        this.setKfAccount(kfAccount);
    }

    @Override
    protected void __buildXML(StringBuilder builder) {
        if (StringUtils.isNotBlank(this.getKfAccount())) {
            builder.append("<TransInfo>\n");
            builder.append("<KfAccount><![CDATA[").append(this.getKfAccount()).append("]]></KfAccount>\n");
            builder.append("</TransInfo>\n");
        }
    }

    @Override
    public JSONObject toJSON() {
        throw new UnsupportedOperationException();
    }
}
