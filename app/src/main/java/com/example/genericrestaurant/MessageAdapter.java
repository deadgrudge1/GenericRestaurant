package com.example.genericrestaurant;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CustomViewHolder> {


    class CustomViewHolder extends RecyclerView.ViewHolder
    {

        TextView textView;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.user_bubble);

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
    }

    @Override
    public int getItemCount() {
        return responseMessagesList.size() ;
    }
}
