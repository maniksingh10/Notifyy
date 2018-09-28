package com.example.msnotify.notify;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class AddingInfo extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private Spinner spinner;
    private EditText editText;
    private DatabaseReference databaseReference;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
    private Button upload;
    private Uri filePath;
    private StorageReference storageReference;
    private String url;
    private ImageView imageView;
    private View root;
    private ProgressDialog progressDialog;
    private final int PICK_IMAGE_GALLERY = 2;
    private String pictureFilePath;
    static final int REQUEST_PICTURE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_info);
        root = findViewById(R.id.rootadd);
        upload = findViewById(R.id.upimg);
        spinner = findViewById(R.id.spinner2);
        editText = findViewById(R.id.notice);
        radioGroup = findViewById(R.id.radioGroup);
        TextView datetv = findViewById(R.id.date);
        datetv.setText(date);
        imageView = findViewById(R.id.uplimg);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("notice");
        storageReference = FirebaseStorage.getInstance().getReference();
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("Sending Notice to Students");
                progressDialog.show();
                done();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddingInfo.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
                            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
//                                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);

                                File pictureFile = null;
                                try {
                                    pictureFile = getPictureFile();
                                } catch (IOException ex) {
                                    Toast.makeText(AddingInfo.this,
                                            "Photo file can't be created, please try again",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (pictureFile != null) {
                                    Uri photoURI = FileProvider.getUriForFile(AddingInfo.this,
                                            getApplicationContext().getPackageName() + ".fileprovider",
                                            pictureFile);
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
                                }
                            }

                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        String pictureFile = "Manik_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        switch (requestCode) {
            case PICK_IMAGE_GALLERY:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    filePath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_PICTURE_CAPTURE:
                File imgFile = new File(pictureFilePath);
                if (imgFile.exists()) {
                    filePath = Uri.fromFile(imgFile);
                    imageView.setImageURI(Uri.fromFile(imgFile));
                }
                break;


        }

    }


    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading

            //getting the storage reference
            final StorageReference sRef = storageReference.child(FConssnts.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return sRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        url = downloadUri.toString();
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(selectedId);
                        String yer = radioButton.getText().toString();
                        String sdate = new SimpleDateFormat("dd-MMM-yy hh:mm aa", Locale.getDefault()).format(new Date());

                        String key = databaseReference.push().getKey();
                        Info info = new Info(yer, editText.getText().toString(), spinner.getSelectedItem().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), sdate, url);
                        databaseReference.child(key).setValue(info);

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(AddingInfo.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            //display an error if no file is selected
            progressDialog.dismiss();
            showError();
        }
    }


    private void done() {
        String not = editText.getText().toString();
        String bra = spinner.getSelectedItem().toString();
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (bra.isEmpty() || not.isEmpty() || selectedId == -1) {
            showError();
        } else {
            uploadFile();
        }

    }


    private void showError() {
        progressDialog.dismiss();
        Snackbar snackbar = Snackbar.make(root, "Error", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
        snackBarView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}
