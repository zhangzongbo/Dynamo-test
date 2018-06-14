package dynamo.tool.common.annotation;

import java.lang.annotation.*;

/**
 * Created by zongbo.zhang on 6/14/18.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JSONGroup {
    String[] value() default {"default"};
}
