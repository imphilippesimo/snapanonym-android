package com.zerofiltre.snapanonym.activity.Snap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zerofiltre.snapanonym.R;
import com.zerofiltre.snapanonym.data.Snap;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SnapsAdapter extends RecyclerView.Adapter<SnapsAdapter.ViewHolder> {

    public static final String EXTRA_SNAP_IMAGE_ID = "com.zerofiltre.snapanonym.activity.Snap.extra.snap_id";
    //Member variables
    private ArrayList<Snap> mSnapsData;
    private Context mContext;

    SnapsAdapter(ArrayList<Snap> mSnapsData, Context mContext) {
        this.mSnapsData = mSnapsData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SnapsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.snap_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SnapsAdapter.ViewHolder holder, int position) {
        final Snap snap = mSnapsData.get(position);
        holder.bindTo(snap);

        holder.mSnapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SnapDetailsActivity.class);
                intent.putExtra(EXTRA_SNAP_IMAGE_ID,snap.getImageResource());
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mSnapsData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView mSnapImage;
        TextView mSnapInfo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mSnapImage = itemView.findViewById(R.id.snap_image);
            mSnapInfo = itemView.findViewById(R.id.snap_info);
        }


        void bindTo(Snap currentSnap) {
            //Populate the textviews with data
            mSnapInfo.setText(currentSnap.getInfo());
           //TODO USE GLIDE PLACEHOLDER AND FALLACK METHODS
            Glide.with(mContext).load(currentSnap.getImageResource()).into(mSnapImage);

        }
    }
}
