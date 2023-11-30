package com.codeandcoke.permissions;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int PICK_PICTURE = 1;
    private final int PICK_CONTACT = 2;

    private ImageView imageView;
    private TextureView textureView;
    private Uri pictureUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        Button callButton = findViewById(R.id.btCall);
        callButton.setOnClickListener(this);
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btCall:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    EditText etPhone = findViewById(R.id.etPhone);
                    String phoneNumber = etPhone.getText().toString();

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel: " + phoneNumber));
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Debes conceder permiso para realizar llamadas telefónicas", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageView:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                }
        }
    }

    ActivityResultLauncher<Intent> startCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        imageView.setImageURI(pictureUri);
                    }
                }
            }
    );

    private void launchCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nueva imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Cámara");
        pictureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

        startCamera.launch(cameraIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_PICTURE:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = extras.getParcelable("data", Bitmap.class);
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);

                    // Aqui podemos recoger la imagen como Bitmap para guardarla en base de datos más adelante
                    Bitmap bitmapImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                }
                break;
            case PICK_CONTACT:

                break;
            default:
                break;
        }
    }
}
