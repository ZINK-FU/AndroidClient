# AndroidClient
An Async HTTP Request For Android

##How to Use
```Java
String url = "";
RequestParams requestParams = new RequestParams();
//config request method
requestParams.setRequestMethod(RequestMethod.POST);
//config request tool
requestParams.setRequestTool(RequestTool.connection);

//config data
requestParams.setUrl(url);
requestParams.addHeader("", "");
requestParams.addTextEntity("name", "ZINK");
requestParams.addFileEntity("file1", file, "fileName");

//start request
HttpRequest = new HttpRequest(requestParams, new BitmapResponseHandler(){
	@Override
	public void onFailure(HttpException exception){

	}

	@Override
	public void onSuccess(Bitmap bitmap){

	}

	@Override
	public int retryCount(){
		return 3;
	}
}, new DownloadProgressListener(){
	@Override
	public void onProgressUpdateListener(int cur, int tol){

	}
}, new UploadProgressListener(){
	@Override
	public void onProgressUpdateListener(int cur, int tol){

	}
});
```

##AndroidClient Class Diagram
![image](https://github.com/ZINKCOL/AndroidClient/blob/master/AndroidClient%E6%A1%86%E6%9E%B6%E7%B1%BB%E5%9B%BE.png)

