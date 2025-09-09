package EditorJSON

import JSONObject
import JSONArray
import javax.swing.JPanel

class JSONModel {
    private val dataJSONObject = JSONObject()
    private val dataJSONArray = JSONArray()
    private val observers: MutableList<JSONModelObserver> = mutableListOf()
    private val panels: MutableList<JPanel> = mutableListOf()
    private var value: Any? = null

    fun addObserver(observer: JSONModelObserver) = observers.add(observer)

    fun removeObserver(observer: JSONModelObserver) = observers.remove(observer)

    fun addJSONObject(jo: JSONObject) {
        observers.forEach {
            it.JSONObjectAdded(jo)
        }
    }

    fun removeJSONObject(jo: JSONObject) {
        observers.forEach {
            it.JSONObjectRemoved(jo)
        }
    }

    fun addJSONArray(ja: JSONArray) {
        dataJSONArray.addElement(ja)
        observers.forEach {
            it.JSONArrayAdded(ja)
        }
    }

    fun removeJSONArray(ja: JSONArray) {
        dataJSONArray.removeElement(ja)
        observers.forEach {
            it.JSONArrayRemoved(ja)
        }
    }

    override fun toString(): String {
        return when (this.value) {
            is JSONObject -> dataJSONObject.serialize()
            is JSONArray -> dataJSONArray.serialize()
            else -> ""
        }
    }
}

interface JSONModelObserver {
    fun JSONObjectAdded(jo: JSONObject)
    fun JSONObjectRemoved(jo: JSONObject)
    fun JSONArrayAdded(ja: JSONArray)
    fun JSONArrayRemoved(ja: JSONArray)
}