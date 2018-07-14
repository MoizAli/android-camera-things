package iot.hello.com.helloiot.services;

import io.reactivex.Single;
import iot.hello.com.helloiot.model.UploadFileResponse;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageUploaderService {
	@Multipart
	@POST("uploadFile") Single<UploadFileResponse> uploadImage(@Part MultipartBody.Part filePart);
}
