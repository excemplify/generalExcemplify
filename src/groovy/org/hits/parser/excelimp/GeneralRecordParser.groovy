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
import org.apache.poi.ss.usermodel.*
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.ByteArrayOutputStream

/**
 *
 * @author rongji
 */
class GeneralRecordParser implements Parser{
	
    Source source   
    Target target
    Action action
    String name
    String parsingway

    def blankcells
    def origWorkbook

 
    def knowledgeMap=[:]

    
    def configure(StateAndQueue state, Map configurations) throws ParserConfigException{
        // to configure the file which need to be parse in the state
        
        if (!state.state.workbook){    // if it is a file which correspond to the upload file
            
            origWorkbook=WorkbookFactory.create(state.state.file)     
            // origWorkbook=new HSSFWorkbook(new FileInputStream(state.state.file))
            state.state.workbook=origWorkbook
        }
        else {
            origWorkbook=state.state.workbook
        }
       
        
        println "state is $state and configs in parser config $configurations"
        
        
        this.action=GeneralCellParserAction.valueOf(configurations.action)
        
        
        this.parsingway=configurations.parsingWay
        
    
        if (configurations.sources.size()>1){
            
            println "configuring multi sources"
            
            source= new GeneralMultiExcelCellsSource(configurations.sources, state)
            
            switch(action){
          
     
            case GeneralCellParserAction.TRANSPOSELISTANDASSIGNLABEL.toString():
                source.setSourceType(SourceType.LISTINGANDLABEL);
                println "set label"
                break;              
            default:
                source.setSourceType(SourceType.LISTING);
                break;
            } 
        }
        else {
                
            println "configuring single source"
            source = new GeneralExcelCellSource(configurations.sources.first(), state)
        }
        
   
        // knowledgeMap=source.getKnowledgeMap()
     
        this.target=new TemplateExcelTarget(configurations.target,state) 
        //        println "source first row${source.getFirstRow()} first col ${source.getFirstColumn()}"
        //        println "target first row ${target.firstRow} first col ${target.firstColumn}"
       

    }
   
    
    StateAndQueue parse(StateAndQueue state) throws ParsingException{
        println "parsing spreadsheet now...${name}"
        /**
         *lei set the target during parsing   
         */
        println"pre traverse actions"
        int recordSize
        if( state.state.recordDiffs?.size()>0){
            List reclist=state.state.recordDiffs
            recordSize=reclist.size()
            target.setRecordDiffs(reclist)
            target.setCurRecordDiffs(reclist)
        }    
        target.setRowDiff(source.getFirstRow())
        target.setColDiff(source.getFirstColumn())
        target.setRowColDiff(source.getFirstColumn())
        target.setColRowDiff(source.getFirstRow())
        target.setCurRow(target.firstRow)
        target.setCurCol(target.firstColumn)
        action.setTarget(target) 
        if(recordSize)
        source.setRecordSize(recordSize)
        
        
        source.traverse(action.doAction)
        
        println"post traverse actions"
        if((state.state.recordDiffs?.size()>0)&&(target.recordDiffs?.size()>state.state.recordDiffs?.size())){
            println"reset recordDiffs ${target.recordDiffs}  size ${target.recordDiffs.size()}"
            state.state.recordDiffs=target.recordDiffs
        }
        else if(state.state.recordDiffs==null && target.recordDiffs?.size()>0){
        
            println"set recordDiffs ${target.recordDiffs}  size ${target.recordDiffs.size()}"
            state.state.recordDiffs=target.recordDiffs       
     
        }else{
            println "reuse existing record Diff size ${state.state.recordDiffs?.size()}"   
        }
    
        // state.state.blankcells=blankcells
        
        byte[] parsedBook
    

        def targetSheetName = target.sheet.getSheetName()+"(by Excemplify)"
  
        println "targetSheetName = $targetSheetName"
          origWorkbook=state.state.workbook
        //targetSheetName=targetSheetName+"_blot$blotNum"
        //targetSheetName=target.sheet.getWorkbook().setSheetName(target.sheet.getWorkbook().getSheetIndex(targetSheetName), "${targetSheetName}_${blotNum}")
        def targetSheet=origWorkbook.getSheet(targetSheetName)
        if (targetSheet==null)
        targetSheet=origWorkbook.createSheet(targetSheetName)
        
        targetSheet=copyRows(target.sheet,targetSheet) //actually copies cell by cell, can't just copy the whole sheet
        // it might be easier to copy the cells from the blank template to a new sheet from the original workbook
        //set that as the target sheet
        //state.state.workbook=origWorkbook //to make sure we don't overwrite old changes //but now there's a problem with the sheets
           File f
        try {
         
            FileOutputStream fileOut  
            if(origWorkbook instanceof XSSFWorkbook){
                println "it is a xssf workbook"
         
                f=new File("${System.nanoTime().toString()}tempworkbook.xlsx")
                fileOut = new FileOutputStream(f);
                println "start write outstream"
                origWorkbook.write(fileOut);
                fileOut.close();
               
                println "finish write outstream" 
                String fileName=f.getName()
                println"fileName $fileName"
                FileInputStream inputstream=new FileInputStream(fileName)
               state.state.workbook = new XSSFWorkbook(inputstream);
            
                inputstream.close()
                println "renewd"
             
            }else if(origWorkbook instanceof HSSFWorkbook){
                println "it is a hssf workbook"
                f=new File("${System.nanoTime().toString()}tempworkbook.xls")
                fileOut = new FileOutputStream(f);
                println "start write outstream"
                origWorkbook.write(fileOut);
                fileOut.close();
             
                println "finish write outstream"
            }
                
     
            //  ByteArrayOutputStream outstream = new ByteArrayOutputStream()
    
           
            //parsedBook=outstream.toByteArray()
            parsedBook=f.getBytes()
           
            
        }catch(Exception e){
            println e
        }
       
        state.state.parsedFile=parsedBook
        state.state.success=true
        println "return"
         f.delete()
        return state
     
    }
    
    
    
    def setSource(Source source){
        this.source=source
    }  
  
    def setTarget(Target target){
        this.target=target
    }
    
    def setAction(Action action){
        this.action=action
    }
    
    def parseCell(cell){
        // println "here SetupExcelParser parseCell"
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
                blankcells = true
                // defaultErrorHandler(cell)
                //cell.setCellStyle(colourCell)
                return null
           
            }
        }
    }
    def copyRows(Sheet origSheet, Sheet toSheet){
        println "copyRows"
        def iterator = origSheet.rowIterator()
        while (iterator.hasNext()){
            Row row = iterator.next()
            if(row!=null){
                Row toRow=toSheet.getRow(row.getRowNum())?:toSheet.createRow(row.getRowNum())
                def cellIt=row.cellIterator()
                if(cellIt!=null){
                    while (cellIt.hasNext()){
                        Cell oldCell= cellIt.next()
                        if(oldCell!=null && parseCell(oldCell)!=null){
                            if (!toRow.getCell(oldCell.getColumnIndex())) {
                                Cell toCell = toRow.createCell(oldCell.getColumnIndex())
                                //  println "${parseCell(oldCell)}"
                                toCell.setCellValue(parseCell(oldCell))
              
                            }
                        }
                    }
                
                }
            }
          
        }
             
        return toSheet
    }   

}

