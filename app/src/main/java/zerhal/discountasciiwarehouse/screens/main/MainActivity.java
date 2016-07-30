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

    public static int NUM_VISIBLE_ITEMS = 6;

    Gson gson = new Gson();

    ArrayList<Product> products = new ArrayList<>(NUM_VISIBLE_ITEMS -1);
    ProductsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configureGrid();
        mAdapter = new ProductsAdapter(products);
        recyclerViewProduct.setAdapter(mAdapter);
        recyclerViewProduct.addOnScrollListener(new OnVerticalScrollListener());
        //TODO: could dynamically calculate visible items count for wider device support
//        NUM_VISIBLE_ITEMS = recyclerViewProduct.getChildCount();
        requestNewProducts();
    }

    @OnClick({R.id.main_checkbox_instock, R.id.main_button_search})
    void requestNewProducts(){
        products.clear();
        mAdapter.setAuxText(getString(R.string.loading));
        mAdapter.notifyDataSetChanged();
        requestProducts(NUM_VISIBLE_ITEMS -1);
    }

    void requestMoreProducts(){
        mAdapter.setAuxText(getString(R.string.loading));
        requestProducts(NUM_VISIBLE_ITEMS);
    }

    void requestProducts(int numItems){
        String searchText = editSearch.getText().toString();
        boolean isInStockChecked = checkBoxInStock.isChecked();
        ApiManager.getInstance().getProductsRequest(numItems, products.size(), searchText, isInStockChecked, new Callback() {
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
                //parse NDJSON string return by splitting on newlines and parsing using GSON
                String ndJsonString = response.body().string();
                String[] jsonStrings = ndJsonString.split("\n");
                for (String json: jsonStrings){
                    Product product = gson.fromJson(json, Product.class);
                    if (product != null)
                        products.add(product);
                }
                //update grid
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (products.size() > 0)
                            mAdapter.setAuxText(getString(R.string.see_more));
                        else
                            mAdapter.setAuxText(getString(R.string.no_results));
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    void configureGrid(){
        //configure some items to be larger as seen in mockups
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

        //set item spacing
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

            //add top margin only for items not at the top of grid
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = 0;
            } else {
                outRect.top = space;
            }
        }
    }

    //scroll listener for loading additional items when reached bottom of grid
    public class OnVerticalScrollListener
            extends RecyclerView.OnScrollListener {

        @Override
        public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1) && dy>0) {
                onScrolledToBottom();
            }
        }

        public void onScrolledToBottom(){
            requestMoreProducts();
        }
    }

    //standard dialog error if any errors in API fetch
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
