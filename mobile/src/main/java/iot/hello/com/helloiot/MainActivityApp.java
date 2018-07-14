package iot.hello.com.helloiot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class MainActivityApp extends AppCompatActivity {

	ImageView imageView;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_app);
		imageView = findViewById(R.id.image);
		onNewIntent(getIntent());
	}

	@Override protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getExtras() != null) {
			if (intent.getExtras().keySet().contains("fileName")) {
				String fileDownloadUrl = intent.getExtras().getString("fileDownloadUrl");
				Picasso.get().invalidate(fileDownloadUrl);
				Picasso.get().load(fileDownloadUrl).into(imageView);
			}
		}
	}
}
