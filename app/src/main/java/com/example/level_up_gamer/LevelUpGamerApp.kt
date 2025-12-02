
package com.example.level_up_gamer

import android.app.Application
import com.example.level_up_gamer.data.DatabaseProvider
import com.example.level_up_gamer.data.SessionManager

class LevelUpGamerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.init(this)
        SessionManager.init(this)
    }
}
