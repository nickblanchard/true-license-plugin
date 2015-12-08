import java.io.File

/**
 * License controller
 *
 * @author Nick Blanchard
 */
class LicenseController {
	
	def licenseService
	
	// verify and display current license
    def index = { 
		def license = licenseService.getLicense() 
		[ license:license ]	
	}
	
	// install new license
	def install = {
		def licenseFile = request.getFile("licenseFile")
		File file = new File('a_file')
		licenseFile.transferTo(file)
		licenseService.installLicense(file)	
		file.delete()
		redirect(action:index)
	}
	
}
