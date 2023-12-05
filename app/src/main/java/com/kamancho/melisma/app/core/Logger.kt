package com.kamancho.melisma.app.core

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by Ilya Boiko @camancho
on 03.12.2023.
 **/
object Logger {

  fun logFragment(tag:String,context: Context){
     val bundle = Bundle()
     bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME,tag)

     FirebaseAnalytics.getInstance(context)
      .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
  }
}