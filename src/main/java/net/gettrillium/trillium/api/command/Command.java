package net.gettrillium.trillium.api.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String command();

    String description();

    String usage();

    String[] aliases() default {};
}
