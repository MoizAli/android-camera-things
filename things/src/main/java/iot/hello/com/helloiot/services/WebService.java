package iot.hello.com.helloiot.services;

import com.google.gson.GsonBuilder;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebService {

	protected Retrofit.Builder createRestAdapterBuilderFromBaseURL(String base_url) {

		final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

		okHttpClientBuilder.retryOnConnectionFailure(true);

		CookieManager handler = new CookieManager();
		handler.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		okHttpClientBuilder.readTimeout(20000, TimeUnit.SECONDS).connectTimeout(20000, TimeUnit.SECONDS);

		OkHttpClient okHttpClient = okHttpClientBuilder.build();
		// Default interceptor
		return new Retrofit.Builder().client(okHttpClient).baseUrl(base_url).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(getDefaultGsonConverter(true));
	}

	private GsonConverterFactory getDefaultGsonConverter(boolean serializeNulls) {
		GsonBuilder gson = new GsonBuilder();
		if (serializeNulls) { gson.serializeNulls(); }
		return GsonConverterFactory.create(gson.create());
	}
}
