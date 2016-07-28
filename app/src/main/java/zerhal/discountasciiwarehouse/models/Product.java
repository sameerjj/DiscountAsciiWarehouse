package zerhal.discountasciiwarehouse.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sameer on 16-07-27.
 */

public class Product {
    public ProductType type;
    public String id;
    public int size;
    public int price;
    public String face;
    public int stock;
    public String[] tags;

    public Product(ProductType type, String id, int size, int price, String face, int stock, String[] tags) {

        this.type = type;
        this.id = id;
        this.size = size;
        this.price = price;
        this.face = face;
        this.stock = stock;
        this.tags = tags;
    }

    public enum ProductType{
        @SerializedName("face")
        FACE,
        OTHER
    }

}
