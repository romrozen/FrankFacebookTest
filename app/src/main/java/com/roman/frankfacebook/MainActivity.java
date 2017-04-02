package com.roman.frankfacebook;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import fragment.FaceBookWelcomeFragment;
import fragment.FriendsListFragment;

public class MainActivity extends AppCompatActivity implements FaceBookWelcomeFragment.Communicator{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFaceBookWelcomeFragment();
    }

    private void addFaceBookWelcomeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FaceBookWelcomeFragment myFaceBookFragment = FaceBookWelcomeFragment.newInstance();
        fm.beginTransaction().add(R.id.main_activity_fragment_holder, myFaceBookFragment, "faceBookWelcomeFragment").commit();
    }

    private void presentFriendsList(String friends_json_array) {
        FragmentManager fm = getSupportFragmentManager();
        FriendsListFragment myFaceBookFragment = FriendsListFragment.newInstance(friends_json_array);
        fm.beginTransaction().replace(R.id.main_activity_fragment_holder, myFaceBookFragment, "FriendsListFragment").addToBackStack(null).commit();
    }

    @Override
    public void friendsListPressed() {
        getUserFriendsList();
    }

    private void getUserFriendsList() {
        Bundle params = new Bundle();
        params.putString(getString(R.string.fields), getString(R.string.taggable_friends));// FaceBook no longer gives the option to get the complete list of friends, taggable_friends available


        GraphRequest.Callback callback = (response) ->  {
            if (response != null) {
                try {
                    JSONObject data = response.getJSONObject();
                    if (data.has(getString(R.string.taggable_friends))) {

                        JSONArray friendsList = data.getJSONObject(getString(R.string.taggable_friends)).getJSONArray(getString(R.string.data));

                        presentFriendsList(friendsList.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new GraphRequest(AccessToken.getCurrentAccessToken(), getString(R.string.me), params, HttpMethod.GET,
              callback).executeAsync();
    }

}
