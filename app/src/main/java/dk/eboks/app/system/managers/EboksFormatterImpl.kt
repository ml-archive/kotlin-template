package dk.eboks.app.system.managers

import android.content.Context
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceiptItem
import dk.eboks.app.domain.models.home.Item
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.nodes.nstack.kotlin.NStack
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bison on 19/02/18.
 */
class EboksFormatterImpl(val context: Context) : EboksFormatter {


    val messageDateFormat: SimpleDateFormat by lazy {
        try {
            SimpleDateFormat("d. MMM", NStack.language)
        } catch (t: Throwable) {
            SimpleDateFormat()
        }
    }
    val messageDateYearFormat: SimpleDateFormat by lazy {
        try {
            SimpleDateFormat("d. MMM YYYY", NStack.language)
        } catch (t: Throwable) {
            SimpleDateFormat()
        }
    }

    val hourDateFormat: SimpleDateFormat by lazy {
        try {
            SimpleDateFormat("hh:mm:ss", NStack.language)

        } catch (t: Throwable) {
            SimpleDateFormat()
        }
    }

    val dayDateFormat: SimpleDateFormat by lazy {
        try {
            SimpleDateFormat("E", NStack.language)

        } catch (t: Throwable) {
            SimpleDateFormat()
        }
    }


    override fun formatCpr(cpr: String): String {
        if (Config.isDK()) {
            if (cpr.length > 9) {
                return "${cpr.substring(0, 6)}-${cpr.substring(6, 10)}"
            }
        }
        // TODO add formatting for SE / NO
        return cpr
    }

    override fun formatDate(target: Message): String {
        try {
            return messageDateFormat.format(target.received)
        } catch (t: Throwable) {
            Timber.e(t)
            return ""
        }
    }

    override fun formatDateRelative(target: Item): String {
        return formatDateRelative(target.date!!)
    }

    override fun formatDateRelative(target: Message): String {
        return formatDateRelative(target.received)
    }

    override fun formatDateRelative(target: StoreboxReceiptItem): String {
        return formatDateRelative(target.purchaseDate)
    }

    private fun formatDateRelative(target: Date?): String {
        var result = ""
        val currentLocale = getCurrentLocale(context)
        val cal_recv = Calendar.getInstance(currentLocale)

        cal_recv.time = target
        val cal = Calendar.getInstance(currentLocale)
        val cal2 = Calendar.getInstance(currentLocale)

        val isThisYear = cal_recv.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
        val isToday = cal_recv.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && cal_recv.get(Calendar.DAY_OF_YEAR) == cal.get(
                Calendar.DAY_OF_YEAR
        )

        cal.add(Calendar.DATE, -1)
        // set call to yesterday
        val isYesterday = cal_recv.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && cal_recv.get(
                Calendar.DAY_OF_YEAR
        ) == cal.get(Calendar.DAY_OF_YEAR)

        if (isToday) {
            result = Translation.defaultSection.today
            return result
        } else if (isYesterday) {
            result = Translation.defaultSection.yesterday
            return result
        }

        cal2.add(Calendar.DATE, -7)
        // mail is received after a week ago
        if (cal_recv.after(cal2)) {
            var day: String? = ""
            if (cal_recv.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                day = dayDateFormat.format(cal_recv.time)
            }
            if (cal_recv.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                day = dayDateFormat.format(cal_recv.time)
            }
            if (cal_recv.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                day = dayDateFormat.format(cal_recv.time)
            }
            if (cal_recv.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                day = dayDateFormat.format(cal_recv.time)
            }
            if (cal_recv.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                day = dayDateFormat.format(cal_recv.time)
            }
            if (cal_recv.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                day = dayDateFormat.format(cal_recv.time)
            }
            if (cal_recv.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                day = dayDateFormat.format(cal_recv.time)
            }

            return if (day == null) {
                cal_recv.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, currentLocale)
            } else {
                day
            }
        }

        if (isThisYear) {
            return messageDateFormat.format(target)
        } else {
            return messageDateYearFormat.format(target)
        }
    }

    override fun formatSize(target: Content): String {
        if (target.fileSize < 1024)
            return "${target.fileSize} B"
        if (target.fileSize < 1024f * 1024f)
            return String.format("%.2f KB", target.fileSize.toFloat() / 1024f)
        if (target.fileSize < 1024f * 1024f * 1024f)
            return String.format("%.2f MB", target.fileSize.toFloat() / (1024f * 1024f))
        if (target.fileSize < 1024f * 1024f * 1024f * 1024f)
            return String.format("%.2f GB", target.fileSize.toFloat() / (1024f * 1024f * 1024f))
        return "0 B"
    }


    fun getCurrentLocale(context: Context): Locale {
        return context.resources.configuration.locale
    }


    override fun formatDateToDay(date: Date): String {
        return messageDateYearFormat.format(date)
    }

    override fun formatDateToTime(date: Date): String {
        return hourDateFormat.format(date)
    }

    override fun formatPrice(item : Item) : String
    {
        return "%.2f ${item.currency}".format(NStack.language, item.amount)
    }

}