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
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'spreadsheet.label', default: 'Spreadsheet')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'spreadsheet.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="createdOn" title="${message(code: 'spreadsheet.createdOn.label', default: 'Date Uploaded')}" />
                            
                            <g:sortableColumn property="dateUpdated" title="${message(code: 'sheetUpdate.dateUpdated.label', default: 'Last Updated')}" />
                        
                            <g:sortableColumn property="filename" title="${message(code: 'spreadsheet.filename.label', default: 'Filename')}" />
                        
                            <g:sortableColumn property="download" title="${message(code: 'spreadsheet.download.label', default: 'Download file')}" />
                           
                        
                        </tr>
                    </thead>
                    <tbody>
                      
                    <g:each in="${spreadsheetInstanceList}" status="i" var="spreadsheetInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${spreadsheetInstance.id}">${fieldValue(bean: spreadsheetInstance, field: "id")}</g:link></td>
                        
                            <td><g:formatDate date="${spreadsheetInstance.createdOn}" /></td>
                            
                            <td><g:formatDate date="${spreadsheetInstance?.lastUpdate()}" /></td>
                            
                            
                            <td>${fieldValue(bean: spreadsheetInstance, field: "filename")}</td>
                        
                            <td><g:link class="menuButton" action="download" id="${spreadsheetInstance.id}">download file</g:link></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate controller="spreadsheet" action="list" total="${spreadsheetInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
