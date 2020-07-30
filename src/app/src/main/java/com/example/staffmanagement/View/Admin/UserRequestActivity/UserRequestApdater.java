package com.example.staffmanagement.View.Admin.UserRequestActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Presenter.Admin.UserRequestPresenter;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.DetailRequestUser.DetailRequestUserActivity;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;

import java.util.ArrayList;

public class UserRequestApdater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<Request> requestArrayList;
    private UserRequestPresenter mPresenter;
    private ArrayList<String> arrayListRequestState;
    private ArrayList<StateRequest> stateRequestArrayList;
    private ArrayAdapter adapter;
    private final int ITEM_VIEW_TYPE = 1;
    private final int LOADING_VIEW_TYPE = 2;

    public UserRequestApdater(Context mContext, ArrayList<Request> requestArrayList, UserRequestPresenter mPresenter) {
        this.mContext = mContext;
        this.requestArrayList = requestArrayList;
        this.mPresenter = mPresenter;
    }

    @Override
    public int getItemViewType(int position) {
        return requestArrayList.get(position) == null ? LOADING_VIEW_TYPE : ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == ITEM_VIEW_TYPE){
            view = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_user_request,parent,false);
            return new ViewHolder(view);
        }else{
            view =((Activity)mContext).getLayoutInflater().inflate(R.layout.view_load_more,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof LoadingViewHolder){
            return;
        }

        final ViewHolder viewHolder= (ViewHolder) holder;
        final String fullName = mPresenter.getFullNameById(requestArrayList.get(position).getIdUser());
        viewHolder.setTxtName(fullName);
        viewHolder.setTxtTitle(mPresenter.getTitleById(requestArrayList.get(position).getId()));

        viewHolder.setTxtDateTime(GeneralFunc.convertMilliSecToDateString(mPresenter.getDateTimeById(requestArrayList.get(position).getId())));
        addRequestState();
        adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, arrayListRequestState) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else return true;
            }

            @Override
            public int getItemViewType(int position) {

                return super.getItemViewType(position);
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.parseColor("#bcbcbb"));
                }
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                if(textView.getText().equals("Waiting")){
                    textView.setTextColor(mContext.getResources().getColor(R.color.colorWaiting));
                }else{
                    if (textView.getText().equals("Accept")){
                        textView.setTextColor(mContext.getResources().getColor(R.color.colorAccept));
                    }else {
                        textView.setTextColor(mContext.getResources().getColor(R.color.colorDecline));
                    }
                }
                return view;
            }
        };
        viewHolder.getSpnRequestState().setAdapter(adapter);

        final int idState = mPresenter.getIdStateById(requestArrayList.get(position).getId());
        viewHolder.getSpnRequestState().setSelection(getIdStateById(idState));
        viewHolder.getSpnRequestState().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameState = viewHolder.spnRequestState.getSelectedItem().toString();
                requestArrayList.get(position).setIdState(getIdStateByName(nameState));
                mPresenter.update(requestArrayList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailRequestUserActivity.class);
                intent.putExtra(Constant.REQUEST_DATA_INTENT, requestArrayList.get(position));
                intent.putExtra(Constant.STATE_NAME_INTENT, String.valueOf(viewHolder.getSpnRequestState().getSelectedItem()));
                intent.putExtra(Constant.FULL_NAME,fullName);
                mContext.startActivity(intent);
            }
        });
    }

    private int getIdStateById(int idRequest) {
        for (int i = 0; i < stateRequestArrayList.size(); i++) {
            if (stateRequestArrayList.get(i).getId() == (idRequest))
                return i;
        }
        return -1;
    }

    private int getIdStateByName(String name) {
        for (int i = 0; i < stateRequestArrayList.size(); i++) {
            if (stateRequestArrayList.get(i).getName().equals(name))
                return stateRequestArrayList.get(i).getId();
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtTitle, txtDateTime;
        private Spinner spnRequestState;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtName = itemView.findViewById(R.id.textViewStaffName);
            txtTitle = itemView.findViewById(R.id.textViewRequestName);
            txtDateTime = itemView.findViewById(R.id.textViewRequestDateTime);
            spnRequestState = itemView.findViewById(R.id.spinnerRequestState);
        }

        public TextView getTxtName() {
            return txtName;
        }

        public void setTxtName(String txtName) {
            this.txtName.setText(txtName);
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public void setTxtTitle(String txtTitle) {
            this.txtTitle.setText(txtTitle);
        }

        public TextView getTxtDateTime() {
            return txtDateTime;
        }

        public void setTxtDateTime(String txtDateTime) {
            this.txtDateTime.setText(txtDateTime);
        }

        public void setSpnRequestState(Spinner spnRequestState) {
            this.spnRequestState = spnRequestState;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public Spinner getSpnRequestState() {
            return spnRequestState;
        }

    }

    private void addRequestState() {
        // StateRequestDbHandler db = new StateRequestDbHandler(mContext);
        arrayListRequestState = new ArrayList<>();
        stateRequestArrayList = new ArrayList<>();
        stateRequestArrayList.addAll(mPresenter.getAllStateRequest());
        for (int i = 0; i < stateRequestArrayList.size(); i++) {
            arrayListRequestState.add(stateRequestArrayList.get(i).getName());
        }
    }


}