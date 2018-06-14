package dynamo.tool.model;

/**
 * Created by zongbo.zhang on 6/14/18.
 */

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import dynamo.tool.common.annotation.JSONGroup;
import dynamo.tool.common.annotation.JSONToStrConvert;
import dynamo.tool.common.model.DynamoDBModel;
import lombok.*;

/**
 * Created by chenwen on 16/11/11.
 * <p>
 * <p>
 * <p>
 * {
 * "handlerData": {
 * "advice": "reject",
 * "creditLimit": 7000,
 * "creditTerm": 0,
 * "feeRateCode": "0.11",
 * "noBus": "20161111212152778253732",
 * "noBusb": "14784114773544",
 * "reason": [
 * "金融黑名单",
 * "互联网黑名单",
 * "公检法黑名单"
 * ],
 * "reasonCode": "CP"
 * },
 * "resCode": "0000",
 * "resMsg": "null"
 * }
 */
@ToString
@Data
@DynamoDBTable(tableName = "o2o-risk-result-prod")
public class RiskResult extends DynamoDBModel {

    @DynamoDBHashKey(attributeName = "_id")
    @JSONField(name = "order_no")
    private String orderNo;

    @DynamoDBAttribute(attributeName = "success")
    private Boolean success;

    @DynamoDBAttribute(attributeName = "service_name")
    @JSONField(name = "service_name")
    private String serviceName;

    /**
     * 该字段为“0000”代 表请求成功
     */
    @DynamoDBAttribute(attributeName = "res_code")
    @JSONField(name = "res_code", serialize = false)
    private String resCode;

    /**
     * 原因说明
     */
    @DynamoDBAttribute(attributeName = "res_msg")
    @JSONField(name = "res_msg", serialize = false)
    private String resMsg;

    /**
     * 具体原因
     */
    @DynamoDBAttribute(attributeName = "handler_data")
    @JSONField(name = "handler_data")
    private HandlerData handlerData;

    @DynamoDBAttribute(attributeName = "risk_json")
    @JSONField(name = "risk_json")
    @JSONToStrConvert
    private JSONObject riskJson;

    @DynamoDBAttribute(attributeName = "contact_call_in_max")
    @JSONField(name = "contact_call_in_max")
    private List<Contact> contactCallInMax;

    @DynamoDBAttribute(attributeName = "contact_call_out_max")
    @JSONField(name = "contact_call_out_max")
    private List<Contact> contactCallOutMax;

    @Data
    @DynamoDBDocument
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contact implements Serializable {

        @DynamoDBAttribute(attributeName = "name")
        private String name = "未知号码";

        @DynamoDBAttribute(attributeName = "mobile")
        private String mobile;
    }

