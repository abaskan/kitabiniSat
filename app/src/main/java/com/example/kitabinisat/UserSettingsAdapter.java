package com.example.kitabinisat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class UserSettingsAdapter extends RecyclerView.Adapter<UserSettingsAdapter.UserSettingsHolder> {
    private Context context;
    private List<UserSettingsClass> userSettingsClassList;
    private FirebaseAuth firebaseAuth;

    public UserSettingsAdapter(Context context, List<UserSettingsClass> userSettingsClassList) {
        this.context = context;
        this.userSettingsClassList = userSettingsClassList;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public UserSettingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_user_settings,parent,false);
        return new UserSettingsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSettingsHolder holder, final int position) {

        UserSettingsClass userSettingsClass = userSettingsClassList.get(position);
        holder.userSettingsTextview.setText(userSettingsClass.getSettingName());
        holder.userSettingsImageview.setImageResource(context.getResources()
                .getIdentifier(userSettingsClass.getSettingIconName(),"drawable",context.getPackageName()));

        holder.userSettingsCardview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        Intent toUserInfo = new Intent(v.getContext(),UserInfoActivity.class);
                        toUserInfo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(toUserInfo);
                        break;

                    case 1:
                        Intent toUserAppHistory = new Intent(v.getContext(), MySalesActivity.class);
                        toUserAppHistory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(toUserAppHistory);
                        break;

                    case 4:
                        firebaseAuth.signOut();
                        Intent toUserLogin = new Intent(v.getContext(),LoginActivity.class);
                        context.startActivity(toUserLogin);
                        toUserLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return userSettingsClassList.size();
    }

    public class UserSettingsHolder extends RecyclerView.ViewHolder{
        public ImageView userSettingsImageview;
        public TextView userSettingsTextview;
        public CardView userSettingsCardview;
        public UserSettingsHolder(@NonNull View itemView) {
            super(itemView);
            userSettingsImageview = itemView.findViewById(R.id.userSettingsImageview);
            userSettingsTextview = itemView.findViewById(R.id.userSettingsTextview);
            userSettingsCardview = itemView.findViewById(R.id.userSettingsCardview);
        }
    }
}
