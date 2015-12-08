import java.util.prefs.Preferences

import de.schlichtherle.license.DefaultCipherParam
import de.schlichtherle.license.DefaultLicenseParam
import de.schlichtherle.license.KeyStoreParam
import de.schlichtherle.license.LicenseManager

/**
 * License Service
 *
 * @author Nick Blanchard
 */
class LicenseService {

    boolean transactional = true
    
	def licenseConfig
	
	// verify and retrieve current license
    def getLicense() {
    	def lc 
    	try {
    	  LicenseManager lm = new LicenseManager(getLicenseParam())
          lc = lm.verify()
    	} catch (Exception e) {
    	  log.error "License could not be verified" + e
    	}
        return lc
    }
    
    // install new license
    def installLicense(file) {
    	def lc
    	try {
    	  LicenseManager lm = new LicenseManager(getLicenseParam())
    	  lm.install(file)
    	  lc = lm.verify()	
    	} catch (Exception e) {
    		log.error "License could not be installed" + e
    	}
    	return lc
    }
    
    // initiate config
    private initLicenseConfig() {
       if(!licenseConfig) {
          ClassLoader parent = getClass().getClassLoader()
          GroovyClassLoader loader = new GroovyClassLoader(parent)
          def ac = loader.loadClass("LicensePublicConfig")
          licenseConfig = new ConfigSlurper().parse(ac)
       }
    }
    
    // get license param
    private getLicenseParam() {
    	initLicenseConfig()
    	def cipherParam = new DefaultCipherParam(licenseConfig.license.cipherPassword) 
    	def peferences = Preferences.systemNodeForPackage(getClass())
    	def publicKeyPath = licenseConfig.license.publicKeyFile
//    	def publicKeyStoreParam = new DefaultKeyStoreParam(getClass(), publicKeyPath, licenseConfig.license.publicKeyAlias, licenseConfig.license.storePassword, null)
		def publicKeyStoreParam = new KeyStoreParam() {
			public InputStream getStream() throws IOException {
				final String resourceName = publicKeyPath;
				final InputStream instream = getClass().getResourceAsStream(publicKeyPath);
				if (instream == null) {
					println "Could not load file: " + resourceName;
					throw new FileNotFoundException(resourceName);
				}
				return instream;
			}
			public String getAlias() {
				return licenseConfig.license.publicKeyAlias;
			}
			public String getStorePwd() {
				return licenseConfig.license.storePassword;
			}
			public String getKeyPwd() {
				return null;
			}
		};
    	def licenseParam = new DefaultLicenseParam(licenseConfig.license.subject, peferences, publicKeyStoreParam, cipherParam)
    	
    	return licenseParam
    }
    
}
