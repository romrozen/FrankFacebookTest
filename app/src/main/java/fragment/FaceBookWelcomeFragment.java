package fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.roman.frankfacebook.R;

/**
 * Created by Roman on 01-Apr-17.
 */

public class FaceBookWelcomeFragment extends Fragment implements View.OnClickListener{

    private static final String IS_USER_LOGGED_IN = "is_user_logged_in";

    private boolean is_user_logged_in;

    Communicator communicator;



    public interface Communicator {
        void friendsListPressed();
    }


    private CallbackManager mCallbackManager;

    Activity mActivityContext;

    public static FaceBookWelcomeFragment newInstance() {
        return new FaceBookWelcomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            is_user_logged_in = getArguments().getBoolean(IS_USER_LOGGED_IN);
        }

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
    }

    /*
    * onAttach(Context) is not called on pre API 23 versions of Android and onAttach(Activity) is deprecated
    * Use onAttachToContext instead
    */
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        mActivityContext = (Activity)context;
        communicator = (Communicator)context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);
        return  view;
    }


    private void initViews(View view) {
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(mActivityContext.getResources().getString(R.string.default_read_permissions));
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallBack);
        ImageView fragment_main_friends_list_btn = (ImageView) view.findViewById(R.id.fragment_main_friends_list_btn);
        fragment_main_friends_list_btn.setOnClickListener(this);

        if(is_user_logged_in){
            loginButton.setVisibility(View.GONE);
            fragment_main_friends_list_btn.setVisibility(View.GONE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        private ProfileTracker mProfileTracker;
        @Override
        public void onSuccess(LoginResult loginResult) {

            if(Profile.getCurrentProfile() == null) {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        // profile2 is the new profile
                        Log.v("facebook - profile", profile2.getFirstName());
                        mProfileTracker.stopTracking();
                    }
                };
                mProfileTracker.startTracking();
            } else {
                Profile profile = Profile.getCurrentProfile();
                Log.v("facebook - profile", profile.getFirstName());
            }

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_main_friends_list_btn:
                if(Profile.getCurrentProfile() != null) {
                    communicator.friendsListPressed();
                }else{
                    Toast.makeText(mActivityContext, R.string.login_message, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
