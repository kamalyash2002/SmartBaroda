package com.uphar.bussinesss.domain.Utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.uphar.bussinesss.domain.Utils.networkException.NoConnectivityException
import com.uphar.smartbaroda.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


val <T>T.exhaustive: T
    get() = this

fun <T> ComponentActivity.collectLatestFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> Fragment.collectLatestFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}


/*fun <T> AppCompatActivity.collectFlow(flow: Flow<T>, collect: FlowCollector<T>) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}

fun <T> AppCompatActivity.collectFlow2(flow: Flow<T>, collect: FlowCollector<T>) {
    lifecycleScope.launch {
        flow.collect(collect)
    }
}*/

fun getLanguageString(ln: String): String {
    return when (ln) {
        "en" -> "English"
        "hi" -> "हिन्दी"
        else -> "English"
    }
}

fun getLanguageStringFAQ(ln: String): String {
    return when (ln) {
        "en" -> "English"
        "hi" -> "हिन्दी"
        else -> "English"
    }
}



fun getErrorMessage(context: Context, message: String): String {
    return when (message) {
        "unknown" -> context.getString(R.string.text_something_went_wrong)
        else -> message
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun isValidEmail(email: CharSequence): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email)
        .matches()
}

fun showKeyboard(activity: Activity) {
    val view = activity.currentFocus
    val methodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    assert(view != null)
    methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.toast(message: String?) {
    if (message.isNullOrEmpty()) return
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastShort(message: CharSequence?) {
    if (message.isNullOrEmpty()) return
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}



fun dateDifference(expDate: String): Boolean {

    val formatter = SimpleDateFormat("dd-MM-yyyy")
    val date = formatter.parse(expDate)
    val currentTime = Calendar.getInstance().time

    val diff: Long = date.time - currentTime.time
    val days = diff / (24 * 60 * 60 * 1000)
    Log.e("DAYS::", "diff : $days")
    return days in 0..2
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatQcDate(date: String?): String {
    val dateTime: ZonedDateTime = OffsetDateTime.parse(date).toZonedDateTime()
    val defaultZoneTime: ZonedDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    /*val output = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    output.timeZone = TimeZone.getTimeZone("GMT+5:30")*/

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")

    var d: Date? = null
    try {
        d = input.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    //val formatted: String = output.format(d)

    val formatted: String = defaultZoneTime.format(formatter)

    Log.e("DATE::", "$formatted")
    return formatted
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertToMilliSeconds(date: String): Long {
    val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    var timeInMilliseconds = 0L
    try {
        val mDate = sdf.parse(date)
        timeInMilliseconds = mDate.time
        println("Date in milli :: $timeInMilliseconds")
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return timeInMilliseconds.toLong()
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateOnly(date: String): String {
    val dateTime: ZonedDateTime = OffsetDateTime.parse(date).toZonedDateTime()
    val defaultZoneTime: ZonedDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    var d: Date? = null
    try {
        d = input.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    //val formatted: String = output.format(d)

    val formatted: String = defaultZoneTime.format(formatter)
    Log.e("DATE::", "$formatted")
    return formatted
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeOnly(date: String): String {
    val dateTime: ZonedDateTime = OffsetDateTime.parse(date).toZonedDateTime()
    val defaultZoneTime: ZonedDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    var d: Date? = null
    try {
        d = input.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    //val formatted: String = output.format(d)

    val formatted: String = defaultZoneTime.format(formatter)
    Log.e("DATE::", "$formatted")
    return formatted
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateOnlyDot(date: String): String {
    val dateTime: ZonedDateTime = OffsetDateTime.parse(date).toZonedDateTime()
    val defaultZoneTime: ZonedDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    var d: Date? = null
    try {
        d = input.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    //val formatted: String = output.format(d)

    val formatted: String = defaultZoneTime.format(formatter)
    Log.e("DATE::", "$formatted")
    return formatted
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTradeStatusDate(date: String): String {
    val dateTime: ZonedDateTime = OffsetDateTime.parse(date).toZonedDateTime()
    val defaultZoneTime: ZonedDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a")

    var d: Date? = null
    try {
        d = input.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    //val formatted: String = output.format(d)

    val formatted: String = defaultZoneTime.format(formatter)
    Log.e("DATE::", "$formatted")
    return formatted
}


@RequiresApi(Build.VERSION_CODES.O)
fun qcResultDate(date: String): String {
    Log.e("DATE::", "$date")
    val dateTime: ZonedDateTime = OffsetDateTime.parse(date).toZonedDateTime()
    val defaultZoneTime: ZonedDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val output = SimpleDateFormat("dd-MM-yyyy")
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    var d: Date? = null
    try {
        d = input.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    //val formatted: String = output.format(d)
    val formatted: String = defaultZoneTime.format(formatter)
    Log.e("DATE::", "$formatted")
    return formatted
}

@RequiresApi(Build.VERSION_CODES.O)
fun qcResultTime(date: String): String {
    Log.e("DATE::", "$date")
    val dateTime: ZonedDateTime = OffsetDateTime.parse(date).toZonedDateTime()
    val defaultZoneTime: ZonedDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val output = SimpleDateFormat("hh:mm a")
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")


    var d: Date? = null
    try {
        d = input.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    // val formatted: String = output.format(d)
    val formatted: String = defaultZoneTime.format(formatter)
    Log.e("DATE::", "$formatted")
    return formatted
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatReviewDate(date: String): String {
    val dateTime: ZonedDateTime = OffsetDateTime.parse(date).toZonedDateTime()
    val defaultZoneTime: ZonedDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    /*val output = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    output.timeZone = TimeZone.getTimeZone("GMT+5:30")*/

    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    var d: Date? = null
    try {
        d = input.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    //val formatted: String = output.format(d)

    val formatted: String = defaultZoneTime.format(formatter)

    Log.e("DATE::", "$formatted")
    return formatted
}

fun setClickableHighLightedText(
    tv: TextView,
    textToHighlight: String,
    onClickListener: View.OnClickListener?
) {
    val tvt = tv.text.toString()
    var ofe = tvt.indexOf(textToHighlight, 0)
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onClickListener?.onClick(textView)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = tv.context.resources.getColor(R.color.purple_200)
            ds.isUnderlineText = false
        }
    }
    val wordToSpan = SpannableString(tv.text)
    var ofs = 0
    while (ofs < tvt.length && ofe != -1) {
        ofe = tvt.indexOf(textToHighlight, ofs)
        if (ofe == -1) break else {
            wordToSpan.setSpan(
                clickableSpan,
                ofe,
                ofe + textToHighlight.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tv.setText(wordToSpan, TextView.BufferType.SPANNABLE)
            tv.movementMethod = LinkMovementMethod.getInstance()
        }
        ofs = ofe + 1
    }
}

fun findDistance(lat1: Double, long1: Double, lat2: Double, long2: Double): Float {
    val startPoint = Location("locationA")
    startPoint.latitude = lat1
    startPoint.longitude = long1

    val endPoint = Location("locationA")
    endPoint.latitude = lat2
    endPoint.longitude = long2

    return startPoint.distanceTo(endPoint)
}

fun roundOffDecimal(number: Double): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toDouble()
}

fun formatToCurrencyDigit(number: Any): String {
   /* val numString = String.format("%,.2f", number)
    return "₹ ${numString}"*/
    val df = DecimalFormat("#,##0.00")
    return "₹ ${df.format(number)}"
}

fun formatToCurrencyDigitForBB(number: Double): String {
    val numString = String.format("%,.2f", number)
    return "₹ ${numString}"
    //val df = DecimalFormat("#,##0.00")
    //return "₹ ${df.format(number)}"
}

fun getErrorMessage(e: Exception) =
    if (e is NoConnectivityException) {
        "No Internet Connection"
    } else if (e.message!!.trim() == "Access denied") {
        "Access denied"
    } else {
        "Something went wrong"
        //e.message
    }

fun getErrorMessageWithOutNetworkError(e: Exception) =
    if (e is NoConnectivityException) {
        ""
    } else if (e.message!!.trim() == "Access denied") {
        "Access denied"
    } else {
        "Something went wrong"
        //e.message
    }



fun EditText.markRequiredInRed() {
    hint = buildSpannedString {
        append(hint)
        color(Color.RED) { append(" *") }
    }
}



fun Context.openYoutubeLink(youtubeID: String) {
    val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeID))
    val intentBrowser =
        Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtubeID))
    try {
        this.startActivity(intentApp)
    } catch (ex: ActivityNotFoundException) {
        this.startActivity(intentBrowser)
    }

}

fun isZeroOnly(text: String): Boolean {
    val patternLabel1: Pattern = Pattern.compile("^(?!0*\$).*$")
    val matcherLabel1: Matcher = patternLabel1.matcher(text)
    return !matcherLabel1.matches()
}

fun isSpecialCharOnly(text: String): Boolean {
    val patternLabel1: Pattern = Pattern.compile("^([^1-9a-zA-Z]*)\$")
    val matcherLabel1: Matcher = patternLabel1.matcher(text)
    return matcherLabel1.matches()
}





fun getCityStateFromAddress(address: String): String {

    val addressArray = address.replace("\n", "")
        .split(",").map { it -> it.trim() }

    return if (addressArray.size >= 3) {
        val arr3 = addressArray.takeLast(3)
        "${arr3[0]}, ${arr3[1]}"
    } else {
        address
    }

    /*return if (addressArray.size >= 4) {
        val pos = addressArray[3].lastIndexOf("-")
        val state = if (pos != -1) addressArray[3].removeRange(
            pos,
            addressArray[3].length
        ) else addressArray[3]
        addressArray[2] + ", " + state
    } else {
        address
    }*/
}

fun getRandomInt() = Random().nextInt(100000)