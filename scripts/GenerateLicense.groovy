import java.text.SimpleDateFormat
import java.util.prefs.Preferences

import javax.security.auth.x500.X500Principal

import de.schlichtherle.license.DefaultCipherParam
import de.schlichtherle.license.DefaultLicenseParam
import de.schlichtherle.license.KeyStoreParam
import de.schlichtherle.license.LicenseContent
import de.schlichtherle.license.LicenseManager
import de.schlichtherle.license.LicenseNotary
import de.schlichtherle.license.LicenseParam

includeTargets << grailsScript("_GrailsInit")

target(generateLicense: "Generates a license") {
	println "Generating License"

	try{
		GroovyClassLoader loader = new GroovyClassLoader(getClass().getClassLoader())
		println "Attempting to load private properties"
		Properties properties = new Properties()
		properties.load new FileInputStream("${basedir}/license-config/LicensePrivateConfig.properties")
		println "Loaded private properties"
		def licenseConfig = new ConfigSlurper().parse(properties)

		def sdf = new SimpleDateFormat("MM-dd-yyyy kk:mm:ss")

		def createLicenseContent = { LicenseParam licenseParam ->
			println "Creating license content"
			Date notAfter
			if (licenseConfig.license.notAfter) {
				notAfter = sdf.parse(licenseConfig.license.notAfter)
			} else {
				def validDate = Calendar.instance
				validDate.add(Calendar.DAY_OF_YEAR, licenseConfig.license.notAfterDays? licenseConfig.license.notAfterDays as int : 0)
				validDate.add(Calendar.HOUR, licenseConfig.license.notAfterHours? licenseConfig.license.notAfterHours as int : 0)
				notAfter = validDate.time
			}

			new LicenseContent(
				holder: new X500Principal(licenseConfig.license.holder),
				issuer: new X500Principal(licenseConfig.license.issuer),
				consumerAmount: licenseConfig.license.consumerAmount as Integer,
				consumerType: licenseConfig.license.consumerType,
				info: licenseConfig.license.info,
				issued: licenseConfig.license.issueDate? sdf.parse(licenseConfig.license.issueDate) : new Date(),
				notAfter: notAfter,
				notBefore: licenseConfig.license.notBefore? sdf.parse(licenseConfig.license.notBefore) : null,
				subject: licenseParam.subject)
		}

		def cipherParam = new DefaultCipherParam(licenseConfig.license.cipherPassword)
		def preferences = Preferences.systemNodeForPackage(getClass())
		def privateKeyPath = "${basedir}/license-config/${licenseConfig.license.privateKeyFile}"

		def privateKeyStoreParam = new KeyStoreParam() {
			InputStream getStream() throws IOException {
				final String resourceName = privateKeyPath
				final InputStream instream = new FileInputStream(resourceName)
				if (!instream) {
					println "Could not load file: $resourceName"
					throw new FileNotFoundException(resourceName)
				}
				instream
			}
			String getAlias() {
				licenseConfig.license.privateKeyAlias
			}
			String getStorePwd() {
				licenseConfig.license.storePassword
			}
			String getKeyPwd() {
				licenseConfig.license.keyPassword
			}
		}

		def licenseParam = new DefaultLicenseParam(licenseConfig.license.subject, preferences, privateKeyStoreParam, cipherParam)
		LicenseContent lc = createLicenseContent(licenseParam)
		new LicenseManager(licenseParam).store(lc, new LicenseNotary(privateKeyStoreParam), new File(licenseConfig.license.output))

		println "License file ${licenseConfig.license.output} generated"
		println "---------------"
		println "License details"
		println "---------------"
		println "Subject: $lc.subject"
		println "Holder: $lc.holder"
		println "Issuer: $lc.issuer"
		println "Issued: $lc.issued"
		println "Valid not after: $lc.notAfter"
		println "Valid not before: $lc.notBefore"
		println "Info: $lc.info"
		println "Consumer type: $lc.consumerType"
		println "# Consumer: $lc.consumerAmount"
	} catch (e){
		println "Unable to store license"
		e.printStackTrace()
	}
}

setDefaultTarget(generateLicense)
