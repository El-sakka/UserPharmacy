package www.sydlinaonline.com.userpharmacy;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import www.sydlinaonline.com.userpharmacy.Model.Medicine;
import www.sydlinaonline.com.userpharmacy.Model.PharmacyAndMedicine;
import www.sydlinaonline.com.userpharmacy.Model.PharmacyInfo;
import www.sydlinaonline.com.userpharmacy.Reservation.ReservationActivity;

public class PackageActivity extends AppCompatActivity {

    private static final String TAG = "PackageActivity";

    private EditText searchEditText1;
    private EditText searchEditText2;
    private EditText searchEditText3;

    private ImageView upImageView1;
    private ImageView upImageView2;
    private ImageView upImageView3;

    private TextView quantityTextView1;
    private TextView quantityTextView2;
    private TextView quantityTextView3;

    private ImageView downImageView1;
    private ImageView downImageView2;
    private ImageView downImageView3;

    private Button searchButton;

    private RecyclerView mRecyclerView;


    private int counter1 = 0, counter2 = 0, counter3 = 0;


    private ArrayList<String> itemsList;
    private ArrayList<String> quantityList;
    final ArrayList<String> list1 = new ArrayList<>();
    private ArrayList<String> medicineNameList;
    private ArrayList<Pair<String, Integer>> itemQuantityList;
    private Map<String, ArrayList<String>> map;

    // for sending them to map location
    private ArrayList<String>lat;
    private ArrayList<String>lng;
    private ArrayList<String> phramacyListName;

    //database
    DatabaseReference mRefMedicinePhramacy;
    DatabaseReference mRefPharmacy;

    private static final String LATITUDE_KEY ="latitude";
    private static final String LANGITUDE_KEY ="longitude";
    private static final String PHRMACY_KEY ="phrmacy";
    private static final String PHRAMACY_QUANTITY="phramcy_quantity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        init();

        upImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter1++;
                quantityTextView1.setText(counter1 + "");
            }
        });
        downImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter1--;
                if (counter1 < 0) counter1 = 0;
                quantityTextView1.setText(counter1 + "");
            }
        });


        upImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter2++;
                quantityTextView2.setText(counter2 + "");
            }
        });
        downImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter2--;
                if (counter2 < 0) counter2 = 0;
                quantityTextView2.setText(counter2 + "");
            }
        });

        upImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter3++;
                quantityTextView3.setText(counter3 + "");
            }
        });
        downImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter3--;
                if (counter3 < 0) counter3 = 0;
                quantityTextView3.setText(counter3 + "");
            }
        });

        deleteDatabaseSearch();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item1 = searchEditText1.getText().toString().trim();
                String item2 = searchEditText2.getText().toString().trim();
                String item3 = searchEditText3.getText().toString().trim();

                Pair<String, Integer> p1 = new Pair<>(item1, counter1);
                Pair<String, Integer> p2 = new Pair<>(item2, counter2);
                Pair<String, Integer> p3 = new Pair<>(item3, counter3);

                itemQuantityList.add(p1);
                itemQuantityList.add(p2);
                itemQuantityList.add(p3);

                for (int i = 0; i < itemQuantityList.size(); i++) {
                    if (itemQuantityList.get(i).first == "") {
                        itemQuantityList.remove(i);
                    } else if (itemQuantityList.get(i).second.equals(0)) {
                        itemQuantityList.remove(i);
                    }
                }

                for(int i=0;i<itemQuantityList.size();i++){
                    Log.d(TAG, "onClick: a7aaaa: "+itemQuantityList.get(i).first);
                }
                getDataPackage(itemQuantityList);
                showFirebaseAdapter();

            }
        });

    }


    private void init() {
        searchEditText1 = (EditText) findViewById(R.id.edt_search_item1);
        searchEditText2 = (EditText) findViewById(R.id.edt_search_item2);
        searchEditText3 = (EditText) findViewById(R.id.edt_search_item3);

        upImageView1 = (ImageView) findViewById(R.id.btn_up1);
        upImageView2 = (ImageView) findViewById(R.id.btn_up2);
        upImageView3 = (ImageView) findViewById(R.id.btn_up3);

        quantityTextView1 = (TextView) findViewById(R.id.edt_quantity_1);
        quantityTextView2 = (TextView) findViewById(R.id.edt_quantity_2);
        quantityTextView3 = (TextView) findViewById(R.id.edt_quantity_3);

        downImageView1 = (ImageView) findViewById(R.id.btn_down1);
        downImageView2 = (ImageView) findViewById(R.id.btn_down2);
        downImageView3 = (ImageView) findViewById(R.id.btn_down3);

        searchButton = (Button) findViewById(R.id.btn_search_package);

        itemsList = new ArrayList<>();
        quantityList = new ArrayList<>();
        itemQuantityList = new ArrayList<>();


        // to show all phrmacies
        lat = new ArrayList<>();
        lng = new ArrayList<>();
        phramacyListName = new ArrayList<>();



        map = new HashMap<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.rc_search_package);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void checkItem(String item) {
        if (item.equals("")) {
            // do nothing
        } else {
            itemsList.add(item);
        }
    }

    private void checkQuantity(int counter) {
        if (counter <= 0) {
            // do nothing
        } else {
            quantityList.add(counter + "");
        }
    }

    private void deleteDatabaseSearch(){
        DatabaseReference mSearchRef = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Search");
        mSearchRef.removeValue();
    }


    private void getDataPackage(final ArrayList<Pair<String, Integer>> list) {

        final ArrayList<String> list2=new ArrayList<>();
        final ArrayList<String> list3=new ArrayList<>();

        mRefMedicinePhramacy = FirebaseDatabase.getInstance().getReference().child("Database")
                .child("PharamcyAndMedicine");
        mRefMedicinePhramacy.keepSynced(true);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            final int counter = i;
            final int itemQuantity = list.get(i).second;
            //og.d(TAG, "getDataPackage: item: "+item);
            Query query = mRefMedicinePhramacy.orderByChild("medicineKey").startAt(list.get(i).first).endAt(list.get(i).first+ "\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PharmacyAndMedicine pharmacyAndMedicine = snapshot.getValue(PharmacyAndMedicine.class);
                        Log.d(TAG, "onDataChange: medicine name: "+pharmacyAndMedicine.getMedicineKey()+ " Pharmacies: " + pharmacyAndMedicine.getPharmacyKey());
                        if (counter == 0 && itemQuantity <= Integer.valueOf(pharmacyAndMedicine.getMedicineQuantity())) {
                            // m1
                            Log.d(TAG, "list1: "+pharmacyAndMedicine.getMedicineKey());
                            list1.add(pharmacyAndMedicine.getPharmacyKey());
                        }else if(counter == 1 && itemQuantity <= Integer.valueOf(pharmacyAndMedicine.getMedicineQuantity() ) ){
                            //m2
                            Log.d(TAG, "list2: "+pharmacyAndMedicine.getMedicineKey());

                            list2.add(pharmacyAndMedicine.getPharmacyKey());

                        }else if(counter ==2 && itemQuantity <= Integer.valueOf(pharmacyAndMedicine.getMedicineQuantity())){
                            //m1
                            Log.d(TAG, "list3: "+pharmacyAndMedicine.getMedicineKey());

                            list3.add(pharmacyAndMedicine.getPharmacyKey());
                        }
                    }
                    Log.d(TAG, "onDataChange: counter: "+counter);
                    if(counter == 0){
                        // made intersection
                        list1.retainAll(list2);
                        list1.retainAll(list3);

                        for(int i=0;i<list1.size();i++)
                            Log.d(TAG, "intersection: "+list1.get(i));

                        list2.clear();
                        list3.clear();
                        //go to phramccy package
                        getPharmcyPackage(list1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }


    }




    private void getPharmcyPackage(final ArrayList<String> phrmacyKeyList) {
        Log.d(TAG, "getPharmcyPackage: ggggggggggg "+phrmacyKeyList.size());

        mRefPharmacy = FirebaseDatabase.getInstance().getReference().child("Database")
                .child("Pharmacy");
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Database")
                .child("Search");
        for(int i=0;i<phrmacyKeyList.size();i++){
            Query query = mRefPharmacy.orderByChild("pharmacyName");
            final int finalI = i;
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        PharmacyInfo pharmacyInfo = snapshot.getValue(PharmacyInfo.class);
                        Log.d(TAG, "onDataChange: finalI: " +finalI);

                        if(phrmacyKeyList.get(finalI).equals(pharmacyInfo.getPharmacyName()) ){
                            reference.push().setValue(pharmacyInfo);
                            Log.d(TAG, "onDataChange: phramacy name: "+pharmacyInfo.getPharmacyName());
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    private void showFirebaseAdapter(){
        DatabaseReference mSearchRef = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Search");

        mSearchRef.keepSynced(true);

        Log.d(TAG, "showFirebaseAdapter: ");

        FirebaseRecyclerAdapter<PharmacyInfo,firebaseViewHolder> adapter =
                new FirebaseRecyclerAdapter<PharmacyInfo, firebaseViewHolder>(
                        PharmacyInfo.class,
                        R.layout.custom_search_layout,
                        firebaseViewHolder.class,
                        mSearchRef
                ) {
                    @Override
                    protected void populateViewHolder(firebaseViewHolder viewHolder, final PharmacyInfo model, final int position) {
                        Log.d(TAG, "populateViewHolder: modelNAme: "+model.getPharmacyName());
                        viewHolder.mPhramacyNameTextView.setText(model.getPharmacyName());
                        viewHolder.mPhramacyContactTextView.setText(model.getPharmacyPhone());

                        phramacyListName.add(model.getPharmacyName());
                        lat.add(model.getPharmacyLat());
                        lng.add(model.getPharmacyLan());

                        viewHolder.mPhramacyMapImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PackageActivity.this,LocationActivity.class);
                                intent.putExtra(PHRMACY_KEY,model.getPharmacyName());
                                intent.putExtra(LANGITUDE_KEY,model.getPharmacyLan());
                                intent.putExtra(LATITUDE_KEY,model.getPharmacyLat());
                                startActivity(intent);
                            }
                        });

                        viewHolder.mPhramacyDoneAllImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PackageActivity.this,ReservationActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                };

        mRecyclerView.setAdapter(adapter);
    }



    public static  class firebaseViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public TextView mPhramacyNameTextView;
        public TextView mPhramacyContactTextView;
        public ImageView mPhramacyDoneAllImageView;
        public ImageView mPhramacyMapImageView;

        public firebaseViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mPhramacyNameTextView = (TextView)itemView.findViewById(R.id.tv_search_pharmacy_name);
            mPhramacyContactTextView = (TextView)itemView.findViewById(R.id.tv_search_pharmacy_phone);
            mPhramacyDoneAllImageView = (ImageView) itemView.findViewById(R.id.tv_search_reserve);
            mPhramacyMapImageView =(ImageView)itemView.findViewById(R.id.tv_search_map);
        }
    }


}
