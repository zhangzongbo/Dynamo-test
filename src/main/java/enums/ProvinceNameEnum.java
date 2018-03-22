package enums;

/**
 * Created by zhangzb on 3/19/18.
 */
public enum ProvinceNameEnum implements StringValueEnum {
    // 华北
    BEI_JING("北京"),
    TIAN_JIN("天津"),
    SHAN_XI("山西"),
    HE_BEI("河北"),
    NEI_MENG_GU("内蒙古"),

    // 东北
    LIAO_NING("辽宁"),
    JI_LIN("吉林"),
    HEI_LONG_JIANG("黑龙江"),

    // 华东
    SHANG_HAI("上海"),
    JIANG_SU("江苏"),
    ZHE_JIANG("浙江"),
    AN_HUI("安徽"),
    FU_JIAN("福建"),
    JIANG_XI("江西"),
    SHAN_DONG("山东"),

    // 中南
    HE_NAN("河南"),
    HU_NAN("湖南"),
    HU_BEI("湖北"),
    GUANG_DONG("广东"),
    GUANG_XI("广西"),
    HAI_NAN("海南"),

    // 西南
    CHONG_QING("重庆"),
    SI_CHUAN("四川"),
    GUI_ZHOU("贵州"),
    YUN_NAN("云南"),
    XI_ZHANG("西藏"),

    // 西北
    SHANXI("陕西"),
    GAN_SU("甘肃"),
    QING_HAI("青海"),
    NING_XIA("宁夏"),
    XIN_JIANG("新疆"),

    // 港澳台
    XIANG_GANG("香港"),
    AO_MEN("澳门"),
    TAI_WAN("台湾");

    private final String strValue;

    ProvinceNameEnum(String strValue) {
        this.strValue = strValue;
    }

    @Override
    public String getValue() {
        return strValue;
    }

    public static ProvinceNameEnum getEnumByValue(String strValue) {
        for (ProvinceNameEnum v : values()) {
            if (v.getValue().equals(strValue)) {
                return v;
            }
        }

        return null;
    }
}