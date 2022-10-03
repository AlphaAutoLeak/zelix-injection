package zelix.eventapi;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {
    byte value() default 2;
}
