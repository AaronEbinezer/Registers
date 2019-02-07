package lucasnetwork.brainmagic.com.register;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.LeadingMarginSpan;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class FilesUpload extends AppCompatActivity {
    private Button btnChoose, btnUpload;
    private ImageView imageView;
        private EditText ImageName;
        private Button ViewImage;
    private Uri filePath;
    private StorageReference firebaseStorage;
    private DatabaseReference storageReference;
    private StorageTask mUploadTask;

    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_upload);
         btnChoose = (Button)findViewById(R.id.chooseimage);
        btnUpload = (Button)findViewById(R.id.uploadimages);
        imageView = (ImageView)findViewById(R.id.imageget);
        ViewImage = (Button)findViewById(R.id.viewimage);
        ImageName = (EditText)findViewById(R.id.editname);
        firebaseStorage = FirebaseStorage.getInstance().getReference("uploads");
        storageReference = FirebaseDatabase.getInstance().getReference("uploads");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(getApplicationContext(),"Upload in Progress",Toast.LENGTH_SHORT).show();
                }else
                UploadImage();
            }
        });
        ViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageActivity();
            }
        });
        
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });
    }

    private void openImageActivity() {
            Intent i =new Intent(FilesUpload.this,ImagesActivity.class);
            startActivity(i);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void UploadImage(){
        if(filePath!= null){
            final ProgressDialog progressDialog =new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            final StorageReference ref = firebaseStorage.child("uploads" + System.currentTimeMillis() +"."+getFileExtension(filePath));

            mUploadTask = ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          /*  ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri downloadUri = uri;
                                }
                            });*/
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.setProgress(0);
                                }
                                },500);
                            Toast.makeText(getApplicationContext(),"UploadSuccesss",Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(ImageName.getText().toString().trim(),taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            String UploadId = storageReference.push().getKey();
                            storageReference.child(UploadId).setValue(upload);
                            progressDialog.dismiss();
                            onNotification();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+(int)progress);                        }
                    });
           /* ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Upload Success",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Upload Failed",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+(int)progress);
                        }
                    });*/

        }else {
            Toast.makeText(getApplicationContext(),"No File Selected",Toast.LENGTH_SHORT).show();
        }

    }

    private void onNotification() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder

                (getApplicationContext()).
                setContentTitle(getResources().getString(R.string.app_name)).
                setContentText("Images Uploaded Successfully!!!").
                setVibrate(new long[]{100,250,100,250,100,250}).
                setSound(alarmSound).
                setSmallIcon(R.drawable.download).build();
        notif.notify(1,notify);
    }

    private void ChooseImage() {
        Intent i =new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Upload Image"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
         try {

             Picasso.with(getApplicationContext()).load(filePath).into(imageView);
            /* Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
             imageView.setImageBitmap(bitmap);*/
         }catch (Exception e){
             e.printStackTrace();
         }
         }
    }
}
