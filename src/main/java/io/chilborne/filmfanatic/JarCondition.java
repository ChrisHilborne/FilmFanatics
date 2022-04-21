package io.chilborne.filmfanatic;

import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class JarCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    ApplicationArguments args = context.getBeanFactory().getBean(ApplicationArguments.class);

    System.out.println("Option names:" + args.getOptionNames());
    for (String name : args.getOptionNames()) {
      System.out.println("Option: " + name + "::" + args.getOptionValues(name));
    }

    System.out.println("NonOptionArgs: " + args.getNonOptionArgs());

    boolean matches = context.getBeanFactory()
      .getBean(ApplicationArguments.class)
      .getOptionNames()
      .contains("jar");

    System.out.println("matches:" + matches);
    return matches;
  }
}

