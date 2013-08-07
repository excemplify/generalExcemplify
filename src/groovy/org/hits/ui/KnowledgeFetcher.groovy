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
import org.hits.ui.exceptions.*
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.util.CellReference

/**
 *
 * @author rongji
 */
class KnowledgeFetcher {
    static List<Map> conceptFetcher(Template template, String knowledgeName) throws Exception{
        def conceptDetails=[]
        def pattern=/(\w+)(\d+)/
        def cellKnowledges=template.knowledgeList.findAll{it.knowledgeName==knowledgeName}
        if(!cellKnowledges){
                
            println "oops, $knowledgeName Concept is one of the mandatory concepts but it is missing in such Template"
            throw new ConceptMissingException("$knowledgeName concept is one of the mandatory concepts, but the mapping for it is missing");
                
        }else{
            cellKnowledges.each{Knowledge cellKnowledge->
                def conceptInfos=[:]
       
                def matcher=cellKnowledge.markCellRange=~ pattern
                println "matcher[0] ${matcher[0]}"
                def cols=matcher[0][1]  
                def rows=matcher[0][2] as int
                int nextRows=Integer.valueOf(rows)+1
                conceptInfos.put("sheetNum", "${cellKnowledge.sheetIndex}") 
                conceptInfos.put("label", "${cellKnowledge.columnName}" )
                if(cellKnowledge.action)
                conceptInfos.put("action", "${cellKnowledge.action}")
              
                if(cellKnowledge.markCellRange.indexOf(":")!=-1){
                    println"find :"
                    String upperleft=cellKnowledge.markCellRange.tokenize(":").first()
      
                    String downright=cellKnowledge.markCellRange.tokenize(":").last()
     
      
                    //        
                    def matcher2 = "$downright" =~ pattern
    
                    def endcols=matcher2[0][1]
                    int endrows= matcher2[0][2] as int
                  
                    if(cols==endcols){
                        println"assume it is vertical"
                   
                        conceptInfos.put("startCellRange", "$cols$nextRows")  
                        conceptInfos.put("autoFindDirection","down" )
                        if(nextRows==endrows){
                            endrows=endrows+1
                        }  
                        conceptInfos.put("cellRange", "$cols$nextRows:$endcols$endrows") 
                        println "cellrange is    $cols$nextRows:$endcols$endrows"
                    }else{  //never test for this case
                        if(rows==endrows){
                            println"assume it is horizonal"  
                            int nextColsNum=CellReference.convertColStringToIndex("$cols")+1
                            def nextCols=CellReference.convertNumToColString(nextColsNum)
                            conceptInfos.put("startCellRange", "$nextCols$rows")  
                            conceptInfos.put("cellRange", "$nextCols$rows:$endcols$endrows") 
                            conceptInfos.put("autoFindDirection","right" )
                        }else{
                            println"we have not clearly define this situation!!"
                            println "rows $rows endrows $endrows"
                        }
                    }            
                }else{
                    conceptInfos.put("startCellRange", "$cols$nextRows") //as default 
                }
                //                else{          
                //                    conceptStrings.put("iterativeStartCellRange", "$cols$nextRows:${cols}*")
                //                }
           
                conceptDetails<<conceptInfos
         
                
            }
        }
  

        println "concept details for concept $knowledgeName in template ${template.templateName} $conceptDetails"
        return conceptDetails
    }
    
    
    
    static boolean containKnowledge(Template template, List knowledgeNames){
        boolean flag=true
        knowledgeNames.each{k->
            println k
            Knowledge cellKnowledge=template.knowledgeList.find{it.knowledgeName==k}  
            if(!cellKnowledge){
                println " no knowledge found return null"
                flag=false
                
            }
        }
        
        return flag
          
      
    }

}

