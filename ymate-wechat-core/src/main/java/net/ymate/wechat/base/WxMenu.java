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

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/31 下午11:15
 * @version 1.0
 */
public class WxMenu {

    @JSONField(name = "button")
    private List<MenuItem> items = new ArrayList<MenuItem>();

    public static WxMenu create() {
        return new WxMenu();
    }

    public WxMenu addItem(MenuItem item) {
        items.add(item);
        return this;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    /**
     * @author 刘镇 (suninformation@163.com) on 16/5/31 下午10:52
     * @version 1.0
     */
    public static class MenuItem {

        private String name;

        @JSONField(name = "sub_button")
        private List<MenuItem> subItems = new ArrayList<MenuItem>();

        private String type;

        private String key;

        private String url;

        @JSONField(name = "media_id")
        private String mediaId;

        @JSONField(name = "appid")
        private String appId;

        @JSONField(name = "pagepath")
        private String pagePath;

        public static MenuItem create() {
            return new MenuItem();
        }

        public MenuItem addItem(MenuItem item) {
            subItems.add(item);
            return this;
        }

        public String getName() {
            return name;
        }

        public MenuItem setName(String name) {
            this.name = name;
            return this;
        }

        public List<MenuItem> getSubItems() {
            return subItems;
        }

        public MenuItem setSubItems(List<MenuItem> subItems) {
            this.subItems = subItems;
            return this;
        }

        public String getType() {
            return type;
        }

        public MenuItem setType(String type) {
            this.type = type;
            return this;
        }

        public String getKey() {
            return key;
        }

        public MenuItem setKey(String key) {
            this.key = key;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public MenuItem setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getMediaId() {
            return mediaId;
        }

        public MenuItem setMediaId(String mediaId) {
            this.mediaId = mediaId;
            return this;
        }

        public String getAppId() {
            return appId;
        }

        public MenuItem setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public String getPagePath() {
            return pagePath;
        }

        public MenuItem setPagePath(String pagePath) {
            this.pagePath = pagePath;
            return this;
        }
    }
}
