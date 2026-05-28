package com.jarredapps.digicount;

import android.app.Activity;
import android.os.Bundle;
import android.app.*;
import android.widget.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Color;

public class AboutActivity extends Activity {
    
	private Toolbar aboutToolbar;
	
	private ImageView aboutImageView;
	private Button emailButton;
	
	private int secretClicks;
	private boolean secretActivated;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
		
		aboutToolbar = findViewById(R.id.aboutToolbar);
	    setActionBar(aboutToolbar);
        aboutToolbar.setTitleTextColor(Color.WHITE);
		
		aboutToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		setTitle("About DigiCounter");
        getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		aboutImageView = findViewById(R.id.aboutImageView);
		emailButton = findViewById(R.id.emailButton);
		
		secretClicks = 0;
		secretActivated = false;
		
		aboutImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				secretClicks++;
				if (secretClicks == 3) {
					secretActivated = true;
				}
			}
		});
		
		emailButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (secretActivated == true) {
					startActivity(new Intent(getApplicationContext(), SecretActivity.class));
				}
				else {
					emailDialog();
				}
			}
		});
    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		secretClicks = 0;
		secretActivated = false;
		super.onPause();
	}
	
	public void emailDialog() {
		final Dialog emailDlg = new Dialog(AboutActivity.this);

		LayoutInflater inflater = getLayoutInflater();
		View emailDlgView = inflater.inflate(R.layout.emaildialog, null);
		emailDlg.setContentView(emailDlgView);

		final EditText bugsText = emailDlgView.findViewById(R.id.bugsText);
		final EditText featureText = emailDlgView.findViewById(R.id.featureText);
		final EditText nameText = emailDlgView.findViewById(R.id.nameText);
		final EditText expectedBehaviorText = emailDlgView.findViewById(R.id.expectedBehaviorText);
		final EditText actualBehaviorText = emailDlgView.findViewById(R.id.actualBehaviorText);
		final EditText featureReasonsText = emailDlgView.findViewById(R.id.featureReasonsText);
		final EditText suggestedBehaviorText = emailDlgView.findViewById(R.id.suggestedBehaviorText);

		Button submitButton = emailDlgView.findViewById(R.id.button1);
		Button cancelButton = emailDlgView.findViewById(R.id.button2);

		submitButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View _view) {
					Boolean emptyCheck;
					if (bugsText.getText().toString().isEmpty())
					{
						emptyCheck = true;
					}
					else {
						emptyCheck = false;
					}

					if (featureText.getText().toString().isEmpty())
					{
						emptyCheck = true;
					}
					else {
						emptyCheck = false;
					}

					if (nameText.getText().toString().isEmpty())
					{
						emptyCheck = true;
					}
					else {
						emptyCheck = false;
					}

					if (expectedBehaviorText.getText().toString().isEmpty())
					{
						emptyCheck = true;
					}
					else {
						emptyCheck = false;
					}

					if (actualBehaviorText.getText().toString().isEmpty())
					{
						emptyCheck = true;
					}
					else {
						emptyCheck = false;
					}

					if (featureReasonsText.getText().toString().isEmpty())
					{
						emptyCheck = true;
					}
					else {
						emptyCheck = false;
					}

					if (suggestedBehaviorText.getText().toString().isEmpty())
					{
						emptyCheck = true;
					}
					else {
						emptyCheck = false;
					}

					if (emptyCheck == false)
					{
						StringBuilder text = new StringBuilder();
						final String bugsTextData = bugsText.getText().toString();
						final String featureTextData = featureText.getText().toString();
						final String nameTextData = nameText.getText().toString();

						final String expectedBehaviorTextData = expectedBehaviorText.getText().toString();
						final String actualBehaviorTextData = actualBehaviorText.getText().toString();
						final String featureReasonsTextData = featureReasonsText.getText().toString();
						final String suggestedBehaviorTextData = suggestedBehaviorText.getText().toString();

						text.append("Hello, Jarred Apps").append("\n");
						text.append("\n");

						text.append("This is me, ").append(nameTextData).append(".\n");
						text.append("\n");

						text.append("I hope you're doing well.").append("\n");
						text.append("\n");

						text.append("I’d like to report an issue I encountered in the app.").append("\n");
						text.append("\n");

						text.append("Issue:").append("\n");
						text.append(bugsTextData).append("\n");
						text.append("\n");

						text.append("The Expected Behavior:").append("\n");
						text.append(expectedBehaviorTextData).append("\n");
						text.append("\n");

						text.append("The Actual Behavior:").append("\n");
						text.append(actualBehaviorTextData).append("\n");
						text.append("\n");

						text.append("Issue:").append("\n");
						text.append(bugsTextData).append("\n");
						text.append("\n");

						text.append("Additional Details:").append("\n");
						text.append("Version: ").append("1.0").append("\n");
						String manufacturer = android.os.Build.MANUFACTURER;
						String model = android.os.Build.MODEL;

						String deviceName;

						if (model.startsWith(manufacturer)) {
							deviceName = capitalize(model);
						} else {
							deviceName = capitalize(manufacturer) + " " + model;
						}
						text.append("Device: ").append(deviceName).append("\n");
						text.append("Android Version: ").append(android.os.Build.VERSION.RELEASE).append(" ").append(android.os.Build.VERSION.CODENAME);
						text.append("\n").append("\n");

						text.append("Also, I’d like to suggest a feature that could improve the user experience.").append("\n");
						text.append("\n");

						text.append("Features request:").append("\n");
						text.append(featureTextData).append("\n");
						text.append("\n");

						text.append("Reasons:").append("\n");
						text.append(featureReasonsTextData).append("\n");
						text.append("\n");

						text.append("Suggested Behavior:").append("\n");
						text.append(suggestedBehaviorTextData).append("\n");
						text.append("\n");

						text.append("Thank you for considering this suggestion. I appreciate your work on the app.").append("\n");
						text.append("\n");
						text.append("Best Regards,\n").append(nameTextData);

						emailFeedback(text.toString());
					}
					else if (emptyCheck == true)
					{
						Toast toast = Toast.makeText(getApplicationContext(), "You need to enter all of the blank text boxes", Toast.LENGTH_LONG);
						View view = toast.getView();

						view.setBackgroundColor(Color.RED);

						TextView text = view.findViewById(android.R.id.message);
						text.setTextColor(Color.GREEN);
						text.setTextSize(16);

						toast.show();
					}
				}
			});

		cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View _view) {
					emailDlg.dismiss();
				}
			});

		emailDlg.setCancelable(true);
		emailDlg.show();
	}

	private String capitalize(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		char first = str.charAt(0);
		if (Character.isUpperCase(first)) {
			return str;
		} else {
			return Character.toUpperCase(first) + str.substring(1);
		}
	}

	private void emailFeedback(String text) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:")); // Only email apps handle this
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"reyesgavinjarred@gmail.com"});
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback or New Feature Request on DigiCounter");
		emailIntent.putExtra(Intent.EXTRA_TEXT, text);

		try {
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "No email apps installed.", Toast.LENGTH_SHORT).show();
		}
	}
}
