
//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'Ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
// Ant.mkdir(dir:"C:\projects/license/grails-app/jobs")
//

Ant.property(environment:"env")
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"


Ant.mkdir(dir:"${basedir}/license-config")
Ant.copy(file:"${pluginBasedir}/scripts/license-config/privateKeys.store", todir:"${basedir}/license-config")
Ant.copy(file:"${pluginBasedir}/scripts/license-config/LicensePrivateConfig.properties", todir:"${basedir}/license-config")
Ant.copy(file:"${pluginBasedir}/scripts/license-config/publicCerts.store", todir:"${basedir}/grails-app/conf")
Ant.copy(file:"${pluginBasedir}/scripts/LicensePublicConfig.groovy", todir:"${basedir}/grails-app/conf")
Ant.copy(file:"${pluginBasedir}/scripts/LicenseFilters.groovy", todir:"${basedir}/grails-app/conf")

