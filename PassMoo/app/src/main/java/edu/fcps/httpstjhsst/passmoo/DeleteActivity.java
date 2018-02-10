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

    private String alphaOrig = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    private String  alphaSub = "$wD6[RMU-\\XO0d%p;svF#m_f17ng&zo3ZN|*`xkW}K<{JaCe2A+48E5y@TS,(?hG9Hl>j~L^c.V!r':IBP)/=Yt\" Qqubi]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        database = FirebaseDatabase.getInstance();
        mCurrentUsername = getIntent().getStringExtra("deleteExtra");  //current user's username
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
                    if(tempacct.getWebsite().equals(webString) && decryptString(tempacct.getUsername()).equalsIgnoreCase(userString)){
                        //if its the thing to delete
                        currUserRef.child(dataSnapshot.getKey()).removeValue();
                    }
                    else{
                        listAcctString.add(tempacct);
                    }
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
                if(listAcctString.size() == 0 && !dataSnapshot.hasChildren()){
                    currUserRef.push().setValue("N/A");
                }
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
//    public AccountInfo makeAccountInfo(String acctString){    //from orig
//        String[] info = acctString.split(";");
//        return new AccountInfo(info[0], info[1], info[2]);
//    }
    public AccountInfo makeAccountInfo(String jsonAcctString) {  // json str of AccountInfo object --> AccountInfo object
        Gson gson = new Gson();
        AccountInfo convert = gson.fromJson(jsonAcctString, AccountInfo.class);
        return convert;
    }
    public String decryptString(String s){
        String decoded = "";
        for(int x = 0; x < s.length(); x++){
            decoded+=alphaOrig.charAt(alphaSub.indexOf(s.charAt(x)));
        }
        return decoded;
    }
}