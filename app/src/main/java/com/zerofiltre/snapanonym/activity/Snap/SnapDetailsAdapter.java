package com.zerofiltre.snapanonym.activity.Snap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zerofiltre.snapanonym.R;
import com.zerofiltre.snapanonym.data.Comment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SnapDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int SNAP_IMAGE_POSITION = 0;
    private List<Comment> mComments;
    private Context mContext;
    private int mCommentedSnapImageResource;

    SnapDetailsAdapter(Context context, List<Comment> comments, int commentedSnapImageResource) {
        this.mComments = comments;
        this.mContext = context;
        this.mCommentedSnapImageResource = commentedSnapImageResource;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == SNAP_IMAGE_POSITION)
            return R.layout.snap_detail_snap_item;

        return R.layout.snap_detail_comment_item_minified;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == R.layout.snap_detail_snap_item)
            return new CommentedSnapImageViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false));

        return new MinifiedCommentViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (position == SNAP_IMAGE_POSITION)
            ((CommentedSnapImageViewHolder) holder).bindTo(mCommentedSnapImageResource);
        else {
            Comment currentComment = mComments.get(position - 1);
            ((MinifiedCommentViewHolder) holder).bindTo(currentComment);
        }


    }

    @Override
    public int getItemCount() {
        //adding extra position cause we have one extra item to display, the snap image
        return mComments.size() + 1;
    }


    public class MinifiedCommentViewHolder extends RecyclerView.ViewHolder {

        TextView mCommentatorPseudo;
        TextView mCommentContent;
        TextView mReadMoreAction;

        MinifiedCommentViewHolder(@NonNull View itemView) {
            super(itemView);

            mCommentContent = itemView.findViewById(R.id.comment_minified_content);
            mReadMoreAction = itemView.findViewById(R.id.comment_minified_readmore_action);
            mCommentatorPseudo = itemView.findViewById(R.id.comment_commentator_pseudo);
        }

        void bindTo(Comment comment) {
            //Populate the textviews with data
            mCommentatorPseudo.setText(comment.getCommentatorPseudo());
            mCommentContent.setText(comment.getContent());


        }
    }

    public class CommentedSnapImageViewHolder extends RecyclerView.ViewHolder {

        ImageView mCommentedSnapImage;

        CommentedSnapImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mCommentedSnapImage = itemView.findViewById(R.id.commented_snap_image);
        }

        void bindTo(int imageResource) {
            //TODO USE GLIDE PLACEHOLDER AND FALLACK METHODS
            Glide.with(mContext).load(imageResource).into(mCommentedSnapImage);

        }
    }
}
