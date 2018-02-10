package edu.fcps.httpstjhsst.passmoo;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.BulletSpan;
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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private Button mStoreAccount;
    private EditText mSiteName;
    private EditText mSiteUsername;
    private EditText mSitePassword;

    private String mCurrentUsername;    // received current user's username from login activity

    private FirebaseDatabase database;
    private DatabaseReference currUserRef;
    private Bundle bundle;

    private Intent homeIntent;
    private ArrayList<AccountInfo> listAcctString = new ArrayList<AccountInfo>();

    private String alphaOrig = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    private String  alphaSub = "$wD6[RMU-\\XO0d%p;svF#m_f17ng&zo3ZN|*`xkW}K<{JaCe2A+48E5y@TS,(?hG9Hl>j~L^c.V!r':IBP)/=Yt\" Qqubi]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        /**************************** ADD ACTIVITY CURRENTLY WORKING ****************************/

        /**** INITIALIZE VARIABLES ****/
        database = FirebaseDatabase.getInstance();
        mCurrentUsername = getIntent().getStringExtra("addExtra");  //current user's username
        currUserRef = database.getReference(mCurrentUsername);
        homeIntent = new Intent(AddActivity.this, HomeActivity.class);

        bundle = new Bundle();

        mSiteName = (EditText) findViewById(R.id.websitename);
        mSiteUsername = (EditText) findViewById(R.id.username);
        mSitePassword = (EditText) findViewById(R.id.password);
        mStoreAccount = (Button)findViewById(R.id.storepassword);
        mStoreAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    String siteName = mSiteName.getText().toString();
                    String siteUsername = mSiteUsername.getText().toString();
                    String sitePassword = mSitePassword.getText().toString();
//                    String fullAcct = siteName + ";" + encryptString(siteUsername) + ";" + encryptString(sitePassword);   //from orig
                    AccountInfo fullAcct = new AccountInfo(siteName, encryptString(siteUsername), encryptString(sitePassword));
                    Gson gson = new Gson();
                    String jsonFullAcct = gson.toJson(fullAcct);
                    currUserRef.push().setValue(jsonFullAcct);
                    /* calling loadDataAndChangeScreen is ESSENTIAL bc it updates firebase AND
                     * the arraylist<accountinfo> that homeactivity will use to display */
                    loadDataAndChangeScreen();
                }
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
    public void loadDataAndChangeScreen(){
        currUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!dataSnapshot.getValue().equals("N/A")){ // if not the default
                    listAcctString.add(makeAccountInfo(dataSnapshot.getValue()+""));  //from orig
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
//    public AccountInfo makeAccountInfo(String acctString){    //from orig
//        String[] info = acctString.split(";");
//        return new AccountInfo(info[0], info[1], info[2]);
//    }
    public AccountInfo makeAccountInfo(String jsonAcctString){  // json str of AccountInfo object --> AccountInfo object
        Gson gson = new Gson();
        AccountInfo convert = gson.fromJson(jsonAcctString, AccountInfo.class);
        return convert;
    }
    public String encryptString(String s){
        String encoded = "";
        for(int x = 0; x < s.length(); x++){
            encoded+=alphaSub.charAt(alphaOrig.indexOf(s.charAt(x)));
        }
        return encoded;
    }
}