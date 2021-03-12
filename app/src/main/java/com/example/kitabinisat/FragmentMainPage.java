package com.example.kitabinisat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentMainPage extends Fragment {
    Toolbar userMainPageToolbar;
    FloatingActionButton addBookFab;
    ArrayList <Book> bookList;
    BookListAdapter bookListAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    RecyclerView userMainPageRv;

    ArrayList<Book> newBookList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        findViewById(view);
        toolbar(view);
        addBookFabClick(view);
        getBookFromFirebase();
        setAdapter(view);

        return view;
    }

    public void toolbar(View view) {
        userMainPageToolbar = view.findViewById(R.id.userMainPageToolbar);
        userMainPageToolbar.setTitle("     Ana Sayfa");
        userMainPageToolbar.setTitleTextColor(Color.WHITE);
        ((MainActivity)getContext()).setSupportActionBar(userMainPageToolbar);
    }

    public void findViewById(View view) {
        addBookFab = view.findViewById(R.id.addBookFab);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        bookList = new ArrayList<>();
    }

    public void addBookFabClick(View view){
        addBookFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddBook = new Intent(getActivity(),BookUploadActivity.class);
                startActivity(toAddBook);

            }
        });
    }

    public void setAdapter(View view) {
        userMainPageRv = view.findViewById(R.id.userMainPageRv);
        userMainPageRv.setHasFixedSize(true);
        userMainPageRv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        bookListAdapter = new BookListAdapter(getActivity(), bookList);
        userMainPageRv.setAdapter(bookListAdapter);
    }

    public void getBookFromFirebase(){
        CollectionReference collectionReference = firebaseFirestore.collection("Books");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
               if(value != null){
                  for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                      String id = documentSnapshot.getString("userId");
                      if (!id.matches(firebaseAuth.getUid())){
                          Book book = documentSnapshot.toObject(Book.class);
                          bookList.add(book);
                          bookListAdapter.notifyDataSetChanged();
                      }
                  }
               }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}