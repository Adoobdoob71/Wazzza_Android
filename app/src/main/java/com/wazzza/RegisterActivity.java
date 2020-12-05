package com.wazzza;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText nickname, email, password;
    MaterialToolbar toolbar;
    ImageView profilePicture;
    Button submitButton;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSharedPreferences("theme", 0).getInt("theme", R.style.AppThemeLight));
        setContentView(R.layout.activity_register);
        InitializeVariables();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_from_top);
            }
        });
    }

    public void InitializeVariables(){
        toolbar = findViewById(R.id.activity_register_toolbar);
        nickname = findViewById(R.id.activity_register_nickname_edit_text);
        email = findViewById(R.id.activity_register_email_edit_text);
        password = findViewById(R.id.activity_register_password_edit_text);
        profilePicture = findViewById(R.id.activity_register_profile_picture);
        submitButton = findViewById(R.id.activity_register_submit_button);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Drawable drawable = Drawable.createFromStream(inputStream, imageUri.toString());
                profilePicture.setImageDrawable(drawable);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void SubmitRegister(View view) {
        try {
            final StorageReference storageReferenceChild = storageReference.child("users");
            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(final @NonNull Task<AuthResult> parentTask) {
                            if (parentTask.isSuccessful()) {
                                if (nickname.getText().toString().trim().length() == 0) {
                                    Toast.makeText(RegisterActivity.this, "Nickname is empty", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("nickname", nickname.getText().toString().trim());
                                    map.put("email", email.getText().toString().trim());
                                    map.put("description", "Default description");
                                    final DocumentReference documentReference  = firebaseFirestore.collection("users").document(parentTask.getResult().getUser().getUid());
                                    documentReference.set(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        final StorageReference ref = storageReferenceChild.child(parentTask.getResult().getUser().getUid());
                                                        UploadTask uploadTask = ref.putFile(imageUri);
                                                        Task<Uri> taskUrl = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                            @Override
                                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                if (!task.isSuccessful())
                                                                    throw task.getException();
                                                                return ref.getDownloadUrl();
                                                            }
                                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Uri> task) {
                                                                if (task.isSuccessful()){
                                                                    Uri downloadUrl = task.getResult();
                                                                    Map<String, Object> update = new HashMap<String, Object>();
                                                                    update.put("profilePicture", downloadUrl);
                                                                    documentReference.update(update)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()){
                                                                                        Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                                                                        finish();
                                                                                        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_from_top);
                                                                                    }
                                                                                    else{
                                                                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                                    }
                                                    else {
                                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, parentTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }catch (Exception error){
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void pickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE);
    }
}