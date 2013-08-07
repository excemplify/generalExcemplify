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

public enum GeneralCellParserAction implements Action{  //action is called cell by cell
    
    NOACTION("NO ACTION"),

    COPY("COPY"),  //make original copy 1*1->1*1
    
    
    TRANSPOSELIST("TRANSPOSELIST"),// (1+1+1+...)*1->1*n
    
    TRANSPOSELISTANDASSIGNLABEL("TRANSPOSELISTANDASSIGNLABEL"), // (labelcell+1+1+1+...)*1->1*n(with label assigned)
    
 
    MERGE("MERGE"),    // joined always by " "   2*n -> 1*n
    
    //SPLITBY("SPLITBY") //lei: hard, unless we defined explicitly split by what? or ...we guess the pattern
    
    
    COPYANDASSIGN("COPYANDASSIGN"), // use the unique value and assign it the target  1*1->1*n(repeat)
    
    ASSIGN("ASSIGN") // use the unique value and assign it the target  1(*)*1->1*n(repeat)
    
    
    String action
    
    Target target
    
    GeneralCellParserAction (String action){
        this.action=action
    }
    
    public static GeneralCellParserAction getByActionName(String action){
        return  GeneralCellParserAction.valueOf(action);
    }
    
    def getAction(){
    
        return action
    }
    def setTarget(Target target){
        this.target=target
       
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
                // blankcells = true
                // defaultErrorHandler(cell)
                //cell.setCellStyle(colourCell)
                return null
           
            }
        }
    }
    def doAction={cells->
        switch (action) {
        case "COPY":
            copy(cells);
            break;
        case "MERGE":
            merge(cells);
            break;   
        case "TRANSPOSELIST":
            transposelist(cells);
        case "TRANSPOSELISTANDASSIGNLABEL":
            transposelist(cells);
            break;
        case "COPYANDASSIGN":
            copyandassign(cells);
            break;
        case "ASSIGN":
            assign(cells);
            break;
        case "NO ACTION":
            println"no action for this concept is assigned"
            break;
        }
      
    }
    
    String toString(){
        return action
    }
    def assign={Cell cell ->
      
        Sheet sheet = target.sheet
        if(cell && parseCell(cell)!=null){
            println "value ${parseCell(cell)}"
            List curRecordDiffs=target.curRecordDiffs       
            int recordDiff=1
            int cur
                   
            if(curRecordDiffs!=null && curRecordDiffs.size()>=1){
                int sum=0
                curRecordDiffs.each{
                    sum=sum+it
                }
                recordDiff=sum  
            }
           
            
            for (int i=0;i<recordDiff;i++){
                // println"curRow ${target.curRow+i} curCol ${target.curCol}"
                Row row = sheet.getRow(target.curRow+i)
                if (row==null){
            
                    row=sheet.createRow(target.curRow+i)
                }
      
                if(parseCell(cell)!=null){
                    Cell targetCell=row.createCell(target.curCol)
                
                    targetCell.setCellValue(parseCell(cell)) 
                }
            }
  
            target.setCurRow(target.firstRow)
            target.setCurCol(target.firstColumn)
     
                
        }else{
            println "cell is null"
        }
    }
    
    def copyandassign={Cell cell ->
      
        Sheet sheet = target.sheet
        if(cell){
            // println"curRow ${target.curRow} curCol ${target.curCol}"    
            List curRecordDiffs=target.curRecordDiffs       
            int recordDiff=1
            int cur
   
            if(curRecordDiffs!=null && curRecordDiffs.size()>=1){
                List newlist
                if(curRecordDiffs.size()>1){
                    newlist=curRecordDiffs[1..curRecordDiffs.size()-1]  
                }else{
                    newlist=[curRecordDiffs[0]]
                }
              
                if( newlist.size()>=1){
                  
                    recordDiff=curRecordDiffs.first()  
                    target.setCurRecordDiffs(newlist) 
                }else{
                    target.setCurRecordDiffs(null)  
                }
            
                //  println"ok? size after remove ${ newlist.size()}"
        
               
            }else{
                
                target.setCurRecordDiffs(null)     
            }
            
            for (int i=0;i<recordDiff;i++){
            
                Row row = sheet.getRow(target.curRow+i)
                if (row==null){
            
                    row=sheet.createRow(target.curRow+i)
                }
      
                Cell targetCell=row.createCell(target.curCol)
                println parseCell(cell)
                targetCell.setCellValue(parseCell(cell))  
              
            }

            cur=target.curRow+recordDiff          
            target.setCurRow(cur)
     
                   println"curRow ${target.curRow} curCol ${target.curCol}"   
        }else{
            println "cell is null"
            
        }
    }
    
    def copy={Cell cell ->
      
        Sheet sheet = target.sheet
        if(cell){
            // println"curRow ${target.curRow} curCol ${target.curCol}"
            Row row = sheet.getRow(target.curRow)
            if (row==null){
            
                row=sheet.createRow(target.curRow)
            }
      
            Cell targetCell=row.createCell(target.curCol)
            println parseCell(cell)
            targetCell.setCellValue(parseCell(cell))
            
            List curRecordDiffs=target.curRecordDiffs       
            int recordDiff=1
            int cur
      
                   
            if(curRecordDiffs!=null && curRecordDiffs.size()>=1){
                List newlist
                if(curRecordDiffs.size()>1){
                    newlist=curRecordDiffs[1..curRecordDiffs.size()-1]  
                }else{
                    newlist=[curRecordDiffs[0]]
                }
              
                if( newlist.size()>=1){
                    target.setCurRecordDiffs(newlist) 
                    recordDiff=newlist.first()  
                }else{
                    target.setCurRecordDiffs(null)  
                }
            
                //  println"ok? size after remove ${ newlist.size()}"
               
            }else{
                
                target.setCurRecordDiffs(null)     
            }
            cur=target.curRow+recordDiff          
            target.setCurRow(cur)
     
                
        }else{
            println "cell is null"
        }
    }

    
    def merge={List<Cell> cells->
        //column difference measured from cell1
        Sheet targetSheet = target.sheet
 

        String targetCellVal=""
           
        cells.each{ targetCellVal="${targetCellVal}${parseCell(it)}"}
        println targetCellVal
      
       
        Row row = targetSheet.getRow(target.curRow)
        if (row==null){
            
            row=targetSheet.createRow(target.curRow)
        }
        Cell targetCell=row.createCell(target.curCol)
        targetCell.setCellValue(targetCellVal)
        target.setCurRow(target.curRow+1)
        List recordDiffs=target.recordDiffs
        recordDiffs << 1
        target.setRecordDiffs(recordDiffs)
    }
    

    
    def transposelist={List<Cell> cells->
        //column difference measured from cell1
        Sheet targetSheet = target.sheet
        def appendRow=0

        cells.each{
            if(it!=null && parseCell(it)!=null && parseCell(it)!=""){
                //  println"curRow ${target.curRow} curCol ${target.curCol}" 
                Row row = targetSheet.getRow(target.curRow+appendRow)
                if (row==null){
            
                    row=targetSheet.createRow(target.curRow+appendRow)
                }
                Cell targetCell=row.createCell(target.curCol)  
                String cellValue="${parseCell(it)}"
                //println cellValue
                targetCell.setCellValue(cellValue)
                appendRow++
            }
        }
        int cur=target.curRow+appendRow
        target.setCurRow(cur)
        List recordDiffs=target.recordDiffs
        recordDiffs << appendRow
        target.setRecordDiffs(recordDiffs)
       
    }    

    

}