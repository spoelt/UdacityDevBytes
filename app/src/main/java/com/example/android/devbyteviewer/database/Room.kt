/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

//define a @Dao interface called VideoDao
@Dao
interface VideoDao {
    // Add getVideos() Query function to VideoDao that returns a List of DatabaseVideo
    @Query("select * from databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>

    // Add an Insert function called insertAll() to your VideoDao that takes vararg DatabaseVideo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo)
}

// Create an abstract VideosDatabase class that extends RoomDatabase, and annotate it with @Database, including entities and version
@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase : RoomDatabase() {
    abstract val videoDao: VideoDao
}

// define an INSTANCE variable to store the singleton
private lateinit var INSTANCE: VideosDatabase

// Define a getDatabase() function to return the VideosDatabase
// Inside getDatabase(), use ::INSTANCE.isInitialized to check if
// the variable has been initialized. If it hasn't, then initialize it
fun getDatabase(context: Context): VideosDatabase {
    // Make sure your code is synchronized so it’s thread safe
    synchronized(VideosDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    VideosDatabase::class.java,
                    "videos").build()
        }
    }
    return INSTANCE
}
