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

<%@ page import="java.text.SimpleDateFormat" %>



<html>


  <body>


    <div class="body"  style="height:600px; width:95%" >
          <div style="width:100%; max-height: 750px; padding:1em; overflow: auto " >
        <table id="publicExperimentList" class="tablesorter">
          <thead>
            <tr>
              <th>Id</th>
              <th>Experiment Name</th>
              <th>Creation Date</th>
              <th>Workbook</th>
              <th>Raw data files</th>
              <th>Export</th>
              <th>Update details (log)</th>
              <th>Author</th>
            </tr>
          </thead>
          <tbody>

          <g:each in="${publicExperimentInstanceList}" status="i" var="publicExperimentInstance">

            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
              <td>${i+1}</td>
              <td>${fieldValue(bean:publicExperimentInstance, field: "filename")}</td>
              <td><g:formatDate type="datetime" style="medium" date="${publicExperimentInstance.createdOn}" /></td>

            <td><g:link controller="experiment" class="menuButton" action="download" id="${publicExperimentInstance.id}"><img src="${createLinkTo(dir:'images/ui', file:'download.png')}" alt="download workbook" ></g:link></td>


            <td ><ul class="inline">
                <g:if test="${publicExperimentInstance.resources.size()>2}">  
                  <li> 
                  <g:link controller="experiment" class="menuButton" action="downloadRawData" id="${publicExperimentInstance.id}">
                    <img style="cursor: pointer" src="${createLinkTo(dir:'images/ui', file:'download.png')}" alt="download all raw data" ></g:link> 
                  (<font color="green"> ${publicExperimentInstance.resources.findAll{it.type=='rawdata'&& it.state=='active'}.size()}</font>|<font color="red">${publicExperimentInstance.resources.findAll{it.type=='rawdata' && it.state=='inactive'}.size()}</font>)
                  </li>
                </g:if>
                <g:else>
                  <li> 
                    (<font size="2" color="green"> ${publicExperimentInstance.resources.findAll{it.type=='rawdata'&& it.state=='active'}.size()}</font>|<font size="2" color="red">${publicExperimentInstance.resources.findAll{it.type=='rawdata' && it.state=='inactive'}.size()}</font> )
                  </li>
                </g:else>
              </ul>
            </td>
            <td> 
            <g:if test="${publicExperimentInstance.resources.size()>1}">
              <ul class="inline">
                <li> <g:link controller="experiment" class="menuButton" action="exportInto" id="${publicExperimentInstance.id}" params="[layout:'gelinspector']"><img style="cursor: pointer" src="${createLinkTo(dir:'images/ui', file:'export.png')}" alt="export into gelinspector" ></g:link>
                GelInspector
                </li>
                <li>
                <g:link controller="experiment" class="menuButton" action="exportInto" id="${publicExperimentInstance.id}" params="[layout:'other']">
                  <img  style="cursor: pointer" src="${createLinkTo(dir:'images/ui', file:'export.png')}" alt="export into other" ></g:link>
                Other?
                </li>
              </ul>
            </g:if>
            <g:else>
              no raw data yet
            </g:else>
            </td>
            <td><g:formatDate  type="datetime" style="medium" date="${publicExperimentInstance?.lastUpdate()}" /><g:link controller="experiment" class="menuButton" action="downloadlog" id="${publicExperimentInstance.id}"><img src="${createLinkTo(dir:'images/ui', file:'log.png')}" alt="check log file" ></g:link>
         
            </td>
            <td> 
${fieldValue(bean: publicExperimentInstance, field: "author")}
            </td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      <div id="pager" class="pager" align="right"  style="height:50px; right:5%; position:  fixed ">
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