    @DynamoDBDocument
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HandlerData implements Serializable {

        /**
         * 客户的请求 id
         */
        @DynamoDBAttribute(attributeName = "no_busb")
        @JSONField(name = "no_busb", serialize = false)
        private String noBusb;

        /**
         * 申请单状态,参看字 典 返回建议
         */
        @DynamoDBAttribute(attributeName = "advice")
        @JSONField(name = "advice", serialize = false)
        private String advice;

        /**
         * 申请单状态,参看字 典 返回建议
         */
        @DynamoDBAttribute(attributeName = "score")
        @JSONField(name = "score")
        @JSONGroup(PermissionSign.Asset.BASIC)
        private String score;

        /**
         * 申请单状态,参看字 典 返回建议
         */
        @DynamoDBAttribute(attributeName = "advice_des")
        @JSONField(name = "advice_des")
        @JSONGroup(PermissionSign.Asset.BASIC)
        private String adviceDes;

        /**
         * 授信额度
         */
        @DynamoDBAttribute(attributeName = "credit_limit")
        @JSONField(name = "credit_limit")
        @JSONGroup(PermissionSign.Asset.BASIC)
        private BigDecimal creditLimit;

        /**
         * 授信期数
         */
        @DynamoDBAttribute(attributeName = "credit_term")
        @JSONField(name = "credit_term")
        @JSONGroup(PermissionSign.Asset.BASIC)
        private Integer creditTerm;

        /**
         * 决策原因编码
         */
        @DynamoDBAttribute(attributeName = "reason_code")
        @JSONField(name = "reason_code")
        @JSONGroup(PermissionSign.Asset.BASIC)
        private String reasonCode;

        /**
         * 决策原因
         */
        @DynamoDBAttribute(attributeName = "reason")
        @JSONField(name = "reason")
        @JSONGroup(PermissionSign.Asset.RISK_RESULT_EXCEPTION)
        private List<String> reason;

        /**
         * 利率码
         */
        @DynamoDBAttribute(attributeName = "interest_code")
        @JSONField(name = "interest_code", serialize = false)
        private String interestCode;

        /**
         * 首付款
         */
        @DynamoDBAttribute(attributeName = "amt_downpay")
        @JSONField(name = "amt_downpay", serialize = false)
        private BigDecimal amtDownpay;

        /**
         * 每期还款
         */
//        @DynamoDBAttribute(attributeName = "amt_monthrepay")
//        @JSONField(name = "amt_monthrepay", serialize = false)
//        private BigDecimal amtMonthrepay;

        /**
         * 费率码
         */
        @DynamoDBAttribute(attributeName = "fee_rate_code")
        @JSONField(name = "fee_rate_code")
        @JSONGroup(PermissionSign.Asset.BASIC)
        private String feeRateCode;

        /**
         * 在信申系统唯一的请求id
         */
        @DynamoDBAttribute(attributeName = "no_bus")
        @JSONField(name = "no_bus", serialize = false)
        private String noBus;

        @DynamoDBAttribute(attributeName = "data_prod")
        @JSONField(name = "data_prod")
        @JSONToStrConvert
        @JSONGroup(PermissionSign.Asset.RISK_RESULT_DATA_PROD)
        private JSONObject dataProd;

        public String getNoBusb() {
            return noBusb;
        }

        public void setNoBusb(String noBusb) {
            this.noBusb = noBusb;
        }

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public BigDecimal getCreditLimit() {
            return creditLimit;
        }

        public void setCreditLimit(BigDecimal creditLimit) {
            this.creditLimit = creditLimit;
        }

        public Integer getCreditTerm() {
            return creditTerm;
        }

        public void setCreditTerm(Integer creditTerm) {
            this.creditTerm = creditTerm;
        }

        public String getReasonCode() {
            return reasonCode;
        }

        public void setReasonCode(String reasonCode) {
            this.reasonCode = reasonCode;
        }

        public List<String> getReason() {
            return reason;
        }

        public void setReason(List<String> reason) {
            this.reason = reason;
        }

        public String getInterestCode() {
            return interestCode;
        }

        public void setInterestCode(String interestCode) {
            this.interestCode = interestCode;
        }

        public BigDecimal getAmtDownpay() {
            return amtDownpay;
        }

        public void setAmtDownpay(BigDecimal amtDownpay) {
            this.amtDownpay = amtDownpay;
        }

        public String getFeeRateCode() {
            return feeRateCode;
        }

        public void setFeeRateCode(String feeRateCode) {
            this.feeRateCode = feeRateCode;
        }

        public String getNoBus() {
            return noBus;
        }

        public void setNoBus(String noBus) {
            this.noBus = noBus;
        }

        public JSONObject getDataProd() {
            return dataProd;
        }

        public void setDataProd(JSONObject dataProd) {
            this.dataProd = dataProd;
        }

        public void setAdviceDes(String adviceDes) {
            this.adviceDes = adviceDes;
        }

        public String getAdviceDes() {
//            if (StringUtils.isEmpty(advice)){
            return adviceDes;
//            }
//            RiskAdviceStatusEnum adviceStatusEnum = RiskAdviceStatusEnum.getValueByName(advice);
//            return adviceDes = adviceStatusEnum == null ? null : adviceStatusEnum.getName();
        }
    }


    /**
     * 文档更新时间
     */
    @DynamoDBAttribute(attributeName = "update_time")
    @DynamoDBAutoGeneratedTimestamp
    @JSONField(name = "update_time", deserialize = false, format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime = updateTime == null ? new Date() : updateTime;
    }

    /**
     * 请求是否成功
     *
     * @return
     */
    public Boolean getSuccess() {
        return resCode != null && resCode.equals("0000");
    }
}
