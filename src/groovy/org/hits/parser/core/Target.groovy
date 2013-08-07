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

package org.hits.parser.core

import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.usermodel.Sheet
/**
 *
 * @author jongle rongji
 */
interface Target {
    String targetString
    CellRangeAddress targetCRA
    Sheet sheet
    int rowDiff
    int colDiff
    int rowColDiff    //target's first row compare to source first col
    int colRowDiff    //target's first col compare to source first row
    int firstRow,firstColumn,lastRow,lastColumn
    int size
    int curCol
    int curRow
    List recordDiffs
    List curRecordDiffs
    def setCurRow(int curRow);
    def setCurCol(int curCol);
    def setColDiff(int sourceFirstCol);
    def setRowDiff(int sourceFirstRow);
    def setRowColDiff(int sourceFirstCol);
    def setColRowDiff(int sourceFirstRow);
    def setRecordDiffs(List diffs);
    def setCurRecordDiffs(List diffs);
}

