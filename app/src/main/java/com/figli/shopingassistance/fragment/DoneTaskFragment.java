package com.figli.shopingassistance.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.figli.shopingassistance.R;
import com.figli.shopingassistance.adapter.DoneTaskAdapter;
import com.figli.shopingassistance.database.DBHelper;
import com.figli.shopingassistance.model.ModelTask;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoneTaskFragment extends TaskFragment {

    public DoneTaskFragment() {
        // Required empty public constructor
    }

    OnTaskRestoreListener onTaskRestoreListener;

    public interface OnTaskRestoreListener {
        void onTaskRestore(ModelTask task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onTaskRestoreListener = (OnTaskRestoreListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnTaskRestoreListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_done_task, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDoneTask);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DoneTaskAdapter(this);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    @Override
    public void checkAdapter() {
        if(adapter == null) {
            adapter = new DoneTaskAdapter(this);
            addTaskFromDB();
        }
    }

    @Override
    public void findTask(String title) {
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(mainActivity.dbHelper.query().getTasksView(DBHelper.SELECTION_LIKE_TITLE_VIEW + " AND "
                        + DBHelper.SELECTION_STATUS_VIEW,
                new String[]{"%" + title + "%",
                        Integer.toString(ModelTask.STATUS_DONE)}, DBHelper.PRODUCT_QUANTITY_VIEW));

        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void addTaskFromDB() {
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(mainActivity.dbHelper.query().getTasksView(DBHelper.SELECTION_STATUS_VIEW,
                new String[]{Integer.toString(ModelTask.STATUS_DONE)}, DBHelper.PRODUCT_QUANTITY_VIEW));

        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void moveTask(ModelTask task) {
        onTaskRestoreListener.onTaskRestore(task);
    }
}
