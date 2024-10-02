package com.android.project.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.database.AppDatabaseHelper;
import com.android.project.model.Attendance;
import com.android.project.model.Labourer;
import com.android.project.utility.Constants;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ViewAttendanceListItemAdapter extends ArrayAdapter<Attendance> {
    private Activity context;
    List<Attendance> attendanceList;
    LayoutInflater inflater;

    public ViewAttendanceListItemAdapter(Activity context, int resourceId)
    {
        super(context, resourceId);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder
    {
        TextView dateTV;
        TextView presentTV;
        TextView jobTV;

        ImageView iconIV;
//        CheckBox presentCB;
    }

    public View getView(int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.viewattendancelist_item, null);
            holder.dateTV = view.findViewById(R.id.date);
            holder.presentTV = view.findViewById(R.id.present);
            holder.jobTV = view.findViewById(R.id.job);

            holder.iconIV = view.findViewById(R.id.profile);
            view.setTag(holder);
        } else
        {
            holder = (ViewHolder) view.getTag();
        }
        holder.presentTV.setText("PRESENT");
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(context);
        holder.presentTV.setText(databaseHelper.getJobWithID(attendanceList.get(position).getJobID()).getTitle());
        Labourer labourer = databaseHelper.getLabourerWithID(attendanceList.get(position).getLabourerID());

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String date = dateFormatter.format(attendanceList.get(position).getDate().getTime());

        holder.dateTV.setText(date);
        String imagePath = labourer.getProfilePath();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        if (null != bitmap) {
            holder.iconIV.setImageBitmap(bitmap);
        }
        else
        {
            int resID = context.getResources().getIdentifier("noimage", Constants.DRAWABLE_RESOURCE, context.getPackageName());
            holder.iconIV.setImageResource(resID);
        }
//        holder.presentCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    selectedStrings.add(tv.getText().toString());
//                }else{
//                    selectedStrings.remove(tv.getText().toString());
//                }
//
//            }
//        });


        return view;
    }

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList)
    {
        this.attendanceList = attendanceList;
    }

    @Override
    public int getCount() {
        return attendanceList.size();
    }

    @Override
    public Attendance getItem(int position) {
        return attendanceList.get(position);
    }

}


