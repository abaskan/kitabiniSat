package com.example.kitabinisat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    Toolbar userChatsToolbar;
    RecyclerView userChatsRv;
    EditText userChatsText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    List<Message> messageList;
    ChatAdapter chatAdapter;
    Button userChatSendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar();
        findViewById();
        getMessageFromFirebase();
    }

    public void userChatsSendButtonClick(View view){
        String userId = firebaseAuth.getUid();
        String sellerId = getIntent().getStringExtra("sellerId");
        String message = userChatsText.getText().toString().trim();

        if(message.isEmpty()){
            userChatsText.setText("");
            userChatsText.setHint("Boş mesaj gönderilemez");

        }else{
            userChatsText.setHint("Mesajınızı yazabilirsiniz");
            String date = getDate();
            sendMessageToFirebase(userId,sellerId,message,date,userId);
            userChatsText.setText("");
        }

    }

    public void sendMessageToFirebase (final String userId, final String sellerId, String message, String date, String from) {
        final String messageId = databaseReference.child("Chats").child(userId).child(sellerId).push().getKey();
        final Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("message",message);
        messageMap.put("date",date);
        messageMap.put("from",from);

        databaseReference.child("Chats").child(userId).child(sellerId).child(messageId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                databaseReference.child("Chats").child(sellerId).child(userId).child(messageId).setValue(messageMap);
            }
        });
    }

    public void getMessageFromFirebase(){
        String sellerId = getIntent().getStringExtra("sellerId");
        databaseReference.child("Chats").child(firebaseAuth.getUid()).child(sellerId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messageList.add(message);
                chatAdapter.notifyDataSetChanged();
                userChatsRv.scrollToPosition(messageList.size() -1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void toolbar(){
        String sellerName = getIntent().getStringExtra("sellerName");
        userChatsToolbar = findViewById(R.id.userChatsToolbar);
        userChatsToolbar.setTitle(sellerName);
        userChatsToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        userChatsToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(userChatsToolbar);
        userChatsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById(){
        userChatsRv = findViewById(R.id.userChatsRv);
        userChatsText = findViewById(R.id.userChatsText);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        messageList = new ArrayList<>();
        userChatSendButton = findViewById(R.id.userChatsSendButton);

        userChatsRv.setHasFixedSize(true);
        userChatsRv.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(this,messageList);
        userChatsRv.setAdapter(chatAdapter);

    }

    public String getDate (){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String date = dateFormat.format(today);
        return date;
    }
}