package zerhal.discountasciiwarehouse.screens.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.main_recyclerview_product)
    RecyclerView recyclerViewProduct;

//    int skipCursor = 0;
    int numVisibleItems = 6;

    Gson gson = new Gson();

    ArrayList<Product> products = new ArrayList<>(numVisibleItems);
    ProductsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configureGrid();
        mAdapter = new ProductsAdapter(products);
        requestProducts();
    }

    void requestProducts(){
        String searchText = editSearch.getText().toString();
        boolean isInStockChecked = checkBoxInStock.isChecked();
        ApiManager.getInstance().getProductsRequest(numVisibleItems, products.size(), searchText, isInStockChecked, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAlertDialogForError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ndJsonString = response.body().string();
                String[] jsonStrings = ndJsonString.split("\n");
                for (String json: jsonStrings){
                    Product product = gson.fromJson(json, Product.class);
                    if (product != null)
                        products.add(product);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewProduct.setAdapter(mAdapter);
                    }
                });
            }
        });
    }

    @OnClick({R.id.main_checkbox_instock, R.id.main_button_search})
    void refreshProducts(){
        products.clear();
        recyclerViewProduct.setAdapter(mAdapter);
        requestProducts();
    }

    void configureGrid(){
        GridLayoutManager manager = new GridLayoutManager(this, 7);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int visibleIndex = position % 6;
                if (visibleIndex == 1 || visibleIndex == 3)
                    return 3;
                else
                    return 2;
            }
        });
        recyclerViewProduct.setLayoutManager(manager);

        int valueInPixels = (int) getResources().getDimension(R.dimen.product_grid_spacing);
        recyclerViewProduct.addItemDecoration(new SpacesItemDecoration(valueInPixels));

        recyclerViewProduct.setHasFixedSize(true);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = 0;
            } else {
                outRect.top = space;
            }
        }
    }

    void showAlertDialogForError(Exception e){
        new AlertDialog.Builder(MainActivity.this)
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
