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
 * This source will be configured by giving a starting cell, we will assume the sourceString describes a single cell eg "a4"
 *  This cell is assumed to be the leftmost cell in the row of headers for the parser. Each column must have a header
 *    q? merged cells in for headlines -> give all the same label but with an iteration number
 *    q? need to set some kind of constraint for checking the rowEnd criteria -> must be in the config map, a closure
 *    q? assume that first column will define the number of rows we need (possibly extend to allow for selecting a column)
 */

package org.hits.parser.excelimp

import org.hits.parser.core.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Workbook
import org.hits.parser.extractExcel.PatchedPoi
import org.hits.parser.SourceDef
import org.hits.ui.Knowledge
import org.hits.ui.Template
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.hits.ui.exceptions.OutOfConsiderationException

/**
 *
 * @author rongji
 */
class GeneralExcelCellSource implements Source{
    
    String sourceString
    CellRangeAddress sourceCRA
    String autoFindDirection
    String labelInfo
    Sheet sheet
    int firstRow,lastRow,firstColumn,lastColumn
    int size
    int currRow,currCol //holds the row and index of the last cell we retrieved
    def knowledgeList=[]
    int recordSize
    boolean autoFind
    
    //we assume now we are only detecting the end of the column, not the number of columns
    // for non contiguous columns we should have more than one source
    public GeneralExcelCellSource (SourceDef sourceDef, StateAndQueue state){
        //this.sheet = state.state.sheet //we always get the sheet from the state?
       

        println "sheetNum: ${sourceDef.sheetNum}"
        println "sheetName: ${sourceDef.sheetName}"
        this.autoFind=false
        if(sourceDef.sheetName!=null)
        this.sheet=state.state.workbook.getSheet(sourceDef.sheetName)  //this could be problematic for sources requiring a different workbook to the one in the state
        //PatchedPoi.getInstance().clearValidationData(this.sheet)
        if (sheet==null) {    
            this.sheet=state.state.workbook.getSheetAt(sourceDef.sheetNum)
            if (sourceDef.sheetName && sourceDef.sheetName!=""){
                state.state.workbook.setSheetName(sourceDef.sheetNum,sourceDef.sheetName)
            }
        }
        println "the sourceDef sheet name ${sheet.getSheetName()}"
        println"sourceDef.cellRange $sourceDef.cellRange"
        this.sourceCRA = CellRangeAddress.valueOf(sourceDef.cellRange)
        this.firstRow=sourceCRA.getFirstRow()
        this.firstColumn=sourceCRA.getFirstColumn()
        this.lastColumn=sourceCRA.getLastColumn()
        this.lastRow=sourceCRA.getLastRow()
        this.labelInfo=sourceDef.knowledgeComment
        this.autoFindDirection=sourceDef.autoFindDirection?:"down"   //get the autoFindDirection, if it is no set then use derault "down"
      
        setKnowledgeList(sourceDef.template.knowledgeList as List)
        println "associated knowledge $knowledgeList"
        if(this.autoFindDirection=="down"){
            println "down direction"
            if(lastColumn==firstColumn){
                if (lastRow==firstRow){
                   
                    def newLastRow=findColumnEnd(sourceDef.template, firstColumn)
                    if(newLastRow)
                    this.lastRow=newLastRow //assuming we will count the first row    
            
                    println "find down, find last Row ${lastRow}"     
                    if(lastRow!=firstRow){
                        println "different from orignal last Row"    
                        this.autoFind=true
                        sourceCRA.setLastRow(lastRow)
                    }
          
                }
                this.recordSize=lastRow-firstRow
            }else{
                println"we do not consider this case with direction down"
                throw new OutOfConsiderationException()
            }
        }else if(this.autoFindDirection=="right"){
            println "right direction"
            if(lastRow==firstRow){
                if (lastColumn==firstColumn){
                   
                    def newLastColumn=findRowEnd(sourceDef.template, firstRow)
                    if(newLastColumn)
                    this.lastColumn=newLastColumn //assuming we will count the first row    
            
                    println "find right, find last Column ${lastColumn}"     
                    if(lastColumn!=firstColumn){
                        println "different from orignal last column"    
                        this.autoFind=true
                        sourceCRA.setLastColumn(lastColumn)
                    }
          
                }  
                this.recordSize=lastColumn-firstColumn
            }else{
                println "we do not consider this case with direction right"
                throw new OutOfConsiderationException()
            }
        }
   
  
        this.size= sourceCRA.getNumberOfCells()
        this.sourceString=sourceCRA.formatAsString()
        println "${sourceString} source size ${this.size}"
        
        resetCellCounter()
       
    }
    
