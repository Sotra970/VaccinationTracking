package classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ebada on 06/03/2017.
 */

public class Clinic implements Serializable
{

    public ArrayList<String> getVacs() {
        return vacs;
    }

    public void setVacs(ArrayList<String> vacs) {
        this.vacs = vacs;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    String ID ;
    String name;
    String country;
    String city;
    String address;
//    String location;
    String phone;
    String distance ;
    ArrayList<String> vacs = new ArrayList<>();
}
