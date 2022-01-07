package com.dummyframework.core;

public class AutowireHelper {

  // BeanRegistry registry = BeanRegistry.getInstance();
  // Logger logger = new Logger(AutowireHelper.class);

  // public void autowire(List<Bean> beans) throws IllegalArgumentException, IllegalAccessException
  // {
  // logger.info("Autowiring started.");
  // for (Bean bean : beans) {
  // Class<?> clazz = bean.getClazz();
  // Field[] fields = clazz.getDeclaredFields();
  // for (Field field : fields) {
  // autowire(field, bean.getBean());
  // }
  // }
  // }

  // public void autowire(Field field, Object component) throws IllegalArgumentException,
  // IllegalAccessException {
  // if (field.isAnnotationPresent(Autowired.class)) {
  // field.setAccessible(true);
  // logger.info(
  // "Autowiring field " + field.getName() + " in component " +
  // component.getClass().getSimpleName());
  // field.set(component, registry.getBean(getBeanName(field)));
  // }
  // }

  // private String getBeanName(Field field) {
  // Autowired annotation = field.getAnnotation(Autowired.class);
  // if (!annotation.name().isEmpty()) {
  // return annotation.name();
  // }
  // return field.getType().getSimpleName().toLowerCase();
  // }
}
