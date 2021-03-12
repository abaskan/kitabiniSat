package com.example.kitabinisat;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FragmentFavorites extends Fragment {
    Toolbar userFavoritesListToolbar;
    ArrayList<Book> bookList;
    FavoriteBookListAdapter favoriteBookListAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    RecyclerView userFavoritesListRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        findViewById(view);
        toolbar(view);
        getFavoritesFromFirebase();
        setAdapter(view);

        return view;
    }

    public void getFavoritesFromFirebase(){
        CollectionReference collectionReference = firebaseFirestore.collection("UserFields")
                .document(firebaseAuth.getUid()).collection("FavoriteBooks");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                        Book book = documentSnapshot.toObject(Book.class);
                        bookList.add(book);
                        favoriteBookListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    public void toolbar(View view) {
        userFavoritesListToolbar = view.findViewById(R.id.userFavoritesListToolbar);
        userFavoritesListToolbar.setTitle("     Favorilerim");
        userFavoritesListToolbar.setTitleTextColor(Color.WHITE);
    }

    public void findViewById(View view) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        bookList = new ArrayList<>();
    }

    public void setAdapter(View view) {
        userFavoritesListRv = view.findViewById(R.id.userFavoritesListRv);
        userFavoritesListRv.setHasFixedSize(true);
        userFavoritesListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoriteBookListAdapter = new FavoriteBookListAdapter(getActivity(), bookList);
        userFavoritesListRv.setAdapter(favoriteBookListAdapter);
    }


}