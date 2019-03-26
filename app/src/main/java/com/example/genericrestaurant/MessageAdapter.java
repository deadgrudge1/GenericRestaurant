package com.example.genericrestaurant;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CustomViewHolder> {


    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    String currenttime = df.format(c.getTime());

    class CustomViewHolder extends RecyclerView.ViewHolder
    {

        TextView textView;
        TextView date_user;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.user_bubble);
            date_user = itemView.findViewById(R.id.time_textview);

        }
    }

    List<ResponseMessage> responseMessagesList;


    public MessageAdapter(List<ResponseMessage> responseMessageList) {

        this.responseMessagesList = responseMessageList;

    }

    @Override
    public int getItemViewType(int position) {
        if(responseMessagesList.get(position).isMe())
        {
            return R.layout.me_bubble;
        }
        else
            return R.layout.bot_bubble;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CustomViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(i,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.CustomViewHolder holder, int position) {
        holder.textView.setText(responseMessagesList.get(position).getTextmessage());
        //holder.date_user.setText(currenttime);
    }

    @Override
    public int getItemCount() {
        return responseMessagesList.size() ;
    }
}
