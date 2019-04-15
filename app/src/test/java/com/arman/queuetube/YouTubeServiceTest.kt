package com.arman.queuetube

import com.arman.queuetube.model.ItemList
import com.arman.queuetube.model.Video
import com.arman.queuetube.modules.search.YouTubeService
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class YouTubeServiceTest {

    private lateinit var service: YouTubeService

    @Before
    fun setup() {
        this.service = YouTubeService.get()
    }

    @Test
    fun testSearch() {
        val latch = CountDownLatch(1)
        var valid = false
        this.service.search("Lil Xan").enqueue(object : Callback<ItemList<Video>> {
            override fun onFailure(call: Call<ItemList<Video>>, t: Throwable) {
                t.printStackTrace()
                latch.countDown()
            }

            override fun onResponse(call: Call<ItemList<Video>>, response: Response<ItemList<Video>>) {
                val items = response.body()!!.items
                println(items.size)
                for (i in 0 until items.size) {
                    println(items[i])
                }
                valid = true
                latch.countDown()
            }
        })
        latch.await()
        assert(valid)
    }

}
