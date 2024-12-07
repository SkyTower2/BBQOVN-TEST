package com.me.ml.webview.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import java.io.Reader
import java.lang.reflect.Type

object GsonUtils {
    private val gson: Gson = GsonBuilder().disableHtmlEscaping().create()

    @JvmStatic
    fun <T> fromJson(json: String?, c: Class<T>?): T {
        return gson.fromJson(json, c)
    }

    @JvmStatic
    fun <T> fromJson(json: JsonElement?, c: Class<T>?): T {
        return gson.fromJson(json, c)
    }

    @JvmStatic
    fun <T> fromJson(json: JsonElement?, type: Type?): T {
        return gson.fromJson(json, type)
    }

    @JvmStatic
    fun <T> fromJson(json: String?, type: Type?): T {
        return gson.fromJson(json, type)
    }

    @JvmStatic
    fun <T> fromJson(json: Reader?, type: Type?): T {
        return gson.fromJson(json, type)
    }

    @JvmStatic
    fun toJson(src: Any?): String {
        return gson.toJson(src)
    }
}