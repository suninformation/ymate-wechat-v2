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
package net.ymate.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.framework.commons.IFileHandler;
import net.ymate.framework.commons.IHttpResponse;
import net.ymate.framework.commons.ParamUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.wechat.api.*;
import net.ymate.wechat.base.*;
import net.ymate.wechat.message.OutMessage;
import net.ymate.wechat.message.TemplateMessage;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/9 下午9:12
 * @version 1.0
 */
public class WechatApiCaller {

    private static final Log _LOG = LogFactory.getLog(WechatApiCaller.class);

    private WxAccount __account;

    private ICommonApi __common;

    private IMaterialApi __material;

    private IMediaApi __media;

    private IMenusApi __menus;

    private IMessageApi __message;

    private IOauthApi __oauth;

    private ITagsApi __tags;

    private IUserApi __user;

    public static WechatApiCaller create(String accountId) {
        return new WechatApiCaller(accountId);
    }

    public static WechatApiCaller create(WxAccount account) {
        return new WechatApiCaller(account);
    }

    private WechatApiCaller(WxAccount account) {
        __init(account);
    }

    private WechatApiCaller(String accountId) {
        WxAccount _account = Wechat.get().getModuleCfg().getAccountProvider().getAccount(accountId);
        if (_account != null) {
            __init(_account);
        }
    }

    private void __init(WxAccount account) {
        if (account == null) {
            throw new NullArgumentException("account");
        }
        __account = account;
        //
        __common = new CommonApiImpl();
        __material = new MaterialApiImpl();
        __media = new MediaApiImpl();
        __menus = new MenusApiImpl();
        __message = new MessageApiImpl();
        __oauth = new OauthApiImpl();
        __tags = new TagsApiImpl();
        __user = new UserApiImpl();
    }

    private WxAccessToken __doGetAccessToken() {
        return Wechat.get().getModuleCfg().getTokenCacheAdapter().getAccessToken(__account);
    }

    private <T extends WxResult> T __doCheckAccessTokenResetIfNeed(T result, WxAccessToken accessToken) {
        if (result.getErrCode() == 40001 || result.getErrCode() == 40014 || result.getErrCode() == 42001 || result.getErrCode() == 42002) {
            Wechat.get().getModuleCfg().getTokenCacheAdapter().resetAccessToken(__account, accessToken);
        }
        return result;
    }

    private boolean __doCheckHttpResponseStatus(IHttpResponse response) {
        if (response != null && response.getStatusCode() == 200) {
            return true;
        } else if (response != null) {
            _LOG.warn(response.toString());
        }
        return false;
    }

    public WxAccount getAccount() {
        return __account;
    }

    public ICommonApi common() {
        return __common;
    }

    public IMaterialApi material() {
        return __material;
    }

    public IMediaApi media() {
        return __media;
    }

    public IMenusApi menus() {
        return __menus;
    }

    public IMessageApi message() {
        return __message;
    }

    public IOauthApi oauth() {
        return __oauth;
    }

    public ITagsApi tags() {
        return __tags;
    }

    public IUserApi user() {
        return __user;
    }

    // ----------

    class CommonApiImpl implements ICommonApi {

