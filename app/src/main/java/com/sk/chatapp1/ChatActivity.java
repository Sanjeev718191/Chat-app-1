package com.sk.chatapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sk.chatapp1.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    String recieverId, userId;
    DatabaseReference referenceSender, referenceReciever;
    String senderRoom, reciverRoom;
    MessageAdapter messageAdapter;
    ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messages = new ArrayList<>();
        recieverId = getIntent().getStringExtra("recieverId");
        userId = FirebaseAuth.getInstance().getUid();
        senderRoom = userId+recieverId;
        reciverRoom = recieverId+userId;

        messageAdapter = new MessageAdapter(this, messages);
        binding.charRecycler.setAdapter(messageAdapter);
        binding.charRecycler.setLayoutManager(new LinearLayoutManager(this));

        referenceReciever = FirebaseDatabase.getInstance().getReference("chats").child(reciverRoom);
        referenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        referenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Message message = ds.getValue(Message.class);
                    messages.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = binding.messege.getText().toString();
                if(msg.trim().length() > 0){
                    sendMessage(msg);
                }
            }
        });

    }

    private void sendMessage(String msg) {
        String messageId = FirebaseDatabase.getInstance().getReference().push().getKey();
        Date date = new Date();
        Message message = new Message(messageId, FirebaseAuth.getInstance().getUid(), msg, date.getTime());

        messages.add(message);
        messageAdapter.notifyDataSetChanged();

        referenceSender.child(messageId)
                .setValue(message);
        referenceReciever.child(messageId)
                .setValue(message);
    }
}