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
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        
    <title>Add Raw Data</title>
  </head>
  <body>
     <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
  <g:uploadForm action="saveRawData" method="post" >
    <g:hiddenField name="id" value="${spreadsheetInstance?.id}" />
    <table>
                  <tr class="prop">
               <td valign="top" class="name">
                          <label for="payload">File:</label>
                        </td>
                        <td valign="top">
                        <input type="file" id="payload" name="payload"/>
                        </td>
                        </tr>
              <tr class="prop">
                <td>Parser</td><td><g:select name="parserType" from="${parserDefList}"/></td>               
                </tr>
                  </table>
     <div class="buttons">
                    <span class="button"><g:submitButton name="saveRawData" class="save" value="${message(code: 'default.button.create.label', default: 'Save Raw Data')}" /></span>
                </div>
  </g:uploadForm>
  </body>
</html>
