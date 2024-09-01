package com.example.mylibary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    public static final String BOOK_ID_KEY = "bookId";

    private ImageView bookImage;
    private Button btnCurrentlyReading, btnWantToRead, btnAlreadyRead, btnAddFavourites;
    private TextView bookName, authorName, numPages, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        String longDescription = "After the misery of life with his ghastly aunt and uncle, Harry Potter is delighted to have the chance to embark on an exciting new life at the Hogwart's School of Wizardry and Witchcraft.";
        //TODO: Get the data from recycler view in here
        Intent intent = getIntent();
        if(null != intent){ // make sure that the book passed to this activity is not null
            int bookId = intent.getIntExtra(BOOK_ID_KEY, -1);
            if(bookId != -1){
                Book incomingBook = Utils.getInstance(this).getBookById(bookId);
                if(null != incomingBook){
                    setData(incomingBook);
                    handleAlreadyRead(incomingBook);
                    handleWantToReadBooks(incomingBook);
                    handleCurrentlyReadingBooks(incomingBook);
                    handleFavouriteBooks(incomingBook);
                }
            }
        }
    }

    /**
     * Enable and Disable button
     * Add the book to Already Read Book ArrayList
     * @param book
     */
    private void handleAlreadyRead(Book book){
        ArrayList<Book> alreadyReadBooks = Utils.getInstance(this).getAlreadyReadBooks();

        boolean existInAlreadyReadBooks = false;
        for(Book b: alreadyReadBooks){
            if(b.getId() == book.getId()){
                existInAlreadyReadBooks = true;
            }
        }

        if(existInAlreadyReadBooks){
            btnAlreadyRead.setEnabled(false);
        }else{
            btnAlreadyRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utils.getInstance(BookActivity.this).addToAlreadyRead(book)){
                        Toast.makeText(BookActivity.this,"Book Added", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, AlreadyReadBooksActivity.class);
                        startActivity(intent);
                    }else{Toast.makeText(BookActivity.this,"Something Wrong, try Again", Toast.LENGTH_SHORT).show();}
                }
            });
        }
    }

    public void handleWantToReadBooks(Book book){
        ArrayList<Book> wantToReadBooks = Utils.getInstance(this).getWantToReadBooks();

        boolean existInWantToReadBooks = false;
        for(Book b: wantToReadBooks){
            if(b.getId() == book.getId()){
                existInWantToReadBooks = true;
            }
        }

        if(existInWantToReadBooks){
            btnWantToRead.setEnabled(false);
        }else{
            btnWantToRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utils.getInstance(BookActivity.this).addToWantToRead(book)){
                        Toast.makeText(BookActivity.this,"Book Added To WR", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, WantToReadActivity.class);
                        startActivity(intent);
                    }else{Toast.makeText(BookActivity.this,"Something Wrong, try Again", Toast.LENGTH_SHORT).show();}
                }
            });
        }
    }
    public void handleCurrentlyReadingBooks(Book book){
        ArrayList<Book> currentlyReadingBooks = Utils.getInstance(this).getCurrentlyReadingBooks();

        boolean existsInCurrentlyReading = false;
        for(Book b: currentlyReadingBooks){
            if(b.getId() == book.getId()){
                existsInCurrentlyReading = true;
            }
        }

        if(existsInCurrentlyReading){
            btnCurrentlyReading.setEnabled(false);
        }else{
            btnCurrentlyReading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utils.getInstance(BookActivity.this).addToCurrentlyReading(book)){
                        Toast.makeText(BookActivity.this,"Book Added CR", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, CurrentlyReadingBooksActivity.class);
                        startActivity(intent);
                    }else{Toast.makeText(BookActivity.this,"Something Wrong, try Again", Toast.LENGTH_SHORT).show();}
                }
            });
        }

    }
    public void handleFavouriteBooks(Book book){
        ArrayList<Book> favouriteBooks = Utils.getInstance(this).getFavouriteBooks();

        boolean existsInFavourite = false;
        for(Book b: favouriteBooks){
            if(b.getId() == book.getId()){
                existsInFavourite = true;
            }
        }

        if(existsInFavourite){
            btnAddFavourites.setEnabled(false);
        }else{
            btnAddFavourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utils.getInstance(BookActivity.this).addToFavourite(book)){
                        Toast.makeText(BookActivity.this,"Book Added F", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookActivity.this, FavouriteBooksActivity.class);
                        startActivity(intent);
                    }else{Toast.makeText(BookActivity.this,"Something Wrong, try Again", Toast.LENGTH_SHORT).show();}
                }
            });
        }
    }



    /**
     * Set data of the book
     * @param book
     */

    private void setData(Book book){
        bookName.setText(book.getName());
        authorName.setText(book.getAuthor());
        numPages.setText(String.valueOf(book.getPages()));
        description.setText((book.getLongDesc()) +(book.getLongDesc()) + (book.getLongDesc()) + (book.getLongDesc()) + (book.getLongDesc()) + (book.getLongDesc()));
        Glide.with(this)
                .asBitmap().load(book.getImageUrl())
                .into(bookImage);
    }

    private void initViews() {
        bookImage = findViewById(R.id.bookImage);

        btnCurrentlyReading = findViewById(R.id.btnReadingCurrently);
        btnWantToRead = findViewById(R.id.btnWantToRead);
        btnAlreadyRead = findViewById(R.id.btnAlreadyRead);
        btnAddFavourites = findViewById(R.id.btnAddFavourites);

        bookName = findViewById(R.id.txtNameOfBook);
        authorName = findViewById(R.id.txtAuthorOfBook);
        numPages = findViewById(R.id.txtNumPages);
        description = findViewById(R.id.txtDescription);
    }
}