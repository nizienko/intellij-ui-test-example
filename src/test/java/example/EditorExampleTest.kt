package example

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.ContainerFixture
import com.intellij.remoterobot.fixtures.DefaultXpath
import org.assertj.swing.core.MouseButton
import org.junit.jupiter.api.Test
import java.awt.Point

class EditorExampleTest {

    /* editor with text should already be opened

        fun main() {
            println("Hello world")
        }
    */
    @Test
    fun selectTextInEditor() = ui {
        val editor = find<EditorFixture>()

        // find text, select and check selected
        editor.findText("println").doubleClick(MouseButton.LEFT_BUTTON)
        assert(editor.selectedText == "println")

        // click on the right from text
        // we can get point of offset
        val offset = editor.text.indexOf("world") + "world".length
        val point = editor.findPointByOffset(offset) // point of the end of 'println'
        editor.click(point)
        assert(editor.caretOffset == offset)

        // and modify the point
        val onTheRightFromPoint = Point(point.x + 50, point.y)
        editor.click(onTheRightFromPoint)
        assert(editor.caretOffset == offset + 2)
    }
}


fun ui(test: RemoteRobot.() -> Unit) {
    val robot = RemoteRobot("http://127.0.0.1:8082")
    robot.apply(test)
}

@DefaultXpath("type", "//div[@class='EditorComponentImpl']")
class EditorFixture(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) : ContainerFixture(remoteRobot, remoteComponent) {
    val text: String
        get() = callJs<String>("component.getEditor().getDocument().getText()", true)

    val selectedText: String
        get() = callJs<String>("component.getEditor().getSelectionModel().getSelectedText()", true)

    val caretOffset: Int
        get() = callJs<Int>("component.getEditor().getCaretModel().getOffset()", runInEdt = true)

    fun findPointByOffset(offset: Int): Point {
        return callJs("""
            const editor = component.getEditor()
            const visualPosition = editor.offsetToVisualPosition($offset)
            editor.visualPositionToXY(visualPosition) 
        """, true)
    }

    fun moveToLine(lineNumber: Int) {
        val pointToClick = callJs<Point>("""
            importClass(com.intellij.openapi.editor.ScrollType)

            const editor = component.getEditor()
            const document = editor.getDocument()
            const offset = document.getLineStartOffset($lineNumber - 1)
            editor.getScrollingModel().scrollTo(editor.offsetToLogicalPosition(offset), ScrollType.CENTER)
            const visualPosition = editor.offsetToVisualPosition(offset)
            editor.visualPositionToXY(visualPosition)
        """, runInEdt = true)
        Thread.sleep(500)
        click(pointToClick)
    }
}