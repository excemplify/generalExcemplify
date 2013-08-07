%{--===================================================
   Copyright 2010-2013 HITS gGmbH

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   ========================================================== 
--}%<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<span id="inlineMenu" style="display: none;">

  <span>
    <a href="#" onclick="sheetInstance.controlFactory.addRow(); return false;" title="Insert Row After Selected">
      <img alt="Insert Row After Selected" src="${resource(dir: 'images', file: 'sheet_row_add.png')}"/></a>
    <a href="#" onclick="sheetInstance.controlFactory.addRow(null, true); return false;" title="Insert Row Before Selected">
      <img alt="Insert Row Before Selected" src="${resource(dir: 'images', file: 'sheet_row_add.png')}"/></a>
    <a href="#" onclick="sheetInstance.controlFactory.addRow(null, null, ':last'); return false;" title="Add Row At End">
      <img alt="Add Row" src="${resource(dir: 'images', file: 'sheet_row_add.png')}"/></a>
    <a href="#" onclick="sheetInstance.controlFactory.addRowMulti(); return false;" title="Add Multi-Rows">
      <img alt="Add Multi-Rows" src="${resource(dir: 'images', file: 'sheet_row_add_multi.png')}"/></a>
    <a href="#" onclick="sheetInstance.deleteRow(); return false;" title="Delete Row">
      <img alt="Delete Row" src="${resource(dir: 'images', file: 'sheet_row_delete.png')}"/></a>
    <a href="#" onclick="sheetInstance.controlFactory.addColumn(); return false;" title="Insert Column After Selected">
      <img alt="Insert Column After Selected" src="${resource(dir: 'images', file: 'sheet_col_add.png')}"/></a>
    <a href="#" onclick="sheetInstance.controlFactory.addColumn(null, true); return false;" title="Insert Column Before Selected">
      <img alt="Insert Column Before Selected" src="${resource(dir: 'images', file: 'sheet_col_add.png')}"/></a>
    <a href="#" onclick="sheetInstance.controlFactory.addColumn(null, null, ':last'); return false;" title="Add Column At End">
      <img alt="Add Column At End" src="${resource(dir: 'images', file: 'sheet_col_add.png')}"/></a>
    <a href="#" onclick="sheetInstance.controlFactory.addColumnMulti(); return false;" title="Insert Multi-Columns">
      <img alt="Add Multi-Columns" src="${resource(dir: 'images', file: 'sheet_col_add_multi.png')}"/></a>
    <a href="#" onclick="sheetInstance.deleteColumn(); return false;" title="Delete Column">
      <img alt="Delete Column" src="${resource(dir: 'images', file: 'sheet_col_delete.png')}"/></a>
    <a href="#" onclick="sheetInstance.getTdRange(null, sheetInstance.obj.formula().val()); return false;" title="Get Cell Range">
      <img alt="Get Cell Range" src="${resource(dir: 'images', file: 'sheet_get_range.png')}"/></a>
<!--    <a href="#" onclick="sheetInstance.s.fnSave(); return false;" title="Save Sheets">
      <img alt="Save Sheet" src="../images/disk.png"/></a>-->
    <a href="#" onclick="sheetInstance.deleteSheet(); return false;" title="Delete Current Sheet">
      <img alt="Delete Current Sheet" src="${resource(dir: 'images', file: 'table_delete.png')}"/></a>
<!--    <a href="#" onclick="sheetInstance.calc(sheetInstance.i); return false;" title="Refresh Calculations">
      <img alt="Refresh Calculations" src="../images/arrow_refresh.png"/></a>-->
    <a href="#" onclick="sheetInstance.cellFind(); return false;" title="Find">
      <img alt="Find" src="${resource(dir: 'images', file: 'find.png')}"/></a>
    <a href="#" onclick="sheetInstance.cellStyleToggle('styleBold'); return false;" title="Bold">
      <img alt="Bold" src="${resource(dir: 'images', file: 'text_bold.png')}"/></a>
    <a href="#" onclick="sheetInstance.cellStyleToggle('styleItalics'); return false;" title="Italic">
      <img alt="Italic" src="${resource(dir: 'images', file: 'text_italic.png')}"/></a>
    <a href="#" onclick="sheetInstance.cellStyleToggle('styleUnderline', 'styleLineThrough'); return false;" title="Underline">
      <img alt="Underline" src="${resource(dir: 'images', file: 'text_underline.png')}"/></a>
    <a href="#" onclick="sheetInstance.cellStyleToggle('styleLineThrough', 'styleUnderline'); return false;" title="Strikethrough">
      <img alt="Strikethrough" src="${resource(dir: 'images', file: 'text_strikethrough.png')}"/></a>
    <a href="#" onclick="sheetInstance.cellStyleToggle('styleLeft', 'styleCenter styleRight'); return false;" title="Align Left">
      <img alt="Align Left" src="${resource(dir: 'images', file: 'text_align_left.png')}"/></a>
    <a href="#" onclick="sheetInstance.cellStyleToggle('styleCenter', 'styleLeft styleRight'); return false;" title="Align Center">
      <img alt="Align Center" src="${resource(dir: 'images', file: 'text_align_center.png')}"/></a>
    <a href="#" onclick="sheetInstance.cellStyleToggle('styleRight', 'styleLeft styleCenter'); return false;" title="Align Right">
      <img alt="Align Right" src="${resource(dir: 'images', file: 'text_align_right.png')}"/></a>
    <a href="#" onclick="sheetInstance.contentFind(); return false;" title="Find">
      <img alt="Good Luck Marker" src="${resource(dir: 'images', file: 'sheet_auto_mark.png')}"/></a>
    <!--				<a href="#" onclick="sheetInstance.fillUpOrDown(); return false;" title="Fill Down">
					<img alt="Fill Down" src="../images/arrow_down.png"/></a>
				<a href="#" onclick="sheetInstance.fillUpOrDown(true); return false;" title="Fill Up">
					<img alt="Fill Up" src="../images/arrow_up.png"/></a>-->
    <!--				<span class="colorPickers">
					<input title="Foreground color" class="colorPickerFont" style="background-image: url('../images/palette.png') ! important; width: 16px; height: 16px;"/>
					<input title="Background Color" class="colorPickerCell" style="background-image: url('../images/palette_bg.png') ! important; width: 16px; height: 16px;"/>
				</span>-->

  </span>


</span>
<span id="sourceInlineMenu" style="display: none;">

  <span>

    <a href="#" onclick="sheetInstance.cellFind(); return false;" title="Cell Find">
      <img alt="Find" src="${resource(dir: 'images', file: 'find.png')}"/></a>

    <a href="#" onclick="sheetInstance.contentFind(); return false;" title="Area Find">
      <img alt="Good Luck Marker" src="${resource(dir: 'images', file: 'sheet_auto_mark.png')}"/></a>

  </span>


</span>