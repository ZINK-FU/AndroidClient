package com.trace.android.client.callback;

import com.trace.android.client.exception.HttpException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public abstract class XMLArrayResponseHandler<T> extends AbsResponseHandler<ArrayList<T>> {

    @Override
    public ArrayList<T> bindData(byte[] data) throws HttpException {
        try {
            Type clazzType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Class<T> clazz = ((Class<T>) clazzType);
            Field[] fields = clazz.getDeclaredFields();
            HashMap<String, Field> reflect = new HashMap<String, Field>();
            for (Field field : fields) {
                field.setAccessible(true);
                reflect.put(field.getName(), field);
            }

            StringReader sr = new StringReader(new String(data));
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(sr);
            T t = null;

            ArrayList<T> datas = new ArrayList<T>();
            for (int type = parser.getEventType(); type != XmlPullParser.END_DOCUMENT; type = parser.next()) {
                if (type == XmlPullParser.START_TAG) {
                    if (clazz.getSimpleName().equals(parser.getName())) {
                        t = clazz.newInstance();
                        datas.add(t);
                    } else {
                        String name = parser.getName();
                        if (reflect.containsKey(name)) {
                            Field field = reflect.get(name);
                            Class<?> paramType = field.getType();
                            String value = parser.nextText();
                            field.set(t, getValue(value, paramType));
                        }
                    }
                }
            }
            return datas;
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(HttpException.ExceptionStatus.UnSupportedEncodingException, "unsupported encoding");
        } catch (XmlPullParserException e) {
            throw new HttpException(HttpException.ExceptionStatus.XMLPullParseException, "get parser failed");
        } catch (IOException e) {
            throw new HttpException(HttpException.ExceptionStatus.IOException, "parser xml failed");
        } catch (InstantiationException e) {
            throw new HttpException(HttpException.ExceptionStatus.InstantiationException, e.getMessage());
        } catch (IllegalAccessException e) {
            throw new HttpException(HttpException.ExceptionStatus.IllegalAccessException, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new HttpException(HttpException.ExceptionStatus.IllegalArgumentException, e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new HttpException(HttpException.ExceptionStatus.NoSuchMethodException, e.getMessage());
        } catch (InvocationTargetException e) {
            throw new HttpException(HttpException.ExceptionStatus.InvocationTargetException, e.getMessage());
        }
    }

    //将字符串转化成基本数据类型或者对应的对象类型
    @SuppressWarnings("unchecked")
    private <V> V getValue(String value, Class<V> clazz) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = null;
        if (clazz == String.class) {
            return (V) value;
        } else if (clazz == int.class || clazz == Integer.class) {
            method = Integer.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        } else if (clazz == float.class || clazz == Float.class) {
            method = Float.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            method = Boolean.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        } else if (clazz == long.class || clazz == Long.class) {
            method = Long.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        } else if (clazz == double.class || clazz == Double.class) {
            method = Double.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        } else if (clazz == short.class || clazz == Short.class) {
            method = Short.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        } else if (clazz == char.class || clazz == Character.class) {
            method = Character.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        } else if (clazz == byte.class || clazz == Byte.class) {
            method = Byte.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        } else {
            return null;
        }
    }
}
