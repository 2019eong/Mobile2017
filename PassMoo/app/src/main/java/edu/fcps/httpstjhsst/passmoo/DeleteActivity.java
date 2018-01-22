package edu.fcps.httpstjhsst.passmoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private String webString;
    private String userString;

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
//        Toast.makeText(DeleteActivity.this, mCurrentUsername, Toast.LENGTH_SHORT).show();
        currUserRef = database.getReference(mCurrentUsername);
        homeIntent = new Intent(DeleteActivity.this, HomeActivity.class);

        bundle = new Bundle();

        mWebsite = (EditText)findViewById(R.id.websitename);
        mUsername = (EditText)findViewById(R.id.username);
        mDeleteAccount = (Button) findViewById(R.id.deleteButton);
        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDataAndChangeScreen();
            }
        });

    }

    public void loadDataAndChangeScreen() {
        webString = mWebsite.getText().toString();
        userString = mUsername.getText().toString();

        currUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getValue().equals("N/A")) { // if not the default
                    AccountInfo tempacct = makeAccountInfo(dataSnapshot.getValue() + "");
                    if(tempacct.getWebsite().equals(webString) && tempacct.getUsername().equalsIgnoreCase(userString)){
                        currUserRefdataSnapshot.getKey();
                    }
                    listAcctString.add(tempacct);
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
                //delete stuff from listAcctString
                int ct = 0;
                boolean tempBool = false;
                for(AccountInfo acct : listAcctString){
                    String webString = mWebsite.getText().toString();
                    String userString = mUsername.getText().toString();
                    if(webString.equalsIgnoreCase(acct.getWebsite()) && userString.equalsIgnoreCase(acct.getUsername())){
                        tempBool = true;
                        break;
                    }
                    else{
                        ct++;
                    }
                }
                if(tempBool){
                    listAcctString.remove(ct);
                }
                Toast.makeText(DeleteActivity.this, listAcctString.toString(), Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                String jsonAcctStrList = gson.toJson(listAcctString);
                bundle.putString("accountlist", jsonAcctStrList);
                bundle.putString("homeExtra", mCurrentUsername);
                homeIntent.putExtras(bundle);
//                Toast.makeText(DeleteActivity.this, jsonAcctStrList, Toast.LENGTH_SHORT).show();
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