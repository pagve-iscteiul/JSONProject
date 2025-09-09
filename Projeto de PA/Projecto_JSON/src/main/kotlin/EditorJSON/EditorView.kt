/*import JSONArray
import JSONData
import JSONObject
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.LineBorder*/

package EditorJSON

import JSONArray
import JSONData
import JSONObject
import java.awt.*
import java.awt.event.*
import javax.swing.*
import javax.swing.border.LineBorder

class EditorView(private val model: JSONModel): JPanel() {
    private val label: JLabel
    val jo = JSONObject()
    val jo1 = JSONObject()
    val jo2 = JSONObject()
    val jo3 = JSONObject()
    val jo4 = JSONObject()
    val jo5 = JSONObject()
    val ja = JSONArray()
    val ja1 = JSONArray()
    var joIsInsidePanel = false
    var jo1IsInsidePanel = false
    var jo2IsInsidePanel = false
    private val textualView: TextualView
    private val panelList: MutableList<JPanel> = mutableListOf()

    init {
        layout = BorderLayout()

        label = JLabel()
        add(label, BorderLayout.CENTER)

        textualView = TextualView(model)
        add(textualView, BorderLayout.EAST)

        updateText()

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
    }

    private fun updateText() {
        label.text = model.toString()
    }

    fun testPanel(): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            val data1 = JSONData("uc", "PA")
            val data2 = JSONData("etcs", "6.0")
            val data3 = JSONData("exame", "N/A")
            val data4 = JSONData("numero", "101101")
            val data5 = JSONData("nome", "Dave Farley")
            val data6 = JSONData("internacional", "true")
            val data7 = JSONData("numero", "101102")
            val data8 = JSONData("nome", "Martin Fowler")
            val data9 = JSONData("internacional", "true")
            val data10 = JSONData("numero", "26502")
            val data11 = JSONData("nome", "Andre Santos")
            val data12 = JSONData("internacional", "false")
            jo.addElement(data1)
            add(MainWidget(jo))
            panelList.add(MainWidget(jo))
            jo4.addElement(data2)
            add(MainWidget(jo4))
            panelList.add(MainWidget(jo4))
            jo5.addElement(data3)
            add(MainWidget(jo5))
            panelList.add(MainWidget(jo5))
            jo1.addElement(data4)
            jo1.addElement(data5)
            jo1.addElement(data6)
            ja1.addElement(jo1)
            jo2.addElement(data7)
            jo2.addElement(data8)
            jo2.addElement(data9)
            ja1.addElement(jo2)
            jo3.addElement(data10)
            jo3.addElement(data11)
            jo3.addElement(data12)
            ja1.addElement(jo3)
            add(MainWidget3("inscritos", ja1, jo1, jo2, jo3))
            panelList.add(MainWidget3("inscritos", ja1, jo1, jo2, jo3))
            ja.addElement("MEI")
            ja.addElement("MIG")
            ja.addElement("METI")
            add(MainWidget2("cursos", ja))
            panelList.add(MainWidget2("cursos", ja))

