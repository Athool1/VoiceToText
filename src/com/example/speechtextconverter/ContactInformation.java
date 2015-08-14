package com.example.speechtextconverter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactInformation extends Activity {

	protected static final int PICK_CONTACT = 0;
	Button button1, button2;
	EditText editText1, email, subject, editText3;
	private int reqCode;
	private String sms, phoneNo;
	private String id;
	private String hasPhone;
	private Cursor phones;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_information);

		// To get voice text from Previous Activity
		Intent forwardMessage = getIntent();
		String txt = forwardMessage.getExtras().getString("Message");

		editText3 = (EditText) findViewById(R.id.editText3);
		editText3.setText(txt);

		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// New Intent select Contacts from Phone book
				Intent intent = new Intent(Intent.ACTION_PICK,
						Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);

			}
		});

		editText1 = (EditText) findViewById(R.id.editText1);
		button2 = (Button) findViewById(R.id.button2);
		email = (EditText) findViewById(R.id.editText2);
		subject = (EditText) findViewById(R.id.editText4);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Performs Send Operation on Network Carrier

				phoneNo = editText1.getText().toString();
				sms = editText3.getText().toString();
				String sbj = subject.getText().toString();
				String message = editText3.getText().toString();
				String to = email.getText().toString();

				if (phoneNo != null && to == null) {

					try {
						SmsManager smsManager = SmsManager.getDefault();
						smsManager.sendTextMessage(phoneNo, null, sms, null,
								null);
						Toast.makeText(getApplicationContext(), "SMS Sent!",
								Toast.LENGTH_LONG).show();

						setContentView(R.layout.activity_main);
					} catch (Exception e) {

						Toast.makeText(getApplicationContext(),
								"SMS failed, please try again later!",
								Toast.LENGTH_LONG).show();
						e.printStackTrace();

					}
				} else {
					try {
						// Run Email Activity
						Intent emailActivity = new Intent(Intent.ACTION_SEND);

						// set up the recipient address
						emailActivity.putExtra(Intent.EXTRA_EMAIL,
								new String[] { to });

						// set up the email subject
						emailActivity.putExtra(Intent.EXTRA_SUBJECT, sbj);

						// you can specify cc addresses as well
						// email.putExtra(Intent.EXTRA_CC, new String[]{ ...});
						// email.putExtra(Intent.EXTRA_BCC, new String[]{ ... }
						// set up the message body
						emailActivity.putExtra(Intent.EXTRA_TEXT, message);
						emailActivity.setType("message/rfc822");
						startActivity(Intent.createChooser(emailActivity,
								"Select your Email Provider :"));

						setContentView(R.layout.activity_main);
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								"Email failed, please try again later!",
								Toast.LENGTH_LONG).show();
						e.printStackTrace();

					}
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_information, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	// Display Contacts List on Clicking Contacts Button.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (reqCode) {
		case (PICK_CONTACT):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = managedQuery(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					id = c.getString(c.getColumnIndexOrThrow(BaseColumns._ID));
					hasPhone = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

					if (hasPhone.equalsIgnoreCase("1")) {
						phones = getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = " + id, null, null);
						phones.moveToFirst();
					}

					phoneNo = phones.getString(phones.getColumnIndex("data1"));
					c.getString(c
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					editText1 = (EditText) findViewById(R.id.editText1);
					editText1.setText(phoneNo);
				}
			}
			break;
		}
	}
}