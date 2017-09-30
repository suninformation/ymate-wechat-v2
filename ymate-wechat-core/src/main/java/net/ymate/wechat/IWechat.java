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
package net.ymate.wechat;

import net.ymate.platform.core.YMP;
import net.ymate.wechat.base.WxAccount;
import net.ymate.wechat.base.WxSnsAccessToken;
import net.ymate.wechat.base.WxSnsUser;
import net.ymate.wechat.base.WxUser;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午1:50
 * @version 1.0
 */
public interface IWechat {

    String MODULE_NAME = "wechat";

    /**
     * @return 返回所属YMP框架管理器实例
     */
    YMP getOwner();

    /**
     * @return 返回模块配置对象
     */
    IWechatModuleCfg getModuleCfg();

    /**
     * @return 返回模块是否已初始化
     */
    boolean isInited();

    // ------

    WxAccount getAccount(String accountId);

    String getAccountAttribute(String accountId, String attrKey);

    //

    WechatApiCaller createApiCaller(String accountId);

    WechatApiCaller createApiCaller(WxAccount account);

    WechatUserStatus currentUserStatus(String accountId);

    WechatUserStatus loadUserStatus(String accountId, String wxUid);

    void saveOrUpdateUserStatus(WechatUserStatus userStatus);

    String buildWxUid(String accountId, String openId);

    void saveOrUpdateUserInfo(String accountId, WxSnsUser user, WxSnsAccessToken snsAccessToken);

    void saveOrUpdateUserInfo(String accountId, WxUser user);

    // ------

    enum LangType {
        zh_CN, zh_TW, en
    }

    /**
     * 微信事件枚举
     */
    enum EventType {

        SUBSCRIBE("subscribe"),
        UNSUBSCRIBE("unsubscribe"),
        MENU_CLICK("click"),
        MENU_VIEW("view"),
        MENU_SCANCODE_PUSH("scancode_push"),
        MENU_SCANCODE_WAITMSG("scancode_waitmsg"),
        MENU_PIC_SYSPHOTO("pic_sysphoto"),
        MENU_PIC_PHOTO_OR_ALBUM("pic_photo_or_album"),
        MENU_PIC_WEIXIN("pic_weixin"),
        MENU_LOCATION_SELECT("location_select"),
        MENU_MEDIA_ID("media_id"),
        LOCATION("LOCATION"),
        SCAN("SCAN"),
        MASS_SEND_JOB_FINISH("MASSSENDJOBFINISH"),
        TEMPLATE_SEND_JOB_FINISH("TEMPLATESENDJOBFINISH"),
        //
        USER_ENTER_TEMPSESSION("user_enter_tempsession");

        private String type;

        EventType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static EventType parse(String type) {
            if (StringUtils.isNotBlank(type)) {
                if ("VIEW".equals(type)) {
                    return EventType.MENU_VIEW;
                }
                if ("CLICK".equals(type)) {
                    return EventType.MENU_CLICK;
                }
                if ("subscribe".equals(type)) {
                    return EventType.SUBSCRIBE;
                }
                if ("unsubscribe".equals(type)) {
                    return EventType.UNSUBSCRIBE;
                }
                if ("scancode_push".equals(type)) {
                    return EventType.MENU_SCANCODE_PUSH;
                }
                if ("scancode_waitmsg".equals(type)) {
                    return EventType.MENU_SCANCODE_WAITMSG;
                }
                if ("pic_sysphoto".equals(type)) {
                    return EventType.MENU_PIC_SYSPHOTO;
                }
                if ("pic_photo_or_album".equals(type)) {
                    return EventType.MENU_PIC_PHOTO_OR_ALBUM;
                }
                if ("pic_weixin".equals(type)) {
                    return EventType.MENU_PIC_WEIXIN;
                }
                if ("location_select".equals(type)) {
                    return EventType.MENU_LOCATION_SELECT;
                }
                if ("media_id".equals(type)) {
                    return EventType.MENU_MEDIA_ID;
                }
                if ("LOCATION".equals(type)) {
                    return EventType.LOCATION;
                }
                if ("SCAN".equals(type)) {
                    return EventType.SCAN;
                }
                if ("TEMPLATESENDJOBFINISH".equals(type)) {
                    return EventType.TEMPLATE_SEND_JOB_FINISH;
                }
                if ("MASSSENDJOBFINISH".equals(type)) {
                    return EventType.MASS_SEND_JOB_FINISH;
                }
                if ("user_enter_tempsession".equals(type)) {
                    return EventType.USER_ENTER_TEMPSESSION;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    /**
     * 微信消息类型枚举
     */
    enum MessageType {

        TEXT("text"),
        IMAGE("image"),
        LINK("link"),
        VOICE("voice"),
        VIDEO("video"),
        SHORT_VIDEO("shortvideo"),
        MUSIC("music"),
        NEWS("news"),
        LOCATION("location"),
        EVENT("event"),
        TRANSFER_CUSTOMER_SERVICE("transfer_customer_service"),
        MINI_PROGRAM_PAGE("miniprogrampage");

        private String type;

        MessageType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static MessageType parse(String type) {
            if (StringUtils.isNotBlank(type)) {
                if ("text".equals(type)) {
                    return IWechat.MessageType.TEXT;
                }
                if ("image".equals(type)) {
                    return IWechat.MessageType.IMAGE;
                }
                if ("voice".equals(type)) {
                    return IWechat.MessageType.VOICE;
                }
                if ("video".equals(type)) {
                    return IWechat.MessageType.VIDEO;
                }
                if ("shortvideo".equals(type)) {
                    return IWechat.MessageType.SHORT_VIDEO;
                }
                if ("location".equals(type)) {
                    return IWechat.MessageType.LOCATION;
                }
                if ("link".equals(type)) {
                    return IWechat.MessageType.LINK;
                }
                if ("event".equals(type)) {
                    return IWechat.MessageType.EVENT;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    enum MediaType {

        IMAGE("image"),
        VOICE("voice"),
        VIDEO("video"),
        SHORT_VIDEO("shortvideo"),
        THUMB("thumb"),
        NEWS("news");

        private String type;

        MediaType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    /**
     * 微信公众号类型
     */
    enum AccountType {

        MP_SERVICE(1),
        MP_SUBSCRIBE(2),
        MINI_PROGRAM(3),
        UNKNOW(0);

        private int type;

        AccountType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static AccountType parse(int type) {
            AccountType _type = null;
            switch (type) {
                case 1:
                    _type = MP_SERVICE;
                    break;
                case 2:
                    _type = MP_SUBSCRIBE;
                    break;
                case 3:
                    _type = MINI_PROGRAM;
                    break;
                default:
                    _type = UNKNOW;
            }
            return _type;
        }

        @Override
        public String toString() {
            return type + "";
        }
    }

    interface MenuType {

        String CLICK = "click";

        String VIEW = "view";

        String SCANCODE_PUSH = "scancode_push";

        String SCANCODE_WAITMSG = "scancode_waitmsg";

        String PIC_SYSPHOTO = "pic_sysphoto";

        String PIC_PHOTO_OR_ALBUM = "pic_photo_or_album";

        String PIC_WEIXIN = "pic_weixin";

        String LOCATION_SELECT = "location_select";

        String MEDIA_ID = "media_id";

        String VIEW_LIMITED = "view_limited";

        String MINI_PROGRAM = "miniprogram";
    }

    /**
     * 微信网页授权作用域
     */
    interface OAuthScope {

        String SNSAPI_BASE = "snsapi_base";

        String SNSAPI_USERINFO = "snsapi_userinfo";
    }
}