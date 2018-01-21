package edu.fcps.httpstjhsst.passmoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Intent addIntent, deleteIntent;
    private Bundle bundle;

    private List<AccountInfo> mAccountInfoArray = new ArrayList<AccountInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /**** INITIALIZE VARIABLES ****/
        bundle = getIntent().getExtras();
        String stringlist = bundle.getString("arraystring");
        mCurrentUsername = bundle.getString("homeExtra");
        Toast.makeText(HomeActivity.this, bundle.getString("testingextra"), Toast.LENGTH_LONG).show();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

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

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AccountInfo>>(){}.getType();
        mAccountInfoArray = gson.fromJson(stringlist, type);



//        for (AccountInfo acct : mAccountInfoArray){
//            Log.i("Acct Data", acct.getWebsite()+"-"+acct.getUsername()+"-"+acct.getPassword());
//        }
//        Toast.makeText(HomeActivity.this, ""+mAccountInfoArray.size(), Toast.LENGTH_LONG).show();



        /*** DOESN'T WORK @ THE MOMENT ***/
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mAccountInfoArray = new ArrayList<AccountInfo>();
//                for (DataSnapshot acct : dataSnapshot.getChildren()) {
//                    if(acct.getKey().equals(mCurrentUsername)){     //only get stuff for logged in user
//                        HashMap<String, AccountInfo> tempMap = (HashMap)acct.getValue();
//                        for (String key : tempMap.keySet()) {
//                            String accountstuff = "" + tempMap.get(key);
//                            if (!accountstuff.equalsIgnoreCase("N/A")) {    // AKA if NOT the placeholder/dummy val
//
//                                //mAccountInfoArray = new ArrayList<AccountInfo>();
////                            accountstuff = "" + tempMap.get(key);
//
//                                String websiteName = accountstuff.substring(accountstuff.indexOf('=') + 1, accountstuff.indexOf(','));
//                                accountstuff = accountstuff.substring(accountstuff.indexOf(',')+1, accountstuff.length()-1);
//                                String passwordName = accountstuff.substring(accountstuff.indexOf('=') + 1, accountstuff.indexOf(','));
//                                accountstuff = accountstuff.substring(accountstuff.indexOf(',')+1, accountstuff.length());
//                                String userName = accountstuff.substring(accountstuff.indexOf('=') +1, accountstuff.length());
//
//                                AccountInfo mAddedAccount = new AccountInfo(websiteName,userName,passwordName);
//
//                                mAccountInfoArray.add(mAddedAccount);
//                            }
//                        }
//                    }
//                }
//                Gson gson = new Gson();
//                String jsonAccountInfo = gson.toJson(mAccountInfoArray);
//                Intent i = new Intent(HomeActivity.this, HomeActivity.class);
//                i.putExtra("arraystring", jsonAccountInfo);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });


        /***** TRYING TO DISPLAY *****/
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
}