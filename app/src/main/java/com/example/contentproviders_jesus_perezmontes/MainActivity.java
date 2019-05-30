package com.example.contentproviders_jesus_perezmontes;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        showCalls();
    }

    private void init(){
        lv = findViewById(R.id.lv);
    }

    public void showCalls(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, Utils.PERMISSIONS_REQUEST_READ_CALL_LOG);
        }else{
            List<String> calls = getCalls();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, calls);
            lv.setAdapter(adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Utils.PERMISSIONS_REQUEST_READ_CALL_LOG){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showCalls();
            }else{
                Toast.makeText(this, "Names can't be displayed until permission is granted.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<String> getCalls(){
        List<String> calls = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String call = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                calls.add(call);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return calls;
    }
}