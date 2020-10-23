package example

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.ComponentFixture
import com.intellij.remoterobot.fixtures.ContainerFixture
import com.intellij.remoterobot.search.locators.byXpath
import org.junit.jupiter.api.Test
import java.awt.event.KeyEvent

class ExampleTest {

    @Test
    fun openAboutFromWelcomeScreen() {
        val robot = RemoteRobot("http://127.0.0.1:8082")
        robot.find<ComponentFixture>(byXpath("//div[@text = 'Get Help']")).click()

        robot.find<ContainerFixture>(byXpath("//div[@class='HeavyWeightWindow']")).apply {
            findText("About").click()
        }
        assert(robot.findAll<ComponentFixture>(byXpath("//div[@class='InfoSurface']")).isNotEmpty()) {
            "About window is not displayed"
        }
        Thread.sleep(3000)
        robot.runJs("robot.pressAndReleaseKey(${KeyEvent.VK_ESCAPE})")
    }
}