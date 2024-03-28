package pagiisnet.pagiisnet;

import com.google.firebase.database.Exclude;

public class PurchasedItemUpload {


    private String imageName;
    private String imageImageUrl;
    private String exRating;
    private String UserId;
    private String ModelName;
    private String  mKey;
    private String ItemPurchaseStatus;

    public PurchasedItemUpload(String name, String imageUrl, String rateEx, String userId, String modelName, String itemPurchaseStatus) {
        if (name.trim().equals("")) {
            name = "No Name";
            modelName = "Promo";
        }

        UserId = userId;
        imageName = name;
        exRating = rateEx;
        imageImageUrl= imageUrl;
        ModelName = modelName;
        ItemPurchaseStatus = itemPurchaseStatus;
    }


    public String getItemPurchaseStatus() {
        return ItemPurchaseStatus;
    }

    public void setItemPurchaseStatus(String itemPurchaseStatus) {
        ItemPurchaseStatus = itemPurchaseStatus;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getExRating() {
        return exRating;
    }

    public void setExRating(String exRating) {
        this.exRating = exRating;
    }

    public String getName() {
        return imageName;
    }

    public void setName(String name) {
        imageName = name;
    }

    public String getImageUrl() {
        return imageImageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        imageImageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String Key) {
        mKey = Key;
    }
}
