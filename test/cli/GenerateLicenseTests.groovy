import grails.test.AbstractCliTestCase

class GenerateLicenseTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGenerateLicense() {

        execute(["generate-license"])

        assertEquals 0, waitForProcess()
        verifyHeader()
    }
}
