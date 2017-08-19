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
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.core.util.UUIDUtils;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.message.OutMessage;
import net.ymate.wechat.message.out.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/9 上午4:46
 * @version 1.0
 */
public class WxAutoreply extends WxResult {

    private static final Log _LOG = LogFactory.getLog(WxAutoreply.class);

    private String id;

    @JSONField(name = "is_autoreply_open")
    private boolean autoreplyOpen;

    @JSONField(name = "is_add_friend_reply_open")
    private boolean subscribeReplyOpen;

    @JSONField(name = "add_friend_autoreply_info")
    private Content subscribeAutoreplyInfo;

    @JSONField(name = "message_default_autoreply_info")
    private Content defaultAutoreplyInfo;

    @JSONField(name = "keyword_autoreply_info")
    private KeywordAutoreply keywordAutoreplyInfo;

    @JSONField(name = "last_modify_time")
    private long lastModifyTime;

    public WxAutoreply(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            this.autoreplyOpen = result.getBooleanValue("is_autoreply_open");
            this.subscribeReplyOpen = result.getBooleanValue("is_add_friend_reply_open");
            this.subscribeAutoreplyInfo = result.getObject("add_friend_autoreply_info", Content.class);
            this.defaultAutoreplyInfo = result.getObject("message_default_autoreply_info", Content.class);
            this.keywordAutoreplyInfo = result.getObject("keyword_autoreply_info", KeywordAutoreply.class);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAutoreplyOpen() {
        return autoreplyOpen;
    }

    public void setAutoreplyOpen(boolean autoreplyOpen) {
        this.autoreplyOpen = autoreplyOpen;
    }

    public boolean isSubscribeReplyOpen() {
        return subscribeReplyOpen;
    }

    public void setSubscribeReplyOpen(boolean subscribeReplyOpen) {
        this.subscribeReplyOpen = subscribeReplyOpen;
    }

    public Content getSubscribeAutoreplyInfo() {
        return subscribeAutoreplyInfo;
    }

    public void setSubscribeAutoreplyInfo(Content subscribeAutoreplyInfo) {
        this.subscribeAutoreplyInfo = subscribeAutoreplyInfo;
    }

    public Content getDefaultAutoreplyInfo() {
        return defaultAutoreplyInfo;
    }

    public void setDefaultAutoreplyInfo(Content defaultAutoreplyInfo) {
        this.defaultAutoreplyInfo = defaultAutoreplyInfo;
    }

    public KeywordAutoreply getKeywordAutoreplyInfo() {
        return keywordAutoreplyInfo;
    }

    public void setKeywordAutoreplyInfo(KeywordAutoreply keywordAutoreplyInfo) {
        this.keywordAutoreplyInfo = keywordAutoreplyInfo;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public AutoreplyMeta toAutoreplyMeta() {
        AutoreplyMeta _confMeta = new AutoreplyMeta();
        _confMeta.setAutoreplyOpen(this.autoreplyOpen);
        _confMeta.setSubscribeReplyOpen(this.subscribeReplyOpen);
        _confMeta.setSubscribeAutoreplyInfo(this.subscribeAutoreplyInfo);
        _confMeta.setDefaultAutoreplyInfo(this.defaultAutoreplyInfo);
        if (this.keywordAutoreplyInfo != null && this.keywordAutoreplyInfo.getItems() != null && !this.keywordAutoreplyInfo.getItems().isEmpty()) {
            List<AutoreplyRule> _rules = new ArrayList<AutoreplyRule>();
            for (KeywordAutoreplyItem _item : this.keywordAutoreplyInfo.getItems()) {
                AutoreplyRule _rule = new AutoreplyRule();
                _rule.setReplyAll(StringUtils.equalsIgnoreCase(_item.getReplyMode(), "reply_all"));
                _rule.setKeywords(_item.getKeywords());
                _rule.setReplyInfos(_item.getReplyInfos());
                //
                _rules.add(_rule);
            }
            _confMeta.setReplyRules(_rules);
        }
        return _confMeta;
    }

    public static class KeywordAutoreply implements Serializable {

        @JSONField(name = "list")
        private List<KeywordAutoreplyItem> items;

        public List<KeywordAutoreplyItem> getItems() {
            return items;
        }

        public void setItems(List<KeywordAutoreplyItem> items) {
            this.items = items;
        }
    }

    public static class KeywordAutoreplyItem implements Serializable {

        private String id;

        @JSONField(name = "rule_name")
        private String ruleName;

        @JSONField(name = "create_time")
        private long createTime;

        @JSONField(name = "reply_mode")
        private String replyMode;

        @JSONField(name = "keyword_list_info")
        private List<Keyword> keywords;

        @JSONField(name = "reply_list_info")
        private List<Content> replyInfos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRuleName() {
            return ruleName;
        }

        public void setRuleName(String ruleName) {
            this.ruleName = ruleName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getReplyMode() {
            return replyMode;
        }

        public void setReplyMode(String replyMode) {
            this.replyMode = replyMode;
        }

        public List<Keyword> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<Keyword> keywords) {
            this.keywords = keywords;
        }

        public List<Content> getReplyInfos() {
            return replyInfos;
        }

        public void setReplyInfos(List<Content> replyInfos) {
            this.replyInfos = replyInfos;
        }
    }

    public static class Keyword extends Content {
        @JSONField(name = "match_mode")
        private String matchMode;

        public String getMatchMode() {
            return matchMode;
        }

        public void setMatchMode(String matchMode) {
            this.matchMode = matchMode;
        }
    }

    public static class Content implements Serializable {

        private String type;
        private String content;

        @JSONField(name = "news_info")
        private News news;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public News getNews() {
            return news;
        }

        public void setNews(News news) {
            this.news = news;
        }

        public OutMessage toOutMessage(String fromUserName, String toUserName) {
            try {
                IWechat.MessageType _type = IWechat.MessageType.valueOf(StringUtils.upperCase(type));
                OutMessage _out = null;
                switch (_type) {
                    case TEXT:
                        _out = new OutTextMessage(fromUserName, toUserName, content);
                        break;
                    case IMAGE:
                        _out = new OutImageMessage(fromUserName, toUserName, JSON.parseObject(content).getString("MediaId"));
                        break;
                    case VIDEO:
                        JSONObject _json = JSONObject.parseObject(content);
                        OutVideoMessage _videoMsg = new OutVideoMessage(fromUserName, toUserName, _json.getString("MediaId"));
                        if (_json.containsKey("Title")) {
                            _videoMsg.setTitle(_json.getString("Title"));
                        }
                        if (_json.containsKey("Description")) {
                            _videoMsg.setDescription(_json.getString("Description"));
                        }
                        _out = _videoMsg;
                        break;
                    case VOICE:
                        _out = new OutVoiceMessage(fromUserName, toUserName, JSON.parseObject(content).getString("MediaId"));
                        break;
                    case NEWS:
                        List<WxArticle> _articles = JSONArray.parseArray(JSONObject.parseObject(content).getJSONArray("Articles").toJSONString(), WxArticle.class);
                        _out = new OutNewsMessage(fromUserName, toUserName, _articles);
                        break;
                }
                return _out;
            } catch (IllegalArgumentException e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }
    }

    public static class News implements Serializable {

        @JSONField(name = "list")
        private List<NewsItem> items;

        public List<NewsItem> getItems() {
            return items;
        }

        public void setItems(List<NewsItem> items) {
            this.items = items;
        }
    }

    public static class NewsItem implements Serializable {

        private String title;

        private String digest;

        private String author;

        @JSONField(name = "show_cover")
        private boolean showCover;

        @JSONField(name = "cover_url")
        private String coverUrl;

        @JSONField(name = "content_url")
        private String contentUrl;

        @JSONField(name = "source_url")
        private String sourceUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public boolean isShowCover() {
            return showCover;
        }

        public void setShowCover(boolean showCover) {
            this.showCover = showCover;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }
    }

    // ------

    public static class Builder {

        private WxAutoreply __target;

        public static Builder create() {
            return new Builder(null);
        }

        public static Builder create(WxAutoreply target) {
            return new Builder(target);
        }

        public Builder(WxAutoreply target) {
            if (target != null) {
                __target = target;
            } else {
                __target = new WxAutoreply(new JSONObject());
            }
        }

        public Builder autoreplyOpen(boolean open) {
            __target.setAutoreplyOpen(open);
            return this;
        }

        public Builder subscribeReplyOpen(boolean open) {
            __target.setSubscribeReplyOpen(open);
            return this;
        }

        public Builder subscribeAutoreplyInfo(Content content) {
            __target.setSubscribeAutoreplyInfo(content);
            return this;
        }

        public Builder defaultAutoreplyInfo(Content content) {
            __target.setDefaultAutoreplyInfo(content);
            return this;
        }

        public Builder keywordAutoreplyInfo(KeywordAutoreply info) {
            __target.setKeywordAutoreplyInfo(info);
            return this;
        }

        public Builder id(String id) {
            __target.setId(id);
            return this;
        }

        public Builder lastModifyTime(long lastModifyTime) {
            __target.setLastModifyTime(lastModifyTime);
            return this;
        }

        public WxAutoreply build() {
            return __target;
        }
    }

    public static class ContentBuilder {

        private Content __target;

        public static ContentBuilder create() {
            return new ContentBuilder(null);
        }

        public static ContentBuilder create(Content target) {
            return new ContentBuilder(target);
        }

        private ContentBuilder(Content target) {
            if (target == null) {
                __target = new Content();
            } else {
                __target = target;
            }
        }

        public ContentBuilder bind(Content content) {
            __target = content;
            return this;
        }

        public Content build() {
            return __target;
        }

        public ContentBuilder bind(String type, String content) {
            __target.setType(IWechat.MessageType.valueOf(type.toUpperCase()).getType());
            __target.setContent(content);
            return this;
        }

        public ContentBuilder text(String content) {
            __target.setType("text");
            __target.setContent(content);
            return this;
        }

        public ContentBuilder image(String mediaId) {
            return image(mediaId, "");
        }

        public ContentBuilder image(String mediaId, String picUrl) {
            __target.setType("image");
            JSONObject _json = new JSONObject();
            _json.put("MediaId", mediaId);
            _json.put("Url", picUrl);
            __target.setContent(_json.toJSONString());
            return this;
        }

        public ContentBuilder voice(String mediaId) {
            return voice(mediaId, "");
        }

        public ContentBuilder voice(String mediaId, String resUrl) {
            __target.setType("voice");
            JSONObject _json = new JSONObject();
            _json.put("MediaId", mediaId);
            _json.put("Url", resUrl);
            __target.setContent(_json.toJSONString());
            return this;
        }

        public ContentBuilder video(String mediaId, String title, String description) {
            return video(mediaId, title, description, "");
        }

        public ContentBuilder video(String mediaId, String title, String description, String resUrl) {
            __target.setType("video");
            JSONObject _json = new JSONObject();
            _json.put("MediaId", mediaId);
            _json.put("Title", title);
            _json.put("Description", description);
            _json.put("Url", resUrl);
            __target.setContent(_json.toJSONString());
            return this;
        }

        public ContentBuilder news(String id, List<WxArticle> articles) {
            __target.setType("news");
            JSONObject _json = new JSONObject();
            _json.put("Id", id);
            _json.put("Url", articles.get(0).getPicUrl());
            _json.put("Articles", JSONArray.toJSONString(articles));
            __target.setContent(_json.toJSONString());
            return this;
        }
    }

    public static class KeywordAutoreplyItemBuilder {

        private KeywordAutoreplyItem __target;

        public static KeywordAutoreplyItemBuilder create() {
            return new KeywordAutoreplyItemBuilder(null);
        }

        public static KeywordAutoreplyItemBuilder create(KeywordAutoreplyItem target) {
            return new KeywordAutoreplyItemBuilder(target);
        }

        public KeywordAutoreplyItemBuilder(KeywordAutoreplyItem target) {
            if (target == null) {
                __target = new KeywordAutoreplyItem();
            } else {
                __target = target;
            }
        }

        public KeywordAutoreplyItem build() {
            return __target;
        }

        public KeywordAutoreplyItemBuilder id(String id) {
            __target.setId(id);
            return this;
        }

        public KeywordAutoreplyItemBuilder ruleName(String ruleName) {
            __target.setRuleName(ruleName);
            return this;
        }

        public KeywordAutoreplyItemBuilder replyMode(String replyMode) {
            __target.setReplyMode(replyMode);
            return this;
        }

        public KeywordAutoreplyItemBuilder createTime(long createTime) {
            __target.setCreateTime(createTime);
            return this;
        }

        public KeywordAutoreplyItemBuilder addKeywordInfo(Keyword info) {
            List<Keyword> _infos = __target.getKeywords();
            if (_infos == null) {
                _infos = new ArrayList<Keyword>();
                __target.setKeywords(_infos);
            }
            _infos.add(info);
            return this;
        }

        public KeywordAutoreplyItemBuilder addKeywordInfo(List<Keyword> infos) {
            List<Keyword> _infos = __target.getKeywords();
            if (_infos == null) {
                _infos = new ArrayList<Keyword>();
                __target.setKeywords(_infos);
            }
            _infos.addAll(infos);
            return this;
        }

        public KeywordAutoreplyItemBuilder addReplyInfo(Content info) {
            List<Content> _infos = __target.getReplyInfos();
            if (_infos == null) {
                _infos = new ArrayList<Content>();
                __target.setReplyInfos(_infos);
            }
            _infos.add(info);
            return this;
        }

        public KeywordAutoreplyItemBuilder addReplyInfo(List<Content> infos) {
            List<Content> _infos = __target.getReplyInfos();
            if (_infos == null) {
                _infos = new ArrayList<Content>();
                __target.setReplyInfos(_infos);
            }
            _infos.addAll(infos);
            return this;
        }
    }

    /**
     * @author 刘镇 (suninformation@163.com) on 16/7/19 下午1:19
     * @version 1.0
     */
    public static class AutoreplyMeta implements Serializable {

        private boolean autoreplyOpen;

        private boolean subscribeReplyOpen;

        private Content subscribeAutoreplyInfo;

        private Content defaultAutoreplyInfo;

        private List<AutoreplyRule> replyRules;

        public List<OutMessage> matchKeywords(String fromUserName, String toUserName, String keywords) throws Exception {
            List<OutMessage> _returnValues = new ArrayList<OutMessage>();
            if (replyRules != null && !replyRules.isEmpty()) {
                for (AutoreplyRule _meta : replyRules) {
                    for (WxAutoreply.Content _info : _meta.matchKeywords(keywords)) {
                        OutMessage _msg = _info.toOutMessage(fromUserName, toUserName);
                        if (_msg != null) {
                            _returnValues.add(_msg);
                        }
                    }
                }
            }
            return _returnValues;
        }

        public boolean isAutoreplyOpen() {
            return autoreplyOpen;
        }

        public void setAutoreplyOpen(boolean autoreplyOpen) {
            this.autoreplyOpen = autoreplyOpen;
        }

        public boolean isSubscribeReplyOpen() {
            return subscribeReplyOpen;
        }

        public void setSubscribeReplyOpen(boolean subscribeReplyOpen) {
            this.subscribeReplyOpen = subscribeReplyOpen;
        }

        public WxAutoreply.Content getSubscribeAutoreplyInfo() {
            return subscribeAutoreplyInfo;
        }

        public void setSubscribeAutoreplyInfo(WxAutoreply.Content subscribeAutoreplyInfo) {
            this.subscribeAutoreplyInfo = subscribeAutoreplyInfo;
        }

        public WxAutoreply.Content getDefaultAutoreplyInfo() {
            return defaultAutoreplyInfo;
        }

        public void setDefaultAutoreplyInfo(WxAutoreply.Content defaultAutoreplyInfo) {
            this.defaultAutoreplyInfo = defaultAutoreplyInfo;
        }

        public List<AutoreplyRule> getReplyRules() {
            return replyRules;
        }

        public void setReplyRules(List<AutoreplyRule> replyRules) {
            this.replyRules = replyRules;
        }
    }

    /**
     * 关键字自动回复规则
     */
    public static class AutoreplyRule implements Serializable {

        private boolean replyAll;

        private List<WxAutoreply.Keyword> keywords;

        private List<WxAutoreply.Content> replyInfos;

        public List<WxAutoreply.Content> matchKeywords(String keywords) throws Exception {
            List<WxAutoreply.Content> _returnValues = new ArrayList<WxAutoreply.Content>();
            for (WxAutoreply.Keyword _info : this.keywords) {
                String _content = StringUtils.trimToEmpty(_info.getContent());
                boolean _flag = StringUtils.equalsIgnoreCase(_content, keywords);
                if (!_flag && "contain".equalsIgnoreCase(_info.getMatchMode())) {
                    _flag = StringUtils.containsIgnoreCase(_content, keywords);
                }
                if (_flag) {
                    if (replyAll) {
                        _returnValues.addAll(replyInfos);
                    } else if (!replyInfos.isEmpty()) {
                        if (replyInfos.size() > 1) {
                            _returnValues.add(replyInfos.get(UUIDUtils.randomInt(0, replyInfos.size() - 1)));
                        } else {
                            _returnValues.add(replyInfos.get(0));
                        }
                    }
                }
            }
            return _returnValues;
        }

        public boolean isReplyAll() {
            return replyAll;
        }

        public void setReplyAll(boolean replyAll) {
            this.replyAll = replyAll;
        }

        public List<WxAutoreply.Keyword> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<WxAutoreply.Keyword> keywords) {
            this.keywords = keywords;
        }

        public List<WxAutoreply.Content> getReplyInfos() {
            return replyInfos;
        }

        public void setReplyInfos(List<WxAutoreply.Content> replyInfos) {
            this.replyInfos = replyInfos;
        }
    }
}
