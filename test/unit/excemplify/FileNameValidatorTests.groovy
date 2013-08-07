/* ===================================================
 * Copyright 2010-2013 HITS gGmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ========================================================== */
package excemplify

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*
import groovy.util.GroovyTestCase
import org.hits.ui.FileNameValidator


/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class FileNameValidatorTests extends GroovyTestCase{

    void setUp() {
        // Setup logic here
def test=FileNameValidator.validateRawDataFileName("psmad2_santa_cruz_blot1_")
println test
    }

    void tearDown() {
        // Tear down logic here
    }

    void testProvedOrNot() {
//     fail "Implement me ha"
     assertEquals(FileNameValidator.validateRawDataFileName("psmad2_santa_cruz_blot1_.xls"),true)

    }
}
