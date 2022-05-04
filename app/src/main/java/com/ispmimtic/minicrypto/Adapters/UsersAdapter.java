package com.ispmimtic.minicrypto.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ispmimtic.minicrypto.Activities.ChatActivity;
import com.ispmimtic.minicrypto.AffineCipher;
import com.ispmimtic.minicrypto.R;
import com.ispmimtic.minicrypto.Models.User;
import com.ispmimtic.minicrypto.databinding.RowConversationBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.userViewHolder>{

    Context context;
    ArrayList<User> users;

    public UsersAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation, parent, false);

        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.userViewHolder holder, int position) {
        User user = users.get(position);

        String senderId = FirebaseAuth.getInstance().getUid();
        String senderRoom = senderId + user.getUid();

        FirebaseDatabase.getInstance().getReference()
                .child("Tafa Tafa")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            if (lastMsg != null) {
                                long time = snapshot.child("lastMsgTime").getValue(Long.class);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                                holder.binding.lastMsg.setText(AffineCipher.decrypterDonnee(lastMsg));
                                holder.binding.msgTime.setText(dateFormat.format(new Date(time)));
                            }
                        }
                        else {
                            holder.binding.lastMsg.setText("Ericre un nouveau message");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.userName.setText(AffineCipher.decrypterDonnee(user.getName()));
        Glide.with(context).load(user.getPhotoProfil())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name", AffineCipher.decrypterDonnee(user.getName()));
                intent.putExtra("uid", user.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class userViewHolder extends RecyclerView.ViewHolder {

        RowConversationBinding binding;

        public userViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = RowConversationBinding.bind(itemView);

        }
    }
}
