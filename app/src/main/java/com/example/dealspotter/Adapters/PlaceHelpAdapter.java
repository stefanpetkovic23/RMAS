package com.example.dealspotter.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dealspotter.Models.Favorites;
import com.example.dealspotter.Models.Place;
import com.example.dealspotter.R;
import com.example.dealspotter.ui.DetailFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceHelpAdapter extends RecyclerView.Adapter<PlaceHelpAdapter.MyViewHolder>{

    Context context;
    ArrayList<Place> list;

    private Map<String, Favorites> favoriteStatusMap;

    boolean isinfavorites = false;


    public PlaceHelpAdapter(Context context, ArrayList<Place> list) {
        this.context = context;
        this.list = list;
        this.favoriteStatusMap = new HashMap<>();
        populateFavoriteStatusMap();
    }
    @NonNull
    @Override
    public PlaceHelpAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemestate,parent,false);
        return new PlaceHelpAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHelpAdapter.MyViewHolder holder, int position) {
        Place place = list.get(position);
        holder.titletxt.setText(place.getTitle());
        holder.pricetxt.setText(place.getPrice().toString()+"€");
        holder.surfacetxt.setText(place.getSurface().toString()+"m2");
        holder.txtadress.setText(place.getAdress());
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        reference.child("Place Pic").child(place.getTitle()+".jpg")
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.estatephoto.getContext())
                                .load(uri)
                                .into(holder.estatephoto);
                    }
                });
        holder.itemplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetailFragment(place.getTitle(), place.getAdress(), place.getNumberofrooms(),place.getNumberofbathrooms(),place.getGarage(),place.getSurface(), place.getDescription(), place.getPhotoURL(), place.getPrice(), place.getOwner())).addToBackStack(null).commit();

            }
        });

        if (favoriteStatusMap.containsKey(place.getTitle())) {
            holder.favorite.setImageResource(R.drawable.baseline_favorite_24);
            holder.favorite.setColorFilter(Color.argb(255, 255, 0, 0));
        } else {
            holder.favorite.setImageResource(R.drawable.baseline_favorite_border_24);
        }
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFavoriteStatus(place);
            }
        });

    }


    private void populateFavoriteStatusMap() {
        // Query user's favorites from Firebase and populate favoriteStatusMap
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Favorites");

        favoritesRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot placeSnapshot : snapshot.getChildren()) {
                    Favorites favorite = placeSnapshot.getValue(Favorites.class);
                    if (favorite != null) {
                        favoriteStatusMap.put(favorite.getPropertyKey(), favorite);
                    }
                }
                notifyDataSetChanged(); // Update the adapter to reflect favorite status
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void toggleFavoriteStatus(Place place) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Favorites");
        DatabaseReference userFavoritesRef = favoritesRef.child(userID).child(place.getTitle());
        if (favoriteStatusMap.containsKey(place.getTitle())) {
            // Property is currently favorite, remove from favorites
            userFavoritesRef.removeValue();
            favoriteStatusMap.remove(place.getTitle());
        } else {
            // Property is not favorite, add to favorites
            Favorites favorite = new Favorites(place.getTitle(), true);
            userFavoritesRef.setValue(favorite);
            favoriteStatusMap.put(place.getTitle(), favorite);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView estatephoto,favorite;
        TextView titletxt,pricetxt,surfacetxt,txtadress;
        ConstraintLayout itemplace;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titletxt = itemView.findViewById(R.id.recycletitle);
            pricetxt = itemView.findViewById(R.id.itemprice);
            surfacetxt = itemView.findViewById(R.id.itemsurface);
            estatephoto = itemView.findViewById(R.id.itempic);
            txtadress = itemView.findViewById(R.id.itemadress);
            itemplace = itemView.findViewById(R.id.placeitemcont);
            favorite = itemView.findViewById(R.id.favorite);

        }
    }
}
