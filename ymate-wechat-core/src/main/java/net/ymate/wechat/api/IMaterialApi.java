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
import net.ymate.wechat.base.WxMaterial;
import net.ymate.wechat.base.WxMedia;
import net.ymate.wechat.base.WxResult;
import org.apache.http.entity.mime.content.ContentBody;

import java.io.File;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/9 下午6:08
 * @version 1.0
 */
public interface IMaterialApi {

    String MATERIAL_ADD_NEWS = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=";

    String MATERIAL_ADD_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=";

    String MATERIAL_GET_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=";

    String MATERIAL_DEL_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=";

    String MATERIAL_UPDATE_NEWS = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=";

    String MATERIAL_GET_COUNT = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=";

    String MATERIAL_BATCH_GET = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=";

    //

    WxMedia addNews(List<WxMassArticle> articles);

    WxMedia addNews(IWechat.MediaType type, File file);

    WxMedia addNews(IWechat.MediaType type, ContentBody file);

    WxMedia addVideo(String title, String introduction, File file);

    WxMedia addVideo(String title, String introduction, ContentBody file);

    WxResult delete(String mediaId);

    WxMaterial batchGet(IWechat.MediaType type, int offset, int count);

    WxMaterial.Detail newsAndVideoGet(String mediaId);

    void download(String mediaId, IFileHandler fileHandler);

    WxResult updateNews(String mediaId, int index, WxMassArticle article);

    WxMaterial.Count count();
}
