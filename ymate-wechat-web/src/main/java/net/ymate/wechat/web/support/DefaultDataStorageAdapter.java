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
package net.ymate.wechat.web.support;

import com.alibaba.fastjson.JSON;
import net.ymate.framework.webmvc.support.UserSessionBean;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.persistence.Fields;
import net.ymate.platform.persistence.jdbc.query.IDBLocker;
import net.ymate.platform.persistence.jdbc.transaction.ITrade;
import net.ymate.platform.persistence.jdbc.transaction.Transactions;
import net.ymate.wechat.IDataStorageAdapter;
import net.ymate.wechat.IWechat;
import net.ymate.wechat.WechatUserStatus;
import net.ymate.wechat.base.*;
import net.ymate.wechat.model.WechatAccountAttribute;
import net.ymate.wechat.model.WechatUser;
import net.ymate.wechat.model.WechatUserAttribute;
import net.ymate.wechat.web.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/11 下午9:50
 * @version 1.0
 */
public class DefaultDataStorageAdapter implements IDataStorageAdapter {

    private static final Log _LOG = LogFactory.getLog(DefaultDataStorageAdapter.class);

    private IWechat __owner;

    @Override
    public void init(IWechat owner) throws Exception {
        __owner = owner;
    }

    @Override
    public void destroy() throws Exception {
    }

    public IWechat getOwner() {
        return __owner;
    }

