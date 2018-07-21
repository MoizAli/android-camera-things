package iot.hello.com.helloiot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import iot.hello.com.helloiot.services.ImageUploadService;
import iot.hello.com.helloiot.services.ImageUploaderService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends Activity {

	private static final String TAG = "MAT14";
	CameraUtil mCamera;
	ImageView imageView1;
	ImageUploadService imageUploadService;
	ImageUploaderService imageUploaderService;
	/**
	 * An additional thread for running Camera tasks that shouldn't block the UI.
	 */
	private HandlerThread mCameraThread;
	/**
	 * A {@link Handler} for running Camera tasks in the background.
	 */
	private Handler mCameraHandler;
	private Handler mainHandler;

	private ButtonInputDriver mButtonInputDriver;
	/**
	 * Listener for new camera images.
	 */
	private ImageReader.OnImageAvailableListener mOnImageAvailableListener = reader -> {
		Image image = reader.acquireLatestImage();
		// get image bytes
		ByteBuffer imageBuf = image.getPlanes()[0].getBuffer();
		final byte[] imageBytes = new byte[imageBuf.remaining()];
		imageBuf.get(imageBytes);
		image.close();

		onPictureTaken(imageBytes);
	};

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView1 = findViewById(R.id.imageView1);

		imageUploadService = new ImageUploadService();
		imageUploaderService = imageUploadService.provideImageService();

		// Creates new handlers and associated threads for camera and networking operations.
		mCameraThread = new HandlerThread("CameraBackground");
		mCameraThread.start();
		mCameraHandler = new Handler(mCameraThread.getLooper());

		mainHandler = new Handler(Looper.getMainLooper());

		initPIO();
		// Camera code is complicated, so we've shoved it all in this closet class for you.
		mCamera = CameraUtil.getInstance();
		mCamera.initializeCamera(this, mCameraHandler, mOnImageAvailableListener);
	}

	private void initPIO() {
		try {
			Button button = RainbowHat.openButtonA();
			button.setOnButtonEventListener((button1, pressed) -> {
				if (pressed) {
					mCamera.takePicture();
				}
			});
		} catch (IOException e) {
			mButtonInputDriver = null;
			Log.w(TAG, "Could not open GPIO pins", e);
		}
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		mCamera.shutDown();

		mCameraThread.quitSafely();
		try {
			mButtonInputDriver.close();
		} catch (IOException e) {
			Log.e(TAG, "button driver error", e);
		}
	}

	private void onPictureTaken(byte[] imageBytes) {
		Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

		//create a file to write bitmap data
		File file = new File(getCacheDir(), "image.png");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Convert bitmap to byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 1 /*ignored for PNG*/, bos);
		byte[] bitmapdata = bos.toByteArray();

		//write the bytes in file
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(bitmapdata);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
		imageUploaderService.uploadImage(filePart).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe((uploadFileResponse, throwable) -> {
		});

		mainHandler.post(() -> imageView1.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView1.getWidth(), imageView1.getHeight(), false)));

	}
}
