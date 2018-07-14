package iot.hello.com.helloiot.services;

import android.content.Context;
import com.google.gson.GsonBuilder;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageUploadService extends WebService{

	public ImageUploaderService provideImageService() {
		return makeRestAdapterBuilderCompanionV2().build().create(ImageUploaderService.class);
	}

	public Retrofit.Builder makeRestAdapterBuilderCompanionV2() {
		String url = "http://192.168.0.125:8080/image/";
		return createRestAdapterBuilderFromBaseURL(url);
	}
}
