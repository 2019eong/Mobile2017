package com.example.a2019eong.firebasecoffeecup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private TextView mTextView;
    private EditText mEditText;
    private Button mButton;

    private String value;

    private Map<String, CoffeeCup> taskMap;
    private DatabaseReference usersRef;

    private CoffeeCup cup1;
    private CoffeeCup cup2;
    private int cupNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cup1 = new CoffeeCup("espresso", 5);
        cup2 = new CoffeeCup("latte", 7);

        //inflate variables
        mTextView = (TextView)findViewById(R.id.text_view);
        mButton = (Button)findViewById(R.id.button);
        mEditText = (EditText)findViewById(R.id.edit_text);

        //get connected to Firebase
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        taskMap = new HashMap<>();
        cupNum = 4;
        taskMap.put("cup1", cup1);
        taskMap.put("cup2", cup2);
        taskMap.put("cup3", new CoffeeCup("mocha", 9));
        myRef.setValue(taskMap);
        //puts data into cloud
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempCoffee = ""+mEditText.getText();
                if(tempCoffee!=""){
                    String[] tempCArray = tempCoffee.split(" ");
                    String typeC = "";
                    for(int x=0; x < tempCArray.length-1; x++){
                        typeC+=tempCArray[x]+" ";
                    }
                    taskMap.put("cup"+cupNum, new CoffeeCup(typeC, Integer.parseInt(tempCArray[tempCArray.length-1])));
                    cupNum++;
                }
                myRef.setValue(taskMap);
                myRef.push();
            }
        });

        //retrieve data from database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tempTextView = "";
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    CoffeeCup tempCup = child.getValue(CoffeeCup.class);
                    tempTextView += tempCup.getCoffeeType() + tempCup.getAmtOfCoffee();
                }
                mTextView.setText(tempTextView);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //LINK: https://console.firebase.google.com/
    }
}