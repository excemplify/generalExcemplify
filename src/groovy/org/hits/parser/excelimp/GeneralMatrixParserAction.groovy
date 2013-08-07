
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

package org.hits.parser.excelimp

import org.hits.parser.core.Action
import org.apache.poi.ss.usermodel.Cell
import org.hits.parser.core.Source
import org.hits.parser.core.Target
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Row
import java.lang.Enum

/**
 *
 * @author rongji
 */

public enum GeneralMatrixParserAction implements Action{  //action is called by matrix of cells 
    
   

    

    
    MERGE_COLUMNS_BLOCK("MERGE_COLUMNS_BLOCK"),

    OUTERPRODUCT_COLUMNS_BLOCK("OUTERPRODUCT_COLUMNS_BLOCK")
    
   
    
    
    String action
    
    Target target
    
    GeneralMatrixParserAction (String action){
        this.action=action
    }
    
    public static  GeneralMatrixParserAction getByActionName(String action){
        return   GeneralMatrixParserAction.valueOf(action);
    }
    
    def getAction(){
    
        return action
    }
    def setTarget(Target target){
        this.target=target
       
    }
    def parseCell(cell){
        // println "here SetupExcelParser parseCell"
        if(cell){
            if (cell.class=="String".class) return cell
            else{
                switch (cell.getCellType()) {
                case cell.CELL_TYPE_STRING :
                    return cell.getStringCellValue()
                case cell.CELL_TYPE_NUMERIC :
                    return cell.getNumericCellValue()
                case cell.CELL_TYPE_BOOLEAN :
                    return cell.getBooleanCellValue()
                case cell.CELL_TYPE_FORMULA :
                    return cell.getCellFormula()
                case cell.CELL_TYPE_ERROR :
                    return cell.getErrorCellValue()
                case cell.CELL_TYPE_BLANK:
                    // blankcells = true
                    // defaultErrorHandler(cell)
                    //cell.setCellStyle(colourCell)
                    return null
           
                }
            }
        }else{return null}
    }
    def doAction={cells->
        switch (action) {
        case "MERGE_COLUMNS_BLOCK":
            mergeColumns(cells)
            break;
        case "OUTERPRODUCT_COLUMNS_BLOCK":
            outerProduct(cells)
            break;     
        }
      
    }
    
    String toString(){
        return action
    }
    def mergeColumns={List cellLists ->
        int members=cellLists.size()
        List targetList
        cellLists.each{list->
            List listValue=[]
            for(int i=0; i<list.size(); i++){
                
                def value=parseCell(list.get(i))
                if(value!=null){
                    //println "value $value"
                    if(listValue!=null){
                       listValue<<value 
                    }
                    else{
                     listValue=[value]   
                    }
                    
                }
                else{
                    if(listValue!=null){
                     listValue<<" "   
                    }
                    
                    else{
                       listValue=[" "]  
                    }
                   
                }
               
            }
            if(targetList!=null)
            targetList << listValue
            else
            targetList=[listValue]
        }
       // println "same dimension lists $targetList"
        targetList= targetList.transpose()
        println "transpose lists $targetList"
        Sheet sheet = target.sheet
        targetList.each{targetCellValueList->
            Row row = sheet.getRow(target.curRow)
            if(row==null){
                row=sheet.createRow(target.curRow)
            }
            Cell targetCell=row.getCell(target.curCol)?:row.createCell(target.curCol)
            String targetCellValue=""
          targetCellValueList.each{it->
              targetCellValue=targetCellValue+" "+it.toString()
              
          }
            targetCell.setCellType(Cell.CELL_TYPE_STRING)
            targetCell.setCellValue(targetCellValue)
            
            target.setCurRow(target.curRow+1)
        }
        
        
    }
        
    def outerProduct={List cellLists ->  //flattens to one column
        //Sheet targetSheet=target.sheet
  
        println "celllists setup $cellLists"  

        
        def allcombos=cellLists.combinations()
          
        Sheet sheet = target.sheet
  
        allcombos.eachWithIndex{rowCombi,n->
            Row row = sheet.getRow(target.curRow)
            
            if(row==null){
                row=sheet.createRow(target.curRow)
            }
       
            Cell targetCell=row.getCell(target.curCol)?:row.createCell(target.curCol)
    
            println "target cords ${targetCell.getRowIndex()} ${targetCell.getColumnIndex()}"
            String targetCellValue=""
     
            rowCombi.each{
                if(parseCell(it)!=""&&parseCell(it)!=" "){
                    if (targetCellValue==""){
                        targetCellValue=parseCell(it)  
                    } 
                    else{
                        targetCellValue="${targetCellValue} | ${parseCell(it)}"
      
                    }
                       
                }
            }
  
            targetCell.setCellType(Cell.CELL_TYPE_STRING)
            targetCell.setCellValue(targetCellValue)
          
            target.setCurRow(target.curRow+1)
   
        }

        
          
    } 
  

}
