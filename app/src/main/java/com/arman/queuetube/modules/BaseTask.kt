package com.arman.queuetube.modules

import android.os.AsyncTask
import com.arman.queuetube.listeners.OnTaskFinishedListener

typealias InBackground<Params, Result> = ((Array<out Params>) -> Result)

class BaseTask<Params, Result>() : AsyncTask<Params, Int, Result>() {

    var onTaskFinishedListener: OnTaskFinishedListener<Result>? = null
    private var doInBackground: InBackground<Params, Result>? = null

    constructor(doInBackground: InBackground<Params, Result>) : this() {
        this.doInBackground = doInBackground
    }

    override fun onProgressUpdate(vararg values: Int?) {
        val progress = values[0]
        super.onProgressUpdate(*values)
    }

    override fun onPostExecute(result: Result?) {
        super.onPostExecute(result)
        result?.let { this.onTaskFinishedListener?.onTaskFinished(result) }
    }

    override fun onCancelled(result: Result?) {
        super.onCancelled(result)
    }

    override fun onCancelled() {
        super.onCancelled()
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: Params): Result? {
        return doInBackground?.invoke(p0)
    }

}