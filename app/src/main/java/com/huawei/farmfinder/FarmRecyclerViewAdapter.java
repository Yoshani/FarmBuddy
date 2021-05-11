package com.huawei.farmfinder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.huawei.farmfinder.FarmActivity.FARM_KEY;

public class FarmRecyclerViewAdapter extends RecyclerView.Adapter<FarmRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "FarmRecyclerViewAdapter";

    private ArrayList<Farm> farms;
    private Context mContext;

    // data is passed into the constructor
    FarmRecyclerViewAdapter(Context context, ArrayList<Farm> farms) {
        this.mContext = context;
        this.farms = farms;
    }

    // inflates the row layout from xml when needed, returns viewholder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_farm, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView and ImageView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        holder.name.setText(farms.get(position).getName());
        String ownerText = "by " + farms.get(position).getOwner();
        holder.owner.setText(ownerText);
        Glide.with(mContext)
                .asBitmap()
                .load(farms.get(position)
                .getImageUrl())
                .transform(new RoundedCornersTransformation(30, 0))
                .into(holder.farmImage);

        // on click see more
        holder.seeMoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, FarmActivity.class);
            intent.putExtra(FARM_KEY, farms.get(position).getId());
            mContext.startActivity(intent);
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return farms.size();
    }


    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView parent;
        private ImageView farmImage;
        private TextView name;
        private TextView owner;
        private Button seeMoreButton;

        ViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            farmImage = itemView.findViewById(R.id.imageFarm);
            name = itemView.findViewById(R.id.name);
            owner = itemView.findViewById(R.id.owner);
            seeMoreButton = itemView.findViewById(R.id.seeMoreButton);
        }
    }


}
