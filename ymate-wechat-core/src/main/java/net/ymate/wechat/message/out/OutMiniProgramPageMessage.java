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
import net.ymate.wechat.message.OutMessage;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/20 下午10:35
 * @version 1.0
 */
public class OutMiniProgramPageMessage extends OutMessage {

    private String title;

    private String appId;

    private String pagePath;

    private String thumbMediaId;

    public OutMiniProgramPageMessage(String fromUserName, String toUserName, String title, String appId, String pagePath, String thumbMediaId) {
        super(fromUserName, toUserName, IWechat.MessageType.MINI_PROGRAM_PAGE);
        this.title = title;
        this.appId = appId;
        this.pagePath = pagePath;
        this.thumbMediaId = thumbMediaId;
    }

    @Override
    protected void __buildXML(StringBuilder builder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject _json = super.toJSON();
        _json.put("msgtype", this.getMsgType());
        JSONObject _miniprogrampage = new JSONObject();
        _miniprogrampage.put("title", this.title);
        _miniprogrampage.put("appid", this.appId);
        _miniprogrampage.put("pagepath", this.pagePath);
        _miniprogrampage.put("thumb_media_id", this.thumbMediaId);
        _json.put("miniprogrampage", _miniprogrampage);
        return _json;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
