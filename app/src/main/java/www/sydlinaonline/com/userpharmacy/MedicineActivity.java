package www.sydlinaonline.com.userpharmacy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import www.sydlinaonline.com.userpharmacy.Maps.CutomMapLocation;
import www.sydlinaonline.com.userpharmacy.Model.PharmacyAndMedicine;
import www.sydlinaonline.com.userpharmacy.Model.PharmacyInfo;

public class MedicineActivity extends AppCompatActivity {

    private static final String TAG = "MedicineActivity";

    private static final String QUANTITY_KEY = "quantity";
    private final static String NAME_KEY="NAME";
    private final static String PRICE_KEY="PRICE";
    private final static String IMAGE_KEY="IMAGE";
    private final static String DES_KEY="DES";
    private static final String BUNDLE_KEY = "bundle";
    private static final String LIST_OBJECTS = "list";
    private static final String MEDICINE_KEY = "medicine";



    private TextView medicineName;
    private TextView medicinePrice;
    private TextView medicineDescription;
    private TextView medicineQuantity;

    private ImageView medicineImage;
    private ImageView medicineUp;
    private ImageView medicineDown;
    private ImageView medicineDone;

    private int countQuantity = 0;

    private Bundle bundle;

    private String medName;
    private String medDes;
    private String medPrice;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        init();
        getDataIntent();

        medicineUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countQuantity++;
                medicineQuantity.setText(countQuantity+"");
            }
        });
        medicineDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countQuantity >0){
                    countQuantity--;
                    medicineQuantity.setText(countQuantity+"");
                }
            }
        });

        medicineDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = countQuantity;
                String name = medicineName.getText().toString();
                getDataPhrmacy(name,quantity);

            }
        });

    }



    private void getDataIntent(){
        Intent intent = getIntent();
        bundle  = intent.getBundleExtra(BUNDLE_KEY);

        medName = bundle.getString(NAME_KEY);
        medDes = bundle.getString(DES_KEY);
        medPrice = bundle.getString(PRICE_KEY);
        imageUrl = bundle.getString(IMAGE_KEY);

        medicineName.setText(medName);
        medicineDescription.setText(medDes);
        medicinePrice.setText(medPrice+"L.E");
        //Picasso.with(context).load(imageUrl).into(medImage);
        if(!imageUrl.equals(""))
            Picasso.with(this).load(imageUrl).into(medicineImage);
    }

    private void init(){
        medicineName =(TextView)findViewById(R.id.tv_detail_medicine_name);
        medicinePrice =(TextView)findViewById(R.id.tv_detail_medicine_price);
        medicineDescription =(TextView)findViewById(R.id.tv_detail_medicine_desciption);
        medicineQuantity =(TextView)findViewById(R.id.tv_detail_medicine_quantity);

        medicineImage =(ImageView) findViewById(R.id.tv_detail_medicine_image);
        medicineDown =(ImageView) findViewById(R.id.imv_detail_medicine_down);
        medicineUp =(ImageView) findViewById(R.id.imv_detail_medicine_up);
        medicineDone =(ImageView) findViewById(R.id.imv_detail_medicine_done);

    }

    private void getDataPhrmacy(String name, final int quantity){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("PharamcyAndMedicine");
        reference.keepSynced(true);

        final ArrayList<String> phramcyKeys = new ArrayList<>();

        Query query = reference.orderByChild("medicineKey").startAt(name).endAt(name+"\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        PharmacyAndMedicine pharmacyAndMedicine = snapshot.getValue(PharmacyAndMedicine.class);
                        if(quantity >= Integer.valueOf(pharmacyAndMedicine.getMedicineQuantity()));
                            phramcyKeys.add(pharmacyAndMedicine.getPharmacyKey());
                    }
                    Log.d(TAG, "onDataChange: ZZZZZZ:" +phramcyKeys.size());
                    getPhramaciesLocation(phramcyKeys);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void getPhramaciesLocation(final ArrayList<String> phramcies){
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Pharmacy");
        reference.keepSynced(true);

        final ArrayList<PharmacyInfo> object = new ArrayList<>();

        final int size = phramcies.size();
        Query query = reference.orderByChild("pharmacyKey");
        for(int i=0;i<phramcies.size();i++){
            final int count = i;
            query.startAt(phramcies.get(i)).endAt(phramcies.get(i)+"\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot :  dataSnapshot.getChildren()){
                        object.add(snapshot.getValue(PharmacyInfo.class));
                    }

                    Log.d(TAG, "onDataChange: XXX: "+object.size());
                    if(count== size-1){
                        redirectToCustomMap(object);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    void redirectToCustomMap(ArrayList<PharmacyInfo> list){
        Intent intent = new Intent(this,CutomMapLocation.class);
        intent.putParcelableArrayListExtra(LIST_OBJECTS,list);
        intent.putExtra(QUANTITY_KEY,countQuantity) ;
        intent.putExtra(MEDICINE_KEY,medName);
        startActivity(intent);
    }


}
