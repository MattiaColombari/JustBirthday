package com.example.justbirthday;

import android.database.sqlite.SQLiteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.example.justbirthday.localDatabaseInteraction.DatabaseManager;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    ArrayList<SingleRowRecycleView> singleRowRecycleViewArrayList;
    DatabaseManager databaseManager;

    public RecyclerViewAdapter(ArrayList<SingleRowRecycleView> singleRowRecycleViewArrayList, DatabaseManager databaseManager) {
        this.singleRowRecycleViewArrayList = singleRowRecycleViewArrayList;
        this.databaseManager = databaseManager;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        SingleRowRecycleView singleRowRecycleView = singleRowRecycleViewArrayList.get(position);
        holder.getNikNameRVRTextView().setText(singleRowRecycleView.getNikName());
        holder.getNameRVRTextView().setText(singleRowRecycleView.getName());
        holder.getSurnameRVRTextView().setText(singleRowRecycleView.getSurname());

        Date currentDate = new Date();
        Date currentDateParsed = new Date(0, currentDate.getMonth(), currentDate.getDate());
        Date birthdayDateParsed = new Date(0, singleRowRecycleView.getBirthdayDate().getMonth(), singleRowRecycleView.getBirthdayDate().getDate());
        long millisDiff = currentDateParsed.getTime() - birthdayDateParsed.getTime();
        int dayDiff = (int) TimeUnit.DAYS.convert(millisDiff, TimeUnit.MILLISECONDS);
        holder.getDateRVRTextView().setText(Integer.toString(dayDiff));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(singleRowRecycleView.getBirthdayDate().getYear());
        stringBuilder.append("/");
        stringBuilder.append(singleRowRecycleView.getBirthdayDate().getMonth());
        stringBuilder.append("/");
        stringBuilder.append(singleRowRecycleView.getBirthdayDate().getDate());
        holder.getBirthdayRVRTextView().setText(stringBuilder.toString());

        holder.getCommentsRVRTextView().setText(singleRowRecycleView.getComments());

        boolean isExpanded = singleRowRecycleView.isExpanded();
        holder.getMoreDetailConstraintLayoutRVR().setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return singleRowRecycleViewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nikNameRVRTextView;
        private TextView dateRVRTextView;
        private TextView nameRVRTextView;
        private TextView surnameRVRTextView;
        private TextView birthdayRVRTextView;
        private TextView commentsRVRTextView;
        private ImageView iconRVRow;
        private Button changeInformationRVRButton;
        private Button deleteRVRButton;
        ConstraintLayout moreDetailConstraintLayoutRVR;
        ConstraintLayout mainLayoutRVRConstraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nikNameRVRTextView = (TextView) itemView.findViewById(R.id.NikNameRVRTextView);
            iconRVRow = (ImageView) itemView.findViewById(R.id.IconRVRImageView);
            dateRVRTextView = (TextView) itemView.findViewById(R.id.DateRVRTextView);
            nameRVRTextView = (TextView) itemView.findViewById(R.id.NameRVRTextView);
            surnameRVRTextView = (TextView) itemView.findViewById(R.id.SurnameRVRTextView);
            birthdayRVRTextView = (TextView) itemView.findViewById(R.id.BirthdayRVRTextView);
            commentsRVRTextView = (TextView) itemView.findViewById(R.id.CommentsRVRTextView);
            changeInformationRVRButton = (Button) itemView.findViewById(R.id.ChangeInformationRVRButton);
            deleteRVRButton = (Button) itemView.findViewById(R.id.DeleteRVRButton);
            moreDetailConstraintLayoutRVR = (ConstraintLayout) itemView.findViewById(R.id.MoreDetailConstraintLayoutRVR);
            mainLayoutRVRConstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.MainLayoutRVRConstraintLayout);

            mainLayoutRVRConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(singleRowRecycleViewArrayList.get(position).isExpanded() == false){
                        for(int i = 0; i < getItemCount(); i++){
                            SingleRowRecycleView singleRowRecycleView = singleRowRecycleViewArrayList.get(i);
                            singleRowRecycleView.setExpanded(i == position ? true : false);
                            notifyItemChanged(i);
                        }
                    }
                    else{
                        singleRowRecycleViewArrayList.get(position).setExpanded(false);
                        notifyItemChanged(position);
                    }
                }
            });

            deleteRVRButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = singleRowRecycleViewArrayList.get(getAdapterPosition()).getId();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                databaseManager.deleteFriendsById(id);
                            }
                            catch (SQLiteException e){
                                e.printStackTrace();
                                //Error.
                            }
                        }
                    }).start();

                    singleRowRecycleViewArrayList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }

        public TextView getNikNameRVRTextView() {
            return nikNameRVRTextView;
        }
        public TextView getDateRVRTextView() {
            return dateRVRTextView;
        }
        public TextView getNameRVRTextView() {
            return nameRVRTextView;
        }
        public TextView getSurnameRVRTextView() {
            return surnameRVRTextView;
        }
        public TextView getBirthdayRVRTextView() {
            return birthdayRVRTextView;
        }
        public TextView getCommentsRVRTextView() {
            return commentsRVRTextView;
        }
        public ImageView getIconRVRow() {
            return iconRVRow;
        }
        public Button getChangeInformationRVRButton() {
            return changeInformationRVRButton;
        }
        public Button getDeleteRVRButton() {
            return deleteRVRButton;
        }
        public ConstraintLayout getMoreDetailConstraintLayoutRVR() {
            return moreDetailConstraintLayoutRVR;
        }
        public ConstraintLayout getMainLayoutRVRConstraintLayout() {
            return mainLayoutRVRConstraintLayout;
        }
    }
}
