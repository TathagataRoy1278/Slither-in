package com.tatan.server;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_1);
        String root = "";
        // Use the current directory as title

        final Activity1 th = this;


        final EditText et1 = (EditText)findViewById((R.id.editText2));

        final Button b1 = (Button)findViewById((R.id.button));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String root = String.valueOf(et1.getText());
                exec(root);
            }
        });

    }

    private void exec(String root) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("root",root);
        startActivity(intent);
    }

}
