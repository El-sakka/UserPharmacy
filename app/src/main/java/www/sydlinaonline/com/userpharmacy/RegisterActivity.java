package www.sydlinaonline.com.userpharmacy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    //Firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //widgets
    private EditText mEmail, mName,mPassword, mConfirmPassword;
    private Button mRegister;
    //private ProgressBar mProgressBar;


    //vars
    private String email, name, password;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegister = (Button) findViewById(R.id.btn_register);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mUser = new User();
        Log.d(TAG, "onCreate: started");


        init();
    }

    private void init() {


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = mEmail.getText().toString();
                password = mPassword.getText().toString();

                if (checkInputs(email, password)) {
                    registerNewEmail(email, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Return true if @param 's1' matches @param 's2'
     * @param s1
     * @param s2
     * @return
     */
    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
    }

    /**
     * Checks all the input fields for null
     * @param email
     * @param password
     * @return
     */
    private boolean checkInputs(String email, String password){
        Log.d(TAG, "checkInputs: checking inputs for null values");
        if(email.equals("")|| password.equals("")){
            Toast.makeText(RegisterActivity.this, "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

      /*
    ---------------------------Firebase-----------------------------------------
     */


    private void setupFirebaseAuth(){
        mFirebaseAuth = FirebaseAuth.getInstance();

    }

    /**
     * Register a new email and password to Firebase Authentication
     * @param email
     * @param password
     */
    public void registerNewEmail(final String email, String password){

        setupFirebaseAuth();

        mFirebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "registerNewEmail: onComplete: " + task.isSuccessful());
                        if(task.isSuccessful()){
                            // shof han3mel eh
                            Toast.makeText(RegisterActivity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                            redirectLoginScreen();
                        }else{
                            Toast.makeText(RegisterActivity.this,"Can't be Registered , please try again",Toast.LENGTH_SHORT).show();
                        }
                       // hideProgressBar();
                    }
                });
    }


    private void redirectLoginScreen(){
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
