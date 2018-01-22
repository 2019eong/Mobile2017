package edu.fcps.httpstjhsst.passmoo;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
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
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private Button mAddButton;
    private Button mDeleteButton;

    private String mCurrentUsername;    // received current user's username from login activity
    private String jsonAcctStrList; // json representation of AccountInfos; used for retrieval from firebase
    private FirebaseDatabase database;
    private DatabaseReference currUserRef;  //firebase reference to current user's accoutns

    private Intent addIntent, deleteIntent;
    private Bundle bundle;  // contains important info (ex. mCurrentUsername, jsonAcctStrList) from LoginActivity

    private List<AccountInfo> mAccountArray = new ArrayList<AccountInfo>(); // holds user's AccountInfos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /**** INITIALIZE VARIABLES ****/
        database = FirebaseDatabase.getInstance();

        bundle = getIntent().getExtras();
        mCurrentUsername = bundle.getString("homeExtra");
        currUserRef = database.getReference(mCurrentUsername);
        currUserRef.keepSynced(true);
        /* WORKING -- Takes string representations of each acct, converts each to
         * AccountInfo object, and adds it to an arraylist of AccountInfo objects. */
        jsonAcctStrList = bundle.getString("accountlist");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AccountInfo>>(){}.getType();
        mAccountArray = gson.fromJson(jsonAcctStrList, type);
        Toast.makeText(HomeActivity.this, mAccountArray.toString(), Toast.LENGTH_LONG).show();

        addIntent = new Intent(HomeActivity.this, AddActivity.class);
        deleteIntent = new Intent(HomeActivity.this, DeleteActivity.class);

        mAddButton = (Button) findViewById(R.id.addButton);
        mDeleteButton = (Button)findViewById(R.id.deleteButton);
        mAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                addIntent.putExtra("addExtra", mCurrentUsername);
                startActivity(addIntent);
            }
        });
        mDeleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                deleteIntent.putExtra("deleteExtra", mCurrentUsername);
                startActivity(deleteIntent);
            }
        });
        /*********************************/
        //need to remember to somehow get new stuff from firebase if coming back to homescreen from add/delete

        /***** TRYING TO DISPLAY *****/
        //use some sort of layout inflater (but idk how)


//        if(mAccountInfoArray!=null) {
//            LinearLayout myRoot = (LinearLayout) findViewById(R.id.LinearLayout01);
//            LinearLayout a = new LinearLayout(this);
//            a.setOrientation(LinearLayout.VERTICAL);
//            for (int x = 0; x < mAccountInfoArray.size(); x++) {
//                Button btnTag = new Button(this);
//                btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                btnTag.setText(mAccountInfoArray.get(x).getWebsite());
//                a.addView(btnTag);
//            }
//            myRoot.addView(a);
//            setContentView(a);
//        }
    }
    public AccountInfo makeAccountInfo(String acctString){
        String[] info = acctString.split(";");
        return new AccountInfo(info[0], info[1], info[2]);
    }
}