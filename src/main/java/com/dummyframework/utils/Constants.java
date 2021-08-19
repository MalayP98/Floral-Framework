package com.dummyframework.utils;

public class Constants {

  public static final String ANNOTATION_MATCHER =
      "[a-zA-Z@\\.]*\\.([A-Za-z]*)\\((([A-Za-z]*)=\"([A-Za-z./\\s]*)\",?\\s?)*\\)";

  public static final String REQUEST_REGEX =
      "\\/([A-Za-z-.]*)\\/([A-Za-z.]*)(\\/[A-Za-z]*)\\/?([A-Za-z?&0-9=]*)\\/?";

  public static final String PAYLOAD_EXTRACTOR =
      "([A-Za-z0-9]*):{1}\\({1}([A-Za-z\"=0-9,.@|\\[\\]$\\s]*)\\){1}";

  public static final String METHOD_CLASS_SEPRATOR_REGEX = "([A-Za-z._]*)#{1}([A-Za-z_]*)";

  public static final String SETTER_METHOD = "set{1}([a-zA-Z_]*)";

  public static final String GETTER_METHOD = "get{1}([a-zA-Z_]*)";

  public static final String EXTRACT_LIST = "\\|{1}([A-Za-z,\\s]*)\\|{1}";

  public static final String REMOVE_QUOTED = "\"{1}([A-Za-z0-9_@.]*)\"{1}";

  public static final String SEMI_COLON = ";";

  public static final String EQUAL = "=";

  public static final String DOLLAR = "$";

  public static final String PARAM_SEPRATOR = ",\\s?";

  public static final String TAG = "tag";

  public static final String INPUT = "input";

  public static final String QUOTES = "\"";

  public static final String DECIMAL_CHECK = "\\.{1}";

  public static final String JAVA = "java";

  public static final String ARRAY_CONTAINER = "|";

  public static final String RESOLVE = "resolve";

  public static final String ARRAY_START = "[";

  public static final String ARRAY_END = "]";

  public static final int ZERO = 0;

  public static final int ONE = 1;

  public static final int TWO = 2;
}
