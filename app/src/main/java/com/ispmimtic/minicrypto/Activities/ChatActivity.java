package com.ispmimtic.minicrypto.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ispmimtic.minicrypto.Adapters.MessageAdapter;
import com.ispmimtic.minicrypto.Models.Message;
import com.ispmimtic.minicrypto.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    MessageAdapter adapter;
    ArrayList<Message> messages;

    String senderRoom, receiverRoom;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messages = new ArrayList<>();

        String pseudo = getIntent().getStringExtra("name");
        String receiverUid = getIntent().getStringExtra("uid");
        String senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        database = FirebaseDatabase.getInstance();

        adapter = new MessageAdapter(this, messages, senderRoom, receiverRoom);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        database.getReference().child("Tafa Tafa")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messages.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = binding.messageId.getText().toString();

                Date date = new Date();
                Message message = new Message(messageText, senderUid, date.getTime());
                binding.messageId.setText("");

                String randomKey = database.getReference().push().getKey();


                database.getReference().child("Tafa Tafa")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomKey)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            database.getReference().child("Tafa Tafa")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .child(randomKey)
                                    .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                    });

                            HashMap<String, Object> lastMsgObj = new HashMap<>();
                            lastMsgObj.put("lastMsg", message.getMessage());
                            lastMsgObj.put("lastMsgTime", date.getTime());

                            database.getReference().child("Tafa Tafa").child(senderRoom).updateChildren(lastMsgObj);
                            database.getReference().child("Tafa Tafa").child(receiverRoom).updateChildren(lastMsgObj);

                        }
                });

            }
        });

        getSupportActionBar().setTitle(pseudo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}