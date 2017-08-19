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

import net.ymate.framework.commons.IFileHandler;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.base.WxMassArticle;
import net.ymate.wechat.base.WxMassVideo;
import net.ymate.wechat.base.WxMedia;
import org.apache.http.entity.mime.content.ContentBody;

import java.io.File;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/9 下午6:06
 * @version 1.0
 */
public interface IMediaApi {

    String MEDIA_GET = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=";

    String MEDIA_UPLOAD = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=";

    String MEDIA_UPLOAD_IMAGE = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=";

    String MEDIA_UPLOAD_NEWS = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=";

    String MEDIA_UPLOAD_VIDEO = "http://file.api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=";

    //

    void downloadFile(String mediaId, IFileHandler fileHandler);

    WxMedia uploadFile(IWechat.MediaType type, File file);

    WxMedia uploadFile(IWechat.MediaType type, ContentBody file);

    WxMedia uploadImage(File file);

    WxMedia uploadImage(ContentBody file);

    WxMedia uploadNews(List<WxMassArticle> articles);

    WxMedia uploadVideo(WxMassVideo video);
}
