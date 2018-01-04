package edu.fcps.httpstjhsst.testlogin;

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

public class RegisterActivity extends AppCompatActivity {

    private EditText mRegisterName, mRegisterUsername, mRegisterPassword;   // Name, username, password of new user on Registration screen
    private Button mRegister;   // register button on Register screen
    private TextView mRegisterLogin;    // Link to Login Screen (for returning users)
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /**** INITIALIZE VARIABLES ****/
        firebaseAuth = FirebaseAuth.getInstance();

        mRegisterName = (EditText)findViewById(R.id.registerNameET);
        mRegisterUsername = (EditText)findViewById(R.id.registerUsernameET);
        mRegisterPassword = (EditText)findViewById(R.id.registerPasswordET);
        mRegister = (Button)findViewById(R.id.registerBTN);
        mRegisterLogin = (TextView)findViewById(R.id.registerLoginTV);
        /****/
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    // upload data to database
                    // database takes "pseudo"-email and password
                    String newPseudoEmail = mRegisterUsername.getText().toString().trim();
                    String newPassword = mRegisterPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(newPseudoEmail, newPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));   // redirect back to login page
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        mRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }

    private Boolean validate(){     // checks if all three fields filled out
        Boolean result = false;
        String name = mRegisterName.getText().toString();
        String username = mRegisterUsername.getText().toString();
        String password = mRegisterPassword.getText().toString();
        if(!name.isEmpty() && !username.isEmpty() && !password.isEmpty()){ // if all fields filled out
            result = true;
        }
        return result;
    }
}