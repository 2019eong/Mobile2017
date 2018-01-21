package edu.fcps.httpstjhsst.passmoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
public class HomeActivity extends AppCompatActivity {


    private Button mAddButton;
    private Button mEditButton;

    private String mCurrentUsername;    // received current user's username from login activity
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Intent addIntent, editIntent;

    private ArrayList<AccountInfo> mAccountInfoArray;//for testing purposes

    private AccountInfo mAddedAccount;
    private String accountstuff;
    private String websiteName;
    private String userName;
    private String passwordName;


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


        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot acct : dataSnapshot.getChildren()) {

                   // if (!acct.equals("N/A")) {     // AKA if NOT the placeholder/dummy val
                        HashMap<String, AccountInfo> tempMap = (HashMap) acct.getValue();
                        for (String key : tempMap.keySet()) {
                            accountstuff = "" + tempMap.get(key);
                            if (!accountstuff.equalsIgnoreCase("N/A")) {

                                mAccountInfoArray = new ArrayList<AccountInfo>();
                             //   accountstuff = "" + tempMap.get(key);

                                websiteName = accountstuff.substring(accountstuff.indexOf('=') + 1, accountstuff.indexOf(','));
                                accountstuff = accountstuff.substring(accountstuff.indexOf(',')+1, accountstuff.length()-1);
                                passwordName = accountstuff.substring(accountstuff.indexOf('=') + 1, accountstuff.indexOf(','));
                                accountstuff = accountstuff.substring(accountstuff.indexOf(',')+1, accountstuff.length());
                                userName = accountstuff.substring(accountstuff.indexOf('=') +1, accountstuff.length());

                                mAddedAccount=new AccountInfo(websiteName,userName,passwordName);

                                mAccountInfoArray.add(mAddedAccount);



                            }

                        }
                    //}
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




            /***TRYING TO DISPLAY*****///
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




        mAddButton = (Button) findViewById(R.id.addButton);
        mEditButton = (Button)findViewById(R.id.deleteButton);



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



}