package edu.fcps.httpstjhsst.passmoo;

/**
 * Created by Sreya on 1/10/18.
 */

public class AccountInfo {
    String website;
    String username;
    String password;
    public AccountInfo()
    {
        website="N/A";
        username="N/A";
        password="N/A";
    }
    public AccountInfo(String w, String u, String p)
    {
        website=w;
        username=u;
        password=p;
    }
    public String getWebsite()
    {
        return website;
    }
    public String getUsername()
    {
        return username;
    }
    public String getPassword()
    {
        return password;
    }
    public void setWebsite(String w)
    {
        website=w;
    }
    public void setUsername(String u)
    {
        username = u;
    }
    public void setPassword(String p)
    {
        password=p;
    }
    public String toString(){
        return website+" "+username+" "+password;
    }
}