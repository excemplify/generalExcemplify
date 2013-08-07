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
package org.hits.ui

class Template {
    String templateName
    String type
    String purpose
    String parsingway
    boolean visible
  //  List knowledgeList
    // boolean containValidaion

    byte[] binaryFileData
    static hasMany=[knowledgeList:Knowledge]
    //    static hasMany=[constraint:Constraint]  
    //    static hasMany=[analyzeAction:Analyze]
    static constraints = {
 
        binaryFileData maxSize: 1024 * 1024 * 2 // 2MB
        templateName(unique:true)
        type(inList:["public","inner"])
       parsingway(blank: true, nullable: true)

    }

 
}
