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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="main" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'index.css')}" />
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery-1.5.2.js')}"></script>
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />

  <title>General Excemplify</title>
  <link rel="shortcut icon" href="${resource(dir: 'images/ui/', file: 'logo.ico')}" type="image/x-icon">
</head>
<body>
<g:render template="/ui/header"/>
<div class="maincontent">
  <div class="nav"  >
    <span class="menuButton"><a class="home" href="${createLink(uri: '/admin')}">Admin Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create">New Experimenter</g:link></span>
  </div>
  <div class="body" >
    <h1>Show Experimenter Details</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="user.id.label" default="Id" /></td>

        <td valign="top" class="value">${fieldValue(bean: userInstance, field: "id")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="user.username.label" default="Username" /></td>

        <td valign="top" class="value">${fieldValue(bean: userInstance, field: "username")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="user.password.label" default="Password" /></td>

        <td valign="top" class="value">${fieldValue(bean: userInstance, field: "password")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="user.name.label" default="Name" /></td>

        <td valign="top" class="value">${fieldValue(bean: userInstance, field: "name")}</td>

        </tr>
        <tr class="prop">
          <td valign="top" class="name"><g:message code="user.name.label" default="Seek Email Address" /></td>

        <td valign="top" class="value">${fieldValue(bean: userInstance, field: "seekEmailAddress")}</td>

        </tr>
        <tr class="prop">
          <td valign="top" class="name"><g:message code="user.accountExpired.label" default="Account Expired" /></td>

        <td valign="top" class="value"><g:formatBoolean boolean="${userInstance?.accountExpired}" /></td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="user.accountLocked.label" default="Account Locked" /></td>

        <td valign="top" class="value"><g:formatBoolean boolean="${userInstance?.accountLocked}" /></td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="user.enabled.label" default="Enabled" /></td>

        <td valign="top" class="value"><g:formatBoolean boolean="${userInstance?.enabled}" /></td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="user.passwordExpired.label" default="Password Expired" /></td>

        <td valign="top" class="value"><g:formatBoolean boolean="${userInstance?.passwordExpired}" /></td>

        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${userInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
      </g:form>
    </div>
  </div>

</div>
  <g:applyLayout name="foot">
  </g:applyLayout>
</body>
</html>
