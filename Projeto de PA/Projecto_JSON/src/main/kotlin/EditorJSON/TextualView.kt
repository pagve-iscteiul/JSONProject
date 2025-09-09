package EditorJSON

import JSONArray
import JSONData
import JSONObject
import javax.swing.JTextArea

class TextualView(private val model: JSONModel): JTextArea() {
    private val textArea: JTextArea = JTextArea()

    init {
        textArea.isEditable = false

        model.addObserver(object : JSONModelObserver {
            override fun JSONObjectAdded(jo: JSONObject) {
                updateText()
            }

            override fun JSONObjectRemoved(jo: JSONObject) {
                updateText()
            }

            override fun JSONArrayAdded(ja: JSONArray) {
                updateText()
            }

            override fun JSONArrayRemoved(ja: JSONArray) {
                updateText()
            }
        })

        updateText()
    }

    fun updateText() {
        val serializedText = model.toString()
        textArea.text = serializedText
    }

    fun TextualData(): JSONObject {
        val teste1 = JSONData("uc", "PA")
        val teste2 = JSONData("etcs", "6.0")
        val teste3 = JSONData("exame", "null")
        val teste4 = JSONData("numero", "101101")
        val teste5 = JSONData("nome", "Dave Farley")
        val teste6 = JSONData("internacional", "true")
        val teste7 = JSONData("numero", "101102")
        val teste8 = JSONData("nome", "Martin Fowler")
        val teste9 = JSONData("internacional", "true")
        val teste10 = JSONData("numero", "26502")
        val teste11 = JSONData("nome", "Andre")
        val teste12 = JSONData("internacional", "false")

        val jo = JSONObject()
        jo.addElement(teste4)
        jo.addElement(teste5)
        jo.addElement(teste6)
        jo.removeElement(teste6)
        jo.addElement(teste6)
        val jo1 = JSONObject()
        jo1.addElement(teste7)
        jo1.addElement(teste8)
        jo1.addElement(teste9)
        val jo2 = JSONObject()
        jo2.addElement(teste10)
        jo2.addElement(teste11)
        jo2.addElement(teste12)
        val ja = JSONArray()
        ja.addElement(jo)
        ja.addElement(jo1)
        ja.addElement(jo2)
        val ja1 = JSONArray()
        ja1.addElement("MEI")
        ja1.addElement("MIG")
        ja1.addElement("METI")
        ja1.removeElement("METI")
        ja1.addElement("METI")

        val teste13 = JSONData("inscritos", ja)

        val jo3 = JSONObject()
        jo3.addElement(teste1)
        jo3.addElement(teste2)
        jo3.addElement(teste3)
        jo3.addElement(teste13)

        val teste14 = JSONData("cursos", ja1)

        jo3.addElement(teste14)

        return jo3
    }
}