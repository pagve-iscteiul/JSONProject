package EditorJSON

import java.awt.*
import javax.swing.*

fun main() {
    Editor().open()
}

class Editor {
    val model = JSONModel()
    val textualView = TextualView(model)
    val frame = JFrame("JSON Object Editor").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0,2)
        size = Dimension(1000, 1000)

        val left = JPanel()
        left.layout = GridLayout()
        val editorView = EditorView(model)
        val scrollPane = JScrollPane(editorView.testPanel()).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }
        left.add(scrollPane)
        add(left)

        val right = JPanel()
        right.layout = GridLayout()
        var srcArea = JTextArea()
        srcArea.font = Font("Arial", Font.PLAIN, 29)
        srcArea.tabSize = 3
        //srcArea.text = "$textualView.updateText()"
        srcArea.text = textualView.TextualData().serialize()
        right.add(srcArea)
        add(right)
    }

    fun open() {
        frame.isVisible = true
    }
}