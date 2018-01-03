package edu.fcps.httpstjhsst.testlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private TextView mAttempt;
    private int counter = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = (EditText)findViewById(R.id.usernameEditText);
        mPassword = (EditText)findViewById(R.id.passwordEditText);
        mLogin = (Button)findViewById(R.id.loginButton);
        mAttempt = (TextView)findViewById(R.id.attemptTextView);
        /******/
        mAttempt.setText("Remaining Attempts: " + String.valueOf(counter));
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(mUsername.getText().toString(), mPassword.getText().toString());
            }
        });


    }

    private void validate(String userName, String userPassword){
        if(userName.equals("Elise") && userPassword.equals("1234")){
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);  // starts the activity by switching to it
        }
        else{
            counter--;
            mAttempt.setText("Remaining Attempts: " + String.valueOf(counter));

            if(counter == 0){   // AKA no more login attempts
                mLogin.setEnabled(false);   // disables login button
            }
        }
    }
}
