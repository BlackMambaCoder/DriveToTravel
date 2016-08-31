package rs.elfak.mosis.drivetotravel.drivetotravel1.Entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;

/**
 * Created by LEO on 23.3.2016..
 */
public abstract class User
{
    public static int USER_TYPE_DRIVER      =    65;
    public static int USER_TYPE_PASSENGER   =    66;

    protected String id                     =    "";
    protected String name                   =    "";
    protected String surname                =    "";
    protected String username               =    "";
    protected String password               =    "";
    protected String phoneNumber            =    "";
    protected String eMail                  =    "";
    protected int userType                  =    -1;
    protected List<String> friends          = new ArrayList<>();

    // === GETTER  === //
    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getSurname()
    {
        return this.surname;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getPhoneNumber()
    {
        return this.phoneNumber;
    }

    public String geteMail()
    {
        return this.eMail;
    }

    public abstract int getUserType();

    public List<String> getFriends()
    {
        return this.friends;
    }

    // === SETTER === //
    public void setId(String id)
    {
        this.id = id;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public void setSurname (String surname)
    {
        this.surname = surname;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public void setPhoneNumber (String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public void seteMail (String eMail)
    {
        this.eMail = eMail;
    }


    public abstract JSONObject toJSONObject () throws JSONException;

    public boolean addFriend(String usernameParam)
    {
        ServerRequest serverRequest = new ServerRequest();
        boolean retValue            = false;

        if (
                !this.friends.contains(usernameParam) &&
                serverRequest.addFriend(usernameParam, this.username) != null
            )
        {
            this.friends.add(usernameParam);
            retValue                = true;
        }

        return retValue;
    }

    public boolean removeFriend(String usernameParam)
    {
        ServerRequest serverRequest = new ServerRequest();
        boolean retValue = false;

        if (
                this.friends.contains(usernameParam) &&
                serverRequest.removeFriend(usernameParam, this.username) != null
            )
        {
            this.friends.remove(usernameParam);
            retValue = true;
        }

        return retValue;
    }

    public boolean friendWith(String usernameParam)
    {
        ServerRequest serverRequest = new ServerRequest();

        return serverRequest.friendWith(usernameParam, this.username);
    }
}
