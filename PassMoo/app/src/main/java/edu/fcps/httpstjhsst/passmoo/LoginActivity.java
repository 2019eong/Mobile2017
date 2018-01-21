package edu.fcps.httpstjhsst.passmoo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername, mPassword;  // username, password typed in on login screen
    private Button mLogin;  // login button
    private TextView mAttempt;  // remaining attempts txtview
    private int counter = 5;    // remaining attempts counter
    private TextView mUserRegister; // Link to Register Screen (for new users)
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;  // shows progress during waiting for authentication

    private String mCurrentUsername; // data will be sent to other activities (Add, Edit, Home)

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Intent homeIntent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**** INITIALIZE VARIABLES ****/
        homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
        bundle = new Bundle();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        myRef.keepSynced(true);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

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
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userPseudoEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
//                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                    mCurrentUsername = mUsername.getText().toString();
                    loadData();
                    bundle.putString("currUser", mCurrentUsername);
                    homeIntent.putExtras(bundle);
                    startActivity(homeIntent);  // switch from login screen to homescreen
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
    public void loadData(){ // retrieves data from firebase; sends as an intent to homeactivity
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<AccountInfo> acctArray = new ArrayList<AccountInfo>();
                for (DataSnapshot acct : dataSnapshot.getChildren()) {
                    if(acct.getKey().equals(mCurrentUsername)){     //only get stuff for logged in user
                        HashMap<String, AccountInfo> tempMap = (HashMap)acct.getValue();
                        for (String key : tempMap.keySet()) {
                            String accountstuff = "" + tempMap.get(key);
                            if (!accountstuff.equalsIgnoreCase("N/A")) {    // AKA if NOT the placeholder/dummy val
                                String websiteName = accountstuff.substring(accountstuff.indexOf('=') + 1, accountstuff.indexOf(','));
                                accountstuff = accountstuff.substring(accountstuff.indexOf(',')+1, accountstuff.length()-1);
                                String passwordName = accountstuff.substring(accountstuff.indexOf('=') + 1, accountstuff.indexOf(','));
                                accountstuff = accountstuff.substring(accountstuff.indexOf(',')+1, accountstuff.length());
                                String userName = accountstuff.substring(accountstuff.indexOf('=') +1, accountstuff.length());

                                AccountInfo tempAccount = new AccountInfo(websiteName,userName,passwordName);
                                acctArray.add(tempAccount);
                            }
                        }
                    }
                }
                Gson gson = new Gson();
                String jsonAccountInfo = gson.toJson(acctArray);
                Toast.makeText(LoginActivity.this, jsonAccountInfo, Toast.LENGTH_LONG).show();
                bundle.putString("arraystring", jsonAccountInfo);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}