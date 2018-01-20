package edu.fcps.httpstjhsst.passmoo;

import java.util.ArrayList;

import static android.R.attr.name;

/**
 * Created by Elise on 1/10/2018.
 */

public class User {
    private String mName, mUsername, mPassword;
    private ArrayList<AccountInfo> mUserAccounts;

    public User(String name, String username, String password){
        mName = name;
        mUsername = username;
        mPassword = password;
        mUserAccounts = new ArrayList<AccountInfo>();
    }
    public String getName(){
        return mName;
    }
    public String getUsername(){
        return mUsername;
    }
    public String getPassword(){
        return mPassword;
    }
    public ArrayList<AccountInfo> getAccountInfo(){
        return mUserAccounts;
    }
    public void addAccount(AccountInfo account){
        mUserAccounts.add(account);
    }
    public String toString(){
        return mName+" "+mUsername+" "+mPassword;
    }
}