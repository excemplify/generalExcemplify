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
package org.hits.parser

import java.util.zip.ZipOutputStream  
import java.util.zip.ZipEntry  
import java.nio.channels.FileChannel  
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.*;



import org.hits.ui.Resource

class AuxillarySpreadsheetService {
    


    def collateDataFiles(datafiles,prefix) {   
        def workbook = new HSSFWorkbook()
        FormulaEvaluator evaluatortarget= workbook.getCreationHelper().createFormulaEvaluator();
        workbook.setForceFormulaRecalculation(true);
        println workbook
        
        datafiles.each{resource->
            
            InputStream bis = new ByteArrayInputStream(resource.binaryData) 
            
            org.apache.poi.ss.usermodel.Workbook datafileworkbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(bis)
           
            datafileworkbook.setForceFormulaRecalculation(true);
            
            FormulaEvaluator evaluator = datafileworkbook.getCreationHelper().createFormulaEvaluator();
            evaluator.evaluateAll();
           
            if(evaluator){
                println "evaluator $evaluator"
            }
            
            if(datafileworkbook instanceof XSSFWorkbook){
                println "XSSFWorkbook"
            }else if(datafileworkbook instanceof HSSFWorkbook){
                println "HSSFWorkbook"
            }
            
            if(datafileworkbook){
                def sheetNum=datafileworkbook.getNumberOfSheets()
            
                for (int i=0; i< sheetNum;i++){
                    println "come to Nr. $i sheet"
                    println "${resource.fileName}${datafileworkbook.getSheetAt(i).getSheetName()}"
                    
                    def tempName
                    def sheetName
                           
                    if(resource.type=="setup"){
                            
                        sheetName="${datafileworkbook.getSheetAt(i).getSheetName()}"
                            
                    }else if(resource.type=="gelinspector"){
                        if(resource.fileName.length()>30){
                            // if(resource.fileName =~ /^.*blot.*[0-9].*$/){
                            if(resource.fileName.toLowerCase().indexOf("gel")==-1){
                                
                                tempName="${datafileworkbook.getSheetAt(i).getSheetName()}"
                            }else{
                                if(resource.fileName.toLowerCase().indexOf("blot")!=-1){
                                    int ind=resource.fileName.toLowerCase().indexOf("blot") 
                                    tempName="Gel Inspector ${resource.fileName.substring(ind, resource.fileName.length())}"
                                }else{
                                    tempName="Gel Inspector  ${resource.fileName.substring(resource.fileName.length()-10, resource.fileName.length())}"
                                
                                }}
                               
                        }else{
                            if(resource.fileName.toLowerCase().indexOf("gel")==-1){
                                
                                tempName="${datafileworkbook.getSheetAt(i).getSheetName()}"
                            }else{
                                tempName="${resource.fileName}${datafileworkbook.getSheetAt(i).getSheetName()}"
                            }
                        }
                        sheetName=tempName  
                      
                    }else{
                        sheetName="${resource.fileName}"
                        if(i>0){
                            sheetName="${resource.fileName}${datafileworkbook.getSheetAt(i).getSheetName()}"   
                        }
                    }
                    try{
                        copySheet(datafileworkbook,i,prefix,sheetName,workbook,evaluator)  
                    }catch(java.lang.IllegalArgumentException ex){
                        println "sheet name confliction"
                        sheetName="${resource.fileName}${datafileworkbook.getSheetAt(i).getSheetName()}"
                       // sheetName="Confliction Name Resolver $i"
                        copySheet(datafileworkbook,i,prefix,sheetName,workbook,evaluator)  
                    }
                }
                
    
            }
          
            
        }
        
        def outstream = new ByteArrayOutputStream()
        
        workbook.write(outstream)
        return outstream.toByteArray()
    }
    
   
    
    
    
