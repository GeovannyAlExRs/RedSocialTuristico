package com.app.dtk.redsocialturistico.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dtk.redsocialturistico.R;
import com.app.dtk.redsocialturistico.model.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPost extends FirestoreRecyclerAdapter<Post, AdapterPost.ViewHolderFirebase> {

    private List<Post> post;

    Context context;

    public AdapterPost(@NonNull FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolderFirebase holder, int position, @NonNull Post p) {
        //holder.bindData(post.get(position));
        if (p.getImg1() != null) {
            if (!p.getImg1().isEmpty()) {
                Picasso.with(context).load(p.getImg1()).into(holder.img_postCard);
            }
        }

        holder.txt_title_post.setText(p.getTitle());
        holder.txt_description_post.setText(p.getDescription());
        holder.txt_reference_post.setText(p.getReference());
    }

    @NonNull
    @Override
    public ViewHolderFirebase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new ViewHolderFirebase(view);
    }
    // Video 4 - 4:50

    public class ViewHolderFirebase extends RecyclerView.ViewHolder {
        ImageView img_postCard;
        TextView txt_title_post, txt_description_post, txt_reference_post;

        public ViewHolderFirebase(View v) {
            super(v);
            img_postCard = v.findViewById(R.id.id_img_postCard);
            txt_title_post = v.findViewById(R.id.id_txt_title_post);
            txt_description_post = v.findViewById(R.id.id_txt_description_post);
            txt_reference_post = v.findViewById(R.id.id_txt_reference_post);
        }

        /*public void bindData(final Post p) {
            if (p.getImg1() != null) {
                if (!p.getImg1().isEmpty()) {
                    Picasso.with(context).load(p.getImg1()).into(img_postCard);
                }
            }

            txt_title_post.setText(p.getTitle());
            txt_description_post.setText(p.getDescription());
            txt_reference_post.setText(p.getReference());
        }*/
    }
}
