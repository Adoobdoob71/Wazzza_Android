package com.wazzza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wazzza.Classes.*;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ImageButton backButton, eraseButton;
    EditText searchQuery;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebaseFirestore.collection("users");
    ArrayList<User> arrayList;
    ContactAdapter contactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSharedPreferences("theme", 0).getInt("theme", R.style.AppThemeLight));
        setContentView(R.layout.activity_search);
        InitializeVariables();
        SearchQueryHandling();
    }

    public void InitializeVariables(){
        backButton = findViewById(R.id.activity_search_back_button);
        eraseButton = findViewById(R.id.activity_search_erase_button);
        searchQuery = findViewById(R.id.activity_search_edit_text_search);
        arrayList = new ArrayList<>();
        contactAdapter = new ContactAdapter(this, arrayList);
    }

    public void HandleButtons(View view) {
        switch (view.getId()) {
            case R.id.activity_search_back_button:
                finish();
                break;
            case R.id.activity_search_erase_button:
                searchQuery.setText("");
                break;
        }
    }

    public void SearchQueryHandling(){
        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                collectionReference.orderBy("nickname").startAt(editable.toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList = new ArrayList<>();
                            contactAdapter.notifyDataSetChanged();
                            QuerySnapshot querySnapshot = task.getResult();
                            for(DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                User user = documentSnapshot.toObject(User.class);
                                arrayList.add(user);
                            }
                            contactAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }
}