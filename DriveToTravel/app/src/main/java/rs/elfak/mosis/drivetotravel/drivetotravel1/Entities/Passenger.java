package rs.elfak.mosis.drivetotravel.drivetotravel1.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

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
        this.userType                   = User.USER_TYPE_PASSENGER;
    }

    public Passenger(User user)
    {
        this.id                         = user.id;
        this.name                       = user.name;
        this.surname                    = user.surname;
        this.username                   = user.username;
        this.password                   = user.password;
        this.eMail                      = user.eMail;
        this.phoneNumber                = user.phoneNumber;
        this.userType                   = User.USER_TYPE_PASSENGER;
        this.friends                    = user.friends;
        this.profileImage               = user.profileImage;
    }

    @Override
    public int getUserType() {
        return User.USER_TYPE_PASSENGER;
    }

    public JSONObject toJSONObject() throws JSONException
    {
        JSONObject retValue = new JSONObject();

        retValue.put(UserStaticAttributes._id, this.id);
        retValue.put(UserStaticAttributes._username, this.username);
        retValue.put(UserStaticAttributes._password, this.password);
        retValue.put(UserStaticAttributes._name, this.name);
        retValue.put(UserStaticAttributes._surname, this.surname);
        retValue.put(UserStaticAttributes._phoneNumber, this.phoneNumber);
        retValue.put(UserStaticAttributes._eMail, this.eMail);
        retValue.put(UserStaticAttributes._userType, User.USER_TYPE_PASSENGER);
        retValue.put(UserStaticAttributes.PROFILE_IMAGE, this.getProfileImageString());

        // friends

        return retValue;
    }

    public static Passenger getUserFromJSONObject(JSONObject userParam)
    {
        Passenger user = new Passenger();

        if (userParam != null)
        {
            try
            {
                user.setUsername(userParam.getString(UserStaticAttributes._username));
                user.setPassword(userParam.getString(UserStaticAttributes._password));
                user.setId(userParam.getInt(UserStaticAttributes._id));
//                user.friends = Arrays.asList(userParam.get(UserStaticAttributes.FRIENDS));

                String friends = userParam.getString(UserStaticAttributes.FRIENDS);
                user.setFriendsFromJSONObjects(friends);

                JSONObject metaData = userParam.getJSONObject("meta_data");

                user.setName(metaData.getString(UserStaticAttributes._name));
                user.setSurname(metaData.getString(UserStaticAttributes._surname));
                user.seteMail(metaData.getString(UserStaticAttributes._eMail));
                user.setPhoneNumber(metaData.getString(UserStaticAttributes._phoneNumber));
                user.setProfileImage(metaData.getString(UserStaticAttributes.PROFILE_IMAGE));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                user = null;
            }
        }
        else
        {
            user = null;
        }


        return user;
    }
}
