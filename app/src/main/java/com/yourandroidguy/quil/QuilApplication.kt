package com.yourandroidguy.quil

import android.app.Application
import com.yourandroidguy.quil.database.QuilDatabase

class QuilApplication: Application() {
    val database: QuilDatabase by lazy {
        QuilDatabase.getInstance(this)
    }
}