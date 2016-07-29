package zerhal.discountasciiwarehouse.screens.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zerhal.discountasciiwarehouse.R;
import zerhal.discountasciiwarehouse.models.Product;

/**
 * Created by sameer on 16-07-28.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    List<Product> productList;

    public ProductsAdapter(List<Product> products){
        this.productList = products;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textFace.setText(product.face);
        holder.textPrice.setText("$" + product.price);
        if (product.stock == 1)
            holder.textBuy.setVisibility(View.VISIBLE);
        else
            holder.textBuy.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return productList.size();
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
}
