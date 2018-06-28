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
    DatabaseReference mRef;
    DatabaseReference mRefPhramacy;


    //ArrayList<String>phramcykeys = new ArrayList<>();
    ArrayList<String>phramcyQuantity = new ArrayList<>();
    static ArrayList<String> medicineKeys = new ArrayList<>();
    ArrayList<PharmacyInfo> phramacyList = new ArrayList<>();

   // HashSet<String> pharamcyKeys = new HashSet<>();

    Set<String>pharamcyKeys = new HashSet<>();

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


        // init Firebase database

        // init views
        mSearchEditText = (EditText) findViewById(R.id.edt_search);
        mSearchImageView = (ImageView) findViewById(R.id.img_ic_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_searchList);

        mRefPhramacy = FirebaseDatabase.getInstance().getReference().child("Database").child("Pharmacy");
        mRefPhramacy.keepSynced(true);


        Log.d(TAG, "onCreate: FirebaseUser: "+mFirebaseAuth.getCurrentUser().getEmail());



        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonPressed();
                searchPressed = true;

                if(searchPressed){
                    Log.d(TAG, "onClick: pressed");
                    final FirebaseRecyclerAdapter<PharmacyInfo,phramcyViewHolder> adapter = new FirebaseRecyclerAdapter<PharmacyInfo, phramcyViewHolder>(
                            PharmacyInfo.class,
                            R.layout.custom_search_layout,
                            phramcyViewHolder.class,
                            mRefPhramacy
                    ) {
                        @Override
                        protected void populateViewHolder(phramcyViewHolder viewHolder, PharmacyInfo model, int position) {
                            //Log.d(TAG, "populateViewHolder: A7mooos");

                            if(pharamcyKeys.contains(model.getPharmacyKey())){
                                viewHolder.mPhramcyNameTextView.setText(model.getPharmacyName());
                            }
                            Log.d(TAG, "phramacylist: "+phramacyList.size());
                        }
                    };
                    mRecyclerView.setAdapter(adapter);
                }

            }
        });

    }


    public static class phramcyViewHolder extends RecyclerView.ViewHolder{

        public TextView mPhramcyNameTextView;
        public phramcyViewHolder(View itemView) {
            super(itemView);
            mPhramcyNameTextView = itemView.findViewById(R.id.tv_search_pharmacy_name);
        }

    }




    private void searchButtonPressed(){
        String fullText = mSearchEditText.getText().toString();
        String[] parts = fullText.split("\\,");
        Log.d(TAG, "searchButtonPressed: "+parts[0]+" "+parts[1]+" "+parts[2]);
        getDataPackage(parts);
        /*
        if(!TextUtils.isEmpty(mSearchEditText.getText().toString())) {
            String fullText = mSearchEditText.getText().toString();
            String[] parts = fullText.split("\\,");
            if(parts.length > 1) {
                Arrays.sort(parts);
                getDataPackage(parts[0], parts[1], parts[2]);
            }
            else{
                // get data for single medicine
            }
        }
        else{
            Toast.makeText(this,"Search bar is Empty",Toast.LENGTH_SHORT).show();
        }*/


    }
    private void getDataPackage(String[] list){

        //Log.d(TAG, "getData: "+item1+" "+item2+" " +item3);
        mRef = FirebaseDatabase.getInstance().getReference().child("Database")
                .child("Medicine");
        mRef.keepSynced(true);
        //mRef.orderByChild("name").startAt(item2).endAt(item2+"\uf8ff");
        final int len = list.length;

        Log.d(TAG, "getDataPackage: listStringArray: "+len);
        for(int i=0;i<len;i++){
            final int counter = i;
            Log.d(TAG, "list item: "+list[i]);
            Query query = mRef.orderByChild("name").startAt(list[i]).endAt(list[i]+"\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Medicine medicine = snapshot.getValue(Medicine.class);
                        Log.d(TAG, "onDataChange: medicince Name: "+medicine.getName()+" key: "+medicine.getMedicineKey());
                        medicineKeys.add(medicine.getMedicineKey());
                    }
                    if(counter == 0){
                        Log.d(TAG, "onDataChange: counter: "+counter+" medicineSize :"+medicineKeys.size());
                        getPharmcyKeys(medicineKeys);
                        medicineKeys.clear();
                    }
                   // Log.d(TAG, "onDataChange: CCCCCC :"+counter+"SSSSSSSSSSSS "+medicineKeys.size());
                   /* getPharmcyKeys(medicineKeys);
                    medicineKeys.clear();*/

                    ///

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
            /*Log.d(TAG, "getDataPackage: counter= "+counter +" i= "+i);
            if(counter == len-1){
                //Log.d(TAG, "onDataChange: MediceneKeys: "+medicineKeys.size());

            }*/
        }

     //   medicineKeys.clear();

    }


    /*private void getDataPackage(String item1,String item2,String item3){

        Log.d(TAG, "getData: "+item1+" "+item2+" " +item3);
        mRef = FirebaseDatabase.getInstance().getReference().child("Database")
                .child("Medicine");
        mRef.keepSynced(true);

        final ArrayList<String> phrmacyKeys = new ArrayList<>();

        //mRef.orderByChild("name").startAt(item2).endAt(item2+"\uf8ff");
        Query query = mRef.orderByChild("name").startAt(item1).endAt(item3+"\uf8ff",item2+"\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Medicine medicine = snapshot.getValue(Medicine.class);
                    Log.d(TAG, "onDataChange: medicince Name: "+medicine.getName()+" key: "+medicine.getMedicineKey());
                    phrmacyKeys.add(medicine.getMedicineKey());
                }
               *//* int len = phrmacyKeys.size();
                for(int i=0;i<len;i++){
                    getPhramcyKey(phrmacyKeys.get(i),phrmacyKeys.get(i+2),phrmacyKeys.get(i+4));
                    Log.d(TAG, "medicine keys : "+phrmacyKeys.get(i));
                }*//*
               getPharmcyKeys(phrmacyKeys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


   /* private void getPhramcyKey(String key1, String key2, String key3){
        DatabaseReference mRefMedicinePhrmacy = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("PharamcyAndMedicine");
        Query query = mRefMedicinePhrmacy.orderByChild("medicineKey").startAt(key1).endAt(key3+"\uf8ff",key2+"\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PharmacyAndMedicine pharmacyAndMedicine = snapshot.getValue(PharmacyAndMedicine.class);
                    pharamcyKeys.add(pharmacyAndMedicine.getPharmacyKey());
                    phramcyQuantity.add(pharmacyAndMedicine.getMedicineQuantity());
                    Log.d(TAG, "getPhramcyKey: "+ pharmacyAndMedicine.getPharmacyKey() + " Quantity: "+pharmacyAndMedicine.getMedicineQuantity());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    private void getPharmcyKeys(ArrayList<String> medKeys){
        int len = medKeys.size();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("PharamcyAndMedicine");
        Log.d(TAG, "getPharmcyKeys: A7aaaaa1 "+len);
        for(int i=0;i<len;i++) {
            Log.d(TAG, "getPharmcyKeys: A7aaaaa2");

            Query query = reference.orderByChild("medicineKey").startAt(medKeys.get(i)).endAt(medKeys.get(i)+"\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot :  dataSnapshot.getChildren()){
                        PharmacyAndMedicine pharmacyAndMedicine = snapshot.getValue(PharmacyAndMedicine.class);
                        pharamcyKeys.add(pharmacyAndMedicine.getPharmacyKey());
                        phramcyQuantity.add(pharmacyAndMedicine.getMedicineQuantity());
                        Log.d(TAG, "onDataChange: A7aaaaa3"+pharmacyAndMedicine.getMedicineKey() +" Sizzzzz"+ pharamcyKeys.size());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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
