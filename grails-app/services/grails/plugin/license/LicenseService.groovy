package grails.plugin.license

import java.util.prefs.Preferences
import java.io.FileInputStream
import java.io.File

import de.schlichtherle.license.DefaultCipherParam
import de.schlichtherle.license.DefaultLicenseParam
import de.schlichtherle.license.KeyStoreParam
import de.schlichtherle.license.LicenseContent
import de.schlichtherle.license.LicenseManager
import grails.plugin.license.prefs.FilePreferencesFactory

/**
 * @author Nick Blanchard
 */
class LicenseService {

	static transactional = false

	private licenseConfig
	
	private licenseContent

	// verify and retrieve current license
	LicenseContent getLicense() {
		if(licenseContent != null){
			return licenseContent
		}
		
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
			licenseContent = lm.install(file)
		} catch (e) {
			log.error "License could not be installed", e
		}
		return licenseContent
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
		System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
		System.setProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE, "myprefs.txt");
	 
		Preferences p = Preferences.userNodeForPackage(this.class);
		def publicKeyPath = licenseConfig.license.publicKeyFile
		def publicKeyStoreParam = new KeyStoreParam() {
			InputStream getStream() throws IOException {
				final String resourceName = publicKeyPath
				File publicKeyFile = new File(publicKeyPath)
				final InputStream instream = new FileInputStream(publicKeyFile)
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
		new DefaultLicenseParam(licenseConfig.license.subject, p, publicKeyStoreParam, cipherParam)
	}
}
