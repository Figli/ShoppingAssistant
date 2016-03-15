package com.figli.shopingassistance.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.figli.shopingassistance.R;

import com.figli.shopingassistance.fragment.CurrentTaskFragment;
import com.figli.shopingassistance.model.Item;
import com.figli.shopingassistance.model.ModelTask;


import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Figli on 12.02.2016.
 */
public class CurrentTaskAdapter extends TaskAdapter {


    private final static int TASK_TYPE = 0;
    private final static int TASK_SEPARATOR = 1;

    private Context context;

    public CurrentTaskAdapter(CurrentTaskFragment taskFragment) {
        super(taskFragment);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TASK_TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_task, parent, false);
                context = parent.getContext();
                TextView title = (TextView) view.findViewById(R.id.textViewTaskTitle);
                TextView quantity = (TextView) view.findViewById(R.id.textViewTaskQuantity);
                CircleImageView priority = (CircleImageView) view.findViewById(R.id.cvTaskPriority);
                CardView cardView = (CardView) view.findViewById(R.id.card_view);
                TextView quantityShort = (TextView) view.findViewById(R.id.textViewTaskQuantityShort);
                ImageView cardMenu = (ImageView) view.findViewById(R.id.dotsVertical);
                return new TaskAdapter.TaskViewHolder(view, title, quantity, priority, cardView, quantityShort, cardMenu);
            case TASK_SEPARATOR:
                break;
            default:
                return null;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Item item = items.get(position);

        if (item.isTask()) {
            holder.itemView.setEnabled(true);
            final ModelTask task = (ModelTask) item;
            final TaskAdapter.TaskViewHolder taskViewHolder = (TaskAdapter.TaskViewHolder) holder;

            final View itemView = taskViewHolder.itemView;
            final Resources resources = itemView.getResources();

            taskViewHolder.title.setText(task.getTitle());
            taskViewHolder.quantity.setText(task.getQuantity());

            itemView.setVisibility(View.VISIBLE);
            taskViewHolder.priority.setEnabled(true);

            itemView.setBackgroundColor(resources.getColor(R.color.gray_50));

            taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_default_material_light));
            taskViewHolder.quantity.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
            taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
            taskViewHolder.priority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);


            final PopupMenu popupMenu = new PopupMenu(context, taskViewHolder.cardMenu);
            popupMenu.inflate(R.menu.menu_current_card);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.card_edit_product:
                            getTaskFragment().showTaskEditDialog(task);
                            break;
                        case R.id.card_delete_product:
                            getTaskFragment().removeTaskDialog(taskViewHolder.getLayoutPosition());
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });

            taskViewHolder.cardMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });

            taskViewHolder.priority.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskViewHolder.priority.setEnabled(false);
                    task.setStatus(ModelTask.STATUS_DONE);
                    getTaskFragment().mainActivity.dbHelper.update().status(task.getTimeStamp(), ModelTask.STATUS_DONE);

                    itemView.setBackgroundColor(resources.getColor(R.color.gray_200));

                    taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_disabled_material_light));
                    taskViewHolder.quantity.setTextColor(resources.getColor(R.color.secondary_text_disabled_material_light));
                    taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));

                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.priority, "rotationY", -180f, 0f);

                    flipIn.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (task.getStatus() == ModelTask.STATUS_DONE) {
                                taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp);

                                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView,
                                        "translationX", 0f, itemView.getWidth());

                                ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView,
                                        "translationX", itemView.getWidth(), 0f);

                                translationX.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        itemView.setVisibility(View.INVISIBLE);
                                        getTaskFragment().moveTask(task);
                                        removeItem(taskViewHolder.getLayoutPosition());
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });

                                AnimatorSet translationSet = new AnimatorSet();
                                translationSet.play(translationX).before(translationXBack);
                                translationSet.start();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    flipIn.start();
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isTask()) {
            return TASK_TYPE;
        } else {
            return TASK_SEPARATOR;
        }
    }
}