            // menu
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    val menu = JPopupMenu("Message")
                    val add = JButton("add")
                    add.addActionListener {
                        val text = JOptionPane.showInputDialog("text")
                        val json = JSONObject()
                        val json1 = JSONObject()
                        val json2 = JSONObject()
                        val d = JSONData(text, "")
                        val d1 = JSONData(text, "N/A")
                        val a = JSONArray()
                        val a1 = JSONArray()
                        if (text == "uc" || text == "etcs") {
                            json.addElement(d)
                            add(MainWidget(json))
                            panelList.add(MainWidget(json))
                            //addJSONObject(json)
                        }
                        if (text == "exame") {
                            json.addElement(d1)
                            add(MainWidget(json))
                            panelList.add(MainWidget(json))
                            //addJSONObject(json)
                        }
                        if (text == "cursos") {
                            a.addElement("")
                            a.addElement("")
                            a.addElement("")
                            add(MainWidget2(text, a))
                            panelList.add(MainWidget2(text, a))
                            //addJSONArray(a)
                        }
                        if (text == "inscritos") {
                            json.addElement(d)
                            json1.addElement(d)
                            json2.addElement(d)
                            a1.addElement(json)
                            a1.addElement(json1)
                            a1.addElement(json2)
                            add(MainWidget3(text, a1, json, json1, json2))
                            panelList.add(MainWidget3(text, a1, json, json1, json2))
                        }
                        menu.isVisible = true
                        revalidate()
                        repaint()
                    }
                    val delAll = JButton("delete all")
                    delAll.addActionListener {
                        components.forEach {
                            remove(it)
                        }
                        panelList.clear()
                        menu.isVisible = true
                        revalidate()
                        repaint()
                    }
                    val undo = JButton("undo")
                    undo.addActionListener {
                        if (panelList.isNotEmpty()) {
                            val lastPanel = panelList.removeAt(panelList.size - 1)
                            remove(lastPanel)
                        }
                        menu.isVisible = true
                        revalidate()
                        repaint()
                    }
                    menu.add(add)
                    menu.add(undo)
                    menu.add(delAll)
                    menu.show(this@apply, 100, 100)
                }
            })
        }

    fun MainWidget(jo: JSONObject): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            val spaceComponents = 10

            val labelPanel = JPanel()
            labelPanel.layout = BoxLayout(labelPanel, BoxLayout.X_AXIS)

            jo.jsondata.forEach {
                val l = JLabel(it.name)
                val m = JTextField(it.value.toString())
                labelPanel.add(l)
                labelPanel.add(Box.createRigidArea(Dimension(spaceComponents, 0)))
                labelPanel.add(m)

                if(it.value == "N/A") {
                    val l2 = JLabel(m.text)
                    labelPanel.remove(m)
                    labelPanel.add(l2)
                }

                if(it.value is Boolean) {
                    val checkbox = JCheckBox()
                    checkbox.isSelected = m.text.toBoolean()
                    labelPanel.remove(m)
                    labelPanel.add(checkbox)
                }
             }
            add(labelPanel)
        }

    fun MainWidget2(key: String, ja: JSONArray): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            val labelPanel = JPanel()
            labelPanel.layout = BoxLayout(labelPanel, BoxLayout.X_AXIS)
            labelPanel.add(JLabel(key))

            ja.arraylist.forEach {
                val panelcursos = JPanel()
                panelcursos.layout = BoxLayout(panelcursos, BoxLayout.X_AXIS)
                panelcursos.border = LineBorder(Color.GRAY, 5)
                val v = JTextField(it.toString())
                panelcursos.add(v)
                labelPanel.add(panelcursos)
            }
            add(labelPanel)
        }

    fun MainWidget3(key: String, ja: JSONArray, jo: JSONObject, jo1: JSONObject, jo2: JSONObject): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            val labelPanel = JPanel()
            labelPanel.layout = BoxLayout(labelPanel, BoxLayout.X_AXIS)
            labelPanel.add(JLabel(key))

            ja.arraylist.forEach {
                val panelinscritos = JPanel()
                panelinscritos.layout = BoxLayout(panelinscritos, BoxLayout.Y_AXIS)
                panelinscritos.border = LineBorder(Color.GRAY, 5)
                if(joIsInsidePanel == false) {
                    val widget = MainWidget(jo)
                    panelinscritos.add(widget)
                    joIsInsidePanel = true
                } else {
                    if(jo1IsInsidePanel == false) {
                        val widget = MainWidget(jo1)
                        panelinscritos.add(widget)
                        jo1IsInsidePanel = true
                    } else {
                        if(jo2IsInsidePanel == false) {
                            val widget = MainWidget(jo2)
                            panelinscritos.add(widget)
                            jo2IsInsidePanel = true
                        }
                    }
                }
                labelPanel.add(panelinscritos)
            }
            add(labelPanel)
        }
}