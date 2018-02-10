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
    private DatabaseReference currUserRef;

    private Intent homeIntent;
    private Bundle bundle;

    private ArrayList<AccountInfo> listAcctString = new ArrayList<AccountInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**************************** LOGIN ACTIVITY CURRENTLY WORKING ****************************/

        /**** INITIALIZE VARIABLES ****/
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
        bundle = new Bundle();

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
                    mCurrentUsername = mUsername.getText().toString();
                    currUserRef = database.getReference(mCurrentUsername);
                    currUserRef.keepSynced(true);
                    loadDataAndChangeScreen();
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
    /* WORKING -- Retrieves data from firebase; sends as an intent to homeactivity */
    public void loadDataAndChangeScreen(){
        currUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!dataSnapshot.getValue().equals("N/A")){ // if not the default
                    listAcctString.add(makeAccountInfo(dataSnapshot.getValue()+""));
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        currUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Gson gson = new Gson();
                String jsonAcctStrList = gson.toJson(listAcctString);
                bundle.putString("accountlist", jsonAcctStrList);
                bundle.putString("homeExtra", mCurrentUsername);
                homeIntent.putExtras(bundle);
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                /* startActivity goes WITHIN onDataChange bc you want it to be
                 * called AFTER all data has been retrieved from Firebase */
                startActivity(homeIntent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
//    public AccountInfo makeAccountInfo(String acctString){    //from orig
//        String[] info = acctString.split(";");
//        return new AccountInfo(info[0], info[1], info[2]);
//    }
    public AccountInfo makeAccountInfo(String jsonAcctString) {  // json str of AccountInfo object --> AccountInfo object
        Gson gson = new Gson();
        AccountInfo convert = gson.fromJson(jsonAcctString, AccountInfo.class);
        return convert;
    }

    @Override
    public void onBackPressed() {
        /* prevents user from going back to their account home page after logging out; disables back button */
    }
}