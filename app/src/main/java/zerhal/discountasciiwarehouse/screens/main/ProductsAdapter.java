package zerhal.discountasciiwarehouse.screens.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import zerhal.discountasciiwarehouse.R;
import zerhal.discountasciiwarehouse.models.Product;

/**
 * Created by sameer on 16-07-28.
 */
public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //adapter for main activity product grid. includes views for product as well as auxilary loading view
    private final static int PRODUCT = 0, AUXILARY = 1;

    List<Product> productList;
    String auxilaryText = "";

    public ProductsAdapter(List<Product> products){
        this.productList = products;
    }

    //allow for auxilary item to change text as items are loading, no results, etc.
    public void setAuxText(String auxilaryText){
        this.auxilaryText = auxilaryText;
        notifyItemChanged(productList.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position < productList.size())
            return PRODUCT;
        else
            return AUXILARY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PRODUCT) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_productaux, parent, false);
            return new ProductAuxilaryViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == PRODUCT) {
            ProductViewHolder productHolder = (ProductViewHolder) holder;
            Product product = productList.get(position);
            productHolder.textFace.setText(product.face);
            productHolder.textPrice.setText("$" + product.price);
            if (product.stock == 1)
                productHolder.textBuy.setVisibility(View.VISIBLE);
            else
                productHolder.textBuy.setVisibility(View.INVISIBLE);
         } else {
            ProductAuxilaryViewHolder auxilaryViewHolder = (ProductAuxilaryViewHolder) holder;
            auxilaryViewHolder.textAux.setText(auxilaryText);
        }

    }

    //+1 for auxilary view
    @Override
    public int getItemCount() {
        return productList.size() + 1;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_text_face)
        public TextView textFace;
        @BindView(R.id.product_text_price)
        public TextView textPrice;
        @BindView(R.id.product_button_buy)
        public Button buttonBuy;
        @BindView(R.id.product_text_buy)
        public TextView textBuy;

        public ProductViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class ProductAuxilaryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.productaux_text)
        public TextView textAux;

        public ProductAuxilaryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
