package com.dummyframework.deserialize;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import com.dummyframework.deserialize.converters.Converter;
import com.dummyframework.deserialize.converters.ConverterException;
import com.dummyframework.deserialize.converters.ResloveConverter;

public class TypeInfo {

  public final String JavaLangPackage = "java.lang";
  public final String JavaUtilPackage = "java.util";

  private Class<?> resolvedClass;
  private boolean isPrimitive = false;
  private boolean isArray = false;
  private boolean isCollection = false;
  private boolean isMap = false;
  private boolean isJavaType = false;
  private boolean isEnum = false;
  private TypeInfo[] generics;
  private Converter converter;
  private TypeInfo arrayComponentTypeInfo;

  public TypeInfo(Type type) throws ClassNotFoundException, ConverterException {
    this.resolve(type);
  }

  public void resolve(Type type) throws ClassNotFoundException, ConverterException {
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      this.resolvedClass = (Class<?>) parameterizedType.getRawType();
      setIsCollection();
      setIsMap();
      resolveGenerics(parameterizedType);
    } else if (type instanceof Class) {
      Class<?> tempClazz = (Class<?>) type;
      this.resolvedClass = tempClazz;
      setIsArray(this.resolvedClass);
      setIsPrimitive(this.resolvedClass);
      setIsEnum(this.resolvedClass);
      setArrayComponentTypeInfo(this.resolvedClass);
    } else if (type instanceof GenericArrayType) {
      GenericArrayType arrayType = (GenericArrayType) type;
      this.isArray = true;
      setArrayComponentTypeInfo(arrayType.getGenericComponentType());
    }
    setIsJavaType();
    setConverter();
  }

  private void setIsArray(Class<?> clazz) throws ClassNotFoundException, ConverterException {
    this.isArray = clazz.isArray();
    if (this.isArray) {
      this.resolvedClass = clazz.getComponentType();
    }
  }

  private void setConverter() throws ConverterException {
    this.converter = ResloveConverter.getConverter(this);
  }

  private void setArrayComponentTypeInfo(Type type)
      throws ClassNotFoundException, ConverterException {
    if (this.isArray && !this.isPrimitive) {
      this.arrayComponentTypeInfo = new TypeInfo(type);
    }
  }

  private void setIsCollection() {
    this.isCollection = Collection.class.isAssignableFrom(this.resolvedClass);
  }

  private void setIsMap() {
    this.isMap = Map.class.isAssignableFrom(this.resolvedClass);
  }

  private void resolveGenerics(ParameterizedType parameterizedType)
      throws ClassNotFoundException, ConverterException {
    Type[] types = parameterizedType.getActualTypeArguments();
    generics = new TypeInfo[types.length];
    for (int i = 0; i < types.length; i++) {
      generics[i] = new TypeInfo(types[i]);
    }
  }

  private void setIsPrimitive(Class<?> clazz) {
    this.isPrimitive = clazz.isPrimitive();
  }

  private void setIsEnum(Class<?> clazz) {
    this.isEnum = clazz.isEnum();
  }

  private void setIsJavaType() {
    if (this.resolvedClass != null) {
      String packageName = this.resolvedClass.getPackageName();
      if (packageName.equals(JavaLangPackage) || packageName.equals(JavaUtilPackage)) {
        this.isJavaType = true;
      }
    }
  }

  public Class<?> getSuperClass(Class<?> clazz) {
    if (clazz == null) {
      return this.resolvedClass.getSuperclass();
    }
    return clazz.getSuperclass();
  }

  public boolean isAssignableFrom(Class<?> assign) {
    return assign.isAssignableFrom(this.resolvedClass);
  }

  public boolean isSuperClassAssignableFrom(Class<?> assign) {
    if (this.getSuperClass(this.resolvedClass) != null) {
      return this.getSuperClass(this.resolvedClass).isAssignableFrom(assign);
    }
    return false;
  }

  public boolean isPrimitive() {
    return this.isPrimitive;
  }

  public TypeInfo getArrayComponentTypeInfo() {
    return this.arrayComponentTypeInfo;
  }

  public boolean isArray() {
    return this.isArray;
  }

  public boolean isJavaType() {
    return this.isJavaType;
  }

  public boolean isCollection() {
    return this.isCollection;
  }

  public Class<?> getResolvedClass() {
    return this.resolvedClass;
  }

  public TypeInfo[] getGenerics() {
    return this.generics;
  }

  public boolean isEnum() {
    return this.isEnum;
  }

  public boolean isMap() {
    return isMap;
  }

  public Converter getConverter() {
    return this.converter;
  }
}
