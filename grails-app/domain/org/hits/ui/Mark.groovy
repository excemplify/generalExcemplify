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
package org.hits.ui

class Mark {

    String firstRow
    String lastRow
    String firstCol
    String lastCol
    String markCellRange //e.g. a1   a1:d3
    String sheetIndex
    String fileName
    String markColor

    static constraints = {
        markCellRange(unique: ['fileName', 'sheetIndex'])
    }
}
