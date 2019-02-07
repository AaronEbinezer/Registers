package lucasnetwork.brainmagic.com.register;

import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import adapter.ImageAdapter;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickedListener {
        private RecyclerView recyclerView;
        private ImageAdapter imageAdapter;
private ProgressBar Progress;
    private TextView textCartItemCount;
    private Integer mCartItemCount;
private FirebaseStorage firebaseStorage;
private  ValueEventListener valueEventListener;
        private DatabaseReference mDatabaseRef;
        private List<Upload> mUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        firebaseStorage = FirebaseStorage.getInstance();
        Progress = (ProgressBar)findViewById(R.id.progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUpload = new ArrayList<>();
        imageAdapter = new ImageAdapter(ImagesActivity.this,mUpload);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(ImagesActivity.this);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
 valueEventListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mUpload.clear();
        for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
            Upload upload = postSnapshot.getValue(Upload.class);
            upload.setmKey(postSnapshot.getKey());
            mUpload.add(upload);
             mCartItemCount = mUpload.size();

        }
        imageAdapter.notifyDataSetChanged();

        Progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

    }
});
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getApplicationContext(),"Normal Click at Position"+position,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWhatEverCLick(int position) {
        Toast.makeText(getApplicationContext(),"Whatever Click at Position"+position,Toast.LENGTH_LONG).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_cart: {
                // Do something
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(mCartItemCount);
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    @Override
    public void onDeleteCLick(int position) {
    Upload SlectItem = mUpload.get(position);
    final String SelectKey = SlectItem.getmKey();

        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(SelectKey);
     try {


         imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 mDatabaseRef.child(SelectKey).removeValue();
                 Toast.makeText(getApplicationContext(), "Item Deletd", Toast.LENGTH_SHORT).show();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 mDatabaseRef.child(SelectKey).removeValue();
                 Toast.makeText(getApplicationContext(), "Item Deletd ", Toast.LENGTH_SHORT).show();

             }
         });
     }catch (Exception e)
     {
         e.printStackTrace();
     }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(valueEventListener);
    }
}
