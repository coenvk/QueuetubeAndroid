package com.arman.queuetube.model

import com.google.gson.annotations.SerializedName

class ItemList<T>(@SerializedName("items") val items: List<T>)