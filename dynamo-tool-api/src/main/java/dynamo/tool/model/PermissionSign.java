package dynamo.tool.model;

/**
 * Created by chenwen on 16/10/21.
 * 权限标示表
 */
public class PermissionSign {
    /**
     * 查看
     */
    public final static String OPERATE_QUERY = "查看";
    /**
     * 操作
     */
    public final static String OPERATE_MODIFY = "操作";

    /**
     * 资产标示
     */
    public static class Asset {
        /**
         * 资产明细-评估汇总
         */
        public final static String BASIC = "f:r:asset:basic";

        /**
         * 资产评估-异常提示
         */
        public final static String RISK_RESULT_EXCEPTION = "f:asset:getRiskResult:exception";

        /**
         * 资产评估-信用明细
         */
        public final static String RISK_RESULT_DATA_PROD = "f:asset:getRiskResult:dataProd";

        /**
         * 资产评估-订单明细
         */
        public final static String DETAIL = "f:asset:detail";

        /**
         * 资产评估-照片查看
         */
        public final static String PICTURE = "f:asset:picture";
    }

    /**
     * 还款明细
     */
    public static class Repay{
        /**
         * 还款明细-基本信息
         */
        public final static String BASIC = "f:repay:query:basic";

        /**
         * 还款明细-买断状态
         */
        public final static String IS_COMPENSATE = "f:repay:query:is_compensate";

        /**
         * 还款明细-买断后详情
         */
        public final static String AFTER_COMP_USER_AMOUNT = "f:repay:query:aftercomp_user_amount";

        /**
         * 还款明细-代偿方
         */
        public final static String COMPENSATE_STAUTS = "f:repay:query:compensate_status";

        /**
         * 还款明细-代偿金额
         */
        public final static String COMPENSATE_AMOUNT = "f:repay:query:compensate_amount";

        /**
         * 还款明细-代偿时间
         */
        public final static String COMPENSATE_DATE = "f:repay:query:compensate_date";

        /**
         * 还款明细-备注
         */
        public final static String REMARK = "f:repay:query:remark";

        /**
         * 还款明细-其他信息
         */
        public final static String DEFAULT = "f:repay:query:default";
    }

    /**
     * 渠道
     */
    public static class Channel{
        /**
         * 渠道列表-对接资方
         */
        public static final String INVESTORS = "f:r:channel:investors";

        /**
         * 渠道列表-其他信息
         */
        public static final String OTHER = "f:r:channel:getList:other";

        /**
         * 渠道充值
         */
        public static class Recharge{
            /**
             * 充值记录-查看凭证
             */
            public static final String CHARGE_CERTIFICATE = "f:channel:charge_certificate";
        }
    }

    /**
     * 商户
     */
    public static class Seller{
        /**
         * 商户列表-商户备注
         */
        public static final String STORE_RISK_NOTES = "f:seller:store_risk_notes";

        /**
         * 商户列表-其他信息
         */
        public static final String OTHER = "f:seller:other";
    }

    /**
     * 保险
     */
    public static class Insurance{
        /**
         * 保险方-详情
         */
        public static final String DETAIL = "f:r:insurance:getDetail:insurance";

        /**
         * 保险方-方案列表
         */
        public static final String INSURANCE_CASES = "f:r:insurance:getDetail:insurance_cases";
    }

    /**
     * 资方回款
     */
    public static class ReturnMoney{
        /**
         * 基本信息(渠道名称、订单编号、用户姓名、手机号、身份证、银行卡号、是否买断、期数 等)
         */
        public static final String BASIC = "f:r:investor:returnMoney:getList:basic";

        /**
         * 平台信息(平台状态、平台应还日、平台还款日、平台放款日、平台每期金额、平台剩余金额、平台应还金额、平台实还金额、实还差价 等)
         */
        public static final String PLATFORM = "f:r:investor:returnMoney:getList:platform";

        /**
         * 资方信息(资方状态、资方应还日、资方还款日 等剩余字段)
         */
        public static final String INVESTOR = "f:r:investor:returnMoney:getList:investor";
    }
}
