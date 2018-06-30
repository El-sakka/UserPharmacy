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
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import www.sydlinaonline.com.userpharmacy.MainActivity;
import www.sydlinaonline.com.userpharmacy.Model.PharmacyAndMedicine;
import www.sydlinaonline.com.userpharmacy.Model.Reservation;
import www.sydlinaonline.com.userpharmacy.PharmacyActivity;
import www.sydlinaonline.com.userpharmacy.R;

public class ReservationActivity extends AppCompatActivity {
    private static final String TAG = "ReservationActivity";

    private static final String PHRMACY_KEY ="phrmacy";
    private static final String QUANTITY_KEY = "quantity";
    private static final String MEDICINE_KEY = "medicine";


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

        Intent intent = getIntent();
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
                makeTransaction(pharmacyName,quantity);
                Toast.makeText(getApplicationContext(),"Congratulations ,Your request confirmed Successfully",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void init(){
        confirmTextView = findViewById(R.id.tv_reserve_confirm);
        codeTextView = findViewById(R.id.tv_reserve_code);
        backButton = findViewById(R.id.btn_reserve_back);
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

    private void makeTransaction(String name, final int q){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("PharamcyAndMedicine");
        Query query = reference.orderByChild("pharmacyKey").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PharmacyAndMedicine model = snapshot.getValue(PharmacyAndMedicine.class);
                    if(model.getMedicineKey().equals(medName)){
                        String currentQuantity = model.getMedicineQuantity();
                        int curQuantity = Integer.valueOf(currentQuantity);
                        int value = curQuantity - q ;
                        snapshot.getRef().child("medicineQuantity").setValue(String.valueOf(value));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
