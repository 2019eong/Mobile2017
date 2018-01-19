package edu.fcps.httpstjhsst.passmoo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private Button mStoreAccount;
    private EditText mSiteName;
    private EditText mSiteUsername;
    private EditText mSitePassword;
    private AccountInfo mAccount;

    private String mCurrentUsername;    // received current user's username from login activity

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Users");
    private Map<String, User> mUserMap;


    //private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        //firebaseAuth = FirebaseAuth.getInstance();
        /**** INITIALIZE VARIABLES ****/

        // Get current user's username -- used for retrieving User object from hashmap
        mCurrentUsername = getIntent().getStringExtra("addExtra");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");


        mStoreAccount= (Button)findViewById(R.id.storepassword);
        mSiteName = (EditText) findViewById(R.id.websitename);
        mSiteUsername = (EditText) findViewById(R.id.username);
        mSitePassword = (EditText) findViewById(R.id.password);


        mStoreAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sitename = mSiteName.getText().toString();
                String username = mSiteUsername.getText().toString();
                String password = mSitePassword.getText().toString();
                mAccount = new AccountInfo(sitename,username,password);
//                if(mAccount != null){
//                    Toast.makeText(AddActivity.this, "made account object", Toast.LENGTH_SHORT).show();
//                }

                // Adding AccountInfo to User's arraylist, send update hashmap to firebase
                mUserMap = new HashMap<String, User>();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){  // if data has already been put in beforehand, retrieve it so it won't override w/ new registry
                            mUserMap = (HashMap<String, User>)dataSnapshot.getValue();  // cast value to HashMap
                            String testdisplay = "";
                            for(String k : mUserMap.keySet()){
                                testdisplay+=k+" ";
                            }
                            testdisplay+=mUserMap.size();
                            if(mUserMap.get(mCurrentUsername) != null){
                                // THIS DARN THING DON'T WORK
                                Toast.makeText(AddActivity.this, mUserMap.get(mCurrentUsername).toString(), Toast.LENGTH_SHORT).show();

                            }
//                            Toast.makeText(AddActivity.this, mUserMap.get(mCurrentUsername).toString(), Toast.LENGTH_SHORT).show();

//                            User currentUser = mUserMap.get(mCurrentUsername);
//                            currentUser.addAccount(mAccount);
//                            mUserMap.put(mCurrentUsername, currentUser);    // update hashmap with updated User
//                            myRef.setValue(mUserMap);   // give updated hashmap to firebase

//                            Toast.makeText(AddActivity.this, "Successfully Added Account!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
//                startActivity(new Intent(AddActivity.this, HomeActivity.class));

                //redirect to home activity, display on homeactivity

            }
        });

    }

}
