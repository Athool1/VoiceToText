package com.example.speechtextconverter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

// import android.widget.Button;

public class MainActivity extends Activity {

	protected static final int RESULT_SPEECH = 1;

	private ImageButton btnSpeak;
	private TextView txtText;

	// private Button button1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtText = (TextView) findViewById(R.id.txtText);

		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

				try {
					startActivityForResult(intent, RESULT_SPEECH);
					txtText.setText("");
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Opps! Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_uninstall:
			actionUninstall();
			return true;
		case R.id.action_info:
			actionInformation();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Shows App Information
	private void actionInformation() {
		// TODO Auto-generated method stub
		setContentView(R.layout.voice_info);
		Button btnBack1 = (Button) findViewById(R.id.button1);

		btnBack1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle savedInstanceState = null;
				// TODO Auto-generated method stub
				onCreate(savedInstanceState);
			}
		});

	}

	// Uninstall App from device.
	@SuppressLint("InlinedApi")
	private void actionUninstall() {
		// TODO Auto-generated method stub
		Uri packageUri = Uri.parse("package:com.example.speechtextconverter");
		Intent uninstalIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE,
				packageUri);
		startActivity(uninstalIntent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				txtText.setText(text.get(0));
			}
			break;
		}

		}
	}

	/** Called when the user clicks the Next button */
	public void nextActivity(View view) {
		// Do something in response to button

		Intent intent = new Intent(this, ContactInformation.class);
		String txt = (String) txtText.getText();
		intent.putExtra("Message", txt);
		startActivity(intent);

	}
}