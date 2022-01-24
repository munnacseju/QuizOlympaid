package com.example.navtest.ui.gallery;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.navtest.Handle;
import com.example.navtest.R;
import com.example.navtest.ui.gallery.GalleryViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private EditText nameEditText, phoneNoEditText, institutionEditText, classEditText;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    Button editprofileButton, changeProfilePic, confirmButton;
    Handle handle;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    ImageView changeImageView;
    private static final int IMAGE_REQUEST1=1;
    Uri imageUri;
    ProgressBar editProfileProgressBar;
    private Boolean hasImage=false;
    private String emailString;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);



        editProfileProgressBar = root.findViewById(R.id.editProfileProgressBarid);
        confirmButton = root.findViewById(R.id.confirmButtonid);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        emailString = sharedPreferences.getString("email", "");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("flag", false);
        editor.commit();

        changeImageView = root.findViewById(R.id.epchangeImageid);
        editprofileButton = root.findViewById(R.id.epeditProfileButtonid);
        changeProfilePic = root.findViewById(R.id.epchangeProfilePictureButtonid);
        phoneNoEditText = root.findViewById(R.id.epphoneNumberEditTextid);
        nameEditText = root.findViewById(R.id.epnameEditTextid);
        institutionEditText = root.findViewById(R.id.epinstitutionEditTextid);
        classEditText = root.findViewById(R.id.epclassEditTextid);

        databaseReference = firebaseDatabase.getReference(emailString.replace(".","rm"));
        storageReference = firebaseStorage.getReference("profile");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                handle = dataSnapshot.getValue(Handle.class);

                phoneNoEditText.setText(handle.getPhone());
                nameEditText.setText(handle.getName());
                institutionEditText.setText(handle.getInstitution());
                classEditText.setText(handle.getClas());
                root.findViewById(R.id.progressBarid).setVisibility(View.GONE);
                editprofileButton.setVisibility(View.VISIBLE);
                changeProfilePic.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        editprofileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handle.setClas(classEditText.getText().toString().trim());
                handle.setInstitution(institutionEditText.getText().toString());
                handle.setName(nameEditText.getText().toString());
                handle.setPhone(phoneNoEditText.getText().toString());

                databaseReference.setValue(handle);

                Toast.makeText(getContext(), "Your Porfile has been updated", Toast.LENGTH_SHORT).show();

            }
        });

        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root.findViewById(R.id.profileChangeLinearLayoutid).setVisibility(View.GONE);
                root.findViewById(R.id.profilePicChangeLinearLayoutid).setVisibility(View.VISIBLE);
                storageReference = firebaseStorage.getReferenceFromUrl(handle.getImageDounloadUri());
                storageReference.delete();
                imageChooser();

            }
        });




        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(hasImage==false){
                    Toast.makeText(getContext(), "You didn't select any image", Toast.LENGTH_SHORT).show();
                    return;
                }

                editProfileProgressBar.setVisibility(View.VISIBLE);
                StorageReference storageReference1 = storageReference.child(System.currentTimeMillis()+"."+getFileExtention(imageUri));

                storageReference1.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri imageDownloadUri = uriTask.getResult();


                        handle.setImageDounloadUri(imageDownloadUri.toString());
                        databaseReference.setValue(handle);
                        Toast.makeText(getContext(), "Successfully Profile Picture changed!", Toast.LENGTH_SHORT).show();
                        editProfileProgressBar.setVisibility(View.GONE);



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Sorry!", Toast.LENGTH_SHORT).show();
                        editProfileProgressBar.setVisibility(View.GONE);

                    }
                });



            }
        });


        return root;


    }

    public void imageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode == IMAGE_REQUEST1 && resultCode == getActivity().RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            Picasso.with(getContext()).load(imageUri).into(changeImageView);
            changeImageView.setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.VISIBLE);
            hasImage = true;
        }




    }

    //Get File Extention
    public String getFileExtention(Uri imageUri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("flag", false);
        editor.commit();
    }
}