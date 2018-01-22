package edu.fcps.httpstjhsst.passmoo;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
     // Toast.makeText(HomeActivity.this, mAccountArray.toString(), Toast.LENGTH_LONG).show();
        if(mAccountArray.size()>0) {
            LinearLayout a = (LinearLayout) findViewById(R.id.ButtonLayout);

            a.setOrientation(LinearLayout.VERTICAL);

            for (int x = 0; x < mAccountArray.size(); x++) {
               // Toast.makeText(HomeActivity.this, "yes"+x, Toast.LENGTH_LONG).show();
                Button newButton = new Button(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,20);
                newButton.setLayoutParams(params);
                //newButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                newButton.setId(x);
               // newButton.setPadding(20,0,20,0);

                newButton.setText(mAccountArray.get(x).getWebsite());
                newButton.setBackgroundColor(Color.parseColor("#FA305E"));
                newButton.setTextColor(Color.WHITE);

                final int finalX = x;
                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(HomeActivity.this, "Username: "+mAccountArray.get(finalX).getUsername()+" " +"Password: "+ mAccountArray.get(finalX).getPassword(), Toast.LENGTH_LONG).show();
                    }
                });
                a.addView(newButton);

            }
        }

        /*********************************/
        //need to remember to somehow get new stuff from firebase if coming back to homescreen from add/delete

        /***** TRYING TO DISPLAY *****/
        //use some sort of layout inflater (but idk how)





    }
    public AccountInfo makeAccountInfo(String acctString){
        String[] info = acctString.split(";");
        return new AccountInfo(info[0], info[1], info[2]);
    }
}