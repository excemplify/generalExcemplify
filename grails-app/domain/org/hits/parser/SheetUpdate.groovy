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
package org.hits.parser

class SheetUpdate {

    
    Date dateUpdated
    String entityName //filename+version
    String fileNameVersion
    String state
    String comment
    static constraints = {
        comment(maxSize:1000, blank: true, nullable: true)
        state( blank: true, nullable: true)  
        entityName (blank: true, nullable: true)
        fileNameVersion (blank: true, nullable: true)
    }
    
    static belongsTo = [spreadsheet:Spreadsheet]
}
