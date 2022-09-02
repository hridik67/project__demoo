package com.example.demoproject.Notification

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendNotification {
    companion object{
        @JvmStatic
        fun send(sendername:String,recievertoken:String,message:String,sender_uid:String) {
            Log.d("fuck","comed")
            PushNotification(
                NotificationData(sendername, message,sender_uid),
                recievertoken
            ).also {
                sendNotification(it)
            }
        }
        @JvmStatic
        private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("fuck","comed")
                val response = RetrofitInstance.api.postNotification(notification)
                if(response.isSuccessful) {
                    Log.d("fuck", "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e("fuck", response.errorBody().toString())
                }
            } catch(e: Exception) {
                Log.e("fuck", e.toString())
            }
        }
    }
    /*@JvmStatic
    fun send12(sendername:String,recievertoken:String,message:String) {
        Log.d("fuck","comed")
        PushNotification(
            NotificationData(sendername, message),
            recievertoken
        ).also {
            sendNotification(it)
        }
    }


    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("fuck", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("fuck", response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e("fuck", e.toString())
        }
    }
     */
}