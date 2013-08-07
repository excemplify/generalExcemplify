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
<%@ page import="org.hits.ui.Resource" %>


<!doctype html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${resource(dir:'css',file:'index.css')}" />
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery-1.5.2.min.js')}"></script>
    <!--jQuery UI-->
    <script type="text/javascript" src="${createLinkTo(dir:'jquery-ui/ui', file:'jquery-ui.min.js')}"></script>
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
    <!--/jQuery UI-->
    <!--jQuery Table Sort-->
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.tablesorter.min.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.tablesorter.pager.js')}"></script>
    <!--/jQuery Table Sort-->
    <!--/jQuery Table Sort-->
    <script type="text/javascript">
      window.onload = function(){   
        $("#resourceList").tablesorter({sortList: [[0,1]]}).tablesorterPager({container: $("#pager")});
        
          $("#warning-form").dialog({
                      autoOpen: false,
                        height: 200,
                        width: 300,
                        modal: true,
                        buttons:{
                           "Close": function() {
                              $("#resource").html("");
                              $( this ).dialog( "close" );
                                        
                                }
                        },
                       close: function() {
                                allFields.val( "" ).removeClass( "ui-state-error" );
                        }
        });
      }
      
       function warning(){
        $("#warning-form" ).dialog( "open" );
     }
 
             function backToLab(){
     
          document.location.href ="${createLink(uri:'/lab')}"
         }

      
    </script>
  <g:set var="entityName" value="${message(code: 'resource.label', default: 'Resource')}" />

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
  <div class="messagecontent" id="resource">
      <g:if test="${flash.message}">
        <P  style="float:right;margin:10px" > oops~ <img onclick="warning()" style=" cursor: pointer"  src="${createLinkTo(dir:'images/ui', file:'warning.png')}" alt="warning" >
        </P> </g:if>
    </div>
  
    <div style="padding: 1em; text-align: right">
      
    <p class="title">${experimentName} Raw Data Resources</p>
      <div class="tablecontent">
        <table id="resourceList" class="tablesorter" >

          <thead>
            <tr>
              <th style=" display:none ">Id</th>
              <th>Resource Name</th>
              <th>Resource Size</th>
              <th>Resource Download</th>
              <th>State(activate/inactivate it)</th>
              <th>Version</th>
              <th>Author</th>
            </tr>
          </thead>
          <tbody>

          <g:each in="${resourceInstanceList}" status="i" var="resourceInstance">
            <tr>
              <td style=" display:none ">${fieldValue(bean: resourceInstance, field: "id")}</td>
              <td>${fieldValue(bean: resourceInstance, field: "fileName")}</td>
              <td><g:formatNumber number="${resourceInstance.binaryData.size()/(1024)}" type="number" maxFractionDigits="2" />KB</td>
              <td><g:link controller="resource" class="menuButton" action="download" id="${resourceInstance.id}" params="[expName:experimentName]"><img src="${createLinkTo(dir:'images/ui', file:'download.png')}" alt="download resource" ></g:link></td>
            <g:if test="${resourceInstance.state=='active'}">
              <td><font color="green"> Active  </font>
              <g:link controller="resource" action="deactive" id="${resourceInstance.id}" params="[expId:experimentId,type:resourceInstance.type]"><img style="cursor: pointer" src="${createLinkTo(dir:'images/ui', file:'deactive.png')}" alt="not count the experiment workbook" ></g:link>
              </td> 
                 <td>${resourceInstance.fileversion?resourceInstance.fileversion:"old data"}</td>
            </g:if>  
            <g:else>
              <td> <font color="red">Inactive   </font>
              <g:link controller="resource" action="active" id="${resourceInstance.id}" params="[expId:experimentId,type:resourceInstance.type]"><img  src="${createLinkTo(dir:'images/ui', file:'active.png')}" alt="count in the experiment workbook" ></g:link>
              </td> 
              <td><font color="red">${resourceInstance.fileversion?resourceInstance.fileversion:"old data"} </font></td>
            </g:else>
  
          
            <td>${fieldValue(bean: resourceInstance, field: "author")}</td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>

    <div id="pager" class="pager" style="padding: 10px;margin-top:-8px; height:5px;right: 5%" >
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

  <div id="warning-form" title="Warning">
    <p style="font-family: serif; color:  blue;"> ${flash.message}</p>
  </div>
  <br/>
  <br/>
    </div>
  </div>
  <g:applyLayout name="foot">
  </g:applyLayout>
</body>
</html>
