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
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午7:22
 * @version 1.0
 */
public class InLocationMessage extends InMessage {

    private String locationX;

    private String locationY;

    private String scale;

    private String label;

    public InLocationMessage(XPathHelper xPathHelper, String toUserName, String fromUserName, Integer createTime, IWechat.MessageType msgType, String msgId) throws XPathExpressionException {
        super(toUserName, fromUserName, createTime, msgType, msgId);
        this.locationX = xPathHelper.getStringValue("//Location_X");
        this.locationY = xPathHelper.getStringValue("//Location_Y");
        this.scale = xPathHelper.getStringValue("//Scale");
        this.label = xPathHelper.getStringValue("//Label");
    }

    public String getLocationX() {
        return locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public String getScale() {
        return scale;
    }

    public String getLabel() {
        return label;
    }
}
