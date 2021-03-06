package com.example.pocketpetlayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.InputStream;

public class JoinActivity4 extends AppCompatActivity {

    String petnickSt;
    String petgender;
    int petbirthDay;
    String emailSt;
    String passwordSt2;
    String nickSt;
    String gender;
    int birthDay;
    ImageView pro2;
    private static final int REQUEST_CODE = 0;
    Bitmap bitmap;

    public static final String TAG = " joinActivity4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join4);
        Button nextbtn3 = (Button) findViewById(R.id.nextbtn3);

        MyDbHelper myDbHelper = new MyDbHelper(getApplicationContext());

        Button overlap3 = (Button) findViewById(R.id.overlap3);
        EditText petnickname = (EditText) findViewById(R.id.petnickname); // ?????????
        Spinner spinner_year = (Spinner)findViewById(R.id.spinner_year); // ??? ??????
        Spinner spinner_month = (Spinner)findViewById(R.id.spinner_month); // ??? ??????
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup); // ?????? ??????
        pro2 = (ImageView) findViewById(R.id.pro2);

        pro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), view);
                getMenuInflater().inflate(R.menu.join_menu, pop.getMenu());
                int permissonCheck = ContextCompat.checkSelfPermission(JoinActivity4.this, Manifest.permission.CAMERA);

                if(permissonCheck == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(JoinActivity4.this, new String[]{Manifest.permission.CAMERA},0 );
                }else{
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,1);
                }
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_camera: //  ???????????? ???????????? ????????? ??????
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                activityResultPicture.launch(intent);
                                break;
                            case R.id.menu_gallery: // ???????????? ???????????? ????????? ??????
                                Intent intent2 = new Intent();
                                intent2.setType("image/*");
                                intent2.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent2, REQUEST_CODE);
                                break;
                        }
                        return true;
                    }
                });
                pop.show();
            }
        });

        nextbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String spinnerYear = spinner_year.getSelectedItem().toString(); // ????????? ?????? spinnerYear??? ??????
                String spinnerMonth = spinner_month.getSelectedItem().toString(); // ????????? ??? spinnerMonth??? ??????

                petbirthDay = Integer.parseInt(spinnerYear + spinnerMonth); //???????????? ??????
                petnickSt= petnickname.getText().toString(); // ????????? ??????

                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId(); // ????????? ?????? ??????
                if (checkedRadioButtonId == -1) {
                    // No item selected
                } else{
                    if (checkedRadioButtonId == R.id.petwoman) {
                        petgender= "woman";
                    }else if(checkedRadioButtonId == R.id.petman){
                        petgender= "man";
                    }
                }

                emailSt = getIntent().getStringExtra("MEMBER_ID");  //2?????? ??????
                passwordSt2 =getIntent().getStringExtra("PASSWORD");

                nickSt = getIntent().getStringExtra("NICKNAME"); //3?????? ??????
                birthDay = getIntent().getIntExtra("BIRTHDAY",0);
                gender = getIntent().getStringExtra("SEX");
                //Log.i(TAG, "Member_id: " + emailSt);
                ContentValues values = new ContentValues();
                values.put(Member.MEMBER_ID, emailSt.trim());
                values.put(Member.PASSWORD, passwordSt2.trim());
                values.put(Member.NICKNAME, nickSt.trim());
                values.put(Member.SEX, gender.trim());
                values.put(Member.BIRTHDAY, birthDay);

                ContentValues values2 = new ContentValues();
                values2.put(Pet.PET_NAME, petnickSt);
                values2.put(Pet.BIRTHDAY, petbirthDay);
                values2.put(Pet.SEX, petgender);

                SQLiteDatabase db = myDbHelper.getWritableDatabase();
                Log.i(TAG, "db ?????? ???"+ db);

                long newRowId = db.insert(Member.TABLE_NAME, null,values);
                Log.i(TAG, "new row ID: " +newRowId);

                long newRowId2 = db.insert(Pet.TABLE_NAME, null,values2);
                Log.i(TAG, "new row ID: " +newRowId2);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        overlap3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDbHelper.getReadableDatabase();
                Cursor c =db.rawQuery("SELECT * FROM " + Pet.TABLE_NAME, null);
                if(c.moveToFirst()){
                    do{
                        String nick = c.getString(0);
                        Log.i(TAG,"nick: " + nick);
                        petnickSt= petnickname.getText().toString();

                        if(nick.equals(petnickSt)){
                            Log.i(TAG, "petnickSt: " + petnickSt);
                            Toast toast = Toast.makeText(JoinActivity4.this, "?????? ???????????? ??????????????????.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }while(c.moveToNext());
                }
            }
        });
    }
    // ????????? ??????
    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null){
                        Bundle extras = result.getData().getExtras();

                        bitmap = (Bitmap) extras.get("data");
                        RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                        bitmapDrawable.setCircular(true);
                        pro2.setImageDrawable(bitmapDrawable);
                    }
                }
            });

    @Override // ?????????
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    pro2.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_LONG).show();
            }
        }
    }
}