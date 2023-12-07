package com.kamancho.melisma.notifications.presentation

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.kamancho.melisma.R
import com.kamancho.melisma.databinding.NotificationItemBinding
import com.kamancho.melisma.main.presentation.MainActivity

/**
 * Created by HP on 21.08.2023.
 **/
interface NotificationUi{

    fun apply(binding: NotificationItemBinding, context: Context)

    fun map(): String

    abstract class Abstract(
        private val id:String,
        private val strokeWidth: Int,
        private val strokeColor: Int,
        private val btnVisibility: Int,
        private val dateVisibility: Int,
        private val mainText: String,
        private val titleText: String,
    ): NotificationUi{

        override fun apply(binding: NotificationItemBinding, context: Context) = with(binding){
            cardBg.strokeColor = strokeColor
            cardBg.strokeWidth = strokeWidth
            notificationBtn.visibility = btnVisibility
            dateTv.visibility = dateVisibility
            titleTv.text = titleText
            mainTextTv.setText(mainText, TextView.BufferType.SPANNABLE)
        }

        override fun map(): String = id
    }

    data class DefaultNotification(
        private val id: String,
        private val title: String,
        private val mainText: String,
        private val date: String
    ): Abstract(
        strokeWidth = 0,
        strokeColor = R.color.orange,
        btnVisibility = View.GONE,
        dateVisibility = View.VISIBLE,
        mainText = mainText,
        titleText = title,
        id = id
    ){

        override fun apply(binding: NotificationItemBinding, context: Context) {
            super.apply(binding,context)
            binding.dateTv.text = date


        }
    }

    open class PrimaryNotification(
        private val id: String,
        private val title: String,
        private val mainText: String,
        private val btnText: String,
        private val stokeColor: Int,
        private val strokeWidth: Int,
        private val colorStateList: ColorStateList,
        private val intent: Intent?,
    ): Abstract(
        strokeWidth = strokeWidth,
        strokeColor = stokeColor,
        btnVisibility = View.VISIBLE,
        dateVisibility = View.GONE,
        mainText = mainText,
        titleText = title,
        id = id
    ){

        override fun apply(binding: NotificationItemBinding, context: Context) {
            super.apply(binding,context)
            binding.notificationBtn.text = btnText
            binding.notificationImg.imageTintList = colorStateList
            binding.notificationBtn.backgroundTintList = colorStateList

            binding.notificationBtn.setOnClickListener {
                context.startActivity(intent)
            }
        }
    }

    data class ReviewNotification(
        private val id: String,
        private val title: String,
        private val mainText: String,
        private val btnText: String,
        private val stokeColor: Int,
        private val strokeWidth: Int,
        private val colorStateList: ColorStateList,
    ): PrimaryNotification(
        id = id,
        title = title,
        mainText = mainText,
        btnText = btnText,
        stokeColor = stokeColor,
        strokeWidth = strokeWidth,
        colorStateList = colorStateList,
        intent = null
    ){

        override fun apply(binding: NotificationItemBinding, context: Context) {
            super.apply(binding, context)
            val manager = ReviewManagerFactory.create(context)

            binding.notificationBtn.setOnClickListener {
                Log.d("reviewErrorCode", "task: click")
                manager.requestReviewFlow().addOnCompleteListener { task ->
                    Log.d("reviewErrorCode", "task: ${task.isSuccessful}")
                    if (task.isSuccessful) {
                        manager.launchReviewFlow(context as MainActivity, task.result)
                            .addOnFailureListener {
                                Log.d("reviewErrorCode", "fail $it")
                            }.addOnSuccessListener {
                                Log.d("reviewErrorCode", "success: ")
                            }
                    } else {
                        // There was some problem, log or handle the error code.
                        val reviewErrorCode = (task.exception as ReviewException).message
                        Log.d("reviewErrorCode", "error: $reviewErrorCode")
                    }
                }
            }
        }

    }

}