package com.simca.attendance.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simca.attendance.ItemClickListener;
import com.simca.attendance.R;

public class AttenHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView rollNo;
    public ItemClickListener listener;

    public AttenHolder(@NonNull View itemView) {
        super(itemView);

        rollNo = itemView.findViewById(R.id.attended_roll);

    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {

        listener.onClick(view,getAdapterPosition(),false);

    }
}
