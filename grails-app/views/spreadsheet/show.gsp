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
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:if test="${session.parsedFile}"><div class="buttons">
              <g:form>
              
                <g:hiddenField name="id" value="${spreadsheetInstance?.id}" />
                
                <span class="buttons">
                  <g:actionSubmit class="download" action="downloadParsing" value="${message(code: 'default.button.download.label', default: 'Download Parsed File')}" /></span>                   
                  <g:actionSubmit class="save" action="saveParsingAsUpdate" value="${message(code: 'default.button.save.label', default: 'Save parsed file as update')}" /></span>
              </g:form>
              </div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="spreadsheet.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: spreadsheetInstance, field: "id")}</td>
                            
                        </tr>
                    
                      
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="spreadsheet.filename.label" default="File name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: spreadsheetInstance, field: "filename")}</td>
                            
                        </tr>
                    
                      <tr class="prop">
                            <td valign="top" class="name"><g:message code="spreadsheet.createdOn.label" default="Created On" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: spreadsheetInstance, field: "createdOn")}</td>
                            
                        </tr>
                        
                    </tbody>
                </table>
              
            </div>
            <div class="updates">
              <table>
                    <tbody>
                    
                        <tr class="prop">
                            
                            <td valign="top">Updates of Spreadsheet</td>
                            
                        </tr>
                        <tr class="prop">
                          <td valign="top" ><g:message code="sheetupdate.comment.label" default="Comment" /></td>
                       
                       
                          <td valign="top" ><g:message code="sheetupdate.updatedOn.label" default="Updated On" /></td>
                        </tr>
                        <g:each in="${spreadsheetInstance.updates}" >
                      
                        <tr class="prop">
                            <td valign="top" class="comment">${it.comment}</td>
                            
                            <td valign="top" class="date">${it.dateUpdated}</td>
                            
                        </tr>
                        </g:each>
                     </tbody>
              </table>       
            </div>
            
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${spreadsheetInstance?.id}" />
                    <span class="button"><g:actionSubmit class="download" action="download" value="${message(code: 'default.button.download.label', default: 'Download')}" /></span>                   
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
            
            <div class=""buttons>
              <g:form>
                    <g:hiddenField name="id" value="${spreadsheetInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="addRawData" value="${message(code: 'default.button.edit.label', default: 'Add Raw Data (Gel Inspector format)')}" /></span>                   
              </g:form>      
            </div>
        </div>
    </body>
</html>
