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
package net.ymate.wechat.message.in;

import net.ymate.framework.commons.XPathHelper;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.message.InMessage;

import javax.xml.xpath.XPathExpressionException;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午6:49
 * @version 1.0
 */
public class InImageMessage extends InMessage {

    private String picUrl;

    private String mediaId;

    public InImageMessage(XPathHelper xPathHelper, String toUserName, String fromUserName, Integer createTime, IWechat.MessageType msgType, String msgId) throws XPathExpressionException {
        super(toUserName, fromUserName, createTime, msgType, msgId);
        this.picUrl = xPathHelper.getStringValue("//PicUrl");
        this.mediaId = xPathHelper.getStringValue("//MediaId");
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }
}
