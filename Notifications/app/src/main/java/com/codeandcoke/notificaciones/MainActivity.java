package com.codeandcoke.notificaciones;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codeandcoke.notificaciones.util.NotificationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String CHANNEL_ID = "notificationes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);

        NotificationUtils.createNotificationChannel(this, CHANNEL_ID);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, OtherActivity.class);
        PendingIntent otraIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationUtils.showNotification(this, R.string.mAviso, R.string.mMensajeAviso, CHANNEL_ID, otraIntent);
    }
}
