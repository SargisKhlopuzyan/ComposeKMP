import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.sargis.composekmp.Counter
import kotlin.test.Test

class CounterTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testCountingUp() = runComposeUiTest{
        setContent {
            Counter()
        }
        onNodeWithText("0").assertExists()
        onNodeWithText("1").assertDoesNotExist()
        onNodeWithText("Increment").performClick()
        onNodeWithText("1").assertExists()
    }
}