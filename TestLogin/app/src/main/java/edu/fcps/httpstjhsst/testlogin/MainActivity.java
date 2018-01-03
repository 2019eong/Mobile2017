package edu.fcps.httpstjhsst.testlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mUsername; // username typed in on login screen
    private EditText mPassword; // password typed in on login screen
    private Button mLogin;  // login button on login screen
    private TextView mAttempt;  // TxtView of remaining attempts
    private int counter = 5;    // Count of remaining attempts
    private TextView mUserRegister; // Link to Register Screen (for new users)
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;  // shows progress during waiting for authentication


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**** INITIALIZE VARIABLES ****/
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        mUsername = (EditText)findViewById(R.id.userUsernameET);
        mPassword = (EditText)findViewById(R.id.userPasswordET);
        mLogin = (Button)findViewById(R.id.userLoginBTN);
        mAttempt = (TextView)findViewById(R.id.userAttemptTV);
        mUserRegister = (TextView)findViewById(R.id.userRegisterTV);
        /******/
        mAttempt.setText("Remaining Attempts: " + String.valueOf(counter));
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPseudoEmail = mUsername.getText().toString()+"@gmail.com";  // adds txt to make it a pseudo email address
                validate(userPseudoEmail, mPassword.getText().toString());
            }
        });
        mUserRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private void validate(String userPseudoEmail, String userPassword){   // checks if user can login
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userPseudoEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, SecondActivity.class)); // switch from login screen to homescreen
                }
                else{
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    mAttempt.setText("Attempts Remaining: " + counter);
                    progressDialog.dismiss();
                    if(counter == 0){   // if no more login attempts available
                        mLogin.setEnabled(false);   // disable login button
                    }
                }
            }
        });
    }
}