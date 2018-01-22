package edu.fcps.httpstjhsst.passmoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity {
    private EditText mWebsite;
    private EditText mUsername;

    private Button mDeleteAccount;

    private FirebaseDatabase database;
    private DatabaseReference currUserRef;
    private Bundle bundle;

    private Intent homeIntent;
    private ArrayList<AccountInfo> listAcctString = new ArrayList<AccountInfo>();

    private String mCurrentUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        database = FirebaseDatabase.getInstance();
        mCurrentUsername = getIntent().getStringExtra("deleteExtra");  //current user's username
        currUserRef = database.getReference(mCurrentUsername);
        homeIntent = new Intent(DeleteActivity.this, HomeActivity.class);

        bundle = new Bundle();

        mWebsite = (EditText) findViewById(R.id.websitename);
        mUsername = (EditText) findViewById(R.id.username);

        mDeleteAccount = (Button) findViewById(R.id.deleteButton);
        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(DeleteActivity.this, "yayy", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(DeleteActivity.this, HomeActivity.class));
                loadDataAndChangeScreen();
            }
        });

    }

    public void loadDataAndChangeScreen() {
        currUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getValue().equals("N/A")) { // if not the default
                    listAcctString.add(makeAccountInfo(dataSnapshot.getValue() + ""));
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
                /* startActivity goes WITHIN onDataChange bc you want it to be
                 * called AFTER all data has been retrieved from Firebase */
                startActivity(homeIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public AccountInfo makeAccountInfo(String acctString) {
        String[] info = acctString.split(";");
        return new AccountInfo(info[0], info[1], info[2]);
    }

}