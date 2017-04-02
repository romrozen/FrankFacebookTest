package model;

/**
 * Created by Roman on 02-Apr-17.
 */

public class FriendModel {
    private String name;
    private String url_image;

    public FriendModel(String name, String url_image) {
        this.name = name;
        this.url_image = url_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl_image() {
        return url_image;
    }

}
