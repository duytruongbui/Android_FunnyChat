package com.example.appchatnew.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchatnew.R;
import com.example.appchatnew.SendData;
import com.example.appchatnew.model.response.InfoUser;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class AdapterListChat extends RecyclerView.Adapter<AdapterListChat.Viewholider> {
    Context context;
    List<InfoUser> list;
    SendData sendData;

    public AdapterListChat(Context context, List<InfoUser> list, SendData sendData) {
        this.context = context;
        this.list = list;
        this.sendData = sendData;
    }

    @NonNull
    @Override
    public Viewholider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_chat, parent, false);
        Viewholider viewHolder = new Viewholider(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholider holder, int position) {
        InfoUser infoUser = list.get(position);
        holder.txtUserName.setText(infoUser.getUsername());

        try {
            Glide.with(context)
                    .load(infoUser.getAvatar())
                    .error(R.drawable.ic_baseline_supervised_user_circle_24)
                    .into(holder.imageView);
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData.sendData(infoUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholider extends RecyclerView.ViewHolder {
        TextView txtUserName;
        RoundedImageView imageView;

        public Viewholider(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageItemProfile);
            txtUserName = itemView.findViewById(R.id.txtItemUser);
        }
    }
}
