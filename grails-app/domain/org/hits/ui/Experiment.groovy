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
import org.hits.parser.Spreadsheet

class Experiment extends Spreadsheet{
    
    //    String logFile
   // String share
   // List stages
    //List resources
    static hasMany=[resources:Resource,stages:Stage]   //uploaded by experimenter
    String setUpTemplateName
    //    String gelInspectorTemplateName="gelInspectorTemplate" //default value if the inner gelInspector template is used
    //    String rawDataTemplateName="rawdata_template.xls"  //default value if the inner rawdata template is used
    //    String rawDataTextTemplateName="rawTextDataTemplate" //default value if the inner rawdata  text template is used
    //    String loadingTemplateName="laneloading_template_new.xls" //default value if the inner loading template is used
    
    

    String type
    String contentType
    //    String topic
    
    static constraints = {
        //      share(inList:["public","private"])
        //   type(inList:["new", "performed"])
        // topic(nullable:true, inList:["Immunoblot", "FACS", "Protein Array","PCR","ELISA","Luminex"])
        filename(unique: ['type', 'author'])
        contentType(inList:["xls", "xlsx"])
       
        //        gelInspectorTemplateName blank: true, nullable: true
        //        rawDataTemplateName blank: true, nullable: true
        //        rawDataTextTemplateName blank: true, nullable: true
        //        loadingTemplateName blank: true, nullable: true
     

    }
    
}
