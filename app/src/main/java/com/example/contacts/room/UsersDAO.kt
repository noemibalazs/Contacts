package com.example.contacts.room

import androidx.room.*
import com.example.contacts.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface UsersDAO {

    @Query("SELECT * FROM users")
    fun observeUsers(): Observable<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUsers(users: List<User>): Completable

    @Query("DELETE FROM users")
    fun clearUsers(): Completable
}