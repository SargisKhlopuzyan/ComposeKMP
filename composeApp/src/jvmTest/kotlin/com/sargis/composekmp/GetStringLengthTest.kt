package com.sargis.composekmp

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class GetStringLengthTest {

    @Test
    fun `string with 4 letters returns 4`() {
        assertThat(getStringLength("abcd")).isEqualTo(4)
    }

}