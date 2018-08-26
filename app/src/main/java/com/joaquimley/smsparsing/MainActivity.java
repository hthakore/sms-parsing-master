package com.joaquimley.smsparsing;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.joaquimley.smsparsing.sqlite.BankList;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String PREF_USER_MOBILE_PHONE = "pref_user_mobile_phone";
    private static final int SMS_PERMISSION_CODE = 0;

    private EditText mNumberEditText;
    private String mUserMobilePhone;
    private SharedPreferences mSharedPreferences;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        if (!hasReadSmsPermission()) {
            showRequestPermissionsInfoAlertDialog();
        }

        initViews();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserMobilePhone = mSharedPreferences.getString(PREF_USER_MOBILE_PHONE, "");
        if (!TextUtils.isEmpty(mUserMobilePhone)) {
            mNumberEditText.setText(mUserMobilePhone);
        }
    }

    private void initViews() {
        mNumberEditText = (EditText) findViewById(R.id.et_number);
        findViewById(R.id.btn_normal_sms).setOnClickListener(this);
        findViewById(R.id.btn_conditional_sms).setOnClickListener(this);
        findViewById(R.id.btngetmessages).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_conditional_sms:
                if (!hasValidPreConditions()) return;
                checkAndUpdateUserPrefNumber();

                //SmsHelper.sendDebugSms(String.valueOf(mNumberEditText.getText()), SmsHelper.SMS_CONDITION + " This SMS is conditional, Hello toast");
                Toast.makeText(getApplicationContext(), R.string.toast_sending_sms, Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_normal_sms:
                if (!hasValidPreConditions()) return;
                checkAndUpdateUserPrefNumber();

                //SmsHelper.sendDebugSms(String.valueOf(mNumberEditText.getText()), "The broadcast should not show a toast for this");
                Toast.makeText(getApplicationContext(), R.string.toast_sending_sms, Toast.LENGTH_SHORT).show();
                break;

            case R.id.btngetmessages:
                checkAndUpdateUserPrefNumber();
                getSMS();
                break;
        }
    }

    public void getSMS(){
        List<String> sms = new ArrayList<String>();
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

        //inserting Bank lists
        BankList sql_BankList = new BankList(context);
        String bankname = "Axis";
        if (!sql_BankList.isBankExist(bankname)) {
            ContentValues cv = new ContentValues();
            cv.put(BankList.BANKNAME, bankname);
            sql_BankList.insert(cv);
        }



        while (cur != null && cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            String date = cur.getString(cur.getColumnIndexOrThrow("date"));
            //sms.add("Number: " + address + " .Message: " + body);
            if (address.contains("AX-AxisBk")) {
                //Log.i("Message", "Number: " + address + " .Message: " + body);

                String isDebited = "";
                if (address.contains("debited"))
                    isDebited = "y";
                else
                    isDebited = "n";

                /*For finding out amount from bank transaction message.*/
                Pattern regEx = Pattern.compile("(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)");
                Pattern regEx_rem = Pattern.compile("(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)");

                /*For finding out merchant name from bank transaction message.*/
                //Pattern regEx = Pattern.compile("(?i)(?:\\sat\\s|in\\*)([A-Za-z0-9]*\\s?-?\\s?[A-Za-z0-9]*\\s?-?\\.?)");

                // Find instance of pattern matches
                Matcher m = regEx.matcher(body);
                while (m.find()){
                    String MyAmount = m.group(0);
                    //Log.i("String ",m.toString());
                    Log.i("Amount ",MyAmount);
                    //Log.i("Amount_2 ",m.group(1));

                }
            }
        }

        if (cur != null) {
            cur.close();
        }
        //return sms;
    }

    /**
     * Checks if stored SharedPreferences value needs updating and updates \o/
     */
    private void checkAndUpdateUserPrefNumber() {
        if (TextUtils.isEmpty(mUserMobilePhone) && !mUserMobilePhone.equals(mNumberEditText.getText().toString())) {
            mSharedPreferences
                    .edit()
                    .putString(PREF_USER_MOBILE_PHONE, mNumberEditText.getText().toString())
                    .apply();
        }
    }


    /**
     * Validates if the app has readSmsPermissions and the mobile phone is valid
     *
     * @return boolean validation value
     */
    private boolean hasValidPreConditions() {
        if (!hasReadSmsPermission()) {
            requestReadAndSendSmsPermission();
            return false;
        }

        if (!SmsHelper.isValidPhoneNumber(mNumberEditText.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.error_invalid_phone_number, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Optional informative alert dialog to explain the user why the app needs the Read/Send SMS permission
     */
    private void showRequestPermissionsInfoAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title);
        builder.setMessage(R.string.permission_dialog_message);
        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestReadAndSendSmsPermission();
            }
        });
        builder.show();
    }

    /**
     * Runtime permission shenanigans
     */
    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                SMS_PERMISSION_CODE);
    }
}