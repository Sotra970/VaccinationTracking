package classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ebada on 07/03/2017.
 */

public class Vaccination implements Parcelable

{
    private String id ;
    private int num_month;
    private String name_vacc;
    private String description;

    protected Vaccination(Parcel in) {
        id = in.readString();
        num_month = in.readInt();
        name_vacc = in.readString();
        description = in.readString();
    }

    public Vaccination()
    {}
    public static final Creator<Vaccination> CREATOR = new Creator<Vaccination>() {
        @Override
        public Vaccination createFromParcel(Parcel in) {
            return new Vaccination(in);
        }

        @Override
        public Vaccination[] newArray(int size) {
            return new Vaccination[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum_month() {
        return num_month;
    }

    public void setNum_month(int num_month) {
        this.num_month = num_month;
    }

    public String getName_vacc() {
        return name_vacc;
    }

    public void setName_vacc(String name_vacc) {
        this.name_vacc = name_vacc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(num_month);
        dest.writeString(name_vacc);
        dest.writeString(description);
    }
}
