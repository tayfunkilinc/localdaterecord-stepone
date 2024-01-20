package com.projects.localdatarecord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    Toast toast;
    TextView toast_Text;
    private FloatingActionButton recordAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordAdd = findViewById(R.id.record_Add);
        toast_Text = new TextView(getApplicationContext());
        toast = new Toast(this);

        recordAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toast_Text.setText("Giriş Yapıldı");
                toast.setView(toast_Text);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();

                Intent record_Add_Page = new Intent(MainActivity.this, recordPage.class);
                startActivity(record_Add_Page);

            }
        });
    }
}