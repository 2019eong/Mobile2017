package edu.fcps.httpstjhsst.passmoo;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {


    private Button mAddButton;
    private Button mEditButton;

    private String mCurrentUsername;    // received current user's username from login activity
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Intent addIntent, editIntent;

    private ArrayList<AccountInfo> mAccountInfoArray;   //for testing purposes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /**** INITIALIZE VARIABLES ****/
        mCurrentUsername = getIntent().getStringExtra("homeExtra");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        addIntent = new Intent(HomeActivity.this, AddActivity.class);
        editIntent = new Intent(HomeActivity.this, EditActivity.class);


        // THIS DOESN'T WORK -- try to retrieve data from database but unsuccessfully
        mAccountInfoArray = new ArrayList<AccountInfo>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot acct : dataSnapshot.child(mCurrentUsername).getChildren()){
                    if(!acct.getValue().equals("N/A")){     // AKA if NOT the placeholder/dummy val
                        HashMap<String, String> tempMap = (HashMap)acct.getValue();
                        mAccountInfoArray.add(unpackDataMap(tempMap));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Toast.makeText(HomeActivity.this, ""+mAccountInfoArray.size() , Toast.LENGTH_SHORT).show();
        /******************************/


        mAddButton = (Button) findViewById(R.id.addButton);
        mEditButton = (Button)findViewById(R.id.editButton);
        mAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                addIntent.putExtra("addExtra", mCurrentUsername);
                startActivity(addIntent);
            }
        });
        mEditButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                editIntent.putExtra("editExtra", mCurrentUsername);
                startActivity(editIntent);
            }
        });
    }
    public AccountInfo unpackDataMap(HashMap<String, String> h){
        // when retrieving from Firebase, it converts data to hashmap form
        // use this method to convert back to AccountInfo object for use in other parts of code
        String w = ""+h.get("website");
        String u = ""+h.get("username");
        String p = ""+h.get("password");
        return new AccountInfo(w, u, p);
    }
}