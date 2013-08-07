/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hits.parser.extractExcel

import org.apache.poi.hssf.usermodel.HSSFSheet
/**
 *
 * @author rongji
 * 
 *    validationList.add(new ValidationImpl(validation.getConstraint().getFormula1(), this, address.getFirstColumn(), address.getLastColumn(), address.getFirstRow(), address.getLastRow()));
      
 */
class Validation {
    String list;

   String  sheet;

    int fromColumn;

    int toColumn;

    int fromRow;

    int toRow;
    
    String[] values
    
    def Validation(list, sheet, fromCol, toCol, fromRow, toRow){
        this.list = list;
        this.sheet = sheet;
        this.fromColumn = fromCol;
        this.toColumn = toCol;
        this.fromRow = fromRow;
        this.toRow = toRow;
    }
    def addValidation(list, sheet, fromCol, toCol, fromRow, toRow){
        this.list = list;
        this.sheet = sheet;
        this.fromColumn = fromCol;
        this.toColumn = toCol;
        this.fromRow = fromRow;
        this.toRow = toRow;
    }
	
}

