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
--}%<%@ page import="org.hits.parser.SecUser" %>
<!doctype html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="main" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'index.css')}" />
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery-1.5.2.js')}"></script>
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
  <g:set var="entityName" value="${message(code: 'secUser.label', default: 'SecUser')}" />
  <title>General Excemplify</title>
  <link rel="shortcut icon" href="${resource(dir: 'images/ui', file: 'logo.ico')}" type="image/x-icon">
</head>
<body>
<g:render template="/ui/header"/>
<div class="maincontent">
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/admin')}">Admin Home</a></span>
    <span class="menuButton"><g:link class="list" controller="user" action="list">User List</g:link></span>
    <span class="menuButton"><g:link class="create" action="create">New Admin User</g:link></span>
  </div>
  <div id="edit-secUser" class="body" role="main">
    <h1>Edit Admin User Details</h1>
    <g:if test="${flash.message}">
      <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${secUserInstance}">
      <ul class="errors" role="alert">
        <g:eachError bean="${secUserInstance}" var="error">
          <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
      </ul>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${secUserInstance?.id}" />
      <g:hiddenField name="version" value="${secUserInstance?.version}" />
      <div class="dialog">

        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="username"><g:message code="secUser.username.label" default="Username" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: secUserInstance, field: 'username', 'errors')}">
          <g:textField name="username" value="${secUserInstance?.username}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="password"><g:message code="secUser.password.label" default="Password" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: secUserInstance, field: 'password', 'errors')}">
          <g:textField name="password" value="${secUserInstance?.password}" />
          </td>
          </tr>



          <tr class="prop">
            <td valign="top" class="name">
              <label for="accountExpired"><g:message code="secUser.accountExpired.label" default="Account Expired" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: secUserInstance, field: 'accountExpired', 'errors')}">
          <g:checkBox name="accountExpired" value="${secUserInstance?.accountExpired}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="accountLocked"><g:message code="secUser.accountLocked.label" default="Account Locked" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean:secUserInstance, field: 'accountLocked', 'errors')}">
          <g:checkBox name="accountLocked" value="${secUserInstance?.accountLocked}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="enabled"><g:message code="secUser.enabled.label" default="Enabled" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: secUserInstance, field: 'enabled', 'errors')}">
          <g:checkBox name="enabled" checked="true" value="${secUserInstance?.enabled}" /> 
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="passwordExpired"><g:message code="secUser.passwordExpired.label" default="Password Expired" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: secUserInstance, field: 'passwordExpired', 'errors')}">
          <g:checkBox name="passwordExpired" value="${secUserInstance?.passwordExpired}" />
          </td>
          </tr>

          </tbody>
        </table>
      </div>

      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
      </div>

    </g:form>
  </div>
</div>
  <g:applyLayout name="foot">
  </g:applyLayout>
</body>
</html>
