package classes;

/**
 * Created by ebada on 06/03/2017.
 */

public class kidsVaccination
{
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    String status;
    String name;
    String Description;
    String month_num ;

    public String getMonth_num() {
        return month_num;
    }

    public void setMonth_num(String month_num) {
        this.month_num = month_num;
    }
}
