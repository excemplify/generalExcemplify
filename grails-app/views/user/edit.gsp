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
    <div class="nav">
      <span class="menuButton"><a class="home" href="${createLink(uri: '/admin')}">Admin Home</a></span>
      <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
      <span class="menuButton"><g:link class="create" action="create">New Experimenter</g:link></span>
    </div>
    <div class="body">
      <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${userInstance}">
        <div class="errors">
          <g:renderErrors bean="${userInstance}" as="list" />
        </div>
      </g:hasErrors>
      
      <g:form method="post" >
        <g:hiddenField name="id" value="${userInstance?.id}" />
        <g:hiddenField name="version" value="${userInstance?.version}" />
        <div class="dialog">
          <table>
            <tbody>

              <tr class="prop">
                <td valign="top" class="name">
                  <label for="username"><g:message code="user.username.label" default="Username" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'username', 'errors')}">
            <g:textField name="username" value="${userInstance?.username}" />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="password"><g:message code="user.password.label" default="Password" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'password', 'errors')}">
            <g:textField name="password" value="${userInstance?.password}" />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="name"><g:message code="user.name.label" default="Name" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'name', 'errors')}">
            <g:textField name="name" value="${userInstance?.name}" />
            </td>
            </tr>
   <tr class="prop">
              <td valign="top" class="name">
                <label for="seekEmailAddress"><g:message code="user.name.label" default="Seek Email Address" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'seekEmailAddress', 'errors')}">
            <g:textField name="seekEmailAddress" value="${userInstance?.seekEmailAddress}" />
            </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="accountExpired"><g:message code="user.accountExpired.label" default="Account Expired" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'accountExpired', 'errors')}">
            <g:checkBox name="accountExpired" value="${userInstance?.accountExpired}" />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="accountLocked"><g:message code="user.accountLocked.label" default="Account Locked" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'accountLocked', 'errors')}">
            <g:checkBox name="accountLocked" value="${userInstance?.accountLocked}" />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="enabled"><g:message code="user.enabled.label" default="Enabled" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'enabled', 'errors')}">
            <g:checkBox name="enabled" value="${userInstance?.enabled}" />
            </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="passwordExpired"><g:message code="user.passwordExpired.label" default="Password Expired" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'passwordExpired', 'errors')}">
            <g:checkBox name="passwordExpired" value="${userInstance?.passwordExpired}" />
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