    public GeneralExcelCellSource(SourceDef sourceDef, Workbook workbook, StateAndQueue state){
        println "with explicit defined workbook and its sourceDef sheetNum is ${sourceDef.sheetNum}"
        this.autoFind=false
        this.sheet=workbook.getSheet(sourceDef.sheetName) //if the number doesn't work try the name careful here, not the same for all parse
        if (sheet==null) {
            
            this.sheet=state.state.workbook.getSheetAt(sourceDef.sheetNum)
            if (sourceDef.sheetName&&sourceDef.sheetName!=""){
                state.state.workbook.setSheetName(sourceDef.sheetNum,sourceDef.sheetName)
            }
        }
        
        println "2.the sourceDef sheet name ${sheet.getSheetName()}"
        this.sourceCRA = CellRangeAddress.valueOf(sourceDef.cellRange)   
        this.firstRow=sourceCRA.getFirstRow()
        this.firstColumn=sourceCRA.getFirstColumn()
        this.lastColumn=sourceCRA.getLastColumn()
        this.lastRow=sourceCRA.getLastRow()
        
        setKnowledgeList(sourceDef.template.knowledgeList as List)
        println "associated knowledge $knowledgeList"
        
        if(this.autoFindDirection=="down"){
            if(lastColumn==firstColumn){
                if (lastRow==firstRow){
                   
                    def newLastRow=findColumnEnd(sourceDef.template, firstColumn)
                    if(newLastRow)
                    this.lastRow=newLastRow //assuming we will count the first row    
            
                    println "find down, find last Row ${lastRow}"     
                    if(lastRow!=firstRow){
                        println "different from orignal last Row"    
                        this.autoFind=true
                        sourceCRA.setLastRow(lastRow)
                    }
          
                }
                this.recordSize=lastRow-firstRow
            }else{
                println"we do not consider this case with direction down"
                throw new OutOfConsiderationException()
            }
        }else if(this.autoFindDirection=="right"){
            if(lastRow==firstRow){
                if (lastColumn==firstColumn){
                   
                    def newLastColumn=findRowEnd(sourceDef.template, firstRow)
                    if(newLastColumn)
                    this.lastColumn=newLastColumn //assuming we will count the first row    
            
                    println "find right, find last Column ${lastColumn}"     
                    if(lastColumn!=firstColumn){
                        println "different from orignal last column"    
                        this.autoFind=true
                        sourceCRA.setLastColumn(lastColumn)
                    }
          
                }  
                this.recordSize=lastColumn-firstColumn
            }else{
                println "we do not consider this case with direction right"
                throw new OutOfConsiderationException()
            }
        }
        
        //        if (lastRow==firstRow){
        //            def newLastRow=findColumnEnd(sourceDef.template, firstColumn)
        //            this.lastRow=newLastRow //assuming we will count the first row        
        //            println "find last Row ${lastRow}"   
        //            if(lastRow!=firstRow){
        //                println "different from orignal las Row"    
        //                this.autoFind=true
        //                sourceCRA.setLastRow(lastRow)
        //            }
        //               
        //          
        //        }
        
        this.size= sourceCRA.getNumberOfCells()
        this.sourceString=sourceCRA.formatAsString()
        
        println "${sourceString} source size ${this.size}"
        
        resetCellCounter()
       
    }
    
    def setLastRow(int synLastRow){
        this.lastRow=synLastRow
    }
    def setFirstRow(int synFirstRow){
        this.firstRow=synFirstRow
    }
    
    def setRecordSize(int size){
        this.recordSize=size
    }
    int getRecordSize(){
        return this.recordSize
    }
    def setSourceCRA(String sourceString){
        sourceCRA=CellRangeAddress.valueOf(sourceString)
        this.firstRow=sourceCRA.getFirstRow()
        this.firstColumn=sourceCRA.getFirstColumn()
        this.lastColumn=sourceCRA.getLastColumn()
        this.lastRow=sourceCRA.getLastRow()
    }
    
