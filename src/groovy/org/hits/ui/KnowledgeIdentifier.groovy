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
import org.apache.poi.ss.util.CellReference

/**
 *
 * @author rongji
 */
class KnowledgeIdentifier {
    static boolean inMarkCellRange(String referenceRange, String unknownLocation){
        boolean inRange=false
        def pattern=/(\w+)(\d+)/
        def matcher3 = "$unknownLocation" =~ pattern     
        println matcher3[0]
        int col=CellReference.convertColStringToIndex("${matcher3[0][1]}")
        int row= matcher3[0][2] as int
        
        if(referenceRange.indexOf(":")!=-1){
            
            String upperleft=referenceRange.tokenize(":").first()
      
            String downright=referenceRange.tokenize(":").last()
     
            def matcher = "$upperleft" =~ pattern
    
            int startcol=CellReference.convertColStringToIndex("${matcher[0][1]}")
            int startrow= matcher[0][2] as int
            //        
            def matcher2 = "$downright" =~ pattern
    
            int endcol=CellReference.convertColStringToIndex("${matcher2[0][1]}")
            int endrow= matcher2[0][2] as int
     
       
            if(((col>=startcol)&&(col<=endcol))&&((row>=startrow)&&(row<=endrow))){
                inRange=true
            }
        }else{
            println "auto case"
            println referenceRange
            def matcher = "$referenceRange" =~ pattern 
            int startcol=CellReference.convertColStringToIndex("${matcher[0][1]}")
            int startrow= matcher[0][2] as int
            println "reference col $startcol row $startrow drop at $col $row"
              if((col==startcol)&&(row>=startrow)){
                inRange=true
            }
            
            
        }
        println "inRange: $inRange"
        return inRange
        
     
    }	
}

