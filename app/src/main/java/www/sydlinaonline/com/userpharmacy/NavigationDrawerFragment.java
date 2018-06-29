package www.sydlinaonline.com.userpharmacy;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    //Name of file for Sharedpreference to save data
    public static final String PREF_FILE_NAME="text_pref";
    public static final String KEY_USER_LEARNERD_DRAWER= "user_learned_drawer";

    // this indicate if the user awear of drawber exist or not
    private boolean mUserLearnedDrawer;
    // this indicate if the fragment start for first time or it comes back from rotation
    private boolean mFromSavedInstanceState;

    private View containerView;



    // views
    private TextView mPhrmacyTextView;
    private TextView mCategoryTextView;
    private TextView mMedicineTextView;
    private TextView mPackageTextView;
    private Button mSignOut;


    FirebaseAuth mFirebaseAuth;


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       mUserLearnedDrawer =  Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNERD_DRAWER,"false"));

       if(savedInstanceState  != null){
           mFromSavedInstanceState = true;
       }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupFirebase();

        mCategoryTextView = (TextView)getActivity().findViewById(R.id.tv_category);
        mMedicineTextView = (TextView)getActivity().findViewById(R.id.tv_medicine);
        mPackageTextView = (TextView)getActivity().findViewById(R.id.tv_search_package);

        mSignOut = (Button)getActivity().findViewById(R.id.btn_sign_out);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });
        mPackageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToPackageActivity();
            }
        });
        mCategoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToCategory();
            }
        });

    }

    private void redirectToCategory(){
        Intent intent = new Intent(getActivity(),CategoryActivity.class);
        startActivity(intent);
    }

    private void redirectToPackageActivity(){
        Intent intent = new Intent(getActivity(),PackageActivity.class);
        startActivity(intent);
    }


    private void setupFirebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser() == null){
            getActivity().finish();
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout , android.support.v7.widget.Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);

        mDrawerLayout= drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,
                R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // if user havn't seen the drawer before
                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNERD_DRAWER,mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        // if the user has never seen the drawer
        if(!mUserLearnedDrawer&& !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }
        // tells us when the drawer open
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    // temporary saved data

    public static void saveToPreferences(Context context,String preferenceName,String preferenceValue){
        // mode private means that only our app can modify the preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
    //get data from preferences
    public static String readFromPreferences(Context context,String preferenceName,String defaultValue){
        // mode private means that only our app can modify the preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
       return sharedPreferences.getString(preferenceName,defaultValue);
    }
}
