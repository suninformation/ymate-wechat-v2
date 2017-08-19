package net.ymate.wechat.model;

import net.ymate.platform.core.beans.annotation.PropertyState;
import net.ymate.platform.persistence.IShardingable;
import net.ymate.platform.persistence.annotation.Entity;
import net.ymate.platform.persistence.annotation.Id;
import net.ymate.platform.persistence.annotation.Property;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.support.BaseEntity;

/**
 * WechatLocation generated By EntityGenerator on 2017/08/18 上午 09:39:10
 *
 * @author YMP
 * @version 1.0
 */
@Entity("wechat_location")
public class WechatLocation extends BaseEntity<WechatLocation, java.lang.String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Property(name = "id", nullable = false, length = 32)
    @PropertyState(propertyName = "id")
    private java.lang.String id;

    @Property(name = "account_id", nullable = false, length = 32)
    @PropertyState(propertyName = "account_id")
    private java.lang.String accountId;

    @Property(name = "wx_uid", nullable = false, length = 32)
    @PropertyState(propertyName = "wx_uid")
    private java.lang.String wxUid;

    @Property(name = "location_lon", nullable = false, length = 20)
    @PropertyState(propertyName = "location_lon")
    private java.lang.Long locationLon;

    @Property(name = "location_lat", nullable = false, length = 20)
    @PropertyState(propertyName = "location_lat")
    private java.lang.Long locationLat;

    @Property(name = "precision", nullable = false, length = 20)
    @PropertyState(propertyName = "precision")
    private java.lang.Long precision;

    @Property(name = "label", length = 100)
    @PropertyState(propertyName = "label")
    private java.lang.String label;

    @Property(name = "create_time", nullable = false, length = 13)
    @PropertyState(propertyName = "create_time")
    private java.lang.Long createTime;

    /**
     * 构造器
     */
    public WechatLocation() {
    }

    /**
     * 构造器
     *
     * @param id
     * @param accountId
     * @param wxUid
     * @param locationLon
     * @param locationLat
     * @param precision
     * @param createTime
     */
    public WechatLocation(java.lang.String id, java.lang.String accountId, java.lang.String wxUid, java.lang.Long locationLon, java.lang.Long locationLat, java.lang.Long precision, java.lang.Long createTime) {
        this.id = id;
        this.accountId = accountId;
        this.wxUid = wxUid;
        this.locationLon = locationLon;
        this.locationLat = locationLat;
        this.precision = precision;
        this.createTime = createTime;
    }

    /**
     * 构造器
     *
     * @param id
     * @param accountId
     * @param wxUid
     * @param locationLon
     * @param locationLat
     * @param precision
     * @param label
     * @param createTime
     */
    public WechatLocation(java.lang.String id, java.lang.String accountId, java.lang.String wxUid, java.lang.Long locationLon, java.lang.Long locationLat, java.lang.Long precision, java.lang.String label, java.lang.Long createTime) {
        this.id = id;
        this.accountId = accountId;
        this.wxUid = wxUid;
        this.locationLon = locationLon;
        this.locationLat = locationLat;
        this.precision = precision;
        this.label = label;
        this.createTime = createTime;
    }

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * @return the accountId
     */
    public java.lang.String getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(java.lang.String accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the wxUid
     */
    public java.lang.String getWxUid() {
        return wxUid;
    }

    /**
     * @param wxUid the wxUid to set
     */
    public void setWxUid(java.lang.String wxUid) {
        this.wxUid = wxUid;
    }

    /**
     * @return the locationLon
     */
    public java.lang.Long getLocationLon() {
        return locationLon;
    }

    /**
     * @param locationLon the locationLon to set
     */
    public void setLocationLon(java.lang.Long locationLon) {
        this.locationLon = locationLon;
    }

    /**
     * @return the locationLat
     */
    public java.lang.Long getLocationLat() {
        return locationLat;
    }

    /**
     * @param locationLat the locationLat to set
     */
    public void setLocationLat(java.lang.Long locationLat) {
        this.locationLat = locationLat;
    }

    /**
     * @return the precision
     */
    public java.lang.Long getPrecision() {
        return precision;
    }

    /**
     * @param precision the precision to set
     */
    public void setPrecision(java.lang.Long precision) {
        this.precision = precision;
    }

    /**
     * @return the label
     */
    public java.lang.String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    /**
     * @return the createTime
     */
    public java.lang.Long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(java.lang.Long createTime) {
        this.createTime = createTime;
    }


    //
    // Chain
    //

    public static WechatLocationBuilder builder() {
        return new WechatLocationBuilder();
    }

    public WechatLocationBuilder bind() {
        return new WechatLocationBuilder(this);
    }

    public static class WechatLocationBuilder {

        private WechatLocation _model;

        public WechatLocationBuilder() {
            _model = new WechatLocation();
        }

        public WechatLocationBuilder(WechatLocation model) {
            _model = model;
        }

        public WechatLocation build() {
            return _model;
        }


        public IConnectionHolder connectionHolder() {
            return _model.getConnectionHolder();
        }

        public WechatLocationBuilder connectionHolder(IConnectionHolder connectionHolder) {
            _model.setConnectionHolder(connectionHolder);
            return this;
        }

        public String dataSourceName() {
            return _model.getDataSourceName();
        }

        public WechatLocationBuilder dataSourceName(String dsName) {
            _model.setDataSourceName(dsName);
            return this;
        }

        public IShardingable shardingable() {
            return _model.getShardingable();
        }

        public WechatLocationBuilder shardingable(IShardingable shardingable) {
            _model.setShardingable(shardingable);
            return this;
        }

        public java.lang.String id() {
            return _model.getId();
        }

        public WechatLocationBuilder id(java.lang.String id) {
            _model.setId(id);
            return this;
        }

        public java.lang.String accountId() {
            return _model.getAccountId();
        }

        public WechatLocationBuilder accountId(java.lang.String accountId) {
            _model.setAccountId(accountId);
            return this;
        }

        public java.lang.String wxUid() {
            return _model.getWxUid();
        }

        public WechatLocationBuilder wxUid(java.lang.String wxUid) {
            _model.setWxUid(wxUid);
            return this;
        }

        public java.lang.Long locationLon() {
            return _model.getLocationLon();
        }

        public WechatLocationBuilder locationLon(java.lang.Long locationLon) {
            _model.setLocationLon(locationLon);
            return this;
        }

        public java.lang.Long locationLat() {
            return _model.getLocationLat();
        }

        public WechatLocationBuilder locationLat(java.lang.Long locationLat) {
            _model.setLocationLat(locationLat);
            return this;
        }

        public java.lang.Long precision() {
            return _model.getPrecision();
        }

        public WechatLocationBuilder precision(java.lang.Long precision) {
            _model.setPrecision(precision);
            return this;
        }

        public java.lang.String label() {
            return _model.getLabel();
        }

        public WechatLocationBuilder label(java.lang.String label) {
            _model.setLabel(label);
            return this;
        }

        public java.lang.Long createTime() {
            return _model.getCreateTime();
        }

        public WechatLocationBuilder createTime(java.lang.Long createTime) {
            _model.setCreateTime(createTime);
            return this;
        }

    }

    /**
     * WechatLocation 字段常量表
     */
    public class FIELDS {
        public static final String ID = "id";
        public static final String ACCOUNT_ID = "account_id";
        public static final String WX_UID = "wx_uid";
        public static final String LOCATION_LON = "location_lon";
        public static final String LOCATION_LAT = "location_lat";
        public static final String PRECISION = "precision";
        public static final String LABEL = "label";
        public static final String CREATE_TIME = "create_time";
    }

    public static final String TABLE_NAME = "wechat_location";

}
