/**
 * @author Nick Blanchard
 */
class LicenseFilters {

	def licenseService

	def filters = {
		// intercept necessary controllers here except license controller
		all {
			before = {
				if (controllerName != 'license' && !licenseService.getLicense()) {
					redirect(controller:"license")
					return false
				}
			}
		}
	}
}
