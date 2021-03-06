package com.example.staffmanagement.Model.Repository.UserState;

import androidx.lifecycle.MutableLiveData;

import com.example.staffmanagement.Model.Entity.UserState;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.NetworkBoundResource;
import com.example.staffmanagement.Model.FirebaseDb.UserState.UserStateService;
import com.example.staffmanagement.Model.Repository.AppDatabase;

import java.util.List;

public class UserStateRepository {
    private UserStateService service;
    private MutableLiveData<List<UserState>> mLiveData;

    public UserStateRepository() {
        service = new UserStateService();
        mLiveData = new MutableLiveData<>();
    }

    public List<UserState> getAll() {
        return null;
    }

    public void getAllService() {
        new NetworkBoundResource<List<UserState>, List<UserState>>() {
            @Override
            protected List<UserState> loadFromDb() {
                return AppDatabase.getDb().userStateDAO().getAll();
            }

            @Override
            protected boolean shouldFetchData(List<UserState> data) {
                return true; //data == null || data.size() == 0;
            }

            @Override
            protected void createCall(ApiResponse apiResponse) {
                service.getAll(apiResponse);
            }

            @Override
            protected void saveCallResult(List<UserState> data) {
//                int count = AppDatabase.getDb().userStateDAO().count();
//                if(count != data.size()){
                AppDatabase.getDb().userStateDAO().deleteAll();
                AppDatabase.getDb().userStateDAO().insertRange(data);
                //}
            }

            @Override
            protected void onFetchFail(String message) {
            }

            @Override
            protected void onFetchSuccess(List<UserState> data) {
                mLiveData.postValue(data);
            }
        }.run();
    }
}

