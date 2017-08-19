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

import net.ymate.wechat.base.WxResult;
import net.ymate.wechat.base.WxTag;
import net.ymate.wechat.base.WxUserList;
import net.ymate.wechat.base.WxUserTags;

import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/9 下午6:03
 * @version 1.0
 */
public interface ITagsApi {

    String TAGS_CREATE = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=";

    String TAGS_GET = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=";

    String TAGS_GET_ID = "https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=";

    String TAGS_UPDATE = "https://api.weixin.qq.com/cgi-bin/tags/update?access_token=";

    String TAGS_DELETE = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=";

    String TAGS_GET_USER = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=";

    String TAGS_UPDATE_USER = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=";

    String TAGS_DELETE_USER = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=";

    //

    WxTag createTag(String tagName);

    WxTag[] tagsList();

    WxResult updateTag(int tagId, String tagName);

    WxResult deleteTag(int tagId);

    WxUserList tagsUserList(int tagId, String nextOpenId);

    WxResult updateUserTag(int tagId, List<String> openIds);

    WxResult deleteUserTag(int tagId, List<String> openIds);

    WxUserTags userTags(String openId);
}
