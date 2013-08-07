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

import org.hits.parser.core.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory

import org.hits.parser.TargetDef
/**
 *
 * @author jongle
 */
class TemplateExcelTarget implements Target{
	
    String targetString
    CellRangeAddress targetCRA
    Sheet sheet
    int rowDiff
    int colDiff
    int rowColDiff
    int colRowDiff
    int firstRow,firstColumn,lastRow,lastColumn
    int size
    int curCol
    int curRow
    List recordDiffs
    List curRecordDiffs
    Workbook template
    
    
    public TemplateExcelTarget(TargetDef targetDef, StateAndQueue state){
       
        
        InputStream is = new ByteArrayInputStream(targetDef.template.binaryFileData)
        this.template = WorkbookFactory.create(is)
        this.targetCRA=CellRangeAddress.valueOf(targetDef.cellRange)     //rough CRA for the whole target block boundary, need to recauculate the row later according to the sourceType and parsingWay
        this.sheet=template.getSheetAt(targetDef.sheetNum)
        
        this.firstRow=targetCRA.getFirstRow()
        this.firstColumn=targetCRA.getFirstColumn()
        this.lastColumn=targetCRA.getLastColumn()
        this.lastRow=targetCRA.getLastRow()
        this.curRow=targetCRA.getFirstRow()
        this.curCol=targetCRA.getFirstColumn()
        this.targetString=targetDef.cellRange
        this.size=targetCRA.getNumberOfCells()
        this.recordDiffs=[]
              this.curRecordDiffs=[]
        println "target firstRow $firstRow firsColumn $firstColumn"
    }
    
    def setCurRow(int curRow){
        this.curRow=curRow
             
    }
    def setRecordDiffs(List diffs){
        this.recordDiffs=diffs
    }
     def setCurRecordDiffs(List diffs){
        this.curRecordDiffs=diffs
    }
    def setCurCol(int curCol){
        this.curCol=curCol

    }

    def setColDiff(int sourceFirstCol){
        this.colDiff = firstColumn-sourceFirstCol
    }
    
    def setRowDiff(int sourceFirstRow){
        this.rowDiff = firstRow-sourceFirstRow
    }
    def setRowColDiff(int sourceFirstCol){
        this.rowColDiff= firstRow-sourceFirstCol
    }
    def setColRowDiff(int sourceFirstRow){
        this.colRowDiff = firstColumn-sourceFirstRow
    }
    
    def getSize(){
        return this.size
    }
     
    def resetTarget(Map configs){
        println "resetting target CRA"
        if (configs.firstColumn) {
            this.targetCRA.setFirstColumn(configs.firstColumn)
            this.firstColumn=configs.firstColumn
        }
        if (configs.lastColumn) {
            this.targetCRA.setLastColumn(configs.lastColumn)
            this.lastColumn=configs.lastColumn
        }
        if (configs.firstRow) {
            this.targetCRA.setFirstRow(configs.firstRow)
            this.firstRow=configs.firstRow
        }
        if (configs.lastRow) {
            this.targetCRA.setLastRow(configs.lastRow)
            this.lastRow=configs.lastRow
        }
        this.targetString=targetCRA.formatAsString()
        this.size=targetCRA.getNumberOfCells()
        println "target $targetString"
        
       
    }
   
    def findLastColumnAndReset(int headerRowNumber){
        Row headers= sheet.getRow(headerRowNumber)
        int lastFilledColumn = headers.getLastCellNum()
        this.resetTarget(firstColumn:lastFilledColumn, lastColumn:lastFilledColumn)
    }
}

