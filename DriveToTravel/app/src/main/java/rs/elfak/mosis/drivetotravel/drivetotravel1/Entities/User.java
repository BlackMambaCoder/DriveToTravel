package rs.elfak.mosis.drivetotravel.drivetotravel1.Entities;

/**
 * Created by LEO on 23.3.2016..
 */
public abstract class User
{
    protected String id = "";
    protected String name = "";
    protected String surname = "";
    protected String username = "";
    protected String password = "";
    protected String phoneNumber = "";
    protected String eMail = "";

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

    public String getUserType() {return "";}

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
}
