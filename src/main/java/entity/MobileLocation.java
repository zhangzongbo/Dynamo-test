package entity;

import com.google.gson.annotations.Expose;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.io.Serializable;
import java.util.Date;

import enums.ProvinceNameEnum;
import enums.ServiceProviderTypeEnum;

/**
 * Created by zhangzb on 3/19/18.
 */
@DynamoDBTable(tableName="mobileLocation-test")
public class MobileLocation implements Serializable {
    @Expose
    @DynamoDBHashKey(attributeName="mobile_no_seqment")
    private String mobileNoSeqment;
    @Expose
    @DynamoDBAttribute(attributeName="type")
    @DynamoDBTypeConverted(converter=ServiceProviderTypeEnumConverter.class)
    private ServiceProviderTypeEnum type;
    @Expose
    @DynamoDBAttribute(attributeName="area_code")
    private String areaCode;
    @Expose
    @DynamoDBAttribute(attributeName="area_name")
    private String areaName;
    @Expose
    @DynamoDBAttribute(attributeName="province_code")
    @DynamoDBTypeConverted(converter=ProvinceNameEnumTypeConverter.class)
    private ProvinceNameEnum provinceCode;
    @Expose
    @DynamoDBAttribute(attributeName="province_name")
    private String provinceName;
    @Expose
    @DynamoDBAttribute(attributeName="virtual_provider_name")
    private String virtualProviderName;
    @DynamoDBAttribute(attributeName="update_time")
    private Date updateTime = new Date();

    // Converts the complex type DimensionType to a string and vice-versa.
    static public class ProvinceNameEnumTypeConverter implements DynamoDBTypeConverter<String,ProvinceNameEnum> {

        @Override
        public String convert(ProvinceNameEnum provinceNameEnum) {
            String converter = null;
            try {
                converter = provinceNameEnum.getValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return converter;
        }

        @Override
        public ProvinceNameEnum unconvert(String value) {
            ProvinceNameEnum provinceName = null;
            try {
                provinceName  = ProvinceNameEnum.getEnumByValue(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return provinceName;
        }
    }

    static public class ServiceProviderTypeEnumConverter implements DynamoDBTypeConverter<String,ServiceProviderTypeEnum> {

        @Override
        public String convert(ServiceProviderTypeEnum serviceProviderTypeEnum) {
            String converter = null;
            try {
                converter = serviceProviderTypeEnum.name();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return converter;
        }

        @Override
        public ServiceProviderTypeEnum unconvert(String value) {
            ServiceProviderTypeEnum serviceProviderTypeEnum = null;
            try {
                serviceProviderTypeEnum  = ServiceProviderTypeEnum.valueOf(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return serviceProviderTypeEnum;
        }
    }


    public String getMobileNoSeqment() {
        return mobileNoSeqment;
    }

    public void setMobileNoSeqment(String mobileNoSeqment) {
        this.mobileNoSeqment = mobileNoSeqment;
    }

    public ServiceProviderTypeEnum getType() {
        return type;
    }

    public void setType(ServiceProviderTypeEnum type) {
        this.type = type;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public ProvinceNameEnum getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(ProvinceNameEnum provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getVirtualProviderName() {
        return virtualProviderName;
    }

    public void setVirtualProviderName(String virtualProviderName) {
        this.virtualProviderName = virtualProviderName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return String.format("mobileNoSeqment:%s\ntype:%s\nareaCode:%s\nareaName:%s\nprovinceCode:%s\nprovinceName:%s\nvirtualProviderName:%s\nupdateTime:%s\n",
                getMobileNoSeqment(), getType(), getAreaCode(), getAreaName(), getProvinceCode(), getProvinceName(), getVirtualProviderName(), getUpdateTime());
    }
}
