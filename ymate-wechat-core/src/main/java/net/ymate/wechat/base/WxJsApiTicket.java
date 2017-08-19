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
package net.ymate.wechat.base;

import com.alibaba.fastjson.JSONObject;
import net.ymate.platform.core.util.DateTimeUtils;

import java.io.Serializable;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/11 下午6:07
 * @version 1.0
 */
public class WxJsApiTicket implements Serializable {

    private String ticket;

    private long expiredTime;

    public WxJsApiTicket(String ticket, long expiredTime) {
        this.ticket = ticket;
        this.expiredTime = expiredTime;
    }

    public String getTicket() {
        return ticket;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expiredTime;
    }

    /**
     * @author 刘镇 (suninformation@163.com) on 16/5/28 上午2:49
     * @version 1.0
     */
    public static class Result extends WxResult {

        private WxJsApiTicket ticket;

        public Result(JSONObject result) {
            super(result);
            if (this.getErrCode() == 0) {
                this.ticket = new WxJsApiTicket(result.getString("ticket"), DateTimeUtils.currentTimeMillis() + (result.getIntValue("expires_in") - 10) * 1000);
            }
        }

        public WxJsApiTicket getTicket() {
            return ticket;
        }
    }
}
