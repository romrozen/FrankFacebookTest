package fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roman.frankfacebook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.FriendsListAdapter;
import model.FriendModel;


public class FriendsListFragment extends Fragment {

    private static final String FRIENDS_JSON_ARRAY = "friends_json_array";

    ArrayList<FriendModel> friendModelArrayList;

    private String friends_json_array_string;

    public FriendsListFragment() {
        // Required empty public constructor
    }

    public static FriendsListFragment newInstance(String friends_json_array_string) {
        FriendsListFragment fragment = new FriendsListFragment();
        Bundle args = new Bundle();
        args.putString(FRIENDS_JSON_ARRAY, friends_json_array_string);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            friends_json_array_string = getArguments().getString(FRIENDS_JSON_ARRAY);
            populateFriendsArrayList(friends_json_array_string);
        }
    }

    private void populateFriendsArrayList(String friends_json_array_string) {
        try {
            JSONArray friends_json_array = new JSONArray(friends_json_array_string);
            friendModelArrayList = new ArrayList<>();
            for (int i = 0; i < friends_json_array.length(); i++) {
                String name = "";
                String profile_pic = "";

                JSONObject friendJsonObject = (JSONObject) friends_json_array.get(i);

                if(friendJsonObject.has(getString(R.string.name))){
                    name = friendJsonObject.getString(getString(R.string.name));
                }
                if(friendJsonObject.has(getString(R.string.picture))&&friendJsonObject.getJSONObject(getString(R.string.picture)).has(getString(R.string.data))&&friendJsonObject.getJSONObject(getString(R.string.picture)).getJSONObject(getString(R.string.data)).has(getString(R.string.url))){
                    profile_pic = friendJsonObject.getJSONObject(getString(R.string.picture)).getJSONObject(getString(R.string.data)).getString(getString(R.string.url));
                }

                friendModelArrayList.add(new FriendModel(name,profile_pic));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new FriendsListAdapter(friendModelArrayList,getActivity()));
        }
        return view;

    }

}
