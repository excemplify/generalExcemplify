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
import org.hits.parser.SourceDef

import org.apache.poi.ss.usermodel.*
import org.hits.ui.exceptions.OutOfConsiderationException

/**
 *
 * @author rongji
 */
class GeneralMultiExcelCellsSource implements Source{
    List<Source> sources
    SourceType sourceType //this tells us how to handle the multiple sources 
    //          whether to merge them together or outerproduct etc
    int firstRow
    int firstColumn
    int lastRow
    int lastColumn
    String autoFindDirection
    
    
    def knowledgeMap=[:]
    
    public GeneralMultiExcelCellsSource(SourceDef sourceDef, StateAndQueue state){
        sources=[new GeneralExcelCellSource(sourceDef,state)]
    }
       
    public GeneralMultiExcelCellsSource(List<SourceDef> sourceDefs, StateAndQueue state){
        sources=[]
        log.info "sourceDef $sourceDefs"
        
        sourceDefs.eachWithIndex{it,n->
            def source
            println it.class
            println it.properties
            
            if (it.resourceType==ResourceType.CURRWORKBOOK.toString()){ //in this case we want the experiment workbook as the source, not the uploaded file
                def instream = new ByteArrayInputStream(state.state.experimentWorkbookFile)
                Workbook experimentWorkbook=WorkbookFactory.create(instream)
                state.state.experimentWorkbook=experimentWorkbook
                
                source=new GeneralExcelCellSource(it,experimentWorkbook,state)
            }
            else{  //source spreadsheet is the uploaded file
                source = new GeneralExcelCellSource(it,state)
            }
            println "setting up sources"
            sources<<source
            if (source.getKnowledge().size()>0){
                knowledgeMap.put((n), source.getKnowledge().first()) //first because at the moment we only have one item per source
            }
            println "multisource knowledgeMap $knowledgeMap"
        }
        def autoDirections=sources.collect{it.getAutoFindDirection()} as Set
        if(autoDirections.size()>1){
            println "you have multi sources but with different auto find direction?? ${sources.collect{it.getAutoFindDirection()}}"
            throw new OutOfConsiderationException()
        }
        
        this.autoFindDirection=sources.collect{it.getAutoFindDirection()}[0]
        
        this.firstRow=sources.collect{it.getFirstRow()}.min()
        
     
        this.firstColumn=sources.collect{it.getFirstColumn()}.min()
        this.lastRow=sources.collect{ it.getLastRow()}.max()
        this.lastColumn=sources.collect{ it.getLastColumn()}.max()

        if((this.autoFindDirection=="down")&&(this.lastRow==1)){
            this.lastRow=sources.collect{ it.getRecordSize()}.max()
            this.lastColumn=sources.collect{ it.getLastColumn()}.max()
        }else if((this.autoFindDirection=="right")&&(this.lastColumn==1)){
            this.lastColumn=sources.collect{ it.getRecordSize()}.max()  
            this.lastRow=sources.collect{ it.getLastRow()}.max()
        }
        
      

    }
    
    def setSourceType(SourceType type){
        sourceType=type
    }
    
    // sets all the sources in the list to the first cell
    def initializeSources(){
        sources.each{
            it.resetCellCounter()
        }
    }

    def traverse(action){ //how this is done depends on the action. actually quite fundamentally different ways of calling them
        println action
     
        // if (sourceType==SourceType.CONCAT||sourceType==SourceType.LISTING){
        //not all sources need to be of the same dimensions, but does 
        println"sources size ${sources.size()}"
        
        if (sourceType==SourceType.OUTERPRODUCT||sourceType==SourceType.CONCAT){ //matrix action      block   
            def cellLists=[]
            println "hello, traversing with the outerproduct or concat action"
        
            sources.each{ source->
            
                def cellList=[]
                for (int i=source.firstRow; i<=source.lastRow;i++){
                
                    Row row = source.sheet.getRow(i)
                    if(row){
                        for (int j=source.firstColumn; j<=source.lastColumn;j++){

                            Cell cell = row.getCell(j)
                            if(sourceType==SourceType.OUTERPRODUCT){
                                if(cell&&cell.getCellType()!=Cell.CELL_TYPE_BLANK){
                                    cellList.add(cell)    
                                }
                            }else{ //allow null cell
                                cellList.add(cell)  
                            }
                        }
                    }
                }
                cellLists<<cellList

            }
            println "finish sources cellLists $cellLists"   
           
            action.call(cellLists)
        }else{  //cell action, record
        
            def cellList=[]   
            def   origCellList=[]
            def  origCellListValue=[]//the best it can until it runs out of cells in all sources
            boolean allEmpty=false
            while (!allEmpty){
                cellList=[]
                origCellList=[]
                origCellListValue=[]
               
                sources.each{
                    if(sourceType==SourceType.LISTING||sourceType==SourceType.LISTINGANDLABEL){
                        if(this.autoFindDirection=="down"){
                            it.setLastRow(lastRow)   //set all sources to the same dimension   
                        }
             
                        else{
                            it.setLastColumn(lastColumn)   //set all sources to the same dimension   
                        }
                    }
              
                    def nextCell=it.getNextCell(this.autoFindDirection)
                
                    def nextCellCopy=nextCell
                
                    if (nextCellCopy!=null){
                        if(sourceType==SourceType.LISTINGANDLABEL){
                            def value
                            def origValue="${nextCell.getStringCellValue()}"
                            value="${it.labelInfo}  ${nextCell.getStringCellValue()}"                                      
               
                            if(value!=null && value!="${it.labelInfo}  "&& value!="${it.labelInfo}  null"){

                                nextCell.setCellValue(value)  
                                origCellList<<nextCell
                                origCellListValue<< origValue
                            }
                       
                        }
                  
                        cellList<<nextCell

                    }else{
                        println "null nextCell"
                    }
                }
                if (cellList.size()==0) allEmpty=true
                else {
                 
                    action.call(cellList)
                    println "cellList $cellList"
                
                }
            
                origCellList.eachWithIndex{cell, index->
                    cell.setCellValue(origCellListValue[index])
                }
            }
        }
        
        println "finish multi source traversing"
    }
    
    def getFirstRow(){
        return this.firstRow
    }
   
    def getFirstColumn(){
        return this.firstColumn
    }

    def setSourcesSameLength(){
        sources.each{
            if(this.autoFindDirection=="down"){
                if (it.lastRow<this.lastRow) {
                    it.resetSize("lastRow":this.lastRow)
                }
            }else if(this.autoFindDirection=="right"){
                if (it.lastColumn<this.lastColumn) {
                    it.resetSize("lastColumn":this.lastColumn)
                } 
            }
        }
    }
    
}
