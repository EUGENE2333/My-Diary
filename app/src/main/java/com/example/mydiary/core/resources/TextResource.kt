package com.example.mydiary.core.resources

import android.content.Context
import android.os.Parcel
import android.os.ParcelFormatException
import android.os.Parcelable
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

/**  A wrapper class for a [String] object or a [StringRes] ID. See: https://hannesdorfmann.com/abstraction-text-resource/ */
@Suppress("SpreadOperator")
class TextResource private constructor(
    private val value: Any,
    private vararg val arguments: Any
) : Parcelable {

    /**
     * Loads the String that the resource represents with any accompanying format arguments
     *
     * @param context A context used to load resources.
     * @return A String that may be formatted with input arguments.
     */
    fun toString(context: Context): String {
        val args = processArgs(context)
        return when {
            value is String && args.isEmpty() -> value
            value is String -> value.format(*args)
            value is Int && args.isEmpty() -> context.getString(value)
            value is Int -> context.getString(value, *args)
            value is Pair<*, *> && args.isEmpty() -> {
                val id = value.first as Int
                val quantity = value.second as Int
                context.resources.getQuantityString(id, quantity)
            }
            value is Pair<*, *> -> {
                val id = value.first as Int
                val quantity = value.second as Int
                context.resources.getQuantityString(id, quantity, *args)
            }
            else -> error("Unsupported type: ${value.javaClass.name}")
        }
    }

    /**
     * @return true if value is empty
     */
    fun isEmpty(): Boolean {
        return value is String && value.isEmpty()
    }

    private fun processArgs(context: Context): Array<Any> =
        arguments.map { (it as? TextResource)?.toString(context) ?: it }.toTypedArray()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextResource

        if (value != other.value) return false
        if (!arguments.contentEquals(other.arguments)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + arguments.contentHashCode()
        return result
    }

    override fun toString(): String {
        val textString = when (value) {
            is String -> "string=$value"
            is Int -> "id=$value"
            is Pair<*, *> -> "id=${value.first}, quantity=${value.second}"
            else -> error("Unsupported type: ${value.javaClass.name}")
        }
        var argString = ""
        if (arguments.isNotEmpty()) {
            argString = arguments.joinToString(
                prefix = ", args=[",
                postfix = "]"
            )
        }
        return "$textString$argString"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        when (value) {
            is Int -> {
                parcel.writeInt(TYPE_STRING_RES)
                parcel.writeInt(value)
            }
            is String -> {
                parcel.writeInt(TYPE_STRING)
                parcel.writeString(value)
            }
            is Pair<*, *> -> {
                parcel.writeInt(TYPE_PLURALS_RES)
                parcel.writeInt(value.first as Int)
                parcel.writeInt(value.second as Int)
            }
            else -> throw ParcelFormatException(
                "Unsupported type class: ${value::class.java} for value: $value"
            )
        }
        parcel.writeArray(arguments)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<TextResource> {
        private const val TYPE_STRING_RES = 1
        private const val TYPE_STRING = 2
        private const val TYPE_PLURALS_RES = 3

        /**
         * Creates a new TextResource from a [String]. Supports formatting using [java.util.Formatter].
         * If the input string is null or empty then it will be set to empty.
         *
         * @param string The string used to create the TextResource
         * @param formatArguments Format arguments used to format the string
         */
        fun from(string: String?, vararg formatArguments: Any): TextResource = if (string.isNullOrEmpty()) {
            TextResource("" as Any)
        } else {
            TextResource(
                string as Any,
                *formatArguments
            )
        }

        /**
         * Creates a new TextResource from an Android [StringRes] ID. Supports formatting using [java.util.Formatter].
         *
         * @param stringRes The String resource ID used to create the TextResource
         * @param formatArguments Format arguments used to format the string
         */
        fun from(@StringRes stringRes: Int, vararg formatArguments: Any): TextResource = TextResource(
            stringRes as Any,
            *formatArguments
        )

        /**
         * Creates a new TextResource from an Android [PluralsRes] ID. Supports formatting using [java.util.Formatter].
         *
         * @param pluralRes The String resource ID used to create the TextResource
         * @param quantity The quantity to use for plural resolution
         * @param formatArguments Format arguments used to format the string
         */
        fun from(@PluralsRes pluralRes: Int, quantity: Int, vararg formatArguments: Any): TextResource = TextResource(
            (pluralRes to quantity),
            *formatArguments
        )

        override fun createFromParcel(parcel: Parcel): TextResource {
            val loader = this::class.java.classLoader
            return when (val type = parcel.readInt()) {
                TYPE_STRING_RES -> from(parcel.readInt(), *(parcel.readArray(loader)!!))
                TYPE_STRING -> from(parcel.readString()!!, *(parcel.readArray(loader)!!))
                TYPE_PLURALS_RES -> from(
                    parcel.readInt(),
                    parcel.readInt(),
                    *(parcel.readArray(loader)!!)
                )
                else -> throw ParcelFormatException("Unsupported type id: $type")
            }
        }

        override fun newArray(size: Int): Array<TextResource?> = arrayOfNulls(size)
    }
}

fun String.toTextResource(): TextResource {
    return TextResource.from(this)
}
