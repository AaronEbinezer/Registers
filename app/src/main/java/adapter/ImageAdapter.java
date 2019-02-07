package adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.UrlLoader;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.OnClick;
import lucasnetwork.brainmagic.com.register.ImagesActivity;
import lucasnetwork.brainmagic.com.register.R;
import lucasnetwork.brainmagic.com.register.Upload;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mcontext;
    private List<Upload> mUplaods;
    private StorageReference storageReference;
    private    OnItemClickedListener listener;

 public ImageAdapter(Context context,List<Upload> uploads){
     mcontext = context;
     mUplaods = uploads;
     listener= (OnItemClickedListener) context;


 }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.imageitem,viewGroup,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Upload uploadcurrent = mUplaods.get(i);
        String urlLoader = uploadcurrent.getmImageUri();
        Uri uri = Uri.parse(urlLoader);

        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(uri.getPath());

            imageViewHolder.textViewname.setText(uploadcurrent.getmName());
        Glide.with(mcontext).using(new FirebaseImageLoader())
                .load(storageReference)
                .error(mcontext.getResources().getDrawable(R.drawable.noimages))
                .into(imageViewHolder.imageView);
          /*  Picasso.with(mcontext).load(uploadcurrent.getmImageUri())

                    .error(mcontext.getResources().getDrawable(R.drawable.noimages))
                    .into(imageViewHolder.imageView);
*/
    }

    @Override
    public int getItemCount() {
        return mUplaods.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener {
public TextView textViewname;
public ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewname = itemView.findViewById(R.id.textviewname);
            imageView = itemView.findViewById(R.id.imageviewupload);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem dowhat = menu.add(Menu.NONE,1,1,"Do WhatEver");
            MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");
            //MenuItem dowhat = menu.add(Menu.NONE,1,1,"Do WhatEver");
        dowhat.setOnMenuItemClickListener(this);
        delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if(listener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            listener.onWhatEverCLick(position);
                            return true;
                            case 2:
                            listener.onDeleteCLick(position);
                            return true;
                    }
                }
            }
        return false;
        }


    }


    public  interface OnItemClickedListener{
     void onItemClick(int position);
     void onWhatEverCLick(int position);
     void onDeleteCLick(int position);

    }
    public void setOnItemClickListener(OnItemClickedListener listener){
     listener = listener;
    }

}
