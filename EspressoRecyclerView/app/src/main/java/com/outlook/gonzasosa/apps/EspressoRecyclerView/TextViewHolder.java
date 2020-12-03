package com.outlook.gonzasosa.apps.EspressoRecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TextViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    TextViewHolder (View itemView) {
        super (itemView);
        textView = (TextView) itemView;
    }
}