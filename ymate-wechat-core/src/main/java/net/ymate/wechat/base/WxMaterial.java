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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/23 下午5:05
 * @version 1.0
 */
public class WxMaterial extends WxResult {

    private int totalCount;

    private int itemCount;

    private List<Item> items;

    public WxMaterial(JSONObject result) {
        super(result);
        this.items = new ArrayList<Item>();
        if (this.getErrCode() == 0) {
            this.totalCount = result.getIntValue("total_count");
            this.itemCount = result.getIntValue("item_count");
            //
            JSONArray _jsonArr = result.getJSONArray("item");
            if (_jsonArr != null && !_jsonArr.isEmpty()) {
                for (int _idx = 0; _idx < _jsonArr.size(); _idx++) {
                    Item _item = _jsonArr.getObject(_idx, Item.class);
                    if (_item != null) {
                        this.items.add(_item);
                    }
                }
            }
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public List<Item> getItems() {
        return items;
    }

    /**
     * @author 刘镇 (suninformation@163.com) on 16/6/24 下午5:27
     * @version 1.0
     */
    public static class Count extends WxResult {

        private int voiceCount;

        private int videoCount;

        private int imageCount;

        private int newsCount;

        public Count(JSONObject result) {
            super(result);
            if (this.getErrCode() == 0) {
                this.voiceCount = result.getIntValue("voice_count");
                this.videoCount = result.getIntValue("video_count");
                this.imageCount = result.getIntValue("image_count");
                this.newsCount = result.getIntValue("news_count");
            }
        }

        public int getVoiceCount() {
            return voiceCount;
        }

        public int getVideoCount() {
            return videoCount;
        }

        public int getImageCount() {
            return imageCount;
        }

        public int getNewsCount() {
            return newsCount;
        }
    }

    /**
     * @author 刘镇 (suninformation@163.com) on 16/6/24 下午4:31
     * @version 1.0
     */
    public static class Detail extends WxResult {

        private Content content;

        public Detail(JSONObject result) {
            super(result);
            if (this.getErrCode() == 0) {
                this.content = JSON.toJavaObject(result, Content.class);
            }
        }

        public Content getContent() {
            return content;
        }
    }

    /**
     * @author 刘镇 (suninformation@163.com) on 16/6/23 下午5:54
     * @version 1.0
     */
    public static class Item {

        @JSONField(name = "media_id")
        private String mediaId;

        private String name;

        private Content content;

        @JSONField(name = "update_time")
        private Long updateTime;

        private String url;

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Content {

        private String title;

        private String description;

        @JSONField(name = "down_url")
        private String downUrl;

        @JSONField(name = "news_item")
        private List<WxMassArticle> newsItem;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDownUrl() {
            return downUrl;
        }

        public void setDownUrl(String downUrl) {
            this.downUrl = downUrl;
        }

        public List<WxMassArticle> getNewsItem() {
            return newsItem;
        }

        public void setNewsItem(List<WxMassArticle> newsItem) {
            this.newsItem = newsItem;
        }
    }
}
