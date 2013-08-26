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

