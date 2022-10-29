package com.example.quiziac;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    FirebaseFirestore database;
    CircleImageView profilePic;
    TextView email;
    EditText username;
    ProgressBar progressBar;
    TextView deleteAccount;

    Button updatebtn;
    String name,fname;
    public static final int CAMERA_CODE = 200;
    public static final int GALLERY_CODE = 100;

    FirebaseUser user;

    FirebaseStorage storage;
    Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_edit_profile, container, false);

        Permissions(); // for permission

        //find all the variable with its id
        profilePic =view.findViewById(R.id.profilePic);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        updatebtn = view.findViewById(R.id.update);
        deleteAccount = view.findViewById(R.id.deleteAccount);
        progressBar = view.findViewById(R.id.progressBar);


        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        //storageReference = FirebaseStorage.getInstance().getReference();

        database = FirebaseFirestore.getInstance();

        database.collection("users").document(user.getUid()).addSnapshotListener((value, error) -> {
            if(Objects.requireNonNull(value).exists())
            {
                User users = Objects.requireNonNull(value).toObject(User.class);
                username.setText(Objects.requireNonNull(users).getUsername());
                fname = users.getUsername();
                username.setSelection(users.getUsername().length());
                email.setText(users.getEmail());
                if(users.getProfileImage().equals("default")){
                    profilePic.setImageResource(R.drawable.ic_user);
                }
                else
                {
                    Activity activity = getActivity();
                    if(activity != null){

                        Glide.with(requireActivity().getApplicationContext()).load(users.getProfileImage()).into(profilePic);

                    }

                }
            }

        });


        deleteAccount.setOnClickListener(view13 -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(view13.getContext());

            dialog.setTitle("Are you sure?");
            dialog.setMessage("Deleting this account will result is completely removing your " +
                    "account from the system and you won't be able to access the app.");


            dialog.setPositiveButton("Delete", (dialogInterface, i) -> {
                progressBar.setVisibility(View.VISIBLE);

                deleteFromCloud();
            });


            dialog.setNegativeButton("Dismiss", (dialogInterface, i) -> dialogInterface.dismiss());
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
        });


        updatebtn.setOnClickListener(view12 -> {
             name = username.getText().toString();


            Map<String,Object> map =new HashMap<>();

            if(name.length() < 3 || name.length() > 12)
            {
                username.setError("Enter valid name");
                username.requestFocus();
                return;
            }
            else {
                if(!name.equals(fname))
                {
                    map.put("username", name);
                    database.collection("users").document(user.getUid()).update(map).addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Updated successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update", Toast.LENGTH_SHORT).show());
                }
            }
        });


        profilePic.setOnClickListener(view1 -> changeImage());


        return view;
    }

    private void changeImage() {

        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose an Option");

        builder.setItems(options, (dialog, i) -> {


            if (i==0) {

                openCamera();

            }

            if (i==1) {

                openGallery();
            }
        });

        builder.create().show();
    }

    private void openGallery() {


        Intent intent = new Intent (Intent.ACTION_PICK);
        intent.setType("image/*"); // all kinds of images
        startActivityForResult(intent, GALLERY_CODE);
    }

    private void openCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        values.put(MediaStore.Images.Media.TITLE, "Temp Desc");
        imageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_CODE);
    }

    private void Permissions() {


        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }

                }).check();



    }


    private void deleteFromCloud(){database.collection("users").document(user.getUid()).delete().addOnSuccessListener(unused -> deleteFromAuth());}private void deleteFromAuth(){user.delete().addOnCompleteListener(task -> {if(task.isSuccessful()){progressBar.setVisibility(View.GONE);Intent i = new Intent(getActivity(), login_activity.class);i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);startActivity(i);Toast.makeText(getActivity(), "Account deleted successfully", Toast.LENGTH_LONG).show();}else{Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();}});}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {

            imageUri = data.getData();

            String filepath = "Photos/" + "userprofile_"+new Date().getTime() + user.getUid()+".png";

            StorageReference reference = FirebaseStorage.getInstance().getReference(filepath);
            reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {

                Task<Uri> task = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl();

                task.addOnSuccessListener(uri -> {

                    String imageURL = uri.toString();

                    //DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                    DocumentReference reference1 = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("profileImage", imageURL);
                    reference1.update(hashMap);
                    Toast.makeText(getActivity(), "Please wait...", Toast.LENGTH_SHORT).show();


                });


            });


        }



        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {

            Uri uri = imageUri;

            String filepath = "Photos/" + "userprofile_"+new Date().getTime() + user.getUid()+".png";

            StorageReference reference = FirebaseStorage.getInstance().getReference(filepath);
            reference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

                Task<Uri> task = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl();

                task.addOnSuccessListener(uri1 -> {

                    String imageURL = uri1.toString();

                    DocumentReference reference1 = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("profileImage", imageURL);
                    reference1.update(hashMap);
                    Toast.makeText(getActivity(), "Please wait...", Toast.LENGTH_SHORT).show();
                });
            });
        }
    }

}
