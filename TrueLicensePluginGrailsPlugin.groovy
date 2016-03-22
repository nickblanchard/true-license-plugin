/* Copyright 2007-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 /**
  * @author Nick Blanchard
  */
class TrueLicensePluginGrailsPlugin {
    def version = "1.1"
    def grailsVersion = "2.4 > *"
    def author = "Nick Blanchard"
    def authorEmail = "blanchard.nick@gmail.com"
    def title = "Provides license management to Grails application."
    def description = 'Utilizes TrueLicense Library Collection(TLC) to securely create, install and verify license for closed source products.'
    def documentation = "http://grails.org/plugin/true-license-plugin"
    def license = "APACHE"
    def issueManagement = [url: "https://github.com/nickblanchard/true-license-plugin/issues"]
    def scm = [url: "https://github.com/nickblanchard/true-license-plugin"]
}
