package com.example.msnotify.notify;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Recycle extends RecyclerView.Adapter<Recycle.ViewHolder> {

    private List<Info> list = new ArrayList<>() ;
    private Context context;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notice");

    public Recycle(Context context, List<Info> infoList){
        this.context = context;
        this.list = infoList;


    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nitem,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Info info= list.get(i);
        viewHolder.tvname.setText(info.gettName());
        viewHolder.tvbra.setText(info.getBranch());
        viewHolder.tvdate.setText(info.getDate());
        viewHolder.tvyear.setText(info.getYear());
        viewHolder.tvdes.setText(info.getNotice());

        final boolean isExpanded = i==mExpandedPosition;
        viewHolder.tvdes.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        viewHolder.itemView.setActivated(isExpanded);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = !isExpanded ? i : -1;
                notifyItemChanged(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvdate;
        TextView tvbra;
        TextView tvname;
        TextView tvdes;
        TextView tvyear;

        public ViewHolder(View v) {
            super(v);
            tvdate=v.findViewById(R.id.tvdate);
            tvbra = v.findViewById(R.id.tvbracnh);
            tvname = v.findViewById(R.id.tvname);
            tvdes = v.findViewById(R.id.tvmain);
            tvyear = v.findViewById(R.id.tvyear);

        }
    }


}

