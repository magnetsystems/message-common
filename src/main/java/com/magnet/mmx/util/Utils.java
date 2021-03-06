/*   Copyright (c) 2015-2016 Magnet Systems, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.magnet.mmx.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;
import java.util.TimeZone;

public class Utils {
  /**
   * Reverse the phone number.
   */
  public final static int FLAG_REVERSE = 0x1;
  /**
   * Append a wild card character to the end of a normalized phone number.
   */
  public final static int FLAG_APPEND_WILDCARD = 0x2;
  /**
   * Insert a wild card character to the beginning of a normalized phone number.
   */
  public final static int FLAG_INSERT_WILDCARD = 0x4;

  public static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  public static final String QUOTE_ENCODE = "&quot;";
  public static final String APOS_ENCODE = "&apos;";
  public static final String AMP_ENCODE = "&amp;";
  public static final String LT_ENCODE = "&lt;";
  public static final String GT_ENCODE = "&gt;";

  /**
   * Normalize a phone number for search or simple match. A normalized phone
   * number will have non-digits stripped, can be reversed with wild card '%'
   * for searching.
   * @param phoneNumber A phone number.
   * @param flags Combination of {@link #FLAG_REVERSE},
   *          {@link #FLAG_APPEND_WILDCARD} and {@link #FLAG_INSERT_WILDCARD}
   * @return A normalized phone number.
   */
  public static String normalizePhone(String phoneNumber, int flags) {
    boolean reverse = (flags & FLAG_REVERSE) != 0;
    boolean insert = (flags & FLAG_INSERT_WILDCARD) != 0;
    boolean append = (flags & FLAG_APPEND_WILDCARD) != 0;
    char[] digits = phoneNumber.toCharArray();
    StringBuilder sb = new StringBuilder(digits.length);
    if (insert) {
      sb.append('%');
    }
    for (int head = 0, tail = digits.length; --tail >= 0; head++) {
      char c = digits[reverse ? tail : head];
      if (Character.isDigit(c)) {
        sb.append(c);
      }
    }
    if (append) {
      sb.append('%');
    }
    return sb.toString();
  }

  /**
   * Get the value of a non-public member.
   * @param obj An object containing the field to be accessed.
   * @param fieldName The field name in the class.
   * @return
   * @throws NoSuchFieldException
   * @throws SecurityException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public static Object getFieldValue(Object obj, String fieldName)
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
              IllegalAccessException {
    boolean accessible;
    Class<?> clz = obj.getClass();
    Field field = clz.getDeclaredField(fieldName);
    if (!(accessible = field.isAccessible())) {
      field.setAccessible(true);
    }
    Object value = field.get(obj);
    if (!accessible) {
      field.setAccessible(false);
    }
    return value;
  }

  /**
   * Get a non-public (i.e. declared) static method from a class.
   * @param clz The class.
   * @param methodName A static method name.
   * @param params Optional parameters.
   * @return A Method object.
   * @throws NoSuchMethodException
   * @throws SecurityException
   */
  public static Method getMethod(Class<?> clz, String methodName, Object... params)
      throws NoSuchMethodException, SecurityException {
    if (params == null) {
      return clz.getMethod(methodName);
    } else {
      int i = 0;
      Class<?>[] paramClasses = new Class<?>[params.length];
      for (Object param : params) {
        paramClasses[i++] = param.getClass();
      }
      return clz.getDeclaredMethod(methodName, paramClasses);
    }
  }

  /**
   * Get a non-public (i.e. declared) method from an object.
   * @param obj The object to be used.
   * @param methodName A method name.
   * @param params Optional parameters.
   * @return A Method object.
   * @throws NoSuchMethodException
   * @throws SecurityException
   */
  public static Method getMethod(Object obj, String methodName, Object... params)
      throws NoSuchMethodException, SecurityException {
    if (params == null) {
      return obj.getClass().getMethod(methodName);
    } else {
      int i = 0;
      Class<?>[] paramClasses = new Class<?>[params.length];
      for (Object param : params) {
        paramClasses[i++] = param.getClass();
      }
      return obj.getClass().getDeclaredMethod(methodName, paramClasses);
    }
  }

  /**
   * Get a non-public (i.e. declared) static method from a class.
   * @param clz The class.
   * @param methodName A method name.
   * @param paramClasses Classes of the optional parameters.
   * @return A Method object.
   * @throws NoSuchMethodException
   * @throws SecurityException
   */
  public static Method getMethod(Class<?> clz, String methodName, Class<?>... paramClasses)
      throws NoSuchMethodException, SecurityException {
    if (paramClasses == null) {
      return clz.getMethod(methodName);
    } else {
      return clz.getDeclaredMethod(methodName, paramClasses);
    }
  }

  /**
   * Get a non-public (i.e. declared) method from an object.
   * @param obj The object to be used.
   * @param methodName A method name.
   * @param paramClasses Classes of the optional parameters.
   * @return A Method object.
   * @throws NoSuchMethodException
   * @throws SecurityException
   */
  public static Method getMethod(Object obj, String methodName, Class<?>... paramClasses)
      throws NoSuchMethodException, SecurityException {
    if (paramClasses == null) {
      return obj.getClass().getMethod(methodName);
    } else {
      return obj.getClass().getDeclaredMethod(methodName, paramClasses);
    }
  }

  /**
   * Invoke a non-public method from an object.  To invoke a static non-public
   * method, <code>obj</code> is ignored.  This invocation may fail if
   * SecurityManager is used to enforce access permission.
   * @param obj The object to be used, or null
   * @param method A Method object with the matching parameter types.
   * @param params Optional parameters.
   * @return Optional result object, or Void.
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  public static Object invokeMethod(Object obj, Method method, Object... params)
                    throws IllegalAccessException, IllegalArgumentException,
                            InvocationTargetException {
    boolean accessible = true;
    try {
      if (!(accessible = method.isAccessible())) {
        method.setAccessible(true);
      }
      return method.invoke(obj, params);
    } finally {
      if ((method != null) && !accessible) {
        method.setAccessible(false);
      }
    }
  }

  /**
   * Invoke a non-public static method by name from a class.  This invocation
   * may faile if SecurityManager is used to enforce access permission.
   * @param clz The class.
   * @param methodName The static method name to be invoked.
   * @param params Optional parameters.
   * @return Optional result object, or Void.
   * @throws NoSuchMethodException
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  public static Object invokeMethod(Class<?> clz, String methodName, Object... params)
      throws NoSuchMethodException, SecurityException, IllegalAccessException,
              IllegalArgumentException, InvocationTargetException
  {
    Method method = getMethod(clz, methodName, params);
    return invokeMethod(null, method, params);
  }

  /**
   * Invoke a non-public method by name from an object.  This invocation may
   * fail if SecurityManager is used to enforce access permission.
   * @param obj The object to be used.
   * @param methodName A method name to be invoked.
   * @param params Optional parameters
   * @return Optional result object, or Void.
   * @throws NoSuchMethodException
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  public static Object invokeMethod(Object obj, String methodName, Object... params)
      throws NoSuchMethodException, SecurityException, IllegalAccessException,
              IllegalArgumentException, InvocationTargetException {
    Method method = getMethod(obj, methodName, params);
    return invokeMethod(obj, method, params);
  }

  /**
   * Estimate the total size of all values in a Map.
   * @param props
   * @return
   */
  private static int valuesSize(Map<String, String> props) {
    int size = 0;
    for (String value : props.values()) {
      size += value.length();
    }
    return size;
  }

  /**
   * Evaluate a template and replace all ${var} with values.  '\' is an escaped
   * character for '$', but nested escape is not supported yet.
   * @param template A in-memory template.
   * @param props
   * @return
   */
  public static CharSequence eval(String template, Map<String, String> props) {
    int dollar, end, start = 0;
    StringBuilder sb = new StringBuilder(template.length()+valuesSize(props));
    while ((dollar = template.indexOf('$', start)) >= 0) {
      if (dollar > 0 && template.charAt(dollar-1) == '\\') {
        sb.append(template.substring(start, dollar-1));
        sb.append('$');
        start = dollar + 1;
      } else if ((template.charAt(dollar+1) != '{') ||
                  (end = template.indexOf('}', dollar+1)) <= 0) {
        sb.append(template.substring(start, dollar));
        sb.append('$');
        start = dollar + 1;
      } else {
        sb.append(template.substring(start, dollar));
        String name = template.substring(dollar+2, end);
        String value = props.get(name);
        if (value != null) {
          sb.append(value);
        }
        start = end + 1;
      }
    }
    sb.append(template.substring(start));
    return sb;
  }

  /**
   * Evaluate a template file and replace all ${var} with values.  '\' is an
   * escaped character for '$', but nested escape is not supported yet.
   * @param template A file-based template.
   * @param props
   * @return
   * @throws IOException
   */
  public static CharSequence eval(File template, Map<String, String> props)
                              throws IOException {
    BufferedReader reader = null;
    try {
      String line;
      reader = new BufferedReader(new FileReader(template));
      StringBuilder sb = new StringBuilder((int) template.length()+valuesSize(props));
      while ((line = reader.readLine()) != null) {
        int dollar, end, start = 0;
        while ((dollar = line.indexOf('$', start)) >= 0) {
          if (dollar > 0 && line.charAt(dollar-1) == '\\') {
            sb.append(line.substring(start, dollar-1));
            sb.append('$');
            start = dollar + 1;
          } else if ((line.charAt(dollar+1) != '{') ||
                      (end = line.indexOf('}', dollar+1)) <= 0) {
            sb.append(line.substring(start, dollar));
            sb.append('$');
            start = dollar + 1;
          } else {
            sb.append(line.substring(start, dollar));
            String name = line.substring(dollar+2, end);
            String value = props.get(name);
            if (value != null) {
              sb.append(value);
            }
            start = end + 1;
          }
        }
        sb.append(line.substring(start));
      }
      return sb;
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }

  /**
   * Get the subsequence from head and tail of given size.  If the
   * <code>size</code> is larger or equal to the length of <code>cs</code>, the
   * original <code>cs</code> will be returned.
   * @param cs The char sequence.
   * @param size The subsequence size.
   * @return A subsequence from head adn tail.
   */
  public static CharSequence subSequenceHeadTail(CharSequence cs, int size)  {
    if (cs.length() < size) {
      return cs;
    }
    CharSequence tail;
    int end = Math.min((size/2), cs.length());
    int start = Math.max(end, cs.length()-(size/2));
    if ((tail = cs.subSequence(start, cs.length())) != null) {
      return cs.subSequence(0, end).toString() + "..." + tail;
    } else {
      return cs.subSequence(0, end);
    }
  }

  /**
   * Construct and return a ISO8601 date format with timezone set to UTC.
   * @return DateFormat
   */
  public static DateFormat buildISO8601DateFormat () {
    TimeZone utc = TimeZone.getTimeZone("UTC");
    SimpleDateFormat sDateTimeFormat = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
    sDateTimeFormat.setTimeZone(utc);
    return sDateTimeFormat;
  }

  /**
   * Escapes all necessary characters in the char sequence so that it can be
   * used in an XML doc.  For any large String, the caller should break it into
   * chunks, or encode it to a file.
   * @param string the char sequence to escape.
   * @return the string with appropriate characters escaped.
   */
  public static CharSequence escapeForXML(final CharSequence string) {
    if (string == null) {
      return null;
    }
    final int len = string.length();
    final StringBuilder out = new StringBuilder((int) (len * 1.3));
    int last = 0;
    int i = 0;
    while (i < len) {
      CharSequence toAppend = null;
      char ch = string.charAt(i);
      switch (ch) {
      case '<':
        toAppend = LT_ENCODE;
        break;
      case '>':
        toAppend = GT_ENCODE;
        break;
      case '&':
        toAppend = AMP_ENCODE;
        break;
      case '"':
        toAppend = QUOTE_ENCODE;
        break;
      case '\'':
        toAppend = APOS_ENCODE;
        break;
      default:
        break;
      }
      if (toAppend == null) {
        ++i;
      } else {
        if (i > last) {
          out.append(string, last, i);
        }
        out.append(toAppend);
        last = ++i;
      }
    }
    if (last == 0) {
      // Nothing to escape.
      return string;
    }
    if (i > last) {
      out.append(string, last, i);
    }
    return out;
  }

  public static boolean isNullOrEmpty(Collection c) {
    return (c == null) || (c.size() == 0);
  }

  /**
   * Escapes the node portion of a JID according to "JID Escaping" (XEP-0106).
   * Escaping replaces characters prohibited by node-prep with escape sequences,
   * as follows:<p>
   *
   * <table border="1">
   * <tr><td><b>Unescaped Character</b></td><td><b>Encoded Sequence</b></td></tr>
   * <tr><td>&lt;space&gt;</td><td>\20</td></tr>
   * <tr><td>"</td><td>\22</td></tr>
   * <tr><td>&</td><td>\26</td></tr>
   * <tr><td>'</td><td>\27</td></tr>
   * <tr><td>/</td><td>\2f</td></tr>
   * <tr><td>:</td><td>\3a</td></tr>
   * <tr><td>&lt;</td><td>\3c</td></tr>
   * <tr><td>&gt;</td><td>\3e</td></tr>
   * <tr><td>@</td><td>\40</td></tr>
   * <tr><td>\</td><td>\5c</td></tr>
   * </table><p>
   *
   * This process is useful when the node comes from an external source that
   * doesn't conform to nodeprep. For example, a username in LDAP may be
   * "Joe Smith". Because the &lt;space&gt; character isn't a valid part of a
   * node, the username should be escaped to "Joe\20Smith" before being made
   * into a JID (e.g. "joe\20smith@example.com" after case-folding, etc. has
   * been applied).<p>
   *
   * All node escaping and un-escaping must be performed manually at the
   * appropriate time; the JID class will not escape or un-escape automatically.
   *
   * This code is copied from org.jivesoft.smack.StringUtils so it can be used
   * by client and server.
   *
   * @param node the node.
   * @return the escaped version of the node.
   */
  public static String escapeNode(String node) {
    if (node == null) {
      return null;
    }
    StringBuilder buf = new StringBuilder(node.length() + 8);
    for (int i = 0, n = node.length(); i < n; i++) {
      char c = node.charAt(i);
      switch (c) {
      case '"':
        buf.append("\\22");
        break;
      case '&':
        buf.append("\\26");
        break;
      case '\'':
        buf.append("\\27");
        break;
      case '/':
        buf.append("\\2f");
        break;
      case ':':
        buf.append("\\3a");
        break;
      case '<':
        buf.append("\\3c");
        break;
      case '>':
        buf.append("\\3e");
        break;
      case '@':
        buf.append("\\40");
        break;
      case '\\':
        buf.append("\\5c");
        break;
      default:
        if (Character.isWhitespace(c)) {
          buf.append("\\20");
        } else {
          buf.append(c);
        }
      }
    }
    return buf.toString();
  }

  /**
   * Un-escapes the node portion of a JID according to "JID Escaping" (XEP-0106).
   * <p>  Escaping replaces characters prohibited by node-prep with escape
   * sequences, as follows:<p>
   *
   * <table border="1">
   * <tr><td><b>Unescaped Character</b></td><td><b>Encoded Sequence</b></td></tr>
   * <tr><td>&lt;space&gt;</td><td>\20</td></tr>
   * <tr><td>"</td><td>\22</td></tr>
   * <tr><td>&</td><td>\26</td></tr>
   * <tr><td>'</td><td>\27</td></tr>
   * <tr><td>/</td><td>\2f</td></tr>
   * <tr><td>:</td><td>\3a</td></tr>
   * <tr><td>&lt;</td><td>\3c</td></tr>
   * <tr><td>&gt;</td><td>\3e</td></tr>
   * <tr><td>@</td><td>\40</td></tr>
   * <tr><td>\</td><td>\5c</td></tr>
   * </table><p>
   *
   * This process is useful when the node comes from an external source that doesn't
   * conform to nodeprep. For example, a username in LDAP may be "Joe Smith". Because
   * the &lt;space&gt; character isn't a valid part of a node, the username should
   * be escaped to "Joe\20Smith" before being made into a JID (e.g. "joe\20smith@example.com"
   * after case-folding, etc. has been applied).<p>
   *
   * All node escaping and un-escaping must be performed manually at the appropriate
   * time; the JID class will not escape or un-escape automatically.
   *
   * This code is copied from org.jivesoft.smack.StringUtils so it can be used
   * by client and server.
   *
   * @param node the escaped version of the node.
   * @return the un-escaped version of the node.
   */
  public static String unescapeNode(String node) {
    if (node == null) {
      return null;
    }
    char[] nodeChars = node.toCharArray();
    StringBuilder buf = new StringBuilder(nodeChars.length);
    for (int i = 0, n = nodeChars.length; i < n; i++) {
      compare: {
        char c = node.charAt(i);
        if (c == '\\' && i + 2 < n) {
          char c2 = nodeChars[i + 1];
          char c3 = nodeChars[i + 2];
          if (c2 == '2') {
            switch (c3) {
            case '0':
              buf.append(' ');
              i += 2;
              break compare;
            case '2':
              buf.append('"');
              i += 2;
              break compare;
            case '6':
              buf.append('&');
              i += 2;
              break compare;
            case '7':
              buf.append('\'');
              i += 2;
              break compare;
            case 'f':
              buf.append('/');
              i += 2;
              break compare;
            }
          } else if (c2 == '3') {
            switch (c3) {
            case 'a':
              buf.append(':');
              i += 2;
              break compare;
            case 'c':
              buf.append('<');
              i += 2;
              break compare;
            case 'e':
              buf.append('>');
              i += 2;
              break compare;
            }
          } else if (c2 == '4') {
            if (c3 == '0') {
              buf.append("@");
              i += 2;
              break compare;
            }
          } else if (c2 == '5') {
            if (c3 == 'c') {
              buf.append("\\");
              i += 2;
              break compare;
            }
          }
        }
        buf.append(c);
      }
    }
    return buf.toString();
  }
}

