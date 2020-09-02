package com.ogungor.Instaclonefirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PostHolder>{
        ArrayList<String> userEmailList;
        ArrayList<String> userCommentList;
        ArrayList<String> userImagetList;

    public RecyclerAdapter(ArrayList<String> userEmailList, ArrayList<String> userCommentList, ArrayList<String> userImagetList) {
        this.userEmailList = userEmailList;
        this.userCommentList = userCommentList;
        this.userImagetList = userImagetList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.recyleview_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public int getItemCount() {
        return userEmailList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.comment.setText(userCommentList.get(position));
        holder.useremail.setText(userEmailList.get(position));
        Picasso.get().load(userImagetList.get(position)).into(holder.imageView);

    }

    class PostHolder extends  RecyclerView.ViewHolder{

        TextView useremail;
        TextView comment;
        ImageView imageView;



        public PostHolder(@NonNull View itemView) {
            super(itemView);

            useremail=itemView.findViewById(R.id.recylerview_row_useremail);
            comment=itemView.findViewById(R.id.recylerview_row_comment);
            imageView=itemView.findViewById(R.id.recylerview_row_imageview);

        }
    }
}
