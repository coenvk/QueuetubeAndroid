package com.arman.queuetube.modules

import com.arman.queuetube.listeners.OnTaskFinishedListener
import com.arman.queuetube.model.ItemList
import com.arman.queuetube.model.Video
import retrofit2.Call
import retrofit2.Response

typealias VideoCall = Call<ItemList<Video>>
typealias VideoCallback = Callback<ItemList<Video>>

class Callback<Result>() : retrofit2.Callback<Result> {

    var onTaskFinishedListener: OnTaskFinishedListener<Result>? = null

    override fun onFailure(call: Call<Result>, t: Throwable) {
        // TODO: show error
        t.printStackTrace()
    }

    override fun onResponse(call: Call<Result>, response: Response<Result>) {
        response.body()?.let { this.onTaskFinishedListener?.onTaskFinished(it) }
    }

}