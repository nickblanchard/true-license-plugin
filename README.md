# true-license-plugin
Grails Plugin for True License

This plugin provides the ability to securely create, install and verify license for closed source grails applications. This plugin utilizes TrueLicense Library Collection (TLC).
This is an update to the original plugin created by Manohar Viswanathan

**Author:** Nick Blanchard (blanchard.nick 'at ' gmail.com)

## Installation

Add the following to your `grails-app/conf/BuildConfig`
```
…
plugins {
…
    compile ':true-license-plugin:1.1'
…
}
```

## Usage

* Generate license file:
```
grails generate-license
```
* Start app:
```
grails run-app
```
* Install/verify license at [http://localhost:8080/your_app/license]. If you try to access your app without installing license, you will be redirected to this page.

## Components

* /license-config/privateKey.store - private key file. This never gets included in your WAR. (You should generate your own private/public keys with _keytool_ command. see tips section below)
* /license-config/LicensePrivateConfig.properties - holds all necessary configuration to create a license such as issuer, holder, validity etc. Again not included in WAR
* /conf/publicCerts.store - public key file. This is included in WAR
* /conf/LicensePublicConfig.groovy - holds all necessary information required for the client. Included in WAR
* /conf/LicenseFilter - defines which controllers, actions need to be protected.
* /plugins/true-license-plugin-1.1/controller/LicenseController - Install, verify license
* /plugins/true-license-plugin-1.1/services/LicenseService - Install, verify license

## Tips

* To generate private key:
```
keytool -genkey -alias privatekey -keystore privateKeys.store
```
* To generate public key
```
keytool -export -alias privatekey -file certfile.cer -keystore privateKeys.store

keytool -import -alias publiccert -file certfile.cer -keystore publicCerts.store
```
* The system preferences are stored in the registry in windows and file system (/etc/.java/) in Linux. You may need appropriate permissions to be able to do this.
