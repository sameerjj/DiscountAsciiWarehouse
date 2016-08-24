package zerhal.discountasciiwarehouse.helpers;

import com.google.gson.Gson;

import java.util.ArrayList;

import zerhal.discountasciiwarehouse.models.Product;

/**
 * Created by sameer on 16-08-23.
 */
public class Parser {
    static Gson gson = new Gson();

    public static ArrayList<Product> parseProducts(String ndJsonString){
        //parse NDJSON string return by splitting on newlines and parsing using GSON
        ArrayList<Product> products = new ArrayList<>();
        String[] jsonStrings = ndJsonString.split("\n");
        for (String json: jsonStrings){
            Product product = gson.fromJson(json, Product.class);
            if (product != null)
                products.add(product);
        }
        return products;
    }

}
