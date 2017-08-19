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
package net.ymate.wechat.api;

import net.ymate.wechat.IWechat;
import net.ymate.wechat.base.WxResult;
import net.ymate.wechat.base.WxUser;
import net.ymate.wechat.base.WxUserList;
import net.ymate.wechat.base.WxUserTags;

import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/9 下午6:02
 * @version 1.0
 */
public interface IUserApi {

    String USER_GET = "https://api.weixin.qq.com/cgi-bin/user/get";

    String USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info";

    String USER_INFO_BATCH = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=";

    String USER_UPDATE_REMARK = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=";

    //

    WxUserList userList(String nextOpenId);

    WxUser userInfo(String openId);

    WxUser userInfo(String openId, IWechat.LangType lang);

    List<WxUser> userInfoBatch(List<String> openIds);

    List<WxUser> userInfoBatch(List<String> openIds, IWechat.LangType lang);

    WxResult updateRemark(String openId, String remark);
}
