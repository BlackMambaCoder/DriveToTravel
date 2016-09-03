package rs.elfak.mosis.drivetotravel.drivetotravel1.Entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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

    protected int id                        =    -1;
    protected String name                   =    "";
    protected String surname                =    "";
    protected String username               =    "";
    protected String password               =    "";
    protected String phoneNumber            =    "";
    protected String eMail                  =    "";
    protected int userType                  =    -1;
    protected List<String> friends          = new ArrayList<>();
    protected Bitmap profileImage           = null;

    // === GETTER  === //
    public int getId()
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

    public Bitmap getProfileImage()
    {
        return this.profileImage;
    }

    public String getProfileImageString()
    {
        return this.bitmapToString(this.profileImage);
    }

    // === SETTER === //

    public void setId(int id)
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

    public void setProfileImage (Bitmap bitmapParam)
    {
        this.profileImage = bitmapParam;
    }

    public boolean setProfileImage (String bitmapStringParam)
    {
        return (this.profileImage = this.stringToBitmap(bitmapStringParam)) != null;
    }

    public abstract JSONObject toJSONObject () throws JSONException;

    public boolean addFriend(String usernameParam)
    {
        ServerRequest serverRequest = new ServerRequest();
        boolean retValue            = false;

        if (
                !this.friends.contains(usernameParam) &&
                serverRequest.addFriend(usernameParam) != null
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
                serverRequest.removeFriend(usernameParam) != null
            )
        {
            this.friends.remove(usernameParam);
            retValue = true;
        }

        return retValue;
    }

    private String bitmapToString(Bitmap profileBitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new  ByteArrayOutputStream();
        profileBitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);

        byte [] b = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private Bitmap stringToBitmap (String imageString)
    {
        try
        {
            byte [] encodeByte=Base64.decode(imageString,Base64.DEFAULT);

            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        }
        catch(Exception e)
        {
            e.getMessage();
            return null;
        }
    }
}
