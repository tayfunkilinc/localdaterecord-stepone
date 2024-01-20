package com.projects.localdatarecord;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;


import java.io.ByteArrayOutputStream;
import java.io.File;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

public class recordPage extends AppCompatActivity {

    //Depolama izinlerinin Kontrolleri
    private static final int cameraDemandCode = 100;
    private static final int storageDemandCode = 101;
    private static final int selectImageDemandCodeFromCamera = 102;
    private static final int selectImageDemandCodeFromStorage = 103;

    ShapeableImageView profile_Image;
    EditText edt_Name_Surname, edt_Commercial_Name, edt_Customer_Phone, edt_Mail_Addres, edt_Customer_Birthday;
    EditText edt_Customer_Addres, edt_Work_Explanation;
    FloatingActionButton record_Add_BTN;
    Toast toast;
    TextView toast_Text;

    private String[] cameraPermission;
    private String[] storagePermission;

    //Resim Tutmak
    private Uri imageUri; //Uri türleri resimleri saklamakta kullanılır
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_page);
        toast_Text = new TextView(getApplicationContext());
        toast = new Toast(this);


        profile_Image = findViewById(R.id.customer_Image);
        edt_Name_Surname = findViewById(R.id.name_Surname);
        edt_Commercial_Name = findViewById(R.id.commercial_Name);
        edt_Customer_Phone = findViewById(R.id.customer_Phone);
        edt_Mail_Addres = findViewById(R.id.mail_Address);
        edt_Customer_Birthday = findViewById(R.id.customer_Birthday);
        edt_Customer_Addres = findViewById(R.id.customer_Address);
        edt_Work_Explanation = findViewById(R.id.work_Explanation);
        record_Add_BTN = findViewById(R.id.new_Record_Add);

        record_Add_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast_Text.setText("Veri Kaydı Başarılı");
                toast.setView(toast_Text);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        cameraPermission = new  String[] {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};


        profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    imageSelectDialog();

            }
        });

    }private void imageSelectDialog() {
        String[] objects = {"Kamera", "Galeri"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seçim Yöntemi");

        builder.setItems(objects, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //KAMeRA
                if (i == 0) {

                    //kamera erişim izni yoksa izni talep et
                    if(!cameraPermissionControl()){
                        cameraPermissionDemand();

                    }
                    else{//kamera erişim izni varsa
                        selectFromCamera();

                    }
                }
                //GALERİ
                if (i == 1) {
                    //galeriye erişişm izni yoksa izin talep edeceğiz

                    if(!storagePermissiomControl()){
                        storagePermissionDemand();
                    }
                    else{
                        selectFromStorage();
                    }
                }
            }
        });
        builder.create().show();
    }
    //GALERİYİ Açtırma
    private void selectFromStorage(){
        //galeriden resim seçim yapma işlemini yaptık
        Intent storageIntent = new Intent(Intent.ACTION_PICK); ///seçme eylemi yapan bir intent oluşturduk.
        storageIntent.setType("image/*");//resim türünde seçim yapacağız./*ile türü belirttik.
        startActivityForResult(storageIntent, selectImageDemandCodeFromStorage); //resim türündeki verilere ulaş ve sonucu onactivity'ye döndür.
        //galeriden seçimi yapılan resimi onActivityResult metodu ile alıp kullanacağız.
    }

    //CAMERADAN Açtırmak
    private void selectFromCamera(){
        ContentValues imageValues = new ContentValues();

        imageValues.put(MediaStore.Images.Media.TITLE,"Resim Başlığı");
        imageValues.put(MediaStore.Images.Media.DESCRIPTION,"Resim Açıklaması");

        imageUri =getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageValues);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, selectImageDemandCodeFromCamera);

    }

    //depolama izni istiyoruz
    private boolean storagePermissiomControl(){
        boolean result1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return  result1;
    }
    //eğer iziz yoksa izin tapep edeceğiz
    private void  storagePermissionDemand(){
        ActivityCompat.requestPermissions(this, storagePermission, storageDemandCode);
    }
    //kamera izni istiyoruz
    private boolean cameraPermissionControl(){
        boolean result2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);
        boolean result3 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        return result2 && result3 ;
    }
    //eğer izin yoksa izin talep edeceğiz
    private void cameraPermissionDemand(){
        ActivityCompat.requestPermissions(this, cameraPermission,cameraDemandCode);
    }
    //seçilen resmi almak
    @Override

    public  void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult){
        //izin durumuna göre yapılması gerekenler / izin verme veya reddedilme
        switch (requestCode){
            case cameraDemandCode:{
                if(grantResult.length>0){
                    //izin alındıysa true alınmadıysa false dönecek
                    boolean cameraAccept = grantResult[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccept = grantResult[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccept && storageAccept){
                        selectFromCamera();
                    }
                    else{
                        toast_Text.setText("Kamera ve Depolama İzni Gerekli");
                        toast.setView(toast_Text);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
            break;
            case storageDemandCode:{
                if(grantResult.length>0){
                    //izin alındıysa true alınmadıysa false dönecek
                    boolean storageAccept = grantResult[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccept){
                        selectFromStorage();
                    }
                    else{
                        toast_Text.setText("Depolama İzni Gerekli");
                        toast.setView(toast_Text);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
            break;

        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResult);


    }

    //kameradan veya galeriden alınacak resim burada işlenip alınacak
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //galeriden resim seçildi
            if (requestCode == selectImageDemandCodeFromCamera) {

                profile_Image.setImageURI(imageUri);



            } 
            else if (requestCode == selectImageDemandCodeFromStorage) {

                imageUri =data.getData();

                profile_Image.setBackground(getDrawable(R.drawable.circularframe));
                profile_Image.setImageURI(imageUri);
            } else {
                toast_Text.setText("Beklenmedik Hata Lütfen Tekrar Deneyin");
                toast.setView(toast_Text);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}