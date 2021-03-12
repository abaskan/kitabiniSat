package com.example.kitabinisat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Random;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListHolder>{
    private Context context;
    private ArrayList<String> userNameList;
    private ArrayList<String> userIdList;
    FirebaseAuth firebaseAuth;

    public ChatListAdapter(Context context, ArrayList<String> userNameList, ArrayList<String> userIdList) {
        this.context = context;
        this.userNameList = userNameList;
        this.userIdList = userIdList;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_chat_list,parent,false);
        return new ChatListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListHolder holder, final int position) {
        holder.chatListUserIcon.setText(userNameList.get(position).substring(0,1).toUpperCase());
        Random random = new Random();
        final int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ((GradientDrawable) holder.chatListUserIcon.getBackground()).setColor(color);

        holder.chatListUserName.setText(userNameList.get(position));

        holder.chatListCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChat = new Intent(v.getContext(), ChatActivity.class);
                toChat.putExtra("sellerId",userIdList.get(position));
                toChat.putExtra("sellerName",userNameList.get(position));
                toChat.putExtra("userId",firebaseAuth.getUid());
                toChat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(toChat);

            }
        });
    }

    @Override
    public int getItemCount() {
        return userIdList.size();
    }

    public class ChatListHolder extends RecyclerView.ViewHolder{
        TextView chatListUserIcon;
        TextView chatListUserName;
        CardView chatListCardView;

        public ChatListHolder(@NonNull View itemView) {
            super(itemView);
            chatListUserIcon = itemView.findViewById(R.id.toSellerProfileUserIcon);
            chatListUserName = itemView.findViewById(R.id.chatListUserName);
            chatListCardView = itemView.findViewById(R.id.chatListCardView);
        }
    }
}
