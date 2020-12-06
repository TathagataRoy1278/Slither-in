package com.tatan.server;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);
        final EditText text = (EditText)findViewById(R.id.editText);
        Button send = (Button) findViewById(R.id.buttonSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = text.getText().toString();

                Toast.makeText(getThis(),command, Toast.LENGTH_LONG).show();
                runCmommand(command);
            }
        });
    }
    void runCmommand(String str)
    {

    }
    public StartActivity getThis()
    {
        return this;
    }
}