    def createGelInspectorZip(byte[] workbookBytes){
        
        def bis = new ByteArrayInputStream(workbookBytes)      
        def workbook = WorkbookFactory.create(bis)
        //iterate thru sheets.
        def gelInspectorWBs=[]
        for (int i=0; i<workbook.getNumberOfSheets();i++){
            if (workbook.getSheetAt(i).getSheetName() =~ /Gel Inspector/){
                def newWorkbook = new HSSFWorkbook()      
                copySheet(workbook,i,"", workbook.getSheetAt(i).getSheetName(),newWorkbook,null)
                //                def file = new File("${System.getProperty("user.home")}","${workbook.getSheetAt(i).getSheetName()}.xls")
                def file = new File("/tmp","${workbook.getSheetAt(i).getSheetName()}.xls")
                println "xls file path ${file.absolutePath}"
                def fos = new FileOutputStream(file)
                newWorkbook.write(fos)
                gelInspectorWBs<<file
            }
        }
        //        String zipFileName = "${System.getProperty("user.home")}/GelInspectorFiles.zip"  
           String zipFileName = "GelInspectorFiles.zip"  
       // String zipFileName = "/tmp/GelInspectorFiles.zip"  
        def thezipfile = new File(zipFileName)
        ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFileName))  
        gelInspectorWBs.each{ file -> 
            
            zipFile.putNextEntry(new ZipEntry(file.getName()))  
           
            if( file.isFile() ){
                zipFile << new FileInputStream(file)
                
            }  
            zipFile.closeEntry()  
            file.delete()
        }
        zipFile.close()  
        return thezipfile
    }
    
    private copySheet(sourceworkbook, sheetnum, prefix,filename,targetworkbook,evaluator){
        
        println "copySheet $targetworkbook   $sheetnum"
        def sourcesheet = sourceworkbook.getSheetAt(sheetnum)
        
        if (sourcesheet!=null){
            println "sourcesheet is not null"
            def rowIt = sourcesheet.rowIterator()
            if(rowIt && rowIt.hasNext()){
                def targetSheet=targetworkbook.createSheet("$prefix$filename")
           
                while (rowIt.hasNext()){
                    Row sourceRow = rowIt.next()
                    Row targetRow = targetSheet.createRow(sourceRow.getRowNum())
                    def cellIt = sourceRow.cellIterator()
                    while (cellIt&&cellIt.hasNext()){
                        Cell sourceCell = cellIt.next()
                        Cell targetCell = targetRow.createCell(sourceCell.getColumnIndex())
                        copyCellAttributes(sourceCell,targetCell,evaluator)
                    }
                }
            }
        }
        
    }
    
    private copyCellAttributes(Cell source, Cell target, FormulaEvaluator evaluator){
        
        target.setCellType(source.getCellType())
        
        CellStyle sourceStyle = source.getCellStyle()
      
        // def sourceVal= evaluator.evaluate(source);
       
        //  CellValue sourceCellValue = evaluator.evaluate(source)
        def  sourceVal = parseCell(source, evaluator)
        // println "sourceVal $sourceVal"
    
        if (sourceVal!=null){
            if(source.getCellType().equals(source.CELL_TYPE_FORMULA)){
                target.setCellFormula(sourceVal) 
            }else{
                target.setCellValue(sourceVal)  
            }
     
       
        }
    }
    
    
    private parseCell(Cell cell, FormulaEvaluator evaluator ){
   
        switch (cell.getCellType()) {
        case cell.CELL_TYPE_STRING :
            return cell.getStringCellValue()
        case cell.CELL_TYPE_NUMERIC :
            return cell.getNumericCellValue()
        case cell.CELL_TYPE_BOOLEAN :
            return cell.getBooleanCellValue()
        case cell.CELL_TYPE_FORMULA :
            println "meet formula ${cell.getCellFormula()} evaluator ${evaluator.evaluate(cell)}"
            return cell.getCellFormula()
        case cell.CELL_TYPE_ERROR :
            return cell.getErrorCellValue()
        case cell.CELL_TYPE_BLANK:
            return null
    
      
           
        }
        
    }
}
