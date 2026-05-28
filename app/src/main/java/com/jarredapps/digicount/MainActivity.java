package com.jarredapps.digicount;
 
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import org.json.JSONArray;

// you can use gson, it is already installed but it will make crashes
//import com.google.gson.*;
//import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity {
    
	private Toolbar mainToolbar;
	
	private Button increaseButton;
	private Button decreaseButton;
	private TextView counterText;
	
	private Intent intent;
	
	private Intent openFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
	private Intent saveFile = new Intent(Intent.ACTION_CREATE_DOCUMENT);
	private final int OPENFILE_CODE = 1001;
	private final int SAVEFILE_CODE = 1002;
	
	private int numCounts;
    
    private String fileName;
    private String counts;

    private String countsName;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {
					Manifest.permission.READ_EXTERNAL_STORAGE, 
					Manifest.permission.WRITE_EXTERNAL_STORAGE}, 
					1000);
			} else {
				initializeLogic();
			}
		} else {
			initializeLogic();
		}
    }

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	protected void initializeLogic() {
		intent = new Intent();
		
		openFile.addCategory(Intent.CATEGORY_OPENABLE);
		openFile.setType("application/json");
		openFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
		String[] mimeTypes = {"application/json", "text/plain"};
		openFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
		
		saveFile.addCategory(Intent.CATEGORY_OPENABLE);
		saveFile.setType("application/json");
		//saveFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
		String[] mimeTypes2 = {"application/json", "text/plain"};
		saveFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes2);

		mainToolbar = findViewById(R.id.mainToolbar);
		setActionBar(mainToolbar);
		getActionBar().setLogo(R.drawable.ic_launcher);
        getActionBar().setSubtitle("NoName");
        mainToolbar.setTitleTextColor(Color.WHITE);
        mainToolbar.setSubtitleTextColor(Color.GRAY);

		increaseButton = findViewById(R.id.increaseButton);
		decreaseButton = findViewById(R.id.decreaseButton);
		counterText = findViewById(R.id.counterText);
        
		if (increaseButton != null) {
			increaseButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String increaseText = increase();
						counterText.setText(increaseText);
					}
				});
		}

		if (decreaseButton != null) {
			decreaseButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String decreaseText = decrease();
						counterText.setText(decreaseText);
					}
				});
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
		{
			String increaseText = increase();
			counterText.setText(increaseText);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
		{
			String decreaseText = decrease();
			counterText.setText(decreaseText);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.openMenu: {
				startActivityForResult(openFile, OPENFILE_CODE);
			}
			return true;
			
			case R.id.saveMenu: {
                    final EditText editTextFileName = new EditText(MainActivity.this);
                    editTextFileName.setHint("Enter Counter Name");
                    
                    AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Save Counter")
                        .setIcon(R.drawable.ic_content_save_all)
                        .setView(editTextFileName)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                fileName = editTextFileName.getText().toString();
                                saveFile.putExtra(Intent.EXTRA_TITLE, fileName);
                                startActivityForResult(saveFile, SAVEFILE_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                    dialog.show();
			}
			return true;
			
			case R.id.aboutMenu: {
				startActivity(new Intent(getApplicationContext(), AboutActivity.class));
			}
			return true;
				
			case R.id.exitMenu: {
				this.onBackPressed();
			}
			return true;
			
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		AlertDialog dialog = new AlertDialog.Builder(this)
			.setTitle("Are you sure to quit?")
			.setIcon(R.drawable.ic_alert)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dia, int which) {
					finish();
				}
			})
			.setNegativeButton("No", null)
			.create();
		dialog.show();
		//super.onBackPressed();
	}
	
	private String increase() {
		numCounts++;
		
		final StringBuilder strBuilder = new StringBuilder();
		final int length = String.valueOf((long)(numCounts)).length();
		
		final String numOutput;
		numOutput = "";
		if (length == 1) {
			strBuilder.append("00000").append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (length == 2) {
			strBuilder.append("0000").append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (length == 3) {
			strBuilder.append("000").append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (length == 4) {
			strBuilder.append("00").append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (length == 5) {
			strBuilder.append("0").append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (length == 6) {
			strBuilder.append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (numCounts == 1000000) {
			numCounts = 0;
			numOutput = "000000";
			strBuilder.setLength(0);
		}
		return numOutput;
	}
	
	private String decrease() {
		numCounts--;
		
		final StringBuilder strBuilder = new StringBuilder();
		final int length = String.valueOf((long)(numCounts)).length();

		final String numOutput;
		numOutput = "";
		if (length == 1) {
			strBuilder.append("00000").append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (length == 2) {
			strBuilder.append("0000").append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (length == 3) {
			strBuilder.append("000").append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (length == 4) {
			strBuilder.append("00").append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (length == 5) {
			strBuilder.append("0").append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (length == 6) {
			strBuilder.append(String.valueOf((long)(numCounts)));
			numOutput = strBuilder.toString();
			strBuilder.setLength(0);
		}
		if (numCounts == -1) {
			numCounts = 999999;
			numOutput = "999999";
			strBuilder.setLength(0);
		}
		return numOutput;
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);

		switch (_requestCode) {
			case SAVEFILE_CODE:
				if (_resultCode == Activity.RESULT_OK) {
					Uri uri;

					if (_data.getClipData() != null) {
						uri = _data.getClipData().getItemAt(0).getUri();
					} else {
						uri = _data.getData();
					}

					if (uri != null) {
					    try {
                            ArrayList<String> counterData = new ArrayList<String>();
                            counterData.add(counterText.getText().toString());
                            counterData.add(fileName);
                            
                            getActionBar().setSubtitle(fileName);
                            
                            JSONArray jsonDataExport = new JSONArray(counterData);
                            final String jsonFileData = jsonDataExport.toString();
                            
                            try {
                                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                                outputStream.write(jsonFileData.getBytes());
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            
                            Toast.makeText(getApplicationContext(), "Saved Successfully!", 500);
                        } catch (Exception e) {
                            e.printStackTrace();
						}
                        
                        
					}
				}
				else {

				}
				break;

			case OPENFILE_CODE:
				if (_resultCode == Activity.RESULT_OK) {
					Uri uri;

					if (_data.getClipData() != null) {
						uri = _data.getClipData().getItemAt(0).getUri();
					} else {
						uri = _data.getData();
					}

					if (uri != null) {
                        final ArrayList<String> jsonData = parseJsonFromUri(uri);
                        counts = jsonData.get(0);
                        countsName = jsonData.get(1);

                        counterText.setText(counts);
                        int countsInt = Integer.parseInt(counts);
                        numCounts = countsInt;

                        getActionBar().setSubtitle(countsName);
					}
				}
				else {

				}
				break;
			default:
				break;
		}
	}
    
    private ArrayList<String> parseJsonFromUri(Uri uri) {
        ArrayList<String> stringList = new ArrayList<>();
        try {
            // Open the stream using ContentResolver
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            // Convert the collected string to a JSONArray
            JSONArray jsonArray = new JSONArray(builder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                stringList.add(jsonArray.getString(i));
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringList;
    }
}
