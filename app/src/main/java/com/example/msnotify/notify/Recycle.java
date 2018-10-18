package com.example.msnotify.notify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.SimpleOnItemTouchListener;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Recycle extends RecyclerView.Adapter<Recycle.ViewHolder> {

    private imgClickListenerInter imgClickListener;

    private List<Info> list = new ArrayList<>();
    private Context context;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notice");


    public Recycle(Context context, List<Info> infoList) {
        this.context = context;
        this.list = infoList;
//        this.imgClickListener = ClickListener;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nitem, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Info info = list.get(i);
        viewHolder.tvname.setText(info.gettName());
        viewHolder.tvbra.setText(info.getBranch());
        viewHolder.tvdate.setText(info.getDate());
        viewHolder.tvyear.setText(info.getYear());
        viewHolder.tvdes.setText(info.getNotice());
        //Glide.with(context).load(info.getUrl()).into(viewHolder.itemIimage);
//        final int a= viewHolder.getAdapterPosition();
//        final boolean isExpanded = a==mExpandedPosition;
//        viewHolder.itemIimage.setVisibility(isExpanded?View.VISIBLE:View.GONE);
//        viewHolder.tvdes.setVisibility(isExpanded?View.GONE:View.VISIBLE);
//        viewHolder.itemView.setActivated(isExpanded);
//
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mExpandedPosition = !isExpanded ? a : -1;
//                notifyItemChanged(a);
//
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvdate;
        TextView tvbra;
        TextView tvname;
        TextView tvdes;
        TextView tvyear;
        ImageView itemIimage;

        public ViewHolder(View v) {
            super(v);
            tvdate = v.findViewById(R.id.tvdate);
            tvbra = v.findViewById(R.id.tvbracnh);
            tvname = v.findViewById(R.id.tvname);
            tvdes = v.findViewById(R.id.tvmain);
            tvyear = v.findViewById(R.id.tvyear);
            itemIimage = v.findViewById(R.id.itemimg);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int cllcpos = getAdapterPosition();
            imgClickListener.onNoticeClicked(cllcpos);
        }

    }

    public interface imgClickListenerInter {
        void onNoticeClicked(int noticePos);
    }


}


