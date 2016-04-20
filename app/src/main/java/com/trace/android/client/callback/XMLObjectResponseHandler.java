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
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public abstract class XMLObjectResponseHandler<T> extends AbsResponseHandler<T> {

    @Override
    public T bindData(byte[] data) throws HttpException {
        try {
            Type clazzType = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Class<T> clazz = ((Class<T>)clazzType);
            Field[] fields = clazz.getDeclaredFields();
            Map<String, Field> reflect = new HashMap<String, Field>();
            for (Field field: fields) {
                field.setAccessible(true);
                reflect.put(field.getName(), field);
            }

            StringReader sr = new StringReader(new String(data));
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(sr);
            T t = null;
            for (int type = xmlPullParser.getEventType(); type != XmlPullParser.END_DOCUMENT; type = xmlPullParser.next()){
                if (type == XmlPullParser.START_TAG){
                    if (clazz.getSimpleName().equals(xmlPullParser.getName())){
                        t = clazz.newInstance();
                    } else {
                        String tagName = xmlPullParser.getName();
                        if (reflect.containsKey(tagName)){
                            Field field = reflect.get(tagName);
                            Class<?> paramType = field.getType();
                            String value = xmlPullParser.nextText();
                            field.set(t, getValue(value, paramType));
                        }
                    }
                }
            }
            return t;

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

    private <V> V getValue(String value, Class<V> clazz) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Method method = null;
        if(clazz == String.class){
            return (V) value;
        }else if(clazz == int.class || clazz == Integer.class){
            method = Integer.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        }else if(clazz == float.class || clazz == Float.class){
            method = Float.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        }else if(clazz == boolean.class || clazz == Boolean.class){
            method = Boolean.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        }else if(clazz == long.class || clazz == Long.class){
            method = Long.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        }else if(clazz == double.class || clazz == Double.class){
            method = Double.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        }else if(clazz == short.class || clazz == Short.class){
            method = Short.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        }else if(clazz == char.class || clazz == Character.class){
            method = Character.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        }else if(clazz == byte.class || clazz == Byte.class){
            method = Byte.class.getDeclaredMethod("valueOf", String.class);
            return (V) method.invoke(null, value);
        }else{
            return null;
        }
    }
}
