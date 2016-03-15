package com.figli.shopingassistance.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.figli.shopingassistance.R;
import com.figli.shopingassistance.fragment.TaskFragment;
import com.figli.shopingassistance.model.Item;
import com.figli.shopingassistance.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Figli on 12.02.2016.
 */
public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Item> items;

    TaskFragment taskFragment;

    protected ModelTask task;

    protected TaskViewHolder taskViewHolder;

    public TaskAdapter(TaskFragment taskFragment) {
        this.taskFragment = taskFragment;
        items = new ArrayList<>();
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    public void addItem(Item item) {
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(int location, Item item) {
        items.add(location, item);
        notifyItemInserted(location);
    }

    public void updateTask(ModelTask newTask) {
        for (int i = 0; i < getItemCount(); i++) {
            if(getItem(i).isTask()) {
                ModelTask modelTask = (ModelTask) getItem(i);
                if(newTask.getTimeStamp() == modelTask.getTimeStamp()) {
                    removeItem(i);
                    getTaskFragment().addTask(newTask, false);
                }
            }
        }
    }

    public void removeItem(int location) {

        if(location >= 0 && location <= getItemCount() -1) {
            items.remove(location);
            notifyItemRemoved(location);
        }
    }

    public void removeAllItems() {
        if(getItemCount() != 0) {
            items = new ArrayList<>();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected class TaskViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected TextView quantity;
        protected TextView date;
        protected CircleImageView priority;
        protected CardView cardView;
        protected TextView quuantityShort;
        protected ImageView cardMenu;

        public TaskViewHolder(View itemView, TextView title, TextView quantity, CircleImageView priority, CardView cardView, TextView quuantityShort, ImageView cardMenu) {
            super(itemView);
            this.title = title;
            this.priority = priority;
            this.quantity = quantity;
            this.cardView = cardView;
            this.quuantityShort = quuantityShort;
            this.cardMenu = cardMenu;
        }
    }

    public TaskFragment getTaskFragment() {
        return taskFragment;
    }
}
