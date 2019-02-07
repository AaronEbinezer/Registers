package lucasnetwork.brainmagic.com.register;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.ViewDataAdapter;

public class ListData extends AppCompatActivity {
    private ListView ListData;
    private List <Users> arrayList = new ArrayList<>();
    private ViewDataAdapter adapter;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        ListData = (ListView)findViewById(R.id.list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Register");
        getalues();

    }

    private void getalues() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        String name,phone,email;
                name=dataSnapshot.child("name").getValue(String.class);
        phone= dataSnapshot.child("phonenumber").getValue(String.class);
        email=  dataSnapshot.child("email").getValue(String.class);
        arrayList.add(new Users(name,phone,email));
        adapter=new ViewDataAdapter(ListData.this,arrayList);
        ListData.setAdapter(adapter);
        adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
