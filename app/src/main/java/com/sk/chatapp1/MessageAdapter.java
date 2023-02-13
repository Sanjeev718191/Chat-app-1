package com.sk.chatapp1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.sk.chatapp1.databinding.ItemRecieveBinding;
import com.sk.chatapp1.databinding.ItemSendBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter{

    private Context context;
    private ArrayList<Message> messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVER = 2;

    public MessageAdapter(){ }
    public MessageAdapter(Context context, ArrayList<Message> messages){
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.item_send, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recieve, parent, false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) return ITEM_SENT;
        else return ITEM_RECEIVER;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if(holder.getClass() == SentViewHolder.class){
            SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.binding.sendMessage.setText(message.getMessage());
        } else {
            RecieverViewHolder viewHolder = (RecieverViewHolder) holder;
            viewHolder.binding.receiveMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

//    public class MyViewHolder extends RecyclerView.ViewHolder{
//        private TextView message;
//        private LinearLayout main;
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            message = itemView.findViewById(R.id.messageText);
//            main = itemView.findViewById(R.id.mainMessageLayout);
//        }
//    }

    public class SentViewHolder extends RecyclerView.ViewHolder{
        ItemSendBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSendBinding.bind(itemView);
        }
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{
        ItemRecieveBinding binding;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRecieveBinding.bind(itemView);
        }
    }

}
