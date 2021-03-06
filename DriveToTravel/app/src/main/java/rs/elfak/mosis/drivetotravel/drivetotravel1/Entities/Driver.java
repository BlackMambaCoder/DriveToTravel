package rs.elfak.mosis.drivetotravel.drivetotravel1.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

/**
 * Created by LEO on 23.3.2016..
 */

public class Driver extends User
{
    private String carModel = "";

    // === GETTER === //

    public Driver()
    {
        this.userType                   = User.USER_TYPE_DRIVER;
    }

    public Driver (User user)
    {
        this.id                         = user.id;
        this.name                       = user.name;
        this.surname                    = user.surname;
        this.username                   = user.username;
        this.password                   = user.password;
        this.eMail                      = user.eMail;
        this.phoneNumber                = user.phoneNumber;
        this.userType                   = User.USER_TYPE_DRIVER;
        this.friends                    = user.friends;
        this.profileImage               = user.profileImage;
    }

    public String getCarModel()
    {
        return this.carModel;
    }

    // === SETTER === //

    public void setCarModel (String carModel)
    {
        this.carModel                   = carModel;
    }

    @Override
    public int getUserType() {
        return User.USER_TYPE_DRIVER;
    }

    public static Driver getDriverFromJSONObject(JSONObject userParam)
    {
        Driver user                     = new Driver();

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
                user.setCarModel(metaData.getString(UserStaticAttributes._carModel));
                user.setProfileImage(metaData.getString(UserStaticAttributes.PROFILE_IMAGE));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                user                        = null;
            }
        }
        else
        {
            user                            = null;
        }


        return user;
    }

    @Override
    public JSONObject toJSONObject() throws JSONException
    {
        JSONObject retValue = new JSONObject();

        retValue.put(UserStaticAttributes._id, this.id);
        retValue.put(UserStaticAttributes._name, this.name);
        retValue.put(UserStaticAttributes._surname, this.surname);
        retValue.put(UserStaticAttributes._username, this.username);
        retValue.put(UserStaticAttributes._password, this.password);
        retValue.put(UserStaticAttributes._phoneNumber, this.phoneNumber);
        retValue.put(UserStaticAttributes._eMail, this.eMail);
        retValue.put(UserStaticAttributes._carModel, this.carModel);
        retValue.put(UserStaticAttributes._userType, User.USER_TYPE_DRIVER);
        retValue.put(UserStaticAttributes.PROFILE_IMAGE, this.getProfileImageString());

        return retValue;
    }
}
