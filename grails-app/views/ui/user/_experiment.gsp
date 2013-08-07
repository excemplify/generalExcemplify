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
<%@ page import="java.text.SimpleDateFormat" %>



<html>
  <body>


    <div class="body"  style="height:350px; width:100%" >
      <div  style="max-height: 350px; overflow: auto  ">
        <table id="experimentList" class="tablesorter">
          <thead>
            <tr>             
              <th>Task</th>
              <th>Creation Date</th>
              <th>Workbook</th>
 
              <th>Delete</th>
            </tr>
          </thead>
          <tbody>

          <g:each  in="${experimentInstanceList}" status="i" var="experimentInstance">

            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
              <td style=" white-space:pre-line; text-align: justify"><b>${i+1}. </b>${fieldValue(bean: experimentInstance, field: "filename")}</td>
<td><g:formatDate type="datetime" style="medium" date="${experimentInstance.createdOn}" /></td>

            <td><g:link controller="experiment" class="menuButton" action="download" id="${experimentInstance.id}"><img src="${createLinkTo(dir:'images/ui', file:'download.png')}" alt="download workbook" ></g:link></td>

            <td> 
              <img  style="cursor: pointer" onclick="DeleteExp('${experimentInstance.id}','${experimentInstance.filename}')" src="${createLinkTo(dir:'images/ui', file:'trash.png')}" alt="delete the whole experiment" >
            </td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      <div id="pager" class="pager" align="right"  style="height:50px; right:5%">
        <g:form>
          <img src="${createLinkTo(dir:'images/ui', file:'first.png')}" class="first"/>
          <img src="${createLinkTo(dir:'images/ui', file:'prev.png')}" class="prev"/>
          <input style="width:80px" type="text" class="pagedisplay"/>
          <img src="${createLinkTo(dir:'images/ui', file:'next.png')}" class="next"/>
          <img src="${createLinkTo(dir:'images/ui', file:'last.png')}" class="last"/>
          <select style="width:50px"  class="pagesize">
            <option selected="selected" value="10">10</option>
            <option value="20">20</option>
            <option value="30">30</option>
            <option value="40">40</option>
          </select>
        </g:form>
      </div>
    </div>
  </body>
</html>
