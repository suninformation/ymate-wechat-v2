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
package net.ymate.wechat.web.event;

import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.persistence.Fields;
import net.ymate.platform.persistence.jdbc.query.IDBLocker;
import net.ymate.platform.persistence.jdbc.transaction.ITrade;
import net.ymate.platform.persistence.jdbc.transaction.Transactions;
import net.ymate.wechat.Wechat;
import net.ymate.wechat.WechatEvent;
import net.ymate.wechat.message.event.ScanEvent;
import net.ymate.wechat.message.event.SubscribeEvent;
import net.ymate.wechat.model.WechatScan;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/18 上午9:04
 * @version 1.0
 */
public class ScanEventListener implements IEventListener<WechatEvent> {

    private static final Log _LOG = LogFactory.getLog(ScanEventListener.class);

    @Override
    public boolean handle(WechatEvent context) {
        switch (context.getEventName()) {
            case SUBSCRIBE:
                SubscribeEvent _subscribe = (SubscribeEvent) context.getSource();
                if (StringUtils.startsWith(_subscribe.getEventKey(), "qrscene_")) {
                    String _scene = StringUtils.substringAfter(_subscribe.getEventKey(), "qrscene_");
                    if (StringUtils.isNotBlank(_scene)) {
                        __doSaveScan(_subscribe.getToUserName(), _subscribe.getFromUserName(), _scene, _subscribe.getTicket());
                    }
                }
                break;
            case SCAN:
                ScanEvent _event = (ScanEvent) context.getSource();
                __doSaveScan(_event.getToUserName(), _event.getFromUserName(), _event.getEventKey(), _event.getTicket());
                break;
        }
        return false;
    }

    private void __doSaveScan(final String accountId, final String openId, final String scene, final String ticket) {
        try {
            Transactions.execute(new ITrade() {
                @Override
                public void deal() throws Throwable {
                    String _wxUid = Wechat.get().buildWxUid(accountId, openId);
                    String _id = DigestUtils.md5Hex(_wxUid + scene + StringUtils.trimToEmpty(ticket));
                    //
                    WechatScan _result = WechatScan.builder().id(_id).build().load(Fields.create(WechatScan.FIELDS.ID, WechatScan.FIELDS.HITS), IDBLocker.DEFAULT);
                    long _currentTime = System.currentTimeMillis();
                    if (_result == null) {
                        WechatScan.builder()
                                .id(_id)
                                .accountId(accountId)
                                .wxUid(_wxUid)
                                .scene(scene)
                                .ticket(ticket)
                                .hits(1L)
                                .createTime(_currentTime)
                                .lastModifyTime(_currentTime)
                                .build().save();
                    } else {
                        WechatScan.builder()
                                .id(_id)
                                .hits(_result.getHits() + 1)
                                .lastModifyTime(_currentTime)
                                .build().update(Fields.create(WechatScan.FIELDS.HITS, WechatScan.FIELDS.LAST_MODIFY_TIME));
                    }
                }
            });
        } catch (Exception e) {
            _LOG.warn(e.getMessage(), RuntimeUtils.unwrapThrow(e));
        }
    }
}
