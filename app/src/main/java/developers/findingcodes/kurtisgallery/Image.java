package developers.findingcodes.kurtisgallery;

import java.io.Serializable;


public class Image implements Serializable{
    private String name;
    private String small, medium, large;
    private String timestamp;

    public Image() {
    }



    public Image(String small) {
        this.small = small;
    }

    public Image(String small, String large) {
        this.small = small;
        this.large = large;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
