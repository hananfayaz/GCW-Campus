package com.example.gcwcampus.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gcwcampus.entities.AdminEntity;

import java.util.List;

@Dao
public interface AdminDao {
    @Query("select * from admin")
    List<AdminEntity> getAllData();

    @Query("select id from admin where ph_no = :phNo")
    int getAdminId(String phNo);

    @Query("select ph_no from admin where id = :id")
    String getAdminPhNo(int id);

    @Query("select password from admin where ph_no = :phNo")
    String getAdminPassword(String phNo);

    @Query("select email from admin where ph_no = :phNo")
    String getAdminEmail(String phNo);

    @Insert
    void addAdminInfo(AdminEntity adminEntity);

    @Query("UPDATE admin SET image_path = :imagePath WHERE id = :id")
    void updateImagePath(int id, String imagePath);

    @Query("SELECT image_path FROM admin WHERE id = :id")
    String getImagePath(int id);

    @Update
    void updateAdminInfo(AdminEntity adminEntity);

    @Delete
    void deleteAdminInfo(AdminEntity adminEntity);
}
