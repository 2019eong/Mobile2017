package edu.fcps.httpstjhsst.passmoo;

import android.content.Intent;
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

    private FirebaseDatabase database;
    private DatabaseReference myRef;
//    private HashMap<String, User> mUserMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        /**** INITIALIZE VARIABLES ****/
        mCurrentUsername = getIntent().getStringExtra("addExtra");  //current user's username
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        /***** DON'T USE AS OF NOW *****/
//        mUserMap = new HashMap<String, User>();
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String tempTextView = "";
//                for(DataSnapshot child : dataSnapshot.getChildren())
//                {
//                    User tempUser = child.getValue(User.class);
//                    mUserMap.put(tempUser.getUsername(), tempUser);
//                }
//                Toast.makeText(AddActivity.this, mUserMap.toString(), Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
        /****************************/

        mStoreAccount = (Button)findViewById(R.id.storepassword);
        mSiteName = (EditText) findViewById(R.id.websitename);
        mSiteUsername = (EditText) findViewById(R.id.username);
        mSitePassword = (EditText) findViewById(R.id.password);

        mStoreAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    String siteName = mSiteName.getText().toString();
                    String siteUsername = mSiteUsername.getText().toString();
                    String sitePassword = mSitePassword.getText().toString();
                    mAccount = new AccountInfo(siteName,siteUsername,sitePassword);
                    myRef.child(mCurrentUsername).push().setValue(mAccount);    //adds to curr user's children of accountInfos
                    Toast.makeText(AddActivity.this, "Account added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddActivity.this, HomeActivity.class));    //redirect to home activity --> display on homeactivity
                }

                /********* DON'T USE AS OF NOW *********/
                // Adding AccountInfo to User's arraylist, send update hashmap to firebase
//                mUserMap = new HashMap<String, User>();
//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        mUserMap = (HashMap)dataSnapshot.getValue();  // cast value to HashMap
//                        HashMap<String, User> newHash = new HashMap<String, User>();
//                        newHash.putAll(mUserMap);
//                        String test = "default test";
////                        newHash.get("user1");
//                        Toast.makeText(AddActivity.this, newHash.values().toString(), Toast.LENGTH_LONG).show();
//
//                        // not doing right thing -- LEAVE OFF HERE
////                        if(mUserMap.get(mCurrentUsername) != null){
////                            // THIS DARN THING DON'T WORK
////                            Toast.makeText(AddActivity.this, mUserMap.get(mCurrentUsername).toString(), Toast.LENGTH_SHORT).show();
////                        }
////                        Toast.makeText(AddActivity.this, mUserMap.get(mCurrentUsername).toString(), Toast.LENGTH_SHORT).show();
//
////                        User currentUser = mUserMap.get(mCurrentUsername);
////                        currentUser.addAccount(mAccount);
////                        mUserMap.put(mCurrentUsername, currentUser);    // update hashmap with updated User
////                        myRef.setValue(mUserMap);   // give updated hashmap to firebase
////
////                        Toast.makeText(AddActivity.this, "Successfully Added Account!", Toast.LENGTH_SHORT).show();
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });

            }
        });
    }
    private boolean validate(){
        Boolean result = false;
        String name = mSiteName.getText().toString();
        String username = mSiteUsername.getText().toString();
        String password = mSitePassword.getText().toString();
        if(!name.isEmpty() && !username.isEmpty() && !password.isEmpty()){ // if all fields filled out
            result = true;
        }
        return result;
    }
}
