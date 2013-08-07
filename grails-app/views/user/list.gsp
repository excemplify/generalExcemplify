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
<%@ page import="org.hits.parser.User" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
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

  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
  <title>General Excemplify</title>
  <link rel="shortcut icon" href="${resource(dir: 'images/ui', file: 'logo.ico')}" type="image/x-icon">
  <script type="text/javascript">
  window.onload = function(){

 refreshTableSorter();
  }
      function refreshTableSorter(){
      if ($("#userList").find("tr").size() > 1){ 
         $("#userList").tablesorter( ).tablesorterPager({container: $("#dpager2")});
         }
          if ($("#adminList").find("tr").size() > 1){ 
     $("#adminList").tablesorter( ).tablesorterPager({container: $("#dpager1")});
     }
        }
    
  
  </script>
</head>
<body>
<g:render template="/ui/header"/>
<div class="maincontent">
  <div class="nav" >
    <span class="menuButton"><a class="home" href="${createLink(uri: '/admin')}">Admin Home</a></span>
    <span class="menuButton"><g:link class="create" controller="secUser" action="create">New Admin User</g:link></span>
    <span class="menuButton"><g:link class="create" controller="user" action="create">New Experimenter</g:link></span>

  </div>
  <div class="messagecontent" id="resource" style="height:20px;">
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
  </div>

  <h1>Admin User</h1>
  <div style=" width:60%;height:20%; padding:1em;  text-align: right; overflow: auto" >
    <div>
      <table id="adminList" class="tablesorter" >
        <thead>
          <tr>
            <th>Id</th>
            <th>Username</th>
            <th>Password</th>
          </tr>
        </thead>
        <tbody>
        <g:each in="${adminInstanceList}" status="i" var="adminInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

            <td class="funtd" title="edit it"><g:link controller="secUser" action="show" id="${adminInstance.id}" >${i+1}</g:link></td>

          <td>${fieldValue(bean: adminInstance, field: "username")}</td>

          <td>********</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div id="dpager1" class="pager" style="right:5%" >
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
  <h1>Experimenters</h1>
  <div style="width:60%;height:60%; padding:1em;  overflow: auto; text-align: right;">
    <div>
      <table id="userList" class="tablesorter"  >
        <thead>
          <tr>

            <th>Id</th>
            <th>Username</th>
            <th>Password</th>
            <th>Name</th>
            <th>SEEK Email Address</th>

            <!--                        
<g:sortableColumn property="accountExpired" title="${message(code: 'user.accountExpired.label', default: 'Account Expired')}" />

<g:sortableColumn property="accountLocked" title="${message(code: 'user.accountLocked.label', default: 'Account Locked')}" />-->

          </tr>
        </thead>
        <tbody>
        <g:each in="${userInstanceList}" status="i" var="userInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

            <td class="funtd" title="edit it" ><g:link controller="user" action="show" id="${userInstance.id}">${i+1}</g:link></td>

          <td>${fieldValue(bean: userInstance, field: "username")}</td>

          <td>********</td>

          <td>${fieldValue(bean: userInstance, field: "name")}</td>
          <td>${fieldValue(bean: userInstance, field: "seekEmailAddress")}</td>

<!--                            <td><g:formatBoolean boolean="${userInstance.accountExpired}" /></td>

    <td><g:formatBoolean boolean="${userInstance.accountLocked}" /></td>-->

          </tr>
        </g:each>
        </tbody>
      </table>

    </div>
    <div id="dpager2" class="pager" style="right:5%" >
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
<g:applyLayout name="foot">
</g:applyLayout>
</body>
</html>
