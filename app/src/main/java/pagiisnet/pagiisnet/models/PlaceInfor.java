package pagiisnet.pagiisnet.models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfor


{

    private String name;
    private String address;
    private String phoneNumber;
    private Uri websiteUrl;
    private LatLng latLng;
    private Float rating;
    private String attribution;

    public PlaceInfor(String name, String address, String phoneNumber, Uri websiteUrl, LatLng latLng, Float rating, String attribution) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.websiteUrl = websiteUrl;
        this.latLng = latLng;
        this.rating = rating;
        this.attribution = attribution;
    }


    public PlaceInfor() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Uri getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(Uri websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }



    @Override
    public String toString()
    {
        return "PlaceInfor{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", websiteUrl=" + websiteUrl +
                ", latLng=" + latLng +
                ", rating=" + rating +
                ", attribution='" + attribution + '\'' +
                '}';
    }

}
