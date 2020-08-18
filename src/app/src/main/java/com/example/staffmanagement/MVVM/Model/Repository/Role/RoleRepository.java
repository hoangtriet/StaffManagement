package com.example.staffmanagement.MVVM.Model.Repository.Role;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Repository.AppDatabase;
import com.example.staffmanagement.MVVM.Model.Repository.Base.ApiResponse;
import com.example.staffmanagement.MVVM.Model.Repository.Base.NetworkBoundResource;
import com.example.staffmanagement.Model.LocalDb.BUS.RoleBUS;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.core.Completable;

public class RoleRepository {
    private RoleService roleService;
    private RoleBUS roleBUS;

    public RoleRepository() {
        roleBUS = new RoleBUS();
        roleService = new RoleService();
    }

    public String getRoleNameById(int idRole) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            Role role = AppDatabase.getDb().roleDAO().getById(idRole);
            return role.getName();
        });
        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}