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

import org.hits.ui.Template

class SourceDef {

   
    String cellRange
    int sheetNum
    String sheetName
    Template template
    String knowledgeComment  //labelinfo
    String resourceType
    String sourceType
    String autoFindDirection
   
    
    
    static constraints = {
        knowledgeComment (blank: true, nullable: true)
        resourceType(inList: ["CURRWORKBOOK", "SIMPLE"])    // whether the parsing resource is a original uploading file or the existing workbook in the queue
        sheetName (blank: true, nullable: true)
        sourceType (blank: true, nullable: true)
        autoFindDirection (blank: true, nullable: true, inList: ["down", "right"])
    }
    
}
