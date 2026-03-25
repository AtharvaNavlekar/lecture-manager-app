package com.example.lecturemanager.di

import android.content.Context
import androidx.room.Room
import com.example.lecturemanager.data.local.dao.TimetableDao
import com.example.lecturemanager.data.local.dao.LeaveDao
import com.example.lecturemanager.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "lecman_db"
        ).build()
    }

    @Provides
    fun provideTimetableDao(database: AppDatabase): TimetableDao {
        return database.timetableDao()
    }

    @Provides
    fun provideLeaveDao(database: AppDatabase): LeaveDao {
        return database.leaveDao()
    }
}