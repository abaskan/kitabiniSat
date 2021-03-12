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

public class SellingBookAdapter extends RecyclerView.Adapter<SellingBookAdapter.SellingBookHolder> {
    private Context context;
    private List<Book> bookList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public SellingBookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public SellingBookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_selling_list,parent,false);
        return new SellingBookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellingBookHolder holder, final int position) {
        final Book book = bookList.get(position);
        Picasso.get().load(book.getBookImageUrl()).into(holder.sellingListImage);
        holder.sellingListPrice.setText(String.format("%s TL", book.getBookPrice()));
        //holder.sellingListProvince.setText(book.getBookLocation());
        holder.sellingListTitle.setText(book.getBookTitle());

        holder.sellingListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Book newBook = new Book(book.getBookId(),book.getUserId(),book.getBookTitle()
                        , book.getBookImageUrl(),book.getBookDetail(),book.getBookLocation(),book.getBookPrice(),book.getAddingTime(),book.getFavorites(),book.getViews());

                Intent toBookDetail = new Intent(context, SellingDetailActivity.class);
                toBookDetail.putExtra("book",newBook);
                toBookDetail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(toBookDetail);
            }
        });

        holder.sellingListCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Ürün Silinsin Mi");
                alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                                .collection("SellingBook").document(book.getBookId()).delete();
                        firebaseFirestore.collection("Books").document(book.getBookId()).delete();
                        Toast.makeText(context, "Ürün  silindi", Toast.LENGTH_SHORT).show();
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
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class SellingBookHolder extends RecyclerView.ViewHolder{
        CardView sellingListCard;
        ImageView sellingListImage;
        TextView sellingListPrice;
        //TextView sellingListProvince;
        TextView sellingListTitle;

        public SellingBookHolder(@NonNull View itemView) {
            super(itemView);
            sellingListCard = itemView.findViewById(R.id.sellingListCard);
            sellingListImage = itemView.findViewById(R.id.sellingListImage);
            sellingListPrice = itemView.findViewById(R.id.sellingListPrice);
            //sellingListProvince = itemView.findViewById(R.id.sellingListProvince);
            sellingListTitle = itemView.findViewById(R.id.sellingListTitle);
        }

    }
}
