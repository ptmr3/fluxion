package com.ptmr3.fluxx


class FluxxAction internal constructor(val type: String, val data: HashMap<String, Any>?) {

    operator fun <T> get(tag: String): T {
        return data!![tag] as T
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is FluxxAction) {
            return false
        }
        val fluxAction = other as FluxxAction?
        return if (type != fluxAction!!.type) {
            false
        } else !if (data != null) data != fluxAction.data else fluxAction.data != null
    }

    override fun toString(): String {
        return "FluxxAction{" + "Type='" + type + '\''.toString() + ", Data=" + data + '}'.toString()
    }

    class Builder {
        private var type: String? = null
        private var data: HashMap<String, Any>? = null

        internal fun with(type: String?): Builder {
            if (type == null) {
                throw IllegalArgumentException("Type may not be null.")
            }
            this.type = type
            this.data = HashMap()
            return this
        }

        fun bundle(key: String?, value: Any?): Builder {
            if (key == null) {
                throw IllegalArgumentException("Key may not be null.")
            }
            if (value == null) {
                throw IllegalArgumentException("Value may not be null.")
            }
            data!![key] = value
            return this
        }

        fun build(): FluxxAction {
            if (type == null || type!!.isEmpty()) {
                throw IllegalArgumentException("At least one key is required.")
            }
            return FluxxAction(type!!, data)
        }
    }

    companion object {

        fun type(type: String): Builder {
            return Builder().with(type)
        }
    }
}