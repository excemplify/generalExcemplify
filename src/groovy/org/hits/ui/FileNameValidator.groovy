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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hits.ui
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *
 * @author rongji
 */
class FileNameValidator {
    
    static boolean validateRawDataFileName(String fileName){
        def proved=false
        def tokenList=fileName.tokenize(".")
        def suffix=tokenList.last()
        if(suffix=='txt'||suffix=='xls'||suffix=='xlsx'){
            def pattern = ~/(.+)(_blot\d+.?)(.?)/
            def content=tokenList.first()
            proved=pattern.matcher(content).matches()    
        }else{
            println "File formate other than .txt or .xls is not acceptted."
        }
     
        return proved
   
    }
    
     static boolean validateGelDataFileName(String fileName){   //simplified validation, to reject other strange file formate
        def proved=false
        def tokenList=fileName.tokenize(".")
        def suffix=tokenList.last()
        if(suffix=='xls'||suffix=='xlsx'){    
            proved=true 
        }else{
            println "File formate other than .xls or .xlsx is not acceptted."
        }
     
        return proved
   
    }

	
}

