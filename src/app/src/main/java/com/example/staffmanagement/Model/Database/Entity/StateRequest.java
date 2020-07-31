package com.example.staffmanagement.Model.Database.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.staffmanagement.Model.Database.DAL.ConstString;

@Entity(tableName = ConstString.STATE_REQUEST_COL_NAME)
public class StateRequest {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConstString.STATE_REQUEST_COL_ID)
    private int id;

    @ColumnInfo(name = ConstString.STATE_REQUEST_COL_NAME)
    private String name;

    public StateRequest(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
