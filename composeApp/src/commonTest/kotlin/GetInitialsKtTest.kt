import assertk.assertThat
import com.sargis.composekmp.getInitials
import kotlin.test.Test

class GetInitialsKtTest {

    @Test
    fun `FullName with only name returns only first 2 capitalized letters`() {
        val fullName = "Sargis"
        assertThat("SR", getInitials(fullName))
    }

    @Test
    fun `fullName with name and surname returns first capitalized name and surname letters`() {
        val fullName = "Sargis Khlopuzyan"
        assertThat("SK", getInitials(fullName))
    }

    @Test
    fun `fullName with name, middle name and surname returns first capitalized name and surname letters`() {
        val fullName = "Sargis Hrachik Khlopuzyan"
        assertThat("SK", getInitials(fullName))
    }
}