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


<html>

  <body>


    <div class="body">
      <div>
        <table id="templateList" class="tablesorter">
          <thead>
            <tr>
  <!--          <util:remoteSortableColumn controller="template" action="list" property="id" update="listTemplateDivId"  title="${message(code: 'template.id.label', default: 'Id')}"/>
          <util:remoteSortableColumn controller="template" action="list"  property="templateName"  update="listTemplateDivId" title="${message(code: 'template.templateName.label', default: 'Template Name')}" />
          <util:remoteSortableColumn controller="template" action="list" property="binaryFileData"  update="listTemplateDivId" title="${message(code: 'template.binaryFileData.label', default: 'Download Template')}" />
          <util:remoteSortableColumn controller="template" action="list" property="knowledgeList"  update="listTemplateDivId" title="${message(code: 'template.knowledgeList.label', default: 'Knowledge List')}" />
              -->
              <th>Id</th>
              <th>Template Name</th>
              <th>Template Purpose</th>
              <th>Download Template</th>
              <th>Included Concepts</th>

            </tr>
          </thead>
          <tbody>

          <g:each in="${templateInstanceList}" status="i" var="templateInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
              <td>${i+1}</td>
            <td>${fieldValue(bean: templateInstance, field: "templateName")}</td>
             <td>${fieldValue(bean: templateInstance, field: "purpose")}</td>
            <td><g:link  controller="template"  class="menuButton"  action="download" id="${templateInstance.id}"><img src="${createLinkTo(dir:'images/ui', file:'download.png')}" alt="download template" > </g:link></td>
            <td>
            <g:if test="${templateInstance.knowledgeList?.size()>0}">
              <g:link controller="knowledge"  action="list" id="${templateInstance.id}">(${templateInstance.knowledgeList.size()})</g:link>
            </g:if>
            <g:else>
              no concepts mapping, need admin
            </g:else>

            </td>


            </tr>
          </g:each>
          </tbody>
        </table>
      </div>

    </div>
  </body>
</html>
