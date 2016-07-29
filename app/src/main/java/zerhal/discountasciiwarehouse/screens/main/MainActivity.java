package zerhal.discountasciiwarehouse.screens.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import zerhal.discountasciiwarehouse.R;
import zerhal.discountasciiwarehouse.api.ApiManager;
import zerhal.discountasciiwarehouse.models.Product;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_edit_search)
    EditText editSearch;
    @BindView(R.id.main_checkbox_instock)
    CheckBox checkBoxInStock;
    @BindView(R.id.main_reyclerview_product)
    RecyclerView recyclerViewProduct;

//    int skipCursor = 0;
    int numVisibleItems = 6;

    Gson gson = new Gson();

    ArrayList<Product> products = new ArrayList<>(numVisibleItems);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    void requestProducts(){
        String searchText = editSearch.getText().toString();
        boolean isInStockChecked = checkBoxInStock.isChecked();
        ApiManager.getInstance().getProductsRequest(numVisibleItems, products.size(), searchText, isInStockChecked, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showAlertDialogForError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ndJsonString = response.toString();
                String[] jsonStrings = ndJsonString.split("\n");
                for (String json: jsonStrings){
                    Product product = gson.fromJson(json, Product.class);
                    products.add(product);
                }
            }
        });
    }

    @OnClick({R.id.main_checkbox_instock, R.id.main_button_search})
    void refreshProducts(){
        products.clear();
        requestProducts();
    }

    void showAlertDialogForError(Exception e){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(e.getMessage())
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
