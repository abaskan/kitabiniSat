package com.example.kitabinisat;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentSelling extends Fragment {
    ArrayList<Book> bookList;
    SellingBookAdapter sellingBookAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    RecyclerView sellingBookRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selling, container, false);


        findViewById(view);
        getFavoritesFromFirebase();
        setAdapter(view);


        return view;
    }

    public void getFavoritesFromFirebase(){

        CollectionReference collectionReference = firebaseFirestore.collection("UserFields")
                .document(firebaseAuth.getUid()).collection("SellingBook");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()){

                        Book book = documentSnapshot.toObject(Book.class);
                        bookList.add(book);

                        sellingBookAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }


    public void findViewById(View view) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        bookList = new ArrayList<>();
    }

    public void setAdapter(View view) {
        sellingBookRv = view.findViewById(R.id.sellingBookRv);
        sellingBookRv.setHasFixedSize(true);
        sellingBookRv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));        sellingBookAdapter = new SellingBookAdapter(getActivity(), bookList);
        sellingBookRv.setAdapter(sellingBookAdapter);
    }
}