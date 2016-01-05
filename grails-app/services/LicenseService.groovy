import java.util.prefs.Preferences

import de.schlichtherle.license.DefaultCipherParam
import de.schlichtherle.license.DefaultLicenseParam
import de.schlichtherle.license.KeyStoreParam
import de.schlichtherle.license.LicenseContent
import de.schlichtherle.license.LicenseManager

/**
 * @author Nick Blanchard
 */
class LicenseService {

	static transactional = false

	private licenseConfig

	// verify and retrieve current license
	LicenseContent getLicense() {
		try {
			return new LicenseManager(getLicenseParam()).verify()
		} catch (e) {
			log.error "License could not be verified", e
		}
	}

	// install new license
	LicenseContent installLicense(File file) {
		try {
			LicenseManager lm = new LicenseManager(getLicenseParam())
			lm.install(file)
			return lm.verify()
		} catch (e) {
			log.error "License could not be installed", e
		}
	}

	// initiate config
	private initLicenseConfig() {
		if(!licenseConfig) {
			GroovyClassLoader loader = new GroovyClassLoader(getClass().getClassLoader())
			licenseConfig = new ConfigSlurper().parse(loader.loadClass("LicensePublicConfig"))
		}
	}

	// get license param
	private getLicenseParam() {
		initLicenseConfig()
		def cipherParam = new DefaultCipherParam(licenseConfig.license.cipherPassword)
		def preferences = Preferences.systemNodeForPackage(getClass())
		def publicKeyPath = licenseConfig.license.publicKeyFile
		def publicKeyStoreParam = new KeyStoreParam() {
			InputStream getStream() throws IOException {
				final String resourceName = publicKeyPath
				final InputStream instream = getClass().getResourceAsStream(publicKeyPath)
				if (!instream) {
					log.error "Could not load file: $resourceName"
					throw new FileNotFoundException(resourceName)
				}
				instream
			}
			String getAlias() {
				licenseConfig.license.publicKeyAlias
			}
			String getStorePwd() {
				licenseConfig.license.storePassword
			}
			String getKeyPwd() {}
		}
		new DefaultLicenseParam(licenseConfig.license.subject, preferences, publicKeyStoreParam, cipherParam)
	}
}
