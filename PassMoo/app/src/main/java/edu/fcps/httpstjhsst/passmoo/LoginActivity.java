package edu.fcps.httpstjhsst.passmoo;

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

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername, mPassword;  // username, password typed in on login screen
    private Button mLogin;  // login button
    private TextView mAttempt;  // remaining attempts txtview
    private int counter = 5;    // remaining attempts counter
    private TextView mUserRegister; // Link to Register Screen (for new users)
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;  // shows progress during waiting for authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**** INITIALIZE VARIABLES ****/
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

//        /**** Check if user has already logged in beforehand ****/
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        if(user != null){
//            finish();
//            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//        }

        mUsername = (EditText)findViewById(R.id.userUsernameET);
        mPassword = (EditText)findViewById(R.id.userPasswordET);
        mLogin = (Button)findViewById(R.id.userLoginBTN);
        mAttempt = (TextView)findViewById(R.id.userAttemptTV);
        mUserRegister = (TextView)findViewById(R.id.userRegisterTV);

        mAttempt.setText("Attempts Remaining: " + counter);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPseudoEmail = mUsername.getText().toString();
                String userPassword = mPassword.getText().toString();
                if(!userPseudoEmail.isEmpty() && !userPassword.isEmpty()) {
                    validate(userPseudoEmail+"@gmail.com", userPassword);   // adds txt to make it a pseudo email address
                }
            }
        });
        mUserRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void validate(String userPseudoEmail, String userPassword){ // checks if user can login
//        //implement firebase authentification
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userPseudoEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));  // switch from login screen to homescreen
                }
                else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    progressDialog.dismiss();
                    mAttempt.setText("Attempts Remaining: " + counter);
                    if (counter == 0) {     // if no more login attempts available
                        mLogin.setEnabled(false);   // disable login button
                    }
                }
            }
        });
    }
}