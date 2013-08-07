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
--}%
<%@ page import="org.hits.ui.Knowledge" %>
<!doctype html>
<html>
  <head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" href="${resource(dir:'css',file:'index.css')}" />
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery-1.5.2.js')}"></script>
    <!--jQuery UI-->
    <script type="text/javascript" src="${createLinkTo(dir:'jquery-ui/ui', file:'jquery-ui.min.js')}"></script>
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
    <!--/jQuery UI-->
    <!--jQuery Table Sort-->
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.tablesorter.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.tablesorter.pager.js')}"></script>
    <!--/jQuery Table Sort-->
    <script type="text/javascript">
      window.onload = function(){$("#knowledgeList").tablesorter( ).tablesorterPager({container: $("#pager")});}
          function backToLab(){
     
          document.location.href ="${createLink(uri:'/lab')}"
         }

    </script>
  <g:set var="entityName" value="${message(code: 'knowledge.label', default: 'Knowledge')}" />

  <title>General Excemplify</title>
  <link rel="shortcut icon" href="${resource(dir: 'images/ui', file: 'logo.ico')}" type="image/x-icon">
</head>


<body>
<g:render template="/ui/header"/>
<div class="maincontent">
  <div class="buttoncontent">
     <span class="navspan" onclick="backToLab()">  <img alt="Back To User Home" src="${createLinkTo(dir: 'images/ui', file: 'home2.png')}" title="Back To User Home" />Back To User Home
 </span>
  </div>
  <br/>

  <div style="padding:1em; text-align: right"  > 
      <p class="title">${templateName} Knowledge Details</p>   
         <div class="tablecontent" >
      <table id="knowledgeList" class="tablesorter" >
        <thead>
          <tr>
            <th>Id</th>
            <th>Knowledge Name</th>
            <th>Corresponding Column Name</th>
            <th>Which Action To Take?</th>
            <th>Mark CellRange</th>
          </tr>
        </thead>
        <tbody>

        <g:each in="${knowledgeInstanceList}" status="i" var="knowledgeInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
            <td>${i+1}</td>
            <td>${fieldValue(bean: knowledgeInstance, field: "knowledgeName")}</td>
            <td>${fieldValue(bean: knowledgeInstance, field: "columnName")}</td>
            <td>${fieldValue(bean: knowledgeInstance, field: "action")}</td>
            <td>${fieldValue(bean: knowledgeInstance, field: "markCellRange")}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <div id="pager" class="pager" style="height:50px;right: 7%" >
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
  </div>
</div>
<br/>
<br/>
<!--  <div style="position:fixed" >
    <a  style="cursor: pointer" href="${createLink(uri: '/lab')}"><h3>Back</h3></a>
  </div>
</div>-->
</div>
<g:applyLayout name="foot">
</g:applyLayout>
</body>
</html>
