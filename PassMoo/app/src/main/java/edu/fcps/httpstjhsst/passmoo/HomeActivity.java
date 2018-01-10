package edu.fcps.httpstjhsst.passmoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {


    private Button mAddButton;
    private Button mEditButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAddButton = (Button) findViewById(R.id.addButton);
        mEditButton = (Button)findViewById(R.id.editButton);

        mAddButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(edu.fcps.httpstjhsst.passmoo.HomeActivity.this, AddActivity.class));
            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(edu.fcps.httpstjhsst.passmoo.HomeActivity.this, EditActivity.class));
            }
        });


    }
}
