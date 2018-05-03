package net.ymate.wechat.model;

import net.ymate.platform.core.beans.annotation.PropertyState;
import net.ymate.platform.persistence.IShardingable;
import net.ymate.platform.persistence.annotation.Default;
import net.ymate.platform.persistence.annotation.Entity;
import net.ymate.platform.persistence.annotation.Id;
import net.ymate.platform.persistence.annotation.Property;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.support.BaseEntity;

/**
 * WechatUserAttribute generated By EntityGenerator on 2017/08/18 上午 09:39:10
 *
 * @author YMP
 * @version 1.0
 */
@Entity("wechat_user_attribute")
public class WechatUserAttribute extends BaseEntity<WechatUserAttribute, java.lang.String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Property(name = "id", nullable = false, length = 32)
    @PropertyState(propertyName = "id")
    private java.lang.String id;

    @Property(name = "wx_id", nullable = false, length = 32)
    @PropertyState(propertyName = "wx_id")
    private java.lang.String wxId;

    @Property(name = "attr_key", nullable = false, length = 255)
    @PropertyState(propertyName = "attr_key")
    private java.lang.String attrKey;

    @Property(name = "attr_value", length = 16383)
    @PropertyState(propertyName = "attr_value")
    private java.lang.String attrValue;

    @Property(name = "type", unsigned = true, length = 2)
    @Default("0")
    @PropertyState(propertyName = "type")
    private java.lang.Integer type;

    @Property(name = "owner", length = 32)
    @PropertyState(propertyName = "owner")
    private java.lang.String owner;

    /**
     * 构造器
     */
    public WechatUserAttribute() {
    }

    /**
     * 构造器
     *
     * @param id
     * @param wxId
     * @param attrKey
     */
    public WechatUserAttribute(java.lang.String id, java.lang.String wxId, java.lang.String attrKey) {
        this.id = id;
        this.wxId = wxId;
        this.attrKey = attrKey;
    }

    /**
     * 构造器
     *
     * @param id
     * @param wxId
     * @param attrKey
     * @param attrValue
     * @param type
     * @param owner
     */
    public WechatUserAttribute(java.lang.String id, java.lang.String wxId, java.lang.String attrKey, java.lang.String attrValue, java.lang.Integer type, java.lang.String owner) {
        this.id = id;
        this.wxId = wxId;
        this.attrKey = attrKey;
        this.attrValue = attrValue;
        this.type = type;
        this.owner = owner;
    }

    @Override
    public java.lang.String getId() {
        return id;
    }

    @Override
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * @return the wxId
     */
    public java.lang.String getWxId() {
        return wxId;
    }

    /**
     * @param wxId the wxId to set
     */
    public void setWxId(java.lang.String wxId) {
        this.wxId = wxId;
    }

    /**
     * @return the attrKey
     */
    public java.lang.String getAttrKey() {
        return attrKey;
    }

    /**
     * @param attrKey the attrKey to set
     */
    public void setAttrKey(java.lang.String attrKey) {
        this.attrKey = attrKey;
    }

    /**
     * @return the attrValue
     */
    public java.lang.String getAttrValue() {
        return attrValue;
    }

    /**
     * @param attrValue the attrValue to set
     */
    public void setAttrValue(java.lang.String attrValue) {
        this.attrValue = attrValue;
    }

    /**
     * @return the type
     */
    public java.lang.Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(java.lang.Integer type) {
        this.type = type;
    }

    /**
     * @return the owner
     */
    public java.lang.String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(java.lang.String owner) {
        this.owner = owner;
    }


    //
    // Chain
    //

    public static WechatUserAttributeBuilder builder() {
        return new WechatUserAttributeBuilder();
    }

    public WechatUserAttributeBuilder bind() {
        return new WechatUserAttributeBuilder(this);
    }

    public static class WechatUserAttributeBuilder {

        private WechatUserAttribute _model;

        public WechatUserAttributeBuilder() {
            _model = new WechatUserAttribute();
        }

        public WechatUserAttributeBuilder(WechatUserAttribute model) {
            _model = model;
        }

        public WechatUserAttribute build() {
            return _model;
        }


        public IConnectionHolder connectionHolder() {
            return _model.getConnectionHolder();
        }

        public WechatUserAttributeBuilder connectionHolder(IConnectionHolder connectionHolder) {
            _model.setConnectionHolder(connectionHolder);
            return this;
        }

        public String dataSourceName() {
            return _model.getDataSourceName();
        }

        public WechatUserAttributeBuilder dataSourceName(String dsName) {
            _model.setDataSourceName(dsName);
            return this;
        }

        public IShardingable shardingable() {
            return _model.getShardingable();
        }

        public WechatUserAttributeBuilder shardingable(IShardingable shardingable) {
            _model.setShardingable(shardingable);
            return this;
        }

        public java.lang.String id() {
            return _model.getId();
        }

        public WechatUserAttributeBuilder id(java.lang.String id) {
            _model.setId(id);
            return this;
        }

        public java.lang.String wxId() {
            return _model.getWxId();
        }

        public WechatUserAttributeBuilder wxId(java.lang.String wxId) {
            _model.setWxId(wxId);
            return this;
        }

        public java.lang.String attrKey() {
            return _model.getAttrKey();
        }

        public WechatUserAttributeBuilder attrKey(java.lang.String attrKey) {
            _model.setAttrKey(attrKey);
            return this;
        }

        public java.lang.String attrValue() {
            return _model.getAttrValue();
        }

        public WechatUserAttributeBuilder attrValue(java.lang.String attrValue) {
            _model.setAttrValue(attrValue);
            return this;
        }

        public java.lang.Integer type() {
            return _model.getType();
        }

        public WechatUserAttributeBuilder type(java.lang.Integer type) {
            _model.setType(type);
            return this;
        }

        public java.lang.String owner() {
            return _model.getOwner();
        }

        public WechatUserAttributeBuilder owner(java.lang.String owner) {
            _model.setOwner(owner);
            return this;
        }

    }

    /**
     * WechatUserAttribute 字段常量表
     */
    public class FIELDS {
        public static final String ID = "id";
        public static final String WX_ID = "wx_id";
        public static final String ATTR_KEY = "attr_key";
        public static final String ATTR_VALUE = "attr_value";
        public static final String TYPE = "type";
        public static final String OWNER = "owner";
    }

    public static final String TABLE_NAME = "wechat_user_attribute";

}
