package mx.uv.fiee.iinf.unittestbasics;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// TDD Test Driven Development
// CI Continous Integration

public class UtilsTest {

    @Test
    public void testIsEmailValid() {
        String testEmail = "gonzalo@gmail.com";
        String falseEmail = "gonzalo@";

        //Assert.assertThat (String.format ("Email Validity Test failed for %s ", testEmail), Utils.checkEmailForValidity (testEmail), is (true));

        assertTrue (String.format ("Email Validity Test failed for %s ", testEmail), Utils.checkEmailForValidity (testEmail));
        assertFalse (String.format ("Email Validity Test failed for %s ", testEmail), Utils.checkEmailForValidity (falseEmail));
    }

    @Test
    public void testCheckDateWasConvertedCorrectly() {
        long inMillis = System.currentTimeMillis ();
        Date date = Utils.calendarDate (inMillis);
        assertEquals ("Date time in millis is wrong", inMillis * 1000, date.getTime ());
    }

}