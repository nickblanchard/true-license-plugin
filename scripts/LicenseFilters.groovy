/**
 * License Filter
 *
 * @author Nick Blanchard
 */
class LicenseFilters {

	def licenseService
	
    def filters = {
	   // intercept necessary controllers here except license controller		
       all(controller:'*', action:'*') {
    	  before = {
    		if (controllerName == 'license' || licenseService.getLicense()) {
    		  return true
    		} else {
    		  redirect(controller:"license")
    	      return false
    		}
          }
       }
  
    }
  
}