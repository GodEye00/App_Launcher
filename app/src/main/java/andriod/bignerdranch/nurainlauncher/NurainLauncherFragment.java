package andriod.bignerdranch.nurainlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NurainLauncherFragment extends Fragment {
    private static final String TAG = "NurainLauncherFragment";
    private RecyclerView mRecyclerView;

    public static NurainLauncherFragment newInstance() {
        return new NurainLauncherFragment();
    }




    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle
                             savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nurain_launcher,
                container, false);
        mRecyclerView = (RecyclerView)
                v.findViewById(R.id.app_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();
        return v;
    }















    private void setupAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);

        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm
                .queryIntentActivities(startupIntent, 0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity().getPackageManager();
                return
                        String.CASE_INSENSITIVE_ORDER.compare(
                                a.loadLabel(pm).toString(),
                                b.loadLabel(pm).toString()
                        );
            }
        });

        Log.i(TAG, "Found " + activities.size() + " activities.");

        mRecyclerView.setAdapter(new ActivityAdapter(activities));

    }



    private class ActivityHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;
        private ImageView mIconView;

        public ActivityHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_text_view);
            mIconView = (ImageView) itemView.findViewById(R.id.list_item_icon_view);
            mNameTextView.setOnClickListener(this);
            mIconView.setOnClickListener(this);

        }

        public void bindActivity(ResolveInfo resolveInfo) {
            mResolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();
            Drawable icon = mResolveInfo.loadIcon(pm);
            mNameTextView.setText(appName);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mIconView.setBackground(icon);
            } else {
                mIconView.setBackgroundDrawable(icon);
            }

        }

        @Override
        public void onClick(View view) {
            ActivityInfo activityInfo = mResolveInfo.activityInfo;

            Intent intent = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }


    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities) {
            mActivities = activities;
        }


        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.launcher_list_view,
                    parent, false);

            return new ActivityHolder(view);
        }


        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }




}