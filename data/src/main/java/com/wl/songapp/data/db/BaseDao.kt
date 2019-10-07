package com.wl.songapp.data.db

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insert(entities: List<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun update(entities: List<T>)

    @Delete
    fun delete(entity: T)

    @Delete
    @JvmSuppressWildcards
    fun delete(entities: List<T>)

}