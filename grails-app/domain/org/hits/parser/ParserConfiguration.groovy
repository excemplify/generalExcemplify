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


class ParserConfiguration {

    String action
    String parserType   //actually is the which parser to use, the parser name
    String name
    String nodeType    // may be now it is not that important
    List sources
    String parsingWay   // new added, let's try

    
    static hasMany=[sources :SourceDef]
    static hasOne=[target:TargetDef]
    //static belongsTo = [parserDef: ParserDef]
    static constraints = {
        parsingWay (inList:["recordbased", "blockbased"])    
        name(nullable:true)
    }
    
  
}
