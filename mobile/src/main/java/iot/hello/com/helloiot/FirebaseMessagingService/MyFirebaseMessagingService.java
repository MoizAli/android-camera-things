package iot.hello.com.helloiot.FirebaseMessagingService;

import android.content.Intent;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import iot.hello.com.helloiot.MainActivityApp;
import iot.hello.com.helloiot.model.InstanceDto;
import iot.hello.com.helloiot.services.NotificationService;
import iot.hello.com.helloiot.services.NotificationWebService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

	@Override public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);

		if(remoteMessage.getData().keySet().contains("fileName")) {
			Intent intent = new Intent(getBaseContext(), MainActivityApp.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("fileName", remoteMessage.getData().get("fileName"));
			intent.putExtra("fileDownloadUrl", remoteMessage.getData().get("fileDownloadUrl"));
			getApplication().startActivity(intent);
		}
	}

	@Override public void onDeletedMessages() {
		super.onDeletedMessages();
	}

	@Override public void onNewToken(String s) {
		super.onNewToken(s);
		NotificationService notificationService = new NotificationService();
		NotificationWebService notificationWebService = notificationService.providNotificationWebService();
		InstanceDto instanceDto = InstanceDto.get(s);
		notificationWebService.subscribe(instanceDto).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe((instanceDto1, throwable) -> {
			if (throwable == null) {
				Log.e("push token sent", s);
			}
		});
	}
}
