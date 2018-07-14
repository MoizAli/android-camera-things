package iot.hello.com.helloiot.services;

import retrofit2.Retrofit;

public class NotificationService extends WebService {

	public NotificationWebService providNotificationWebService() {
		return makeRestAdapterBuilderNotification().build().create(NotificationWebService.class);
	}

	public Retrofit.Builder makeRestAdapterBuilderNotification() {
		String url = "http://192.168.0.125:8080/notification/";
		return createRestAdapterBuilderFromBaseURL(url);
	}
}
