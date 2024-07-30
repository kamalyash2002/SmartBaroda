package com.example.infuxion_alternate.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import java.lang.NullPointerException
import java.lang.NumberFormatException
import java.util.*

class TinyDB(appContext: Context) {
    private val context: Context
    private val preferences: SharedPreferences = appContext.getSharedPreferences("tempPref", Context.MODE_PRIVATE)

    /**
     * Get int value from SharedPreferences at 'key'. If key not found, return 0
     * @param key SharedPreferences key
     * @return int value at 'key' or 0 if key not found
     */
    fun getInt(key: String?): Int {
        return preferences.getInt(key, 0)
    }

    /**
     * Get parsed ArrayList of Integers from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of Integers
     */
    fun getListInt(key: String?): ArrayList<Int> {
        val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
        val arrayToList = ArrayList(Arrays.asList(*myList))
        val newList = ArrayList<Int>()
        for (item in arrayToList) newList.add(item.toInt())
        return newList
    }

    /**
     * Get long value from SharedPreferences at 'key'. If key not found, return 0
     * @param key SharedPreferences key
     * @return long value at 'key' or 0 if key not found
     */
    fun getLong(key: String?): Long {
        return preferences.getLong(key, 0)
    }

    /**
     * Get float value from SharedPreferences at 'key'. If key not found, return 0
     * @param key SharedPreferences key
     * @return float value at 'key' or 0 if key not found
     */
    fun getFloat(key: String?): Float {
        return preferences.getFloat(key, 0f)
    }

    /**
     * Get double value from SharedPreferences at 'key'. If exception thrown, return 0
     * @param key SharedPreferences key
     * @return double value at 'key' or 0 if exception is thrown
     */
    fun getDouble(key: String?): Double {
        val number = getString(key)
        return try {
            number!!.toDouble()
        } catch (e: NumberFormatException) {
            0.toDouble()
        }
    }

    /**
     * Get parsed ArrayList of Double from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of Double
     */
    fun getListDouble(key: String?): ArrayList<Double> {
        val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
        val arrayToList = ArrayList(Arrays.asList(*myList))
        val newList = ArrayList<Double>()
        for (item in arrayToList) newList.add(item.toDouble())
        return newList
    }

    /**
     * Get parsed ArrayList of Integers from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of Longs
     */
    fun getListLong(key: String?): ArrayList<Long> {
        val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
        val arrayToList = ArrayList(Arrays.asList(*myList))
        val newList = ArrayList<Long>()
        for (item in arrayToList) newList.add(item.toLong())
        return newList
    }

    /**
     * Get String value from SharedPreferences at 'key'. If key not found, return ""
     * @param key SharedPreferences key
     * @return String value at 'key' or "" (empty String) if key not found
     */
    fun getString(key: String?): String? {
        return preferences.getString(key, "")
    }

    /**
     * Get parsed ArrayList of String from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of String
     */
    fun getListString(key: String?): ArrayList<String> {
        return ArrayList(Arrays.asList(*TextUtils.split(preferences.getString(key, ""), "‚‗‚")))
    }

    /**
     * Get boolean value from SharedPreferences at 'key'. If key not found, return false
     * @param key SharedPreferences key
     * @return boolean value at 'key' or false if key not found
     */
    fun getBoolean(key: String?): Boolean {
        return preferences.getBoolean(key, false)
    }

    /**
     * Get parsed ArrayList of Boolean from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of Boolean
     */
    fun getListBoolean(key: String?): ArrayList<Boolean> {
        val myList = getListString(key)
        val newList = ArrayList<Boolean>()
        for (item in myList) {
            if (item == "true") {
                newList.add(true)
            } else {
                newList.add(false)
            }
        }
        return newList
    }

    /**
     * Put int value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value int value to be added
     */
    fun putInt(key: String?, value: Int) {
        checkForNullKey(key)
        preferences.edit().putInt(key, value).apply()
    }

    /**
     * Put ArrayList of Integer into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param intList ArrayList of Integer to be added
     */
    fun putListInt(key: String?, intList: ArrayList<Int>) {
        checkForNullKey(key)
        val myIntList = intList.toTypedArray()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply()
    }

    /**
     * Put long value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value long value to be added
     */
    fun putLong(key: String?, value: Long) {
        checkForNullKey(key)
        preferences.edit().putLong(key, value).apply()
    }

    /**
     * Put ArrayList of Long into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param longList ArrayList of Long to be added
     */
    fun putListLong(key: String?, longList: ArrayList<Long>) {
        checkForNullKey(key)
        val myLongList = longList.toTypedArray()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myLongList)).apply()
    }

    /**
     * Put float value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value float value to be added
     */
    fun putFloat(key: String?, value: Float) {
        checkForNullKey(key)
        preferences.edit().putFloat(key, value).apply()
    }

    /**
     * Put double value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value double value to be added
     */
    fun putDouble(key: String?, value: Double) {
        checkForNullKey(key)
        putString(key, value.toString())
    }

    /**
     * Put ArrayList of Double into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param doubleList ArrayList of Double to be added
     */
    fun putListDouble(key: String?, doubleList: ArrayList<Double>) {
        checkForNullKey(key)
        val myDoubleList = doubleList.toTypedArray()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myDoubleList)).apply()
    }

    /**
     * Put String value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value String value to be added
     */
    fun putString(key: String?, value: String?) {
        checkForNullKey(key)
        checkForNullValue(value)
        preferences.edit().putString(key, value).apply()
    }

    /**
     * Put ArrayList of String into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param stringList ArrayList of String to be added
     */
    fun putListString(key: String?, stringList: ArrayList<String>) {
        checkForNullKey(key)
        val myStringList = stringList.toTypedArray()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply()
    }

    /**
     * Put boolean value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value boolean value to be added
     */
    fun putBoolean(key: String?, value: Boolean) {
        checkForNullKey(key)
        preferences.edit().putBoolean(key, value).apply()
    }

    /**
     * Put ArrayList of Boolean into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param boolList ArrayList of Boolean to be added
     */
    fun putListBoolean(key: String?, boolList: ArrayList<Boolean>) {
        checkForNullKey(key)
        val newList = ArrayList<String>()
        for (item in boolList) {
            if (item) {
                newList.add("true")
            } else {
                newList.add("false")
            }
        }
        putListString(key, newList)
    }
    /**
     * Remove SharedPreferences item with 'key'
     * @param key SharedPreferences key
     */
    fun remove(key: String?) {
        preferences.edit().remove(key).apply()
    }

    /**
     * Clear SharedPreferences (remove everything)
     */
    fun clear() {
        preferences.edit().clear().apply()
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     * @param key the pref key to check
     */
    private fun checkForNullKey(key: String?) {
        if (key == null) {
            throw NullPointerException()
        }
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     * @param value the pref value to check
     */
    private fun checkForNullValue(value: String?) {
        if (value == null) {
            throw NullPointerException()
        }
    }

    init {
        context = appContext
    }
}