    @Override
    public WxAccessToken loadAccessToken(String accountId) {
        try {
            WechatAccountAttribute _attr = WechatAccountAttribute.builder().id(DigestUtils.md5Hex(accountId + Constants.ATTR_ACCESS_TOKEN)).build().load();
            if (_attr != null && StringUtils.isNotBlank(_attr.getAttrValue())) {
                return new WxAccessToken.Result(JSON.parseObject(_attr.getAttrValue())).getAccessToken();
            }
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
        return null;
    }

    @Override
    public void saveOrUpdateAccessToken(String accountId, WxAccessToken.Result accessToken) {
        try {
            String _id = DigestUtils.md5Hex(accountId + Constants.ATTR_ACCESS_TOKEN);
            WechatAccountAttribute _attr = WechatAccountAttribute.builder().id(_id).build().load();
            if (_attr != null) {
                _attr.setAttrValue(accessToken.getOriginalResult().toJSONString());
                _attr.update(Fields.create(WechatUserAttribute.FIELDS.ATTR_VALUE));
            } else {
                WechatAccountAttribute.builder()
                        .id(_id)
                        .accountId(accountId)
                        .attrKey(Constants.ATTR_ACCESS_TOKEN)
                        .attrValue(accessToken.getOriginalResult().toJSONString())
                        .owner(IWechat.MODULE_NAME)
                        .build().save();
            }
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
    }

    @Override
    public WxJsApiTicket loadJsApiTicket(String accountId) {
        try {
            WechatAccountAttribute _attr = WechatAccountAttribute.builder().id(DigestUtils.md5Hex(accountId + Constants.ATTR_JSAPI_TICKET)).build().load();
            if (_attr != null && StringUtils.isNotBlank(_attr.getAttrValue())) {
                return new WxJsApiTicket.Result(JSON.parseObject(_attr.getAttrValue())).getTicket();
            }
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
        return null;
    }

    @Override
    public void saveOrUpdateJsApiTicket(String accountId, WxJsApiTicket.Result jsApiTicket) {
        try {
            String _id = DigestUtils.md5Hex(accountId + Constants.ATTR_JSAPI_TICKET);
            WechatAccountAttribute _attr = WechatAccountAttribute.builder().id(_id).build().load();
            if (_attr != null) {
                _attr.setAttrValue(jsApiTicket.getOriginalResult().toJSONString());
                _attr.update(Fields.create(WechatUserAttribute.FIELDS.ATTR_VALUE));
            } else {
                WechatAccountAttribute.builder()
                        .id(_id)
                        .accountId(accountId)
                        .attrKey(Constants.ATTR_JSAPI_TICKET)
                        .attrValue(jsApiTicket.getOriginalResult().toJSONString())
                        .owner(IWechat.MODULE_NAME)
                        .build().save();
            }
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
    }

    @Override
    public WechatUserStatus currentUserStatus(String accountId) {
        WechatUserStatus _userStatus = UserSessionBean.current(WechatUserStatus.class.getName());
        if (_userStatus != null && StringUtils.equals(_userStatus.accountId(), accountId)) {
            return _userStatus;
        }
        return _userStatus;
    }

    @Override
    public WechatUserStatus loadUserStatus(String accountId, String wxUid) {
        try {
            WechatUserAttribute _attr = WechatUserAttribute.builder().id(DigestUtils.md5Hex(accountId + wxUid + Constants.ATTR_ACCESS_TOKEN)).build().load();
            if (_attr != null && StringUtils.isNotBlank(_attr.getAttrValue())) {
                return ClassUtils.wrapper(WechatUserStatus.class).fromMap(JSON.parseObject(_attr.getAttrValue())).getTargetObject();
            }
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
        return null;
    }

    @Override
    public void saveOrUpdateUserStatus(final WechatUserStatus userStatus) {
        UserSessionBean.createIfNeed().addAttribute(WechatUserStatus.class.getName(), userStatus).saveIfNeed();
        //
        try {
            Transactions.execute(new ITrade() {
                @Override
                public void deal() throws Throwable {
                    String _attrId = DigestUtils.md5Hex(userStatus.accountId() + userStatus.id() + Constants.ATTR_ACCESS_TOKEN);
                    String _attrValue = JSON.toJSONString(ClassUtils.wrapper(userStatus).toMap());
                    //
                    WechatUserAttribute _attr = WechatUserAttribute.builder().id(_attrId).build().load(IDBLocker.DEFAULT);
                    if (_attr != null) {
                        _attr.setAttrValue(_attrValue);
                        _attr.update(Fields.create(WechatUserAttribute.FIELDS.ATTR_VALUE));
                    } else {
                        WechatUserAttribute.builder()
                                .id(_attrId)
                                .wxId(userStatus.id())
                                .attrKey(Constants.ATTR_ACCESS_TOKEN)
                                .attrValue(_attrValue)
                                .owner(IWechat.MODULE_NAME)
                                .build().save();
                    }
                }
            });
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
    }

    @Override
    public String buildWxUid(String accountId, String openId) {
        return DigestUtils.md5Hex(accountId + openId);
    }

    @Override
    public void saveOrUpdateUserInfo(final String accountId, final WxSnsUser snsUser, final WxSnsAccessToken snsAccessToken) {
        try {
            Transactions.execute(new ITrade() {
                @Override
                public void deal() throws Throwable {
                    String _wxUid = buildWxUid(accountId, snsUser.getOpenId());
                    WechatUser _wxUser = WechatUser.builder().id(_wxUid).build().load(Fields.create(WechatUser.FIELDS.ID), IDBLocker.DEFAULT);
                    long _currentTime = System.currentTimeMillis();
                    boolean _isNewUser = false;
                    Fields _fields = Fields.create();
                    if (_wxUser == null) {
                        _wxUser = WechatUser.builder().id(_wxUid).openId(snsUser.getOpenId()).accountId(accountId).createTime(_currentTime).build();
                        _fields.add(Fields.create(WechatUser.FIELDS.ID, WechatUser.FIELDS.OPEN_ID, WechatUser.FIELDS.ACCOUNT_ID, WechatUser.FIELDS.CREATE_TIME));
                        _isNewUser = true;
                    }
                    _wxUser.setNickName(snsUser.getNickname());
                    _wxUser.setHeadImgUrl(snsUser.getHeadImgUrl());
                    _wxUser.setGender(snsUser.getSex());
                    _wxUser.setCountry(snsUser.getCountry());
                    _wxUser.setProvince(snsUser.getProvince());
                    _wxUser.setCity(snsUser.getCity());
                    _wxUser.setUnionId(snsUser.getUnionId());
                    _wxUser.setLastModifyTime(_currentTime);
                    //
                    _fields.add(Fields.create(WechatUser.FIELDS.NICK_NAME,
                            WechatUser.FIELDS.HEAD_IMG_URL,
                            WechatUser.FIELDS.GENDER,
                            WechatUser.FIELDS.COUNTRY,
                            WechatUser.FIELDS.PROVINCE,
                            WechatUser.FIELDS.CITY,
                            WechatUser.FIELDS.UNION_ID,
                            WechatUser.FIELDS.LANGUAGE,
                            WechatUser.FIELDS.REMARK,
                            WechatUser.FIELDS.LAST_MODIFY_TIME));
                    //
                    if (_isNewUser) {
                        _wxUser.save(_fields);
                    } else {
                        _wxUser.update(_fields);
                    }
                    //
                    if (snsAccessToken != null) {
                        String _attrId = DigestUtils.md5Hex(accountId + _wxUid + Constants.ATTR_ACCESS_TOKEN);
                        String _attrValue = JSON.toJSONString(ClassUtils.wrapper(WechatUserStatus.create(_wxUid, accountId, snsAccessToken)).toMap());
                        //
                        WechatUserAttribute _attr = WechatUserAttribute.builder().id(_attrId).build().load(IDBLocker.DEFAULT);
                        if (_attr != null) {
                            _attr.setAttrValue(_attrValue);
                            _attr.update(Fields.create(WechatUserAttribute.FIELDS.ATTR_VALUE));
                        } else {
                            WechatUserAttribute.builder()
                                    .id(_attrId)
                                    .wxId(_wxUid)
                                    .attrKey(Constants.ATTR_ACCESS_TOKEN)
                                    .attrValue(_attrValue)
                                    .owner(IWechat.MODULE_NAME)
                                    .build().save();
                        }
                    }
                }
            });
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
    }

    @Override
    public void saveOrUpdateUserInfo(final String accountId, final WxUser user) {
        try {
            Transactions.execute(new ITrade() {
                @Override
                public void deal() throws Throwable {
                    String _wxUid = buildWxUid(accountId, user.getOpenId());
                    WechatUser _wxUser = WechatUser.builder().id(_wxUid).build().load(Fields.create(WechatUser.FIELDS.ID, WechatUser.FIELDS.IS_SUBSCRIBE, WechatUser.FIELDS.SUBSCRIBE_TIME), IDBLocker.DEFAULT);
                    long _currentTime = System.currentTimeMillis();
                    boolean _isNewUser = false;
                    Fields _fields = Fields.create();
                    if (_wxUser == null) {
                        _wxUser = new WechatUser();
                        _wxUser.setId(_wxUid);
                        _wxUser.setOpenId(user.getOpenId());
                        _wxUser.setAccountId(accountId);
                        _wxUser.setCreateTime(_currentTime);
                        //
                        _fields.add(Fields.create(WechatUser.FIELDS.ID, WechatUser.FIELDS.OPEN_ID, WechatUser.FIELDS.ACCOUNT_ID, WechatUser.FIELDS.CREATE_TIME));
                        //
                        _isNewUser = true;
                    }
                    _wxUser.setNickName(user.getNickname());
                    _wxUser.setHeadImgUrl(user.getHeadImgUrl());
                    _wxUser.setGender(user.getSex());
                    _wxUser.setCountry(user.getCountry());
                    _wxUser.setProvince(user.getProvince());
                    _wxUser.setCity(user.getCity());
                    _wxUser.setUnionId(user.getUnionId());
                    _wxUser.setLanguage(user.getLanguage());
                    _wxUser.setIsSubscribe(user.getSubscribe());
                    if (_isNewUser || BlurObject.bind(_wxUser.getSubscribeTime()).toLongValue() == 0 && user.getSubscribe() == 1) {
                        _wxUser.setSubscribeTime(_currentTime);
                        _fields.add(WechatUser.FIELDS.SUBSCRIBE_TIME);
                    }
                    _wxUser.setRemark(user.getRemark());
                    _wxUser.setLastModifyTime(_currentTime);
                    //
                    _fields.add(Fields.create(WechatUser.FIELDS.NICK_NAME,
                            WechatUser.FIELDS.HEAD_IMG_URL,
                            WechatUser.FIELDS.GENDER,
                            WechatUser.FIELDS.COUNTRY,
                            WechatUser.FIELDS.PROVINCE,
                            WechatUser.FIELDS.CITY,
                            WechatUser.FIELDS.UNION_ID,
                            WechatUser.FIELDS.LANGUAGE,
                            WechatUser.FIELDS.IS_SUBSCRIBE,
                            WechatUser.FIELDS.REMARK,
                            WechatUser.FIELDS.LAST_MODIFY_TIME));
                    //
                    if (user.getTagidList() != null && user.getTagidList().length > 0) {
                        _wxUser.setTagIdList(StringUtils.join(user.getTagidList(), ","));
                        //
                        _fields.add(WechatUser.FIELDS.TAG_ID_LIST);
                    }
                    //
                    if (_isNewUser) {
                        _wxUser.save(_fields);
                    } else {
                        _wxUser.update(_fields);
                    }
                }
            });
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
    }
}
