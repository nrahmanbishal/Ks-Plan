package com.example.ksplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION=54;
    private static final int READ_REQUEST_CODE=64;
    RecyclerView recyclerViewTable;
    Button btnSelectFile;
    Button btnSelectBack;
    ArrayList<TaskHelper> tableTasks;
    TableAdapter tableAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);

            return;
        }

        recyclerViewTable=findViewById(R.id.recyclerview_table);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewTable.setLayoutManager(layoutManager);
        recyclerViewTable.setHasFixedSize(true);
        btnSelectFile=findViewById(R.id.btn_select_file);
        btnSelectBack=findViewById(R.id.btn_select_back);
        tableTasks=new ArrayList<>();

        btnSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent,READ_REQUEST_CODE);
            }
        });

        btnSelectBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent();
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)tableTasks);
                in.putExtra("BUNDLE",args);
                setResult(RESULT_OK, in);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();

                String dir = Environment.getExternalStorageDirectory()+"/"+path;
                File file = new File(dir);

                try {
                    FileInputStream input =new FileInputStream(file);
                    BufferedReader br=new BufferedReader(new InputStreamReader(input,"UTF-8"));
                    String line;
                    while ((line=br.readLine())!=null)
                    {
                        String[] tokens = line.split(",");
                        TaskHelper row=new TaskHelper(tokens[0],tokens[1],Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]),Integer.parseInt(tokens[4]),tokens[5]);
                        tableTasks.add(row);

                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tableAdapter=new TableAdapter(tableTasks,this);
                recyclerViewTable.setAdapter(tableAdapter);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                // User refused to grant permission.
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}