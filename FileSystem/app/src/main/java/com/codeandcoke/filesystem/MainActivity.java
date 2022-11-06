package com.codeandcoke.filesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etContent, etLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLog = findViewById(R.id.etLog);
        etContent = findViewById(R.id.etContent);
        Button btSave = findViewById(R.id.btSave);
        btSave.setOnClickListener(this);

        loadContent();
        listAndDeletePrivateFiles();
        createInternalCacheFile();
        createExternalCacheFile();
        accessExternalStorage();
    }

    private void accessExternalStorage() {
        File documentDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(documentDirectory, "Documento de texto.txt");
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(file));
            writer.println("Esto es un documento de texto en memoria externa");
            writer.close();
        } catch (FileNotFoundException fnfe) {
            Toast.makeText(this, "No se ha podido escribir en Documentos", Toast.LENGTH_LONG).show();
        }
    }

    private void createExternalCacheFile() {
        try {
            File cacheFile = File.createTempFile("file", null, getExternalCacheDir());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void createInternalCacheFile() {
        try {
            File cacheFile = File.createTempFile("file", null, getCacheDir());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void listAndDeletePrivateFiles() {
        String[] files = fileList();
        for (String filename : files) {
            etLog.append(filename);
            deleteFile(filename);
        }
    }

    private void loadContent() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("fichesdgsro.txt")));
            String content = null;
            String line = null;
            while ((line = reader.readLine()) != null) {
                content += line;
            }

            etContent.setText(content);
            etLog.append("Fichero leído\n");
        } catch (IOException ioe) {
            Toast.makeText(this, "No se ha podido leer el fichero", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        String content = etContent.getText().toString();
        try {
            FileOutputStream fos = openFileOutput("fichero2.txt", Context.MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(fos);
            writer.println(content);
            writer.close();

            etLog.append("Fichero creado\n");
        } catch (FileNotFoundException fnfe) {
            Toast.makeText(this, "Fichero no encontrado", Toast.LENGTH_LONG).show();
        }
    }
}
