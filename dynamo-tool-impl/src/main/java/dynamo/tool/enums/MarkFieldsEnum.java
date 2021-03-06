package dynamo.tool.enums;

/**
 * Created by zongbo.zhang on 4/2/18.
 */
public enum  MarkFieldsEnum implements IntValueEnum {
    EMPTY(0x0),
    BLACKLIST(0x1),
    MINGJIAN(0x2),
    XUNJI(0x4),
    ZHICHA(0x8),
    HUIYAN(0x10),
    BLACKLIST_SUC(0x20),
    MINGJIAN_SUC(0x40),
    XUNJI_SUC(0x80),
    ZHICHA_SUC(0x100),
    HUIYAN_SUC(0x200),
    ;


    private int intValue;

    MarkFieldsEnum(int value) {
        this.intValue = value;
    }

    @Override
    public int getValue() {
        return intValue;
    }
}
