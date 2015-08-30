package com.pqpo.android.http.async;

import com.pqpo.android.http.HttpException;
import com.pqpo.android.http.HttpRequest;
import com.pqpo.android.http.HttpResponse;
import com.pqpo.android.http.HttpTransfer;

import android.os.AsyncTask;

public abstract class HttpAsync extends AsyncTask<HttpRequest, Integer, HttpResponse> {
    private HttpException exception = new HttpException("未知错误");
    
    @Override  
    protected HttpResponse doInBackground(HttpRequest... params) {
        try {
            return HttpTransfer.execute(params[0]);
        } catch (HttpException e) {
            exception = e;
            return null;
        }
    }
    
    @Override  
    protected void onPostExecute(HttpResponse response) {
        if(response != null) {
        	try {
				String body = response.body(response.getCharset());
				if(body!=null&&!body.equals("")){
//					int ret = JsonUtil.getRet(body);
//					if(ret==0){
//						onSuccess(body);
//					}else{
//						onFail(new ApiException(ret));
//					}
					onSuccess(body);
				}else{
					onError(new HttpException("Body is empty!"));
				}
			} catch (HttpException e) {
				onError(e);
			}
        } else {
            onError(exception);
        }
    }

    protected abstract void onSuccess(String json);
//    protected abstract void onFail(ApiException exception);
    public abstract void onError(HttpException exception);
}