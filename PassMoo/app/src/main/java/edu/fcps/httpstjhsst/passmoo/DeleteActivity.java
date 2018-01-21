package edu.fcps.httpstjhsst.passmoo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteActivity extends AppCompatActivity {
    private EditText mWebsite;
    private EditText mUsername;

    private Button mDeleteAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);


        mWebsite = (EditText) findViewById(R.id.websitename);
        mUsername = (EditText) findViewById(R.id.username);

        mDeleteAccount= (Button)findViewById(R.id.deleteButton);
        mDeleteAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Toast.makeText(DeleteActivity.this, "yayy", Toast.LENGTH_SHORT).show();



            }
        });
    }
}
