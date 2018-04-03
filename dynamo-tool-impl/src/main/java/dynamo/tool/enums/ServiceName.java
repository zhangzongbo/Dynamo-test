package dynamo.tool.enums;

/**
 * Created by zongbo.zhang on 4/3/18.
 */
public enum ServiceName implements StringValueEnum{

    XUNJI("寻迹"),
    ZHICHA("智查"),
    BLACK("黑名单"),
    MINGJIAN("明鉴")

    ;

    private final String service;

    ServiceName(String service) {
        this.service = service;
    }

    @Override
    public String getValue() {
        return service;
    }
}
