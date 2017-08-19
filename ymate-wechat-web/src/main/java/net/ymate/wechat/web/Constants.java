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
package net.ymate.wechat.web;

import java.math.BigDecimal;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/16 上午9:55
 * @version 1.0
 */
public class Constants {

    public static final String ACCOUNT_ID = "account_id";

    public static final String SITE_ID = "site_id";

    public static final String REDIRECT_URI = "redirect_uri";

    public static final String WX_STATE = "wx_state";

    public static final String CHECK_STATE = "check_status";

    public static final String OAUTH_BASE_SCOPE = "oauth_base_scope";

    public static final String OAUTH_REDIRECT_URI = "oauth_redirect_uri";

    public static final String ATTR_ACCESS_TOKEN = "access_token";

    public static final String ATTR_JSAPI_TICKET = "jsapi_ticket";

    public static final String ATTR_JSAPI_CONFIG = "jsapi_config";

    public static final BigDecimal __10000000L = new BigDecimal(10000000L);
}
