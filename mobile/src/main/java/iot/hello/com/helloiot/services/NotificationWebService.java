package iot.hello.com.helloiot.services;

import io.reactivex.Single;
import iot.hello.com.helloiot.model.InstanceDto;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotificationWebService {
	@POST("subscribe") Single<InstanceDto> subscribe(@Body InstanceDto instanceDto);
}
