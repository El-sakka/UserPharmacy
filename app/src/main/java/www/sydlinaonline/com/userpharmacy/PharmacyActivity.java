package www.sydlinaonline.com.userpharmacy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import www.sydlinaonline.com.userpharmacy.Model.Medicine;
import www.sydlinaonline.com.userpharmacy.Model.PharmacyAndMedicine;
import www.sydlinaonline.com.userpharmacy.Model.PharmacyInfo;

public class PharmacyActivity extends AppCompatActivity {

    private static final String TAG = "PharmacyActivity";

    private static final String CLICKED_KEY = "clicked_key";
    private static final String IMAGE_KEY = "image_key";
    private static final String DES_KEY = "des_key";
    private static final String PRICE_KEY = "price_key";
    private static final String NAME_KEY = "name_key";

    //views
    private EditText mSearchEditText;
    private ImageView mSearchImageView;

    private RecyclerView mRecyclerView;

    boolean searchPressed = false;

    //layout
    DrawerLayout drawerLayout ;

    //toolbar
    private android.support.v7.widget.Toolbar toolbar;

    FirebaseAuth mFirebaseAuth;
    DatabaseReference mRefMedicine;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupFirebase();



        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layour);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, toolbar);

        mSearchEditText = (EditText) findViewById(R.id.edt_search);
        mSearchImageView = (ImageView) findViewById(R.id.img_ic_search);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_searchList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRefMedicine = FirebaseDatabase.getInstance().getReference().child("Database")
                .child("Medicine");



        Log.d(TAG, "onCreate: FirebaseUser: "+mFirebaseAuth.getCurrentUser().getEmail());

        mSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medicineSearch  = mSearchEditText.getText().toString();
                firebaeMedicineSearch(medicineSearch);
            }
        });

    }

    private void firebaeMedicineSearch(String medicine){

        Log.d(TAG, "firebaeMedicineSearch: ");
        Query query =  mRefMedicine.orderByChild("name").startAt(medicine).endAt(medicine+ "\uf8ff");


        FirebaseRecyclerAdapter<Medicine,medicineViewHolder> adapter = new FirebaseRecyclerAdapter<Medicine, medicineViewHolder>(
                Medicine.class,
                R.layout.search_medicine_layout,
                medicineViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(medicineViewHolder viewHolder, final Medicine model, int position) {
                //        Picasso.with(mContext).load(poster).into(holder.mPosterImageView);
                Log.d(TAG, "populateViewHolder: ");
                viewHolder.mMedicineNameTextView.setText(model.getName());
                viewHolder.mMedicineDescriptionTextView.setText(model.getDescription());
                viewHolder.mMedicinePriceTextView.setText(model.getPrice()+"$");

                String imageUrl = model.getImageUrl();
                if(!imageUrl.equals(""))
                    Picasso.with(getApplicationContext()).load(imageUrl).into(viewHolder.mMedicineImageView);
                
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent  = new Intent(PharmacyActivity.this,MedicineActivity.class);
                        intent.putExtra("Class","A");
                        intent.putExtra(NAME_KEY,model.getName());
                        intent.putExtra(PRICE_KEY,model.getPrice());
                        intent.putExtra(DES_KEY,model.getDescription());
                        intent.putExtra(IMAGE_KEY,model.getImageUrl());
                        //intent.putExtra(CLICKED_KEY,bundle);
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(adapter);
    }


    public static class medicineViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public TextView mMedicineNameTextView;
        public TextView mMedicinePriceTextView;
        public TextView mMedicineDescriptionTextView;
        public ImageView mMedicineImageView;
        public medicineViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mMedicineNameTextView = (TextView)itemView.findViewById(R.id.tv_medicine_name);
            mMedicinePriceTextView = (TextView)itemView.findViewById(R.id.tv_medicine_price);
            mMedicineDescriptionTextView = (TextView)itemView.findViewById(R.id.tv_medicine_description);
            mMedicineImageView = (ImageView) itemView.findViewById(R.id.imv_medicine_image);
        }

    }


    private void setupFirebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(PharmacyActivity.this,MainActivity.class));
        }
    }



}