    def getSourceCRA(){
        return sourceCRA
    }
    
    def resetSize(Map configs){
        
        println "resetting expanding excel source"
        if (configs.firstColumn) {
            this.sourceCRA.setFirstColumn(configs.firstColumn)
            this.firstColumn=configs.firstColumn
        }
        if (configs.lastColumn) {
            this.sourceCRA.setLastColumn(configs.lastColumn)
            this.lastColumn=configs.lastColumn
        }
        if (configs.firstRow) {
            this.sourceCRA.setFirstRow(configs.firstRow)
            this.firstRow=configs.firstRow
        }
        if (configs.lastRow) {
            this.sourceCRA.setLastRow(configs.lastRow)
            this.lastRow=configs.lastRow
        }
        this.sourceString=sourceCRA.formatAsString()
        this.size=sourceCRA.getNumberOfCells()
      
        
       
   
    }
    
    private boolean isRowEmpty(Row row) {
        
        def empty=true
        if(row){
            for (int c = 0; c <= row.getLastCellNum(); c++) {
                Cell cell = row.getCell(c);
                if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK){
                    empty=empty&&false
                }
          
            }
       
        }
        return empty;
    }
    def findRowEnd(int rownumber){

        println "first column $firstColumn"
        int  lastNonEmptyColumn=firstRow
        Row currentRow=sheet.getRow(rownumber)
        if(currentRow){
            for (int c = firstColumn; c <= currentRow.getLastCellNum(); c++) {
                Cell cell = currentRow.getCell(c);
                if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK){
                    lastNonEmptyColumn=c+1
                }
          
            } 
        }
        return lastNonEmptyColumn
   
    }
    def findColumnEnd(int colnumber){
        int i=firstRow
        println "first row $firstRow"

        int  lastNonEmptyRow=firstRow
        def rowIterator = sheet.rowIterator()
        while(rowIterator.hasNext()) {
            def row = rowIterator.next()
            if(!isRowEmpty(row)){
                lastNonEmptyRow=row.getRowNum()+1 
            }
              
        }
        println "finding column end ${lastNonEmptyRow}"
        return lastNonEmptyRow
    }
    
    def findColumnEnd(Template template, int colnumber){  // if it does not be done in the knowledge fetcher stage
        int i=firstRow
        println "first row $firstRow current col $colnumber"
        int till
        int  lastEmptyRow=firstRow
        def rowIterator = sheet.rowIterator()
        while(rowIterator.hasNext()) {
            def row = rowIterator.next()
            if(!isRowEmpty(row)){
                lastEmptyRow=row.getRowNum()+1 
            }
              
        }
        if(sheet instanceof HSSFSheet){
            till=lastEmptyRow 
        }   
        else{
            till=lastEmptyRow-1
        }
        println "finding column end till ${till}"
        int j=0
        if(sheet.getRow(firstRow)!=null){
            Cell cell = sheet.getRow(firstRow).getCell(colnumber) 
            def origCellType = cell?.getCellType()
      
            while (i<=till ){
                def row=sheet.getRow(i)
                cell = row?.getCell(colnumber) //want the first data row not the header   
            
           
                if ( !isRowEmpty(row) ) {
                    j=i
                }
            
         
                i++ 
                if(template.knowledgeList.find{(it.firstRow==i)&&(it.firstCol==colnumber)}){
                    println "another new concept start"
                    break
                }
            }
     
        }
        return j
    }
    
    def resetCellCounter(){
        currCol=firstColumn
        currRow=firstRow
    }
    
    def getNextCell(String direction){  // this is used when the sources has the same direction
        //assuming a left to right then down direction
        println "currRow $currRow lastRow $lastRow currCol $currCol lastCol $lastColumn"
        if ((direction=="down")&&(currRow>lastRow)) return null
        else if ((direction=="right")&&(currCol>lastColumn)) return null 
        else{
    
            Row row=sheet.getRow(currRow)
            if(row!=null){
                Cell cell= row.getCell(currCol)
                
                if (cell==null){
                    println"null cell here"
                    if(direction=="down"){
                        currRow=currRow+1
                    }  else if(direction=="right"){
                        currCol= currCol+1
                    }
                    return null   
                } 
                else{
                    cell.setCellType(Cell.CELL_TYPE_STRING)
                    if(direction=="down"){
                      
                        if (currCol>=lastColumn ){
                            currCol=firstColumn
                            currRow=currRow+1
                        }
                        else {
                    
                            currCol=currCol+1  //lei has question with this
                        }
                    }else if(direction=="right"){
                        
                        println "right direction"
                        if ( currRow>=lastRow){
                            currRow=firstRow
                            currCol= currCol+1
                        }
                        else {
                    
                            currRow=currRow+1
                        }         
                    }
                    return cell    
                }
        
            }else{
                println "null row here"
                reutrn null
            }
        }
    }
  
    
    def traverse(action){
        
        println action
      
      
        int pleaseTraverse
        if(this.autoFindDirection=="down"){
            pleaseTraverse=sourceCRA.getLastRow()
            if(this.autoFind){
                println"different auto find the column end"
                if(sheet instanceof HSSFSheet)
                pleaseTraverse=sheet.getPhysicalNumberOfRows()  
                else
                pleaseTraverse=sheet.getPhysicalNumberOfRows()-1 
                if(pleaseTraverse<this.recordSize+sourceCRA.getFirstRow()){
            
                    println"set recordSize"
            
                    pleaseTraverse=this.recordSize+sourceCRA.getFirstRow()
                }
            }
        
            for (int i=sourceCRA.getFirstRow(); i<=pleaseTraverse;i++){
   
                Row row = sheet.getRow(i)
                if(row!=null){
                    for (int j=sourceCRA.getFirstColumn(); j<=sourceCRA.getLastColumn();j++){
                        println "coords ${i},${j}"
                        Cell cell = row.getCell(j)
                        currRow=i
                        currCol=j
                        action.call(cell)      
                    }
                }
            }
         
        }else if(this.autoFindDirection=="right"){
            Row row=sheet.getRow(sourceCRA.getFirstRow())
            pleaseTraverse=sourceCRA.getLastColumn()
            if(this.autoFind){
                println"different auto find the row end" 
                //Row row=sheet.getRow(sourceCRA.getFirstRow())
                pleaseTraverse=row.getPhysicalNumberOfCells()
          
                if(pleaseTraverse<this.recordSize+sourceCRA.getFirstColumn()){
            
                    println"set recordSize"
            
                    pleaseTraverse=this.recordSize+sourceCRA.getFirstColumn()
                }
            }    
            if(row!=null){
                int i=sourceCRA.getFirstRow()
                for (int j=sourceCRA.getFirstColumn(); j<=pleaseTraverse;j++){
                    println "coords ${i},${j}"
                    Cell cell = row.getCell(j)
                    currRow=i
                    currCol=j
                    action.call(cell)      
                    
                }
            }
        } 
       
        return sheet
                        
    }
    def getAutoFindDirection(){
        return this.autoFindDirection
    }
    
    def getFirstRow(){
        return sourceCRA.getFirstRow()
    } 
  
    def getFirstColumn(){
        return sourceCRA.getFirstColumn()
    }   
 
    def getSize(){
        return this.size
    }
    //  

  
    def setKnowledgeList(allknowledge){
        //iterate through columns to find out what we have in the list that is applicable for this source
        if(this.autoFindDirection=="down"){
            for (int i=firstColumn;i<=lastColumn;i++){
                def columnLabel=allknowledge.find{it.firstCol=="$i"}?.knowledgeName
                println "columnLabel $columnLabel"
                if (columnLabel!=null){
                    this.knowledgeList<<columnLabel
                }
            }
        }else if(this.autoFindDirection=="right"){
            for (int i=firstRow;i<=lastRow;i++){
                def columnLabel=allknowledge.find{it.firstRow=="$i"}?.knowledgeName
                println "columnLabel $columnLabel"
                if (columnLabel!=null){
                    this.knowledgeList<<columnLabel
                }
            }
        }
    }  
    
    
    def getKnowledge(){
        return this.knowledgeList
    }  
}

