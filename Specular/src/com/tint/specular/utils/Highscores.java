package com.tint.specular.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;

public class Highscores {
	
	public static void main(String[] args) {
		HttpRequest request = new HttpRequest(HttpMethods.GET);
        request.setUrl("http://libgdx.badlogicgames.com/nightlies/dist/AUTHORS");
        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
                @Override
                public void handleHttpResponse(HttpResponse httpResponse) {
                        Gdx.app.log("HttpRequestExample", "response: " + httpResponse.getResultAsString());
                }

                @Override
                public void failed(Throwable t) {
                        Gdx.app.error("HttpRequestExample", "something went wrong", t);
                }
        });
	}
}
