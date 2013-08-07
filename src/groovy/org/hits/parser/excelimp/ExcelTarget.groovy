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
import org.apache.poi.ss.usermodel.Sheet

/**
 *
 * @author jongle
 */
class ExcelTarget implements Target{
	
    String targetString
    CellRangeAddress targetCRA
    Sheet sheet
    int curRow
    int curCol
    int rowDiff
    int colDiff
    int rowColDiff    //target's first row compare to source first col
    int colRowDiff    //target's first col compare to source first row
    int firstRow,firstColumn,lastRow,lastColumn
    int size
    List recordDiffs
     List curRecordDiffs
    
    public ExcelTarget(Map configs,StateAndQueue state){
        targetCRA=new CellRangeAddress(configs.firstRow,configs.lastRow,configs.firstColumn,configs.lastColumn)
        this.firstRow=targetCRA.getFirstRow()
        this.firstColumn=targetCRA.getFirstColumn()
        this.curRow=targetCRA.getFirstRow()
        this.curCol=targetCRA.getFirstColumn()
        this.lastRow=targetCRA.getLastRow()
        this.lastColumn=targetCRA.getLastColumn()
        targetString=targetCRA.formatAsString()
        println targetString
        println state
        if (state.state.targetsheet==null){
            sheet=state.state.sheet.getWorkbook().createSheet("nextstage")
            state.state.targetsheet=sheet
            //state.state.sheet=sheet.getWorkbook().getSheetAt(state.state.targetsheetnum-1)
            println "targetsheet set at:${state.state.targetsheet}"
            
        }
        else{
            println "targetsheet already in state ${state.state.targetsheet}"
            // sheet=state.state.sheet.getWorkbook().getSheetAt(state.state.targetsheetnum)
            sheet=state.state.targetsheet
        }
        this.size=targetCRA.getNumberOfCells()
        println "${targetString} target size ${size}"
        
    }
    
    public ExcelTarget(String targetString, StateAndQueue state){
        
        targetCRA=CellRangeAddress.valueOf(targetString)
        this.firstRow=targetCRA.getFirstRow()
        this.firstColumn=targetCRA.getFirstColumn()
        this.lastRow=targetCRA.getLastRow()
        this.lastColumn=targetCRA.getLastColumn()
        this.curRow=targetCRA.getFirstRow()
        this.curCol=targetCRA.getFirstColumn()
      
        if (state.state.targetsheet==null){
            sheet=state.state.sheet.getWorkbook().createSheet("nextstage")
            state.state.targetsheet=sheet
            //state.state.sheet=sheet.getWorkbook().getSheetAt(state.state.targetsheetnum-1)
            println "targetsheet set at:${state.state.targetsheet}"
            
        }
        else{
            println "targetsheet already in state ${state.state.targetsheet}"
            // sheet=state.state.sheet.getWorkbook().getSheetAt(state.state.targetsheetnum)
            sheet=state.state.targetsheet
        }
        this.size=targetCRA.getNumberOfCells()
        println "${targetString} target size ${size}"
        
    }
    
    def resetTarget(Map configs){
        println "resetting target"
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
    def setRecordDiffs(List diffs){
        this.recordDiffs=diffs
    }
       def setCurRecordDiffs(List diffs){
        this.curRecordDiffs=diffs
    }
    def setCurRow(int curRow){
        this.curRow=curRow

             
    }
    def setCurCol(int curCol){
        this.curCol=curCol
 
    }
   
    def setRowColDiff(int sourceFIrstCol){
        this.rowColDiff=firstRow-sourceFirstCol 
        
    }
    def setColRowDiff(int sourceFirstRow){
        this.colRowDiff=firstColumn-sourceFirstRow
    }
    def setColDiff(int sourceFirstCol){
        this.colDiff = firstColumn-sourceFirstCol
   
    }
    
    def setRowDiff(int sourceFirstRow){
        this.rowDiff=firstRow-sourceFirstRow

    }
    
    def getSize(){
        return this.size
    }
}

