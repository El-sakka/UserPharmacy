package www.sydlinaonline.com.userpharmacy.Reservation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import www.sydlinaonline.com.userpharmacy.MainActivity;
import www.sydlinaonline.com.userpharmacy.Model.Medicine;
import www.sydlinaonline.com.userpharmacy.Model.PharmacyAndMedicine;
import www.sydlinaonline.com.userpharmacy.Model.PopularSales;
import www.sydlinaonline.com.userpharmacy.Model.Reservation;
import www.sydlinaonline.com.userpharmacy.PharmacyActivity;
import www.sydlinaonline.com.userpharmacy.R;

public class ReservationActivity extends AppCompatActivity {
    private static final String TAG = "ReservationActivity";

    private static final String PHRMACY_KEY ="phrmacy";
    private static final String QUANTITY_KEY = "quantity";
    private static final String MEDICINE_KEY = "medicine";
    private static final String ITEM_LIST = "item_list";
    private static final String QUANTITY_LIST = "quantity_list";
    private static final String PHRMACY_NAME = "name";


    TextView confirmTextView;
    TextView codeTextView;
    Button backButton;


    FirebaseAuth mFirebaseAuth ;
    DatabaseReference mDatabaseReference;

    private String pharmacyName;
    private String userEmail;
    private int quantity;
    private int codeValue;
    private String location="";
    private String medName;
    private String currentDate;

    ArrayList<String> medList;
    ArrayList<Integer> quanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        setupFirebase();
        init();

        Log.d(TAG, "onCreate: "+mFirebaseAuth.getCurrentUser().getEmail());
        // getting user email
        userEmail = mFirebaseAuth.getCurrentUser().getEmail();

        Random rand = new Random();
        codeValue = rand.nextInt(1000000);

        final Intent intent = getIntent();

        String classType = intent.getStringExtra("Class");

        if(classType.equals("Package")){

            pharmacyName = intent.getStringExtra(PHRMACY_NAME);
            medList = intent.getStringArrayListExtra(ITEM_LIST);
            quanList = intent.getIntegerArrayListExtra(QUANTITY_LIST);

            Log.d(TAG, "onCreate: PhramacyName : "+pharmacyName);

            long cutoff = new Date().getTime();
            //Get days number for delete data
            Date d = new Date(cutoff );
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = dateFormatGmt.format(new Date(cutoff));

            Log.d(TAG, "onCreate: Old_date" + currentDate);


            confirmTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // reserver her
                    codeTextView.setText(""+codeValue);
                    for(int i=0;i<medList.size();i++) {

                        Log.d(TAG, "onClick: sssssss"+medList.get(i)+" "+quanList.get(i));
                        setUpDatabaseList(medList.get(i),quanList.get(i));
                        makeTransaction(pharmacyName, quanList.get(i),medList.get(i));
                        setPopularSales(medList.get(i), quanList.get(i));
                    }
                    Toast.makeText(getApplicationContext(),"Congratulations ,Your request confirmed Successfully",Toast.LENGTH_LONG).show();
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),PharmacyActivity.class);
                    startActivity(i);
                }
            });

        }else if(classType.equals("Custom")){
            pharmacyName = intent.getStringExtra(PHRMACY_KEY);
            quantity = intent.getIntExtra(QUANTITY_KEY,0);
            medName = intent.getStringExtra(MEDICINE_KEY);


            // long cutoff = new Date().getTime()- TimeUnit.MILLISECONDS.convert(10, TimeUnit.DAYS);
            long cutoff = new Date().getTime();
            //Get days number for delete data
            Date d = new Date(cutoff );
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = dateFormatGmt.format(new Date(cutoff));

            Log.d(TAG, "onCreate: Old_date" + currentDate);


            confirmTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // reserver her
                    codeTextView.setText(""+codeValue);
                    setUpDatabase();
                    makeTransaction(pharmacyName,quantity,medName);
                    setPopularSales(medName,quantity);
                    Toast.makeText(getApplicationContext(),"Congratulations ,Your request confirmed Successfully",Toast.LENGTH_LONG).show();
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),PharmacyActivity.class);
                    startActivity(i);
                }
            });

        }
    }

    private void init(){
        confirmTextView = findViewById(R.id.tv_reserve_confirm);
        codeTextView = findViewById(R.id.tv_reserve_code);
        backButton = findViewById(R.id.btn_reserve_back);
        medList = new ArrayList<>();
        quanList = new ArrayList<>();
    }


    private void setupFirebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    private void setUpDatabase(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Reservation");
        Reservation reservation = new Reservation(pharmacyName,userEmail,codeValue+"",location,quantity+"",currentDate,medName);
        mDatabaseReference.push().setValue(reservation);

    }

    private void setUpDatabaseList(String medicineName,int quantity){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Reservation");
        Reservation reservation = new Reservation(pharmacyName,userEmail,codeValue+"",location,quantity+"",currentDate,medicineName);
        mDatabaseReference.push().setValue(reservation);
    }

    private void makeTransaction(String name, final int q, final String mediName){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("PharamcyAndMedicine");
        Query query = reference.orderByChild("pharmacyKey").equalTo(name);
        Log.d(TAG, "makeTransaction: phrmacyname: "+name+" q: "+q);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PharmacyAndMedicine model = snapshot.getValue(PharmacyAndMedicine.class);
                    if(model.getMedicineKey().equals(mediName)){
                        String currentQuantity = model.getMedicineQuantity();
                        int curQuantity = Integer.valueOf(currentQuantity);
                        int value = curQuantity - q ;
                        Log.d(TAG, "onDataChange: current value: "+value+" Q: "+q);
                        snapshot.getRef().child("medicineQuantity").setValue(String.valueOf(value));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setPopularSales(String medicineName, final int quantity){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Medicine");

        Query query = mRef.orderByChild("name").startAt(medicineName).endAt(medicineName+"\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:  dataSnapshot.getChildren()){
                    Medicine model = snapshot.getValue(Medicine.class);
                    int oldVal = model.getMostSales();
                    int newVal = oldVal + quantity;
                    Log.d(TAG, "onDataChange: puplar: "+newVal);
                    snapshot.getRef().child("mostSales").setValue(newVal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
