import java.text.SimpleDateFormat
import java.util.prefs.Preferences

import javax.security.auth.x500.X500Principal
import de.schlichtherle.license.DefaultCipherParam
import de.schlichtherle.license.DefaultKeyStoreParam
import de.schlichtherle.license.KeyStoreParam
import de.schlichtherle.license.LicenseContent
import de.schlichtherle.license.LicenseManager
import de.schlichtherle.license.DefaultLicenseParam
import de.schlichtherle.license.LicenseParam
import de.schlichtherle.license.LicenseNotary

includeTargets << grailsScript("_GrailsInit")

target(generateLicense: "The description of the script goes here!") {
    // TODO: Implement script here
	println "Generating License"
	
	try{
		ClassLoader parent = getClass().getClassLoader()
		GroovyClassLoader loader = new GroovyClassLoader(parent)
		Properties properties = new Properties()
//		FileInputStream infile = new FileInputStream("${basedir}/grails-app/conf/license-config/LicensePrivateConfig.properties")
		
		println "Attempting to load private properties"
		FileInputStream infile = new FileInputStream("${basedir}/license-config/LicensePrivateConfig.properties")
		properties.load(infile)
		infile.close()
		println "Loaded private properties"
		def licenseConfig = new ConfigSlurper().parse(properties)
		
		def sdf = new SimpleDateFormat("MM-dd-yyyy kk:mm:ss")
		
		def createLicenseContent = { LicenseParam licenseParam ->
			println "Creating license content"
			LicenseContent result = new LicenseContent()
			X500Principal holder = new X500Principal(licenseConfig.license.holder)
			result.setHolder(holder)
			X500Principal issuer = new X500Principal(licenseConfig.license.issuer)
			result.setIssuer(issuer)
			result.setConsumerAmount(licenseConfig.license.consumerAmount?.toInteger())
			result.setConsumerType(licenseConfig.license.consumerType)
			result.setInfo(licenseConfig.license.info)
			result.setIssued(licenseConfig.license.issueDate? sdf.parse(licenseConfig.license.issueDate) : new Date())
			if (licenseConfig.license.notAfter) {
				result.setNotAfter(sdf.parse(licenseConfig.license.notAfter))
			} else {
				def validDate = Calendar.getInstance()
				validDate.add(Calendar.DAY_OF_YEAR, licenseConfig.license.notAfterDays? licenseConfig.license.notAfterDays.toInteger():0)
				validDate.add(Calendar.HOUR, licenseConfig.license.notAfterHours? licenseConfig.license.notAfterHours.toInteger():0)
				result.setNotAfter(validDate.time)
			}
			result.setNotBefore(licenseConfig.license.notBefore? sdf.parse(licenseConfig.license.notBefore) : null)
			result.setSubject(licenseParam.getSubject())
			return result
		}
		
		def cipherParam = new DefaultCipherParam(licenseConfig.license.cipherPassword)
		def peferences = Preferences.systemNodeForPackage(getClass())
//		def privateKeyPath = "${basedir}/grails-app/conf/license-config/${licenseConfig.license.privateKeyFile}"
		def privateKeyPath = "${basedir}/license-config/${licenseConfig.license.privateKeyFile}"
		println "privateKeyPath - " + privateKeyPath
		
//		def privateKeyStoreParam = new DefaultKeyStoreParam(parent.getClass(), privateKeyPath, licenseConfig.license.privateKeyAlias, licenseConfig.license.storePassword, licenseConfig.license.keyPassword)
		def privateKeyStoreParam = new KeyStoreParam() {
			public InputStream getStream() throws IOException {
				final String resourceName = privateKeyPath;
				final InputStream instream = new FileInputStream(resourceName);
				if (instream == null) {
					println "Could not load file: " + resourceName;
					throw new FileNotFoundException(resourceName);
				}
				return instream;
			}
			public String getAlias() {
				return licenseConfig.license.privateKeyAlias;
			}
			public String getStorePwd() {
				return licenseConfig.license.storePassword;
			}
			public String getKeyPwd() {
				return licenseConfig.license.keyPassword;
			}
		};
	
		println "privateKeyStoreParam.alias - " + privateKeyStoreParam.getAlias()
		def licenseParam = new DefaultLicenseParam(licenseConfig.license.subject, peferences, privateKeyStoreParam, cipherParam)
		LicenseNotary licenseNotary = new LicenseNotary(privateKeyStoreParam)
		
		LicenseManager lm = new LicenseManager(licenseParam)
		def lc = createLicenseContent(licenseParam)
		lm.store(lc, licenseNotary, new File(licenseConfig.license.output))
	
		
		println "License file ${licenseConfig.license.output} generated"
		println "---------------"
		println "License details"
		println "---------------"
		println "Subject: ${lc.subject}"
		println "Holder: ${lc.holder}"
		println "Issuer: ${lc.issuer}"
		println "Issued: ${lc.issued}"
		println "Valid not after: ${lc.notAfter}"
		println "Valid not before: ${lc.notBefore}"
		println "Info: ${lc.info}"
		println "Consumer type: ${lc.consumerType}"
		println "# Consumer: ${lc.consumerAmount}"
	} catch (Exception e){
		println "Unable to store license"
		println e.printStackTrace()
	}
}

setDefaultTarget(generateLicense)
