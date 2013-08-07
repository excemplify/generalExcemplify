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
<%@ page import="org.hits.ui.Template" %>
<!doctype html>
<html>
  <head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="main" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'index.css')}" />
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery-1.5.2.js')}"></script>
    <!--jQuery UI-->
    <script type="text/javascript" src="${createLinkTo(dir:'jquery-ui/ui', file:'jquery-ui.min.js')}"></script>
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
    <!--/jQuery UI-->
    <!--jQuery Table Sort-->
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.tablesorter.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.tablesorter.pager.js')}"></script>
    <!--/jQuery Table Sort-->
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.dialog.js')}"></script>

  <g:set var="entityName" value="${message(code: 'template.label', default: 'Template')}" />

  <title>General Excemplify</title>
  <link rel="shortcut icon" href="${createLinkTo(dir: 'images/ui/', file: 'logo.ico')}" type="image/x-icon">
  <script type="text/javascript">
     window.onload = function(){

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
         $("#confirm-dialog").dialog({
                  autoOpen: false,
                    height: 200,
                    width: 300,
                    modal: true,
                    buttons:{
                      "Yes, delete it": function() {
                         var id= $("#deleteTempId").html(); 
${remoteFunction(controller:'template', action:'deleteTemplate',  params:'\'id=\'+id' ,update:[success:'update',failure:'resource'], onFailure:'warning()') };
                                         
                                    $( this ).dialog( "close" );
                                      $("#deleteTempName").html(""); 
                                      $("#deleteTempId").html(""); 

                            }, //
                      Cancel: function() {
              
                          $( this ).dialog( "close" );
                                        
                            }
                    },
                   close: function() {
                            allFields.val( "" ).removeClass( "ui-state-error" );
                    }
    });
      refreshTableSorter();    
     }
            function refreshTableSorter(){
             if ($("#templateList").find("tr").size() >= 1){ 
             $("#templateList").tablesorter( ).tablesorterPager({container: $("#dpager")});
             }
           //  alert("refresh")
            }
         
       function warning(str){
        $("#warning-form" ).html( "<p style='font-family: serif; color:  blue;'>"+str+"</p>");
           
    $("#warning-form" ).dialog( "open" );
 }
        function Display(id){
      ${remoteFunction(controller:'template', action:'display',  params:'\'id=\'+id' ,update:[success:'update',failure:'resource'], onFailure:'warning()') };
                                         
    }
      function StopDisplay(id){
      ${remoteFunction(controller:'template', action:'stopDisplay',  params:'\'id=\'+id' ,update:[success:'update',failure:'resource'], onFailure:'warning()') };
                                         
    } 
     
   function DeleteTemplate(id, name){
    $("#deleteTempName").html(name); 
     $("#deleteTempId").html(id); 
    $("#confirm-dialog" ).dialog( "open" );
 }
  </script>
</head>
<body>

<g:render template="/ui/header"/>
<div class="maincontent">
  <div class="nav" style="height: 20px">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/admin')}">Admin Home</a></span>
    <span class="menuButton"><a class="create" href="${createLink(uri:'/admin')}" >Create New Template</a></span>
  </div>
  <br>
  <div class="messagecontent" id="resource" style="height:20px;">
    <g:if test="${flash.message}">
      <P  style="float:right;margin:20px" > oops~ <img onclick="warning('${flash.message}')" style=" cursor: pointer"  src="${createLinkTo(dir:'images/ui', file:'warning.png')}" alt="warning" >
      </P> 
    </g:if>
  </div>
  <div  id="update" style=" text-align: right;  width: 95%; height:500px" >
    <g:render template="/template/list" model="['templateInstanceList':Template?.list()]"/>

  </div>

  <div id="warning-form" title="Warning">
    <p style="font-family: serif; color:  blue;"> ${flash.message}</p>
  </div>
  <div id="confirm-dialog" title="Are you sure" >
    <p class="ui-widget" > Are you sure you want to delete the <span style="color:blue" id="deleteTempName"></span><span style="display: none" id="deleteTempId"></span>??</p>
  </div>
</div>

  <g:applyLayout name="foot">
  </g:applyLayout>
</body>
</html>
