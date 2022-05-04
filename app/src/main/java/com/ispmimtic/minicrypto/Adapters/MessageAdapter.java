package com.ispmimtic.minicrypto.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ispmimtic.minicrypto.AffineCipher;
import com.ispmimtic.minicrypto.Models.Message;
import com.ispmimtic.minicrypto.R;
import com.ispmimtic.minicrypto.databinding.ItemReceiveBinding;
import com.ispmimtic.minicrypto.databinding.ItemSentBinding;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    String senderRoom;
    String receiverRoom;

    public MessageAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom) {
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false);
            return new SentViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) {
            return ITEM_SENT;
        }
        else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        int reactions[] = new int[] {
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if (holder.getClass() == SentViewHolder.class) {
                SentViewHolder viewHolder = (SentViewHolder)holder;
                viewHolder.binding.reaction.setImageResource(reactions[pos]);
                viewHolder.binding.reaction.setVisibility(View.VISIBLE);
            }
            else {
                ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
                viewHolder.binding.reaction.setImageResource(reactions[pos]);
                viewHolder.binding.reaction.setVisibility(View.VISIBLE);
            }

            message.setReaction(pos);

            FirebaseDatabase.getInstance().getReference()
                     .child("Tafa Tafa")
                    .child(senderRoom)
                    .child("messages")
                    .child(message.getMessageId()).setValue(message);
            FirebaseDatabase.getInstance().getReference()
                    .child("Tafa Tafa")
                    .child(receiverRoom)
                    .child("messages")
                    .child(message.getMessageId()).setValue(message);
            return true;
        });

        if (holder.getClass() == SentViewHolder.class) {
            SentViewHolder viewHolder = (SentViewHolder)holder;
            viewHolder.binding.message.setText(AffineCipher.decrypterDonnee(message.getMessage()));

            if (message.getReaction() >= 0) {
                //message.setReaction(reactions[(int) message.getReaction()]);
                viewHolder.binding.reaction.setImageResource(reactions[message.getReaction()]);
                viewHolder.binding.reaction.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.binding.reaction.setVisibility(View.GONE);
            }

            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });
        }
        else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder)holder;
            viewHolder.binding.message.setText(AffineCipher.decrypterDonnee(message.getMessage()));

            if (message.getReaction() >= 0) {
                //message.setReaction(reactions[(int) message.getReaction()]);
                viewHolder.binding.reaction.setImageResource(reactions[message.getReaction()]);
                viewHolder.binding.reaction.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.binding.reaction.setVisibility(View.GONE);
            }

            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        ItemSentBinding binding;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSentBinding.bind(itemView);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        ItemReceiveBinding binding;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);
        }
    }

}
