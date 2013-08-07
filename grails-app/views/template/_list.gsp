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

<p class="title">All Stored Templates</p>
<div class="tablecontent" >

  <table id="templateList" class="tablesorter" >
    <thead>
      <tr>
        <th>Id</th>
        <th>Template Name</th>
        <th>Purpose</th>
        <th>Template Type</th>
        <th>Download Template</th>
        <th>Include Concepts</th>
        <th>Visible</th>
        <th></th>
      </tr>
    </thead>
    <tbody>

    <g:each in="${templateInstanceList}" status="i" var="templateInstance">
      <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
        <td>${i+1}</td>
        <td>${fieldValue(bean: templateInstance, field: "templateName")}</td>
        <td>${fieldValue(bean: templateInstance, field: "purpose")}</td>
        <td>${fieldValue(bean: templateInstance, field: "type")}</td>
        <td><g:link class="menuButton" action="download" id="${templateInstance.id}"><img src="${createLinkTo(dir:'images/ui', file:'download.png')}" alt="download resource" >download template</g:link></td>
      <td>   <g:if test="${templateInstance.knowledgeList?.size()>0}">
        <g:link controller="knowledge"  action="list" id="${templateInstance.id}">(${templateInstance.knowledgeList.size()})</g:link>
      </g:if>
      <g:else>
        no concepts mapping, need admin
      </g:else>  
      </td>

      <td>
      <g:if test="${(templateInstance.visible==false)&&(templateInstance.type=='public')}">
        <img style="cursor: pointer" onclick="Display('${templateInstance.id}')" src="${createLinkTo(dir:'images/ui', file:'invisible.png')}" alt="click to make your template visible to experimentalists" >

      </g:if> 
      <g:elseif test="${(templateInstance.visible==true)&&(templateInstance.type=='public')}">
        <img style="cursor: pointer" onclick="StopDisplay('${templateInstance.id}')" src="${createLinkTo(dir:'images/ui', file:'visible.png')}" alt="click to hide your template from experimentalists" >         
      </g:elseif>

      </td>

      <td> 
      <g:if test="${templateInstance.type=="public"}">
        <img style="cursor: pointer" onclick="DeleteTemplate('${templateInstance.id}','${templateInstance.templateName}')" src="${createLinkTo(dir:'images/ui', file:'trash.png')}" alt="delete such template" >
      </g:if>
      </td>
      </tr>
    </g:each>
    </tbody>
  </table>

</div>
<div id="dpager" class="pager" style="right:5%" >
  <g:form>

    <img src="${createLinkTo(dir:'images/ui', file:'first.png')}" class="first"/>
    <img src="${createLinkTo(dir:'images/ui', file:'prev.png')}" class="prev"/>
    <input style="width:80px"  type="text" class="pagedisplay"/>
    <img src="${createLinkTo(dir:'images/ui', file:'next.png')}" class="next"/>
    <img src="${createLinkTo(dir:'images/ui', file:'last.png')}" class="last"/>
    <select style="width:50px" class="pagesize">
      <option selected="selected" value="10">10</option>
      <option value="20">20</option>
      <option value="30">30</option>
      <option value="40">40</option>
    </select>
  </g:form>
</div>
