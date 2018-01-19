package edu.fcps.httpstjhsst.passmoo;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText mRegisterName, mRegisterUsername, mRegisterPassword;   // Name, username, password of new user on Registration screen
    private Button mRegister;   // register button on Register screen
    private TextView mRegisterLogin;    // Link to Login Screen (for returning users)
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference usersRef;
    private Map<String, User> mUserMap;
    private boolean childrenExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /**** INITIALIZE VARIABLES ****/
        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
      // usersRef = myRef.child("users");

        childrenExist = false;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    HashMap<String, User> blah = child.getValue(HashMap.class);
                    mUserMap = child.getValue(HashMap.class);
                    childrenExist = true;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(!childrenExist){
            mUserMap = new HashMap<String, User>();
            myRef.setValue(mUserMap);
            //usersRef.setValue(mUserMap);
        }


        mRegisterName = (EditText)findViewById(R.id.registerNameET);
        mRegisterUsername = (EditText)findViewById(R.id.registerUsernameET);
        mRegisterPassword = (EditText)findViewById(R.id.registerPasswordET);
        mRegister = (Button)findViewById(R.id.registerBTN);
        mRegisterLogin = (TextView)findViewById(R.id.registerLoginTV);

        //Strings of edit text stuff
        final String registername = mRegisterName.getText().toString();
        final String registerusername = mRegisterUsername.getText().toString();
        final String registerpassword = mRegisterPassword.getText().toString();


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    // upload data to database
                    // database takes "pseudo"-email and password
                    String pseudoEmail = mRegisterUsername.getText().toString().trim()+"@gmail.com";
                    String password = mRegisterPassword.getText().toString().trim();


                    firebaseAuth.createUserWithEmailAndPassword(pseudoEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));  // redirect back to login page
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
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
            pushToDatabase(name,new User(name,username,password));

        }
        return result;
    }
    public void pushToDatabase(String username, User user){
        //retrieve existing map stuff from database
//        DatabaseReference childRef = myRef.child("Users");
//        childRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mUserMap = dataSnapshot.getValue(Map.class);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        mUserMap.put(username, user);
        myRef.setValue(mUserMap);
    }
    public static class EditActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit);
        }
    }
}