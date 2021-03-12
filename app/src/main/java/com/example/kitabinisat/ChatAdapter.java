package com.example.kitabinisat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder>{
    Context context;
    List<Message> messageList;
    String userId;
    FirebaseAuth firebaseAuth;
    int viewUser = 1, viewSeller = 2;
    Boolean state;

    public ChatAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();
        state = false;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == viewUser){
            view = LayoutInflater.from(context).inflate(R.layout.card_message_right,parent,false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.card_message_left,parent,false);
        }
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        Message message = messageList.get(position);
        holder.messageText.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder{
        TextView messageText;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            if (state){
                messageText = itemView.findViewById(R.id.messageTextRight);
            }else {
                messageText = itemView.findViewById(R.id.messageTextLeft);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getFrom().equals(userId)){
            state = true;
            return viewUser;
        }else{
            state = false;
            return viewSeller;
        }
    }
}
