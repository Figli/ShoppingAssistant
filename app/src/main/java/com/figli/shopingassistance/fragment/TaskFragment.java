package com.figli.shopingassistance.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.figli.shopingassistance.MainActivity;
import com.figli.shopingassistance.R;
import com.figli.shopingassistance.adapter.TaskAdapter;
import com.figli.shopingassistance.dialog.EditTaskDialogFragment;
import com.figli.shopingassistance.model.Item;
import com.figli.shopingassistance.model.ModelTask;

/**
 * Created by Figli on 12.02.2016.
 */
public abstract class TaskFragment extends Fragment {

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected TaskAdapter adapter;

    public MainActivity mainActivity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getActivity() != null) {
            mainActivity = (MainActivity) getActivity();
        }

        addTaskFromDB();
    }

    public void addTask(ModelTask newTask, boolean saveToDB) {
        int position = -1;

        for (int i = 0; i < adapter.getItemCount(); i ++) {
            if(adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if(newTask.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }

        if(position != -1) {
            adapter.addItem(position, newTask);
        } else {
            adapter.addItem(newTask);
        }

        if(saveToDB) {
            mainActivity.dbHelper.saveTask(newTask);
            mainActivity.dbHelper.saveTaskView(newTask);
        }
    }

    public void updateTask(ModelTask modelTask) {
        adapter.updateTask(modelTask);
    }

    public void removeTaskDialog(final int location) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_massage);

        Item item = adapter.getItem(location);

        if (item.isTask()) {
            ModelTask removingTask = (ModelTask) item;
            final long timeStamp = removingTask.getTimeStamp();
            final boolean[] isRemoved = {false};

            dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.removeItem(location);
                    isRemoved[0] = true;

                    if(isRemoved[0]) {
                        mainActivity.dbHelper.removeTaskView(timeStamp);
                    }

                    dialog.dismiss();
                }
            });

            dialogBuilder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        dialogBuilder.show();
    }

    public void showTaskEditDialog(ModelTask task) {
        DialogFragment editingTaskDialog = EditTaskDialogFragment.newInstance(task);
        editingTaskDialog.show(getActivity().getFragmentManager(), "EditTaskDialogFragment");
    }

    public abstract void checkAdapter();

    public abstract void findTask(String title);

    public abstract void addTaskFromDB();

    public abstract void moveTask(ModelTask task);
}
