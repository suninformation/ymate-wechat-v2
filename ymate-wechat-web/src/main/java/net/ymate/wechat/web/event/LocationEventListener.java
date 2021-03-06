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
import net.ymate.platform.core.util.UUIDUtils;
import net.ymate.wechat.Wechat;
import net.ymate.wechat.WechatEvent;
import net.ymate.wechat.message.event.LocationEvent;
import net.ymate.wechat.model.WechatLocation;
import net.ymate.wechat.web.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/10 下午10:44
 * @version 1.0
 */
public class LocationEventListener implements IEventListener<WechatEvent> {

    private static final Log _LOG = LogFactory.getLog(LocationEventListener.class);

    @Override
    public boolean handle(WechatEvent context) {
        switch (context.getEventName()) {
            case LOCATION:
                LocationEvent _location = (LocationEvent) context.getSource();
                try {
                    WechatLocation.builder()
                            .id(UUIDUtils.UUID())
                            .accountId(_location.getToUserName())
                            .wxUid(_location.getFromUserName())
                            .precision(new BigDecimal(_location.getPrecision()).multiply(Constants.__10000000L).longValue())
                            .locationLat(new BigDecimal(_location.getLatitude()).multiply(Constants.__10000000L).longValue())
                            .locationLon(new BigDecimal(_location.getLongitude()).multiply(Constants.__10000000L).longValue())
                            .createTime(_location.getCreateTime().longValue())
                            .build().save();
                } catch (Exception e) {
                    _LOG.warn(e.getMessage(), RuntimeUtils.unwrapThrow(e));
                }
                break;
        }
        return false;
    }
}
