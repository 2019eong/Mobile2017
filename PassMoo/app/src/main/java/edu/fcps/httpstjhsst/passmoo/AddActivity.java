package edu.fcps.httpstjhsst.passmoo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    private Button mStorePassword;
    private EditText mSiteName;
    private EditText mSiteUsername;
    private EditText mSitePassword;
    private AccountInfo mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mStorePassword = (Button)findViewById(R.id.storepassword);
        mSiteName = (EditText) findViewById(R.id.websitename);
        mSiteUsername = (EditText) findViewById(R.id.username);
        mSitePassword = (EditText) findViewById(R.id.password);
        String sitename = mSiteName.getText().toString();
        String username = mSiteUsername.getText().toString();
        String password = mSitePassword.getText().toString();

        mAccount = new AccountInfo(sitename,username,password);

        mStorePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toast success, redirect to home activity, store AccountInfo into User, display on homeactivity

            }
        });

    }
}
