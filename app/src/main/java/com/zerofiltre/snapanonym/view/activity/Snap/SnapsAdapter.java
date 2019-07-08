package com.zerofiltre.snapanonym.view.activity.Snap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.zerofiltre.snapanonym.R;
import com.zerofiltre.snapanonym.infrastructure.Network.AppUtils;
import com.zerofiltre.snapanonym.model.Snap;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SnapsAdapter extends RecyclerView.Adapter<SnapsAdapter.ViewHolder> {

    public static final String EXTRA_SNAP_IMAGE_ID = "com.zerofiltre.snapanonym.view.activity.Snap.extra.snap_id";
    //Member variables
    private ArrayList<Snap> mSnapsData;
    private Context mContext;

    SnapsAdapter(ArrayList<Snap> mSnapsData, Context mContext) {
        this.mSnapsData = mSnapsData;
        this.mContext = mContext;
    }

    public void setmSnapsData(ArrayList<Snap> mSnapsData) {
        this.mSnapsData = mSnapsData;
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

//        holder.mSnapImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, SnapDetailsActivity.class);
//                intent.putExtra(EXTRA_SNAP_IMAGE_ID, snap.getImageResource());
//                mContext.startActivity(intent);
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mSnapsData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mSnapImage;
        TextView mSnapInfo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mSnapImage = itemView.findViewById(R.id.snap_image);
            mSnapInfo = itemView.findViewById(R.id.snap_info);
        }


        void bindTo(Snap currentSnap) {
            //Populate the textviews with data
            StringBuilder builder = new StringBuilder();
            builder.append(String.valueOf(currentSnap.getMilesAway())).append(" ").append(mContext.getString(R.string.distance_info_suffix));

            mSnapInfo.setText(builder.toString());
            //TODO USE GLIDE PLACEHOLDER AND FALLACK METHODS
            Glide.with(mContext).load(AppUtils.bytesFromStringContent(currentSnap.getPicture().getContent())).into(mSnapImage);


        }
    }
}
