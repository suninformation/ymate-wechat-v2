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

import net.ymate.wechat.base.WxMenu;
import net.ymate.wechat.base.WxResult;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/9 下午6:05
 * @version 1.0
 */
public interface IMenusApi {

    String MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

    String MENU_GET = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";

    String MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";

    String MENU_SELF_INFO = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=";

    String MENU_ADD_CONDITIONAL = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=";

    String MENU_DEL_CONDITIONAL = "https://api.weixin.qq.com/cgi-bin/menu/delconditional?access_token=";

    String MENU_TRY_MATCH = "https://api.weixin.qq.com/cgi-bin/menu/trymatch?access_token=";

    //

    WxResult menuCreate(WxMenu menu);

    WxMenu menuGet();

    WxResult menuDelete();
}
