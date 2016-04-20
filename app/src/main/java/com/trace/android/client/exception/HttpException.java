package com.trace.android.client.exception;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public class HttpException extends Exception{
    private static final long serialVersionUID = 1L;

    public enum ExceptionStatus{
        FileNotFoundException, IllegalStateException, ParseException, IOException,
        CancelException, ServerException, ParameterException, TimeOutException,
        InstantiationException, IllegalAccessException, UnSupportedEncodingException,
        ParseJsonException, XMLPullParseException, NoSuchMethodException, IllegalArgumentException,
        ExceptionStatus, InvocationTargetException
    }

    private ExceptionStatus status;

    public HttpException(ExceptionStatus status, String detailMessage){
        super(detailMessage);
        this.status = status;
    }

    public ExceptionStatus getExceptionStatus(){
        return this.status;
    }
}
