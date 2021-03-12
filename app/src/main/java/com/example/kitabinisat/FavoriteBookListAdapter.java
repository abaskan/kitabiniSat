package com.example.kitabinisat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteBookListAdapter extends RecyclerView.Adapter<FavoriteBookListAdapter.FavoriteBookListHolder> {
    private Context context;
    private List<Book> bookList;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    public FavoriteBookListAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public FavoriteBookListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_favorite_book_list,parent,false);
        return new FavoriteBookListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteBookListHolder holder, final int position) {
        final Book book = bookList.get(position);
        Picasso.get().load(book.getBookImageUrl()).into(holder.favoriteBookListImage);
        holder.favoriteBookListPrice.setText(String.format("%s TL", book.getBookPrice()));
        holder.favoriteBookListTitle.setText(book.getBookTitle());

        holder.favoriteBookListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book newBook = new Book(book.getBookId(),book.getUserId(),book.getBookTitle()
                        , book.getBookImageUrl(),book.getBookDetail(),book.getBookLocation(),book.getBookPrice(),book.getAddingTime(),book.getFavorites(),book.getViews());

                Intent toBookDetail = new Intent(context, BookDetailActivity.class);
                toBookDetail.putExtra("book",newBook);
                toBookDetail.putExtra("value","favori");
                toBookDetail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(toBookDetail);
            }
        });

        holder.favoriteBookListDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Ürün Favorilerimden Silinsin Mi");
                alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                                .collection("FavoriteBooks").document(book.getBookId()).delete();

                        Toast.makeText(context, "Ürün favorilerinizden silindi", Toast.LENGTH_SHORT).show();

                        bookList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,getItemCount());

                    }
                });

                alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });



                alertDialog.create().show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class FavoriteBookListHolder extends RecyclerView.ViewHolder{
        CardView favoriteBookListCard;
        ImageView favoriteBookListImage;
        TextView favoriteBookListPrice;
        TextView favoriteBookListProvince;
        TextView favoriteBookListTitle;
        ImageView favoriteBookListDelete;
        public FavoriteBookListHolder(@NonNull View itemView) {
            super(itemView);

            favoriteBookListCard = itemView.findViewById(R.id.favoriteBookListCard);
            favoriteBookListImage = itemView.findViewById(R.id.favoriteBookListImage);
            favoriteBookListPrice = itemView.findViewById(R.id.favoriteBookListPrice);
            favoriteBookListTitle = itemView.findViewById(R.id.favoriteBookListTitle);
            favoriteBookListDelete = itemView.findViewById(R.id.favoriteBookListDelete);

        }
    }
}
