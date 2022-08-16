package com.example.appchatnew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchatnew.R;
import com.example.appchatnew.SendDataMessage;
import com.example.appchatnew.model.response.ChatResponse;
import com.example.appchatnew.utilities.Constants;
import com.example.appchatnew.utilities.StoreUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListMessage extends RecyclerView.Adapter<AdapterListMessage.Viewholider> {
    Context context;
    List<ChatResponse> list;
    SendDataMessage sendDataMessage;

    public AdapterListMessage(Context context, List<ChatResponse> list, SendDataMessage sendDataMessage) {
        this.context = context;
        this.list = list;
        this.sendDataMessage = sendDataMessage;
    }

    @NonNull
    @Override
    public Viewholider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Constants.right) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_right, parent, false);
            Viewholider viewHolder = new Viewholider(view);
            return viewHolder;
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_left, parent, false);
            Viewholider viewHolder = new Viewholider(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholider holder, int position) {
        ChatResponse chatResponse = list.get(position);

        if (chatResponse.getDatacombo().contains("https://firebasestorage.googleapis.com/")){
            holder.txtText.setVisibility(View.GONE);
            holder.imageHinhAnh.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(chatResponse.getDatacombo())
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imageHinhAnh);
        }else{
            holder.txtText.setText(chatResponse.getDatacombo());

        }


        if (!list.get(position).getUser().equals(StoreUtils.get(context, Constants.username))) {
            Glide.with(context)
                    .load(StoreUtils.get(context, Constants.avatar))
                    .error(R.drawable.ic_baseline_supervised_user_circle_24)
                    .into(holder.imageView);
          //  holder.imageView.setVisibility(View.GONE);
        } else {
            Glide.with(context)
                    .load(StoreUtils.get(context, Constants.customer))
                    .error(R.drawable.ic_baseline_supervised_user_circle_24)
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataMessage.sendData(chatResponse);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholider extends RecyclerView.ViewHolder {
        TextView txtText;
        CircleImageView imageView;
        ImageView imageHinhAnh;
        public Viewholider(@NonNull View itemView) {
            super(itemView);
            txtText = itemView.findViewById(R.id.txtGetMessage);
            imageView = itemView.findViewById(R.id.cricleLeft);
            imageHinhAnh = itemView.findViewById(R.id.imageHinhAnh);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (!list.get(position).getUser().equals(StoreUtils.get(context, Constants.username))) {
            return Constants.right;
        } else {
            return Constants.left;
        }
    }
}
