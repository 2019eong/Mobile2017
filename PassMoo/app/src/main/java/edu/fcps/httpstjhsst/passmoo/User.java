package edu.fcps.httpstjhsst.passmoo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elise on 1/10/2018.
 */

public class User {
    private String name;
    private String username;
    private String password;
    private ArrayList<AccountInfo> userAccounts;

    public User(String n, String u, String p){
        name = n;
        username = u;
        password = p;
        userAccounts = new ArrayList<AccountInfo>();
    }
    public String getName(){
        return name;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public ArrayList<AccountInfo> getAccountInfo(){
        return userAccounts;
    }
}
