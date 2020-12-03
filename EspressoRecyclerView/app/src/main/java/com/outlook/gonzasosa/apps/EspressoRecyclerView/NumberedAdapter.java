package com.outlook.gonzasosa.apps.EspressoRecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NumberedAdapter extends RecyclerView.Adapter<TextViewHolder> {
    private final OnItemClickListener listener;
    private List<String> labels;

    public interface OnItemClickListener {
        void onItemClick (int position);
    }

    NumberedAdapter (int count, OnItemClickListener listener) {
        labels = new ArrayList<>(count);

        for (int i = 0; i < count; ++i) {
            labels.add (String.valueOf (i));
        }

        this.listener = listener;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (android.R.layout.simple_list_item_1, parent, false);
        return new TextViewHolder (view);
    }

    @Override
    public void onBindViewHolder (@NonNull final TextViewHolder holder, final int position) {
        final String label = labels.get (position);
        holder.textView.setText (label);

        TypedValue outValue = new TypedValue ();
        holder.textView.getContext ().getTheme ().resolveAttribute (android.R.attr.selectableItemBackground, outValue, true);
        holder.textView.setBackgroundResource (outValue.resourceId);

        if (listener != null) {
            holder.textView.setOnClickListener (v -> listener.onItemClick (position));
        }
    }

    @Override
    public int getItemCount () {
        return labels.size ();
    }
}