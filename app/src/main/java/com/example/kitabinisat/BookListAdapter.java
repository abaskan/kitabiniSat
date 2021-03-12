package com.example.kitabinisat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookListHolder>{
    private Context context;
    private List<Book> bookList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public BookListAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public BookListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_book_list,parent,false);
        return new BookListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookListHolder holder, int position) {
        final Book book = bookList.get(position);
        Picasso.get().load(book.getBookImageUrl()).into(holder.bookListImage);
        holder.bookListPrice.setText(String.format("%s TL", book.getBookPrice()));
        //holder.bookListProvince.setText(book.getBookLocation());
        holder.bookListTitle.setText(book.getBookTitle());

        holder.bookListAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book newFavoriteBook = new Book(book.getBookId(),book.getUserId(),book.getBookTitle()
                        , book.getBookImageUrl(),book.getBookDetail(),book.getBookLocation(),book.getBookPrice(),book.getAddingTime(),book.getFavorites() + 1,book.getViews());

                firebaseFirestore.collection("UserFields")
                        .document(firebaseAuth.getUid()).collection("FavoriteBooks")
                        .document(book.getBookId()).set(newFavoriteBook);

                firebaseFirestore.collection("UserFields")
                        .document(book.getUserId()).collection("SellingBook")
                        .document(book.getBookId()).set(newFavoriteBook);
                firebaseFirestore.collection("Books").document(book.getBookId()).update("favorites",book.getFavorites() + 1);

                holder.bookListAddFavorite.setImageResource(R.drawable.icon_favorite_red);
                Toast.makeText(context, "Ürün favorilerinize Eklendi", Toast.LENGTH_SHORT).show();
            }
        });

        holder.bookListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Book newBook = new Book(book.getBookId(),book.getUserId(),book.getBookTitle()
                        , book.getBookImageUrl(),book.getBookDetail(),book.getBookLocation(),book.getBookPrice(),book.getAddingTime(),book.getFavorites(),book.getViews() + 1);

                firebaseFirestore.collection("UserFields")
                        .document(book.getUserId()).collection("SellingBook")
                        .document(book.getBookId()).set(newBook);
                firebaseFirestore.collection("Books").document(book.getBookId()).set(newBook);

                Intent toBookDetail = new Intent(context, BookDetailActivity.class);
                toBookDetail.putExtra("book",newBook);
                toBookDetail.putExtra("value","liste");
                toBookDetail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(toBookDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookListHolder extends RecyclerView.ViewHolder{
        CardView bookListCard;
        ImageView bookListImage;
        TextView bookListPrice;
        //TextView bookListProvince;
        TextView bookListTitle;
        ImageView bookListAddFavorite;


        public BookListHolder(@NonNull View itemView) {
            super(itemView);
            bookListCard = itemView.findViewById(R.id.bookListCard);
            bookListImage = itemView.findViewById(R.id.bookListImage);
            bookListPrice = itemView.findViewById(R.id.bookListPrice);
           // bookListProvince = itemView.findViewById(R.id.bookListProvince);
            bookListTitle = itemView.findViewById(R.id.bookListTitle);
            bookListAddFavorite = itemView.findViewById(R.id.bookListAddFavorite);
        }
    }
}
