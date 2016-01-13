import grails.test.AbstractCliTestCase

class GenerateLicenseTests extends AbstractCliTestCase {

    void testGenerateLicense() {

        execute(["generate-license"])

        assert waitForProcess() == 0
        verifyHeader()
    }
}
