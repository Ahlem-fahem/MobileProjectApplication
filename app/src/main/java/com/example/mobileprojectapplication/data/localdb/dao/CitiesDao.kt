package com.example.mobileprojectapplication.data.localdb.dao

import androidx.room.*
import com.example.mobileprojectapplication.data.models.City

@Dao
interface CitiesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: City)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<City>)

    @Query("SELECT * FROM City")
    fun getAllCities(): List<City>

    @Query("SELECT * FROM City WHERE name = :name")
    fun getCityByName(name: String): City


    @Query("DELETE FROM City")
    fun clear()

    @Delete
    fun delete(entity: City)
}