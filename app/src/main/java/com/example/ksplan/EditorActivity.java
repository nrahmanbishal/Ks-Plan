package com.example.ksplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    Button btnSaveFile;
    ArrayList<TaskHelper> tableTasks;
    TableAdapter tableAdapter;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        context=getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);

            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();

            } else {


                Toast.makeText(this, "revoked", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");

        }


        recyclerViewTable=findViewById(R.id.recyclerview_table);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewTable.setLayoutManager(layoutManager);
        recyclerViewTable.setHasFixedSize(true);
        btnSelectFile=findViewById(R.id.btn_select_file);
        btnSelectBack=findViewById(R.id.btn_select_back);
        btnSaveFile=findViewById(R.id.btn_select_folder);
        //tableTasks=new ArrayList<>();

        btnSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableTasks=new ArrayList<>();
                Intent intent= new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent,READ_REQUEST_CODE);
            }
        });

        btnSelectBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableTasks!=null){
                Intent in =new Intent();
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)tableTasks);
                in.putExtra("BUNDLE",args);
                setResult(RESULT_OK, in);
                finish();}

            }
        });

        Intent intent = getIntent();
        if(intent.getBundleExtra("BUNDLE")!=null){
            Bundle args = intent.getBundleExtra("BUNDLE");
            String name=String.valueOf(intent.getLongExtra("name",1111));

            tableTasks=(ArrayList<TaskHelper>) args.getSerializable("ARRAYLIST");
            tableAdapter=new TableAdapter(tableTasks,this);
            recyclerViewTable.setAdapter(tableAdapter);

            btnSaveFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tableTasks!=null){

                    }

                    final StorageChooser chooser = new StorageChooser.Builder()
                            // Specify context of the dialog
                            .withActivity(EditorActivity.this)
                            .withFragmentManager(getFragmentManager())
                            .withMemoryBar(true)
                            .allowCustomPath(true)
                            // Define the mode as the FOLDER/DIRECTORY CHOOSER
                            .setType(StorageChooser.DIRECTORY_CHOOSER)
                            .build();

// 2. Handle what should happend when the user selects the directory !
                    chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
                        @Override
                        public void onSelect(String path) {
                            // e.g /storage/emulated/0/Documents
                            Toast.makeText(EditorActivity.this, path, Toast.LENGTH_SHORT).show();


                            StringBuilder data=new StringBuilder();
                            data.append("Name,Unit,Min,Max,Increment,info,result,Result%\n");
                            for (int i = 0; i < tableTasks.size(); i++) {
                                data.append(tableTasks.get(i).getTaskName()+","+tableTasks.get(i).getUnit()+","+String.valueOf(tableTasks.get(i).getMin())+","+String.valueOf(tableTasks.get(i).getMax())+","+
                                        String.valueOf(tableTasks.get(i).getIncrement())+","+tableTasks.get(i).getInfoLink()+","+String.valueOf(tableTasks.get(i).getResultNumber())+","+
                                        String.valueOf(tableTasks.get(i).getResult())+"\n");
                            }


//                            File myDir = new File(path);
//                            if (!myDir.exists()) {
//                                myDir.mkdirs();
//                            }
                            File file = new File (path, name+".csv");

                            try {
                                FileOutputStream out = new FileOutputStream(file);
                                out.write(data.toString().getBytes());
                                out.close();
                                Toast.makeText(EditorActivity.this, "Filesaved", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

// 3. Display File Picker whenever you need to !
                    chooser.show();
                }
            });

        }
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
                        TaskHelper row=new TaskHelper(tokens[0],tokens[1],Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]),Double.parseDouble(tokens[4]),tokens[5]);
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
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            
            Toast.makeText(this, "Permission: \"+permissions[0]+ \"was \"+grantResults[0]", Toast.LENGTH_SHORT).show();
            //resume tasks needing this permission
        }
    }
}