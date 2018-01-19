package edu.fcps.httpstjhsst.passmoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {


    private Button mAddButton;
    private Button mEditButton;

    private String mCurrentUsername;    // received current user's username from login activity

    private Intent addIntent, editIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mCurrentUsername = getIntent().getStringExtra("homeExtra");
        addIntent = new Intent(HomeActivity.this, AddActivity.class);
        editIntent = new Intent(HomeActivity.this, EditActivity.class);

        mAddButton = (Button) findViewById(R.id.addButton);
        mEditButton = (Button)findViewById(R.id.editButton);

        mAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                addIntent.putExtra("addExtra", mCurrentUsername);
                startActivity(addIntent);
            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                editIntent.putExtra("editExtra", mCurrentUsername);
                startActivity(editIntent);
            }
        });
    }
}
