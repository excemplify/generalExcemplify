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

<%@ page import="org.hits.parser.Spreadsheet" %>
<%@ page import="org.hits.parser.SheetUpdate" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'spreadsheet.label', default: 'Spreadsheet')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
         <r:require modules="uploadr"/>
         <r:layoutResources/>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${spreadsheetInstance}">
            <div class="errors">
                <g:renderErrors bean="${spreadsheetInstance}" as="list" />
            </div>
            </g:hasErrors>
            <uploadr:add name="uploadr"  path="payload/" direction="up"  action="uploadrAction" controller="spreadsheet"/>
            <r:layoutResources/>
            <g:form method="post" >
                <g:hiddenField name="id" value="${spreadsheetInstance?.id}" />
                
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                              <td valign="top" class="name"/>
                                  <label for="comment"><g:message code="sheetUpdate.comment.label" default="Change description" /></label>
                                  <td valign="top" class="value ${hasErrors(bean: sheetUpdate, field: 'comment', 'errors')}">
                                    <g:textArea name="comment" rows="5" cols="40"/>
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
    </body>
</html>
