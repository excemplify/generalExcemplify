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
 * 
 */

package org.hits.parser.excelimp

import org.hits.parser.core.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Cell

/**
 *
 * @author jongle
 */
class ExcelSource implements Source{
    
    String sourceString
    CellRangeAddress sourceCRA
    Sheet sheet
    int firstRow,lastRow,firstColumn,lastColumn
    int size
    
    public ExcelSource(String sourceString, StateAndQueue state){
        this.sourceCRA=CellRangeAddress.valueOf(sourceString)
        println sourceCRA.getFirstRow()
        this.firstRow=sourceCRA.getFirstRow()
        this.firstColumn=sourceCRA.getFirstColumn()
        this.lastRow=sourceCRA.getLastRow()
        this.lastColumn=sourceCRA.getLastColumn()
        this.sourceString=sourceString
        
        this.sheet = state.state.sheet
        
        this.size= sourceCRA.getNumberOfCells()
        
        println "${sourceString} source size ${this.size}"
    }
    
    def setSourceCRA(String sourceString){
        sourceCRA=CellRangeAddress.valueOf(sourceString)
    }
    
    def getSourceCRA(){
        return sourceCRA
    }
    
    def traverse(Closure action){
        
        println "traversing..."
        
      //  sheet=unmergeRegions(sheet)
        
        for (int i=sourceCRA.getFirstRow(); i<=sourceCRA.getLastRow();i++){
                Row row = sheet.getRow(i)
                if (row){
                 for (int j=sourceCRA.getFirstColumn(); j<=sourceCRA.getLastColumn();j++){
                    println "coords ${i},${j}"
                    Cell cell = row.getCell(j)
                    action.call(cell)
                    } 
                }
        }
       return sheet
                        
    }
    
  def getFirstRow(){
      return sourceCRA.getFirstRow()
  } 
  
  def getSize(){
      return this.size
  }
  
 def unmergeRegions(sheet){
     for (int i=0;i<sheet.getNumMergedRegions();i++){
         println "unmerging region $i"
          def cra=sheet.getMergedRegion(i)
          println cra.toString()
          Cell firstCell = sheet.getRow(cra.getFirstRow()).getCell(cra.getFirstColumn())
          for (int k=cra.getFirstColumn();k<=cra.getLastColumn();k++){
                
                    Cell cell = sheet.getRow(cra.getFirstRow()).getCell(k)
                    cell.setCellValue(firstCell.getStringCellValue())
                    println cell.getStringCellValue()
                }
            
            sheet.removeMergedRegion(i)
          }
         return sheet 
        }
   
}

