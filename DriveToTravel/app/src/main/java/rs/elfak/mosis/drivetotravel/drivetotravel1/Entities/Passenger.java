package rs.elfak.mosis.drivetotravel.drivetotravel1.Entities;

/**
 *
 * Created by LEO on 23.3.2016..
 * Implemented by Zdravko on 06.06.2016
 *
 */
public class Passenger extends User
{
    public Passenger()
    {

    }

    public Passenger(User user)
    {
        this.id = user.id;
        this.name = user.name;
        this.surname = user.surname;
        this.username = user.username;
        this.password = user.password;
        this.eMail = user.eMail;
        this.phoneNumber = user.phoneNumber;
    }

    @Override
    public String getUserType() {
        return "Passanger";
    }
}
