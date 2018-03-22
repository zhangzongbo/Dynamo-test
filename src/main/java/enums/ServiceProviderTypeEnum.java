package enums;

/**
 * Created by zhangzb on 3/19/18.
 */
public enum ServiceProviderTypeEnum implements IntValueEnum  {
    CHINA_MOBILE,
    CHINA_UNICOM,
    CHINA_TELECOM;

    @Override
    public int getValue() {
        return this.ordinal();
    }

    public static ServiceProviderTypeEnum getEnumByValue(String name) {
        switch (name) {
            case "移动":
                return CHINA_MOBILE;
            case "联通":
                return CHINA_UNICOM;
            case "电信":
                return CHINA_TELECOM;
        }

        return null;
    }

    public static ServiceProviderTypeEnum getEnumByName(String name){
        for(ServiceProviderTypeEnum serviceProviderTypeEnum : values()){
            if (serviceProviderTypeEnum.name().equalsIgnoreCase(name)){
                return serviceProviderTypeEnum;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getEnumByName("CHINA_TELECOM"));
        System.out.println(getEnumByValue("移动"));
        System.out.println(CHINA_UNICOM.name()+"========="+CHINA_UNICOM.getValue());
    }
}