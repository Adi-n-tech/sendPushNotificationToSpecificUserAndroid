package com.adintech.sendnotificationtospecificuser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //variables
    private String mUserToken;
    private EditText mNotificationTitle, mNotificationDescription, mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fcm settings for particular user
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            mUserToken = Objects.requireNonNull(task.getResult()).getToken();
                            Log.d("token", mUserToken);
                        }

                    }
                });

        //get views
        mNotificationTitle = findViewById(R.id.title);
        mNotificationDescription = findViewById(R.id.des);
        mToken = findViewById(R.id.token);

    }

    //on click send button
    public void send(View view) {
        if (mNotificationTitle.getText().toString().isEmpty() || mNotificationDescription.getText().toString().isEmpty() || mToken.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all data", Toast.LENGTH_SHORT).show();
        } else {
            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(mToken.getText().toString(), mNotificationTitle.getText().toString(), mNotificationDescription.getText().toString(), getApplicationContext(), MainActivity.this);
            notificationsSender.SendNotifications();
        }
    }

    //on click share button
    public void shareYourToken(View view) {
        if (!mToken.getText().toString().isEmpty()) {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mToken.getText().toString());
            startActivity(shareIntent);
        } else {
            Toast.makeText(this, "please enter token", Toast.LENGTH_SHORT).show();
        }
    }
}