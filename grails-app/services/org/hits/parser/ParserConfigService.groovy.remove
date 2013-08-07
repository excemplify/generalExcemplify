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

import org.hits.parser.core.*
import org.hits.parser.excelimp.*

class ParserConfigService {

    def addParserConfig(List<Map> sources, String action, Map target, String name, String nodeType, String parserType){
        ParserDef pdef = ParserDef.findByName(name)?:new ParserDef(name:name)
        def sourceDefList=[]
        sources.each{
            sourceDefList<<createSourceDef(it)
        }
        def targetDef = createTargetDef(target)
        ParserConfiguration pconfig = new ParserConfiguration(sources:sourceDefList,action:action,
            target:targetDef, name:name, nodeType:nodeType, parserType:"ImmunoExcelParser")
        pdef.parserConfigurations.add(pconfig).save(failOnError: true)
    }
    
    def getListParserTypes(){
        return ["ImmunoExcelParser"]
    }
    
    def saveFileAsTargetTemplate(File filename,String templateName){
        TemplateFile template = new TemplateFile(name:filename, binaryData:filename.getBytes()).save(failOnError:true)
    }
    
    private SourceDef createSourceDef(Map params){
        SourceDef sdef = new SourceDef("cellRange":params.cellRange,"sheetNum":params.sheetNum,"sourceType":params.sourceType)
        return sdef
    }
    
   
    
    private Target createTargetDef(Map params){
       TemplateFile template = TemplateFile.findByName(params.templateName)
//        TargetDef tdef = new TargetDef(cellRange:params.cellRange, sheetNum:params.sheetNum, template:template )
    }
    
    
}
