package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.roman.frankfacebook.R;

import java.util.ArrayList;
import java.util.Locale;

import model.FriendModel;
import network.MySingleton;

/**
 * Created by Roman on 02-Apr-17.
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private final ArrayList<FriendModel> mValues;
    private Context mContext;

    public FriendsListAdapter(ArrayList<FriendModel> items,Context context) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.friend_name.setText(String.format(Locale.getDefault(),"%s",mValues.get(position).getName()));

        ImageLoader mImageLoader = MySingleton.getInstance(mContext).getImageLoader();


        mImageLoader.get(mValues.get(position).getUrl_image(), ImageLoader.getImageListener(holder.friend_profile_pic,
                R.mipmap.ic_launcher, android.R.drawable
                        .ic_dialog_alert));

        holder.friend_profile_pic.setImageUrl(mValues.get(position).getUrl_image(), mImageLoader);

        holder.mView.setOnClickListener(v -> Toast.makeText(mContext, mValues.get(position).getName(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView friend_name;
        final NetworkImageView friend_profile_pic;
        FriendModel mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            friend_name = (TextView) view.findViewById(R.id.friend_name_tv);
            friend_profile_pic = (NetworkImageView) view.findViewById(R.id.friend_profile_pic_img);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + friend_name.getText() + "'";
        }
    }
}
