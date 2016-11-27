package com.teixeira.diogo.stcp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddStopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop);
    }

    public void addStop(View view) {
        EditText editText = (EditText) findViewById(R.id.stopNumber);
        String stopNumber = editText.getText().toString();

        StopsList.IdStops.add(stopNumber);

        Intent intent = new Intent(this, StopsList.class);
        startActivity(intent);
    }
}
