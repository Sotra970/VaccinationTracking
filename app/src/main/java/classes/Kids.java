package classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by ebada on 02/03/2017.
 */

public class Kids implements Parcelable
{
    private Kids(Parcel in) {
        name = in.readString();
        date = in.readString();
        parentID = in.readString();
        gender = in.readString();
        ID = in.readString();
    }

    public Kids()
    {}
    public static final Creator<Kids> CREATOR = new Creator<Kids>() {
        @Override
        public Kids createFromParcel(Parcel in) {
            return new Kids(in);
        }

        @Override
        public Kids[] newArray(int size) {
            return new Kids[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String name ;
    private String date;
    private String parentID;
    private String gender;
    private String ID;
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(parentID);
        dest.writeString(gender);
        dest.writeString(ID);
    }

}