        @Override
        public String[] callbackIp() {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().get(WX_GET_CALLBACK_IP.concat(_accessToken.getToken()));
                if (__doCheckHttpResponseStatus(_response)) {
                    WxResult _result = __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                    if (_result.isOK()) {
                        JSONArray _array = _result.getOriginalResult().getJSONArray("ip_list");
                        if (_array != null && !_array.isEmpty()) {
                            return _array.toArray(new String[_array.size()]);
                        }
                    }
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxQRCode qrCodeCreate(int sceneId, int expireSeconds) {
            JSONObject _paramJSON = new JSONObject();
            if (expireSeconds >= 0) {
                _paramJSON.put("action_name", "QR_SCENE");
                _paramJSON.put("expire_seconds", expireSeconds);
            } else {
                _paramJSON.put("action_name", "QR_LIMIT_SCENE");
            }
            JSONObject _sceneJSON = new JSONObject();
            _sceneJSON.put("scene_id", sceneId);
            JSONObject _infoJSON = new JSONObject();
            _infoJSON.put("scene", _sceneJSON);
            _paramJSON.put("action_info", _infoJSON);
            //
            return __doQrCodeCreate(_paramJSON);
        }

        @Override
        public WxQRCode qrCodeCreate(int sceneId) {
            return qrCodeCreate(sceneId, -1);
        }

        @Override
        public WxQRCode qrCodeCreate(String sceneStr, int expireSeconds) {
            JSONObject _paramJSON = new JSONObject();
            if (expireSeconds >= 0) {
                _paramJSON.put("action_name", "QR_STR_SCENE");
                _paramJSON.put("expire_seconds", expireSeconds);
            } else {
                _paramJSON.put("action_name", "QR_LIMIT_STR_SCENE");
            }
            JSONObject _sceneJSON = new JSONObject();
            _sceneJSON.put("scene_str", sceneStr);
            JSONObject _infoJSON = new JSONObject();
            _infoJSON.put("scene", _sceneJSON);
            _paramJSON.put("action_info", _infoJSON);
            //
            return __doQrCodeCreate(_paramJSON);
        }

        private WxQRCode __doQrCodeCreate(JSONObject paramJSON) {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(QRCODE_CREATE.concat(_accessToken.getToken()), paramJSON.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxQRCode(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxQRCode qrCodeCreate(String sceneStr) {
            return qrCodeCreate(sceneStr, -1);
        }

        @Override
        public String qrCodeShow(String ticket) {
            try {
                return QRCODE_SHOW.concat(URLEncoder.encode(ticket, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxShortUrl shortUrl(String longUrl) {
            JSONObject _paramJSON = new JSONObject();
            _paramJSON.put("action", "long2short");
            _paramJSON.put("long_url", longUrl);
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(SHORT_URL.concat(_accessToken.getToken()), _paramJSON.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxShortUrl(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxAutoreply autoreplyGetInfo() {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().get(AUTOREPLY_GET_INFO.concat(_accessToken.getToken()));
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxAutoreply(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }
    }

    class MaterialApiImpl implements IMaterialApi {

        @Override
        public WxMedia addNews(List<WxMassArticle> articles) {
            if (articles == null || articles.isEmpty()) {
                throw new NullArgumentException("articles");
            }
            JSONObject _json = new JSONObject();
            JSONArray _articleJSON = new JSONArray();
            for (WxMassArticle article : articles) {
                _articleJSON.add(article.toJSON());
            }
            _json.put("articles", _articleJSON);
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MATERIAL_ADD_NEWS.concat(_accessToken.getToken()), _json.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMedia(IWechat.MediaType.NEWS, JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMedia addNews(IWechat.MediaType type, File file) {
            return addNews(type, new FileBody(file));
        }

        @Override
        public WxMedia addNews(IWechat.MediaType type, ContentBody file) {
            if (IWechat.MediaType.NEWS.equals(type)) {
                throw new UnsupportedOperationException("News media need use uploadNews method.");
            } else if (IWechat.MediaType.VIDEO.equals(type) || IWechat.MediaType.SHORT_VIDEO.equals(type)) {
                throw new UnsupportedOperationException("Unsupported Video and Short Video");
            }
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().upload(MATERIAL_ADD_MATERIAL.concat(_accessToken.getToken()).concat("&type=").concat(type.toString().toLowerCase()), "media", file, null);
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMedia(IWechat.MediaType.NEWS, JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMedia addVideo(String title, String introduction, File file) {
            return addVideo(title, introduction, new FileBody(file));
        }

        @Override
        public WxMedia addVideo(String title, String introduction, ContentBody file) {
            try {
                String _description = URLEncoder.encode("{\"title\":\"" + StringUtils.trimToEmpty(title) + "\", \"introduction\":\"" + StringUtils.trimToEmpty(introduction) + "\"}", "UTF-8");
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().upload(MATERIAL_ADD_MATERIAL + _accessToken.getToken() + "&description=" + _description + "&type=video", "media", file, null);
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMedia(IWechat.MediaType.VIDEO, JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxResult delete(String mediaId) {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MATERIAL_DEL_MATERIAL.concat(_accessToken.getToken()), "{\"media_id\":\"" + mediaId + "\"}");
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMaterial batchGet(IWechat.MediaType type, int offset, int count) {
            try {
                String _params = "{\"type\":\"" + type.toString().toLowerCase() + "\", \"offset\":" + offset + ", \"count\":" + count + "}";
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MATERIAL_BATCH_GET.concat(_accessToken.getToken()), _params);
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMaterial(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMaterial.Detail newsAndVideoGet(String mediaId) {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MATERIAL_GET_MATERIAL.concat(_accessToken.getToken()), "{\"media_id\":\"" + mediaId + "\"}");
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMaterial.Detail(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public void download(String mediaId, IFileHandler fileHandler) {
            try {
                HttpClientHelper.create().download(MATERIAL_GET_MATERIAL.concat(__doGetAccessToken().getToken()), "{\"media_id\":\"" + mediaId + "\"}", fileHandler);
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
        }

        @Override
        public WxResult updateNews(String mediaId, int index, WxMassArticle article) {
            JSONObject _params = new JSONObject();
            _params.put("media_id", mediaId);
            _params.put("index", index);
            _params.put("articles", article.toJSON());
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MATERIAL_UPDATE_NEWS.concat(_accessToken.getToken()), _params.toJSONString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMaterial.Count count() {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MATERIAL_GET_COUNT.concat(_accessToken.getToken()), "");
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMaterial.Count(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }
    }

    class MediaApiImpl implements IMediaApi {

        @Override
        public void downloadFile(String mediaId, IFileHandler fileHandler) {
            try {
                HttpClientHelper.create().download(MEDIA_GET.concat(__doGetAccessToken().getToken()).concat("&media_id=").concat(mediaId), fileHandler);
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
        }

        @Override
        public WxMedia uploadFile(IWechat.MediaType type, File file) {
            return uploadFile(type, new FileBody(file));
        }

        @Override
        public WxMedia uploadFile(IWechat.MediaType type, ContentBody file) {
            if (IWechat.MediaType.NEWS.equals(type) || IWechat.MediaType.SHORT_VIDEO.equals(type)) {
                throw new UnsupportedOperationException("Unsupported News and Short Video");
            }
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().upload(MEDIA_UPLOAD.concat(_accessToken.getToken()).concat("&type=").concat(type.toString().toLowerCase()), "media", file, null);
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMedia(type, JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMedia uploadImage(File file) {
            return uploadImage(new FileBody(file));
        }

        @Override
        public WxMedia uploadImage(ContentBody file) {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().upload(MEDIA_UPLOAD_IMAGE.concat(_accessToken.getToken()), "media", file, null);
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMedia(IWechat.MediaType.IMAGE, JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMedia uploadNews(List<WxMassArticle> articles) {
            if (articles == null || articles.isEmpty()) {
                throw new NullArgumentException("articles");
            }
            JSONObject _json = new JSONObject();
            JSONArray _articleJSON = new JSONArray();
            for (WxMassArticle article : articles) {
                _articleJSON.add(article.toJSON());
            }
            _json.put("articles", _articleJSON);
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MEDIA_UPLOAD_NEWS.concat(_accessToken.getToken()), _json.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMedia(IWechat.MediaType.NEWS, JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMedia uploadVideo(WxMassVideo video) {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MEDIA_UPLOAD_VIDEO.concat(_accessToken.getToken()), video.toJSON());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMedia(IWechat.MediaType.VIDEO, JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }
    }

    class MenusApiImpl implements IMenusApi {

        @Override
        public WxResult menuCreate(WxMenu menu) {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MENU_CREATE.concat(_accessToken.getToken()), JSON.toJSONString(menu));
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMenu menuGet() {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().get(MENU_GET.concat(_accessToken.getToken()));
                if (__doCheckHttpResponseStatus(_response)) {
                    WxResult _result = __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                    if (_result.isOK()) {
                        return JSON.toJavaObject(_result.getOriginalResult().getJSONObject("menu"), WxMenu.class);
                    }
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxResult menuDelete() {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().get(MENU_DELETE.concat(_accessToken.getToken()));
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }
    }

    class MessageApiImpl implements IMessageApi {

        @Override
        public WxResult customSend(OutMessage message) {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MESSAGE_CUSTOM_SEND.concat(_accessToken.getToken()), message.toJSON().toJSONString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMsgSend templateSend(TemplateMessage message) {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MESSAGE_TEMPLATE_SEND.concat(_accessToken.getToken()), message.toJSON().toJSONString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMsgSend(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        private WxMsgSend __doMassSend(JSONObject json, String toURL, IWechat.MessageType msgType, String mediaIdOrContent) {
            String _msgType = "mpnews";
            String _bodyAttr = "media_id";
            //
            switch (msgType) {
                case NEWS:
                    break;
                case TEXT:
                    _msgType = "text";
                    _bodyAttr = "content";
                    break;
                case VOICE:
                    _msgType = "voice";
                    break;
                case IMAGE:
                    _msgType = "image";
                    break;
                case VIDEO:
                    _msgType = "mpvideo";
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported MessageType \"" + msgType + "\".");
            }
            //
            JSONObject _bodyJSON = new JSONObject();
            _bodyJSON.put(_bodyAttr, mediaIdOrContent);
            //
            json.put(_msgType, _bodyJSON);
            json.put("msgtype", _msgType);
            //
            try {
                IHttpResponse _response = HttpClientHelper.create().post(toURL, json.toJSONString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return new WxMsgSend(JSON.parseObject(_response.getContent()));
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMsgSend massSend(String clientmsgid, String tagId, boolean toAll, IWechat.MessageType msgType, String mediaIdOrContent, int sendIgnoreReprint) {
            JSONObject _json = new JSONObject();
            //
            JSONObject _filterJSON = new JSONObject();
            _filterJSON.put("is_to_all", toAll);
            _filterJSON.put("tag_id", tagId);
            _json.put("filter", _filterJSON);
            _json.put("send_ignore_reprint", sendIgnoreReprint);
            if (StringUtils.isNotBlank(clientmsgid)) {
                _json.put("clientmsgid", clientmsgid);
            }
            //
            WxAccessToken _accessToken = __doGetAccessToken();
            return __doCheckAccessTokenResetIfNeed(__doMassSend(_json, MASS_SEND_ALL.concat(_accessToken.getToken()), msgType, mediaIdOrContent), _accessToken);
        }

        @Override
        public WxMsgSend massSend(String tagId, boolean toAll, IWechat.MessageType msgType, String mediaIdOrContent) {
            return massSend(null, tagId, toAll, msgType, mediaIdOrContent, 0);
        }

        @Override
        public WxMsgSend massSend(String clientmsgid, List<String> openIds, IWechat.MessageType msgType, String mediaIdOrContent, int sendIgnoreReprint) {
            if (openIds == null || openIds.isEmpty()) {
                throw new NullArgumentException("openIds");
            }
            JSONObject _json = new JSONObject();
            _json.put("touser", openIds);
            _json.put("send_ignore_reprint", sendIgnoreReprint);
            if (StringUtils.isNotBlank(clientmsgid)) {
                _json.put("clientmsgid", clientmsgid);
            }
            //
            WxAccessToken _accessToken = __doGetAccessToken();
            return __doCheckAccessTokenResetIfNeed(__doMassSend(_json, MASS_SEND_BY_OPENID.concat(_accessToken.getToken()), msgType, mediaIdOrContent), _accessToken);
        }

        @Override
        public WxMsgSend massSend(List<String> openIds, IWechat.MessageType msgType, String mediaIdOrContent) {
            return massSend(null, openIds, msgType, mediaIdOrContent, 0);
        }

        @Override
        public WxMsgSend massPreview(String openId, IWechat.MessageType msgType, String mediaIdOrContent) {
            if (StringUtils.isBlank(openId)) {
                throw new NullArgumentException("openId");
            }
            JSONObject _json = new JSONObject();
            _json.put("touser", openId);
            //
            WxAccessToken _accessToken = __doGetAccessToken();
            return __doCheckAccessTokenResetIfNeed(__doMassSend(_json, MASS_PREVIEW.concat(_accessToken.getToken()), msgType, mediaIdOrContent), _accessToken);
        }

        @Override
        public WxResult massDelete(String msgId) {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MASS_DELETE.concat(_accessToken.getToken()), "{\"msg_id\":" + msgId + "}");
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxMsgSend massGet(String msgId) {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(MASS_GET.concat(_accessToken.getToken()), "{\"msg_id\":\"" + msgId + "\"}");
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxMsgSend(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }
    }

    class OauthApiImpl implements IOauthApi {

        @Override
        public String codeUrl(String scope, String state, String redirectUri) {
            if (StringUtils.isBlank(redirectUri)) {
                throw new NullArgumentException("redirectUri");
            }
            if (!StringUtils.equals(scope, IWechat.OAuthScope.SNSAPI_BASE) && !StringUtils.equals(scope, IWechat.OAuthScope.SNSAPI_USERINFO)) {
                throw new IllegalArgumentException("invalid scope");
            }
            try {
                Map<String, Object> _params = new HashMap<String, Object>();
                _params.put("appid", __account.getAppId());
                _params.put("response_type", "code");
                _params.put("redirect_uri", URLEncoder.encode(redirectUri, HttpClientHelper.DEFAULT_CHARSET));
                _params.put("scope", scope);
                _params.put("state", StringUtils.trimToEmpty(state) + "#wechat_redirect");
                return OAUTH_GET_CODE.concat(ParamUtils.buildQueryParamStr(_params, false, HttpClientHelper.DEFAULT_CHARSET));
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxSnsAccessToken accessToken(String code) {
            if (StringUtils.isBlank(code)) {
                throw new NullArgumentException("code");
            }
            Map<String, String> _params = new HashMap<String, String>();
            _params.put("appid", __account.getAppId());
            _params.put("secret", __account.getAppSecret());
            _params.put("code", code);
            _params.put("grant_type", "authorization_code");
            //
            return __doGetToken(OAUTH_ACCESS_TOKEN, _params);
        }

        private WxSnsAccessToken __doGetToken(String url, Map<String, String> params) {
            try {
                IHttpResponse _response = HttpClientHelper.create().get(url, params);
                if (__doCheckHttpResponseStatus(_response)) {
                    return new WxSnsAccessToken(JSON.parseObject(_response.getContent()));
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxSnsAccessToken refreshToken(String refreshToken) {
            if (StringUtils.isBlank(refreshToken)) {
                throw new NullArgumentException("refreshToken");
            }
            Map<String, String> _params = new HashMap<String, String>();
            _params.put("appid", __account.getAppId());
            _params.put("grant_type", "refresh_token");
            _params.put("refresh_token", refreshToken);
            //
            return __doGetToken(OAUTH_REFRESH_TOKEN, _params);
        }

        @Override
        public WxSnsUser userInfo(String oauthAccessToken, String openid) {
            return userInfo(oauthAccessToken, openid, null);
        }

        @Override
        public WxSnsUser userInfo(String oauthAccessToken, String openid, IWechat.LangType lang) {
            if (StringUtils.isBlank(oauthAccessToken)) {
                throw new NullArgumentException("oauthAccessToken");
            }
            if (StringUtils.isBlank(openid)) {
                throw new NullArgumentException("openid");
            }
            Map<String, String> _params = new HashMap<String, String>();
            _params.put("openid", openid);
            _params.put("lang", lang == null ? IWechat.LangType.zh_CN.toString() : lang.toString());
            try {
                IHttpResponse _response = HttpClientHelper.create().get(OAUTH_USER_INFO.concat(oauthAccessToken), _params);
                if (__doCheckHttpResponseStatus(_response)) {
                    return new WxSnsUser(JSON.parseObject(_response.getContent()));
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxResult authAccessToken(String oauthAccessToken, String openid) {
            if (StringUtils.isBlank(oauthAccessToken)) {
                throw new NullArgumentException("oauthAccessToken");
            }
            if (StringUtils.isBlank(openid)) {
                throw new NullArgumentException("openid");
            }
            Map<String, String> _params = new HashMap<String, String>();
            _params.put("openid", openid);
            try {
                IHttpResponse _response = HttpClientHelper.create().get(OAUTH_AUTH_ACCESS_TOKEN.concat(oauthAccessToken), _params);
                if (__doCheckHttpResponseStatus(_response)) {
                    return new WxResult(JSON.parseObject(_response.getContent()));
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }
    }

    class TagsApiImpl implements ITagsApi {

        @Override
        public WxTag createTag(String tagName) {
            if (StringUtils.isBlank(tagName)) {
                throw new NullArgumentException("tagName");
            }
            JSONObject _paramJSON = new JSONObject();
            JSONObject _item = new JSONObject();
            _item.put("name", tagName);
            _paramJSON.put("tag", _item);
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(TAGS_CREATE.concat(_accessToken.getToken()), _paramJSON.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxTag(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxTag[] tagsList() {
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().get(TAGS_GET.concat(_accessToken.getToken()));
                if (__doCheckHttpResponseStatus(_response)) {
                    WxResult _result = __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                    if (_result.getErrCode() == 0) {
                        List<WxTag> _results = new ArrayList<WxTag>();
                        JSONArray _tags = _result.getOriginalResult().getJSONArray("tags");
                        for (Object _item : _tags) {
                            JSONObject _tag = (JSONObject) _item;
                            _results.add(new WxTag(_tag.getIntValue("id"), _tag.getString("name"), _tag.getLongValue("count")));
                        }
                        if (!_results.isEmpty()) {
                            return _results.toArray(new WxTag[0]);
                        }
                    }
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxResult updateTag(int tagId, String tagName) {
            if (StringUtils.isBlank(tagName)) {
                throw new NullArgumentException("tagName");
            }
            JSONObject _paramJSON = new JSONObject();
            JSONObject _item = new JSONObject();
            _item.put("id", tagId);
            _item.put("name", tagName);
            _paramJSON.put("tag", _item);
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(TAGS_UPDATE.concat(_accessToken.getToken()), _paramJSON.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxResult deleteTag(int tagId) {
            JSONObject _paramJSON = new JSONObject();
            JSONObject _item = new JSONObject();
            _item.put("id", tagId);
            _paramJSON.put("tag", _item);
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(TAGS_DELETE.concat(_accessToken.getToken()), _paramJSON.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxUserList tagsUserList(int tagId, String nextOpenId) {
            JSONObject _paramJSON = new JSONObject();
            _paramJSON.put("tagid", tagId);
            if (StringUtils.isNotBlank(nextOpenId)) {
                _paramJSON.put("next_openid", nextOpenId);
            }
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(TAGS_GET_USER.concat(_accessToken.getToken()), _paramJSON.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxUserList(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxResult updateUserTag(int tagId, List<String> openIds) {
            return __doTagsBatchOpt(tagId, openIds, TAGS_UPDATE_USER);
        }

        private WxResult __doTagsBatchOpt(int tagId, List<String> openIds, String apiUrl) {
            if (openIds == null || openIds.isEmpty()) {
                throw new NullArgumentException("openIds");
            }
            JSONObject _paramJSON = new JSONObject();
            _paramJSON.put("tagid", tagId);
            JSONArray _item = new JSONArray();
            _item.addAll(openIds);
            _paramJSON.put("openid_list", _item);
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(apiUrl.concat(_accessToken.getToken()), _paramJSON.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxResult deleteUserTag(int tagId, List<String> openIds) {
            return __doTagsBatchOpt(tagId, openIds, TAGS_DELETE_USER);
        }

        @Override
        public WxUserTags userTags(String openId) {
            if (StringUtils.isBlank(openId)) {
                throw new NullArgumentException("openId");
            }
            JSONObject _openidJSON = new JSONObject();
            _openidJSON.put("openid", openId);
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(TAGS_GET_ID.concat(_accessToken.getToken()), _openidJSON.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxUserTags(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }
    }

    class UserApiImpl implements IUserApi {

        @Override
        public WxUserList userList(String nextOpenId) {
            WxAccessToken _accessToken = __doGetAccessToken();
            //
            Map<String, String> params = new HashMap<String, String>();
            params.put("access_token", _accessToken.getToken());
            if (StringUtils.isNotBlank(nextOpenId)) {
                params.put("next_openid", nextOpenId);
            }
            try {
                IHttpResponse _response = HttpClientHelper.create().get(USER_GET, params);
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxUserList(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public WxUser userInfo(String openId) {
            return userInfo(openId, null);
        }

        @Override
        public WxUser userInfo(String openId, IWechat.LangType lang) {
            if (StringUtils.isBlank(openId)) {
                throw new NullArgumentException("openId");
            }
            WxAccessToken _accessToken = __doGetAccessToken();
            //
            Map<String, String> _params = new HashMap<String, String>();
            _params.put("access_token", _accessToken.getToken());
            _params.put("openid", openId);
            _params.put("lang", lang == null ? IWechat.LangType.zh_CN.toString() : lang.toString());
            try {
                IHttpResponse _response = HttpClientHelper.create().get(USER_INFO, _params);
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxUser(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }

        @Override
        public List<WxUser> userInfoBatch(List<String> openIds) {
            return userInfoBatch(openIds, null);
        }

        @Override
        public List<WxUser> userInfoBatch(List<String> openIds, IWechat.LangType lang) {
            if (openIds == null || openIds.isEmpty()) {
                throw new NullArgumentException("openIds");
            }
            String _lang = (lang == null ? IWechat.LangType.zh_CN.toString() : lang.toString());
            JSONArray _userList = new JSONArray();
            for (String openid : openIds) {
                JSONObject _item = new JSONObject();
                _item.put("openid", openid);
                _item.put("lang", _lang);
                _userList.add(_item);
            }
            JSONObject _paramJSON = new JSONObject();
            _paramJSON.put("user_list", _userList);
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(USER_INFO_BATCH.concat(_accessToken.getToken()), _paramJSON.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    WxResult _result = __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                    if (_result.isOK()) {
                        JSONArray _jsonArr = _result.getOriginalResult().getJSONArray("user_info_list");
                        if (_jsonArr != null && !_jsonArr.isEmpty()) {
                            List<WxUser> _users = new ArrayList<WxUser>();
                            for (Object _item : _jsonArr) {
                                _users.add(new WxUser((JSONObject) _item));
                            }
                            return _users;
                        }
                    }
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return Collections.emptyList();
        }

        @Override
        public WxResult updateRemark(String openId, String remark) {
            if (StringUtils.isBlank(openId)) {
                throw new NullArgumentException("openId");
            }
            JSONObject _paramJSON = new JSONObject();
            _paramJSON.put("openid", openId);
            _paramJSON.put("remark", StringUtils.trimToEmpty(remark));
            try {
                WxAccessToken _accessToken = __doGetAccessToken();
                IHttpResponse _response = HttpClientHelper.create().post(USER_UPDATE_REMARK.concat(_accessToken.getToken()), _paramJSON.toString());
                if (__doCheckHttpResponseStatus(_response)) {
                    return __doCheckAccessTokenResetIfNeed(new WxResult(JSON.parseObject(_response.getContent())), _accessToken);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
            return null;
        }
    }
}
