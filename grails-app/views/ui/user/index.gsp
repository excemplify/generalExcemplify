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
<%@ page import="org.hits.ui.Template" %>
<%@ page import="org.hits.ui.Experiment" %>
<%@ page import="org.hits.parser.Spreadsheet" %>
<%@ page import="org.hits.parser.User" %>
<%@ page import="org.hits.parser.SecUser"%>
<%@ page import="grails.plugins.springsecurity.SpringSecurityService" %>
<% def springSecurityService %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <!--Required-->
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'index.css')}" />
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'jquery.ui.selectmenu.css')}" />
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery-1.5.2.min.js')}"></script>
    <!--jQuery UI-->
    <script type="text/javascript" src="${createLinkTo(dir:'jquery-ui/ui', file:'jquery-ui.min.js')}"></script>
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
    <!--/jQuery UI-->
    <!--Required-->


    <script type="text/javascript" src="${createLinkTo(dir:'js/plugins', file:'jquery.elastic.min.js')}"></script>
    <!--/Elastic-->

    <!--jQuery Table Sort-->
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.tablesorter.pager.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.tablesorter.min.js')}"></script>
    <!--jQuery Table Sort-->
    <!--jQuery Table Select Menue-->
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.selectmenu.js')}"></script>
    <!--jQuery Table Select Menue-->


    <!--Other Required jQuery UI Widget-->
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.core.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.widget.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.accordion.js')}"> </script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.dialog.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.mouse.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.position.js')}"> </script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.resizable.js')}"></script>
    <!--Other Required jQuery UI Widget-->




    <title>General Excemplify</title>
    <link rel="shortcut icon" href="${createLinkTo(dir: 'images/ui/', file: 'logo.ico')}" type="image/x-icon"/>
    <script type="text/javascript">

     window.onload = function(){
      var active=0;
           if("${session.getAttribute('active')}"==""){  
           active=0;
           }else{
             active=parseInt("${session.getAttribute('active')}");
           }
           
           
        $( "#accordion" ).accordion({
                        collapsible: true,
                        autoHeight:true,
                        active:active
                });
      
  
        $( "#dialog:ui-dialog" ).dialog( "destroy" );
        $("#profile-form").dialog({
                      autoOpen: false,
                        height:350,
                        width: 600,
                        modal: true,
                        buttons:{
                           "Update User Profile": function() {
                        var currentuserid=$("#curuserid").val(); 
                        var currentusername=$("#curusername").val(); 
                        var currentuserpwd=$("#curuserpwd").val();  
                        var currentemail=$("#curemail").val();
${remoteFunction(controller:'user', action:'updateProfile', params:'\'id=\'+currentuserid+\'&username=\'+currentusername+\'&password=\'+currentuserpwd+\'&seekEmailAddress=\'+currentemail', update:[success:'profile-form',failure:'resource'], onFailure:'warning()', onLoading:'showSpinner()', onComplete:'hideSpinner()') };       
                             $( this ).dialog( "close" );
                                
                                }, //
                          Cancel: function() {
              
                              $( this ).dialog( "close" );
                                        
                                }
                        },
                       close: function() {
                                allFields.val( "" ).removeClass( "ui-state-error" );
                        }
        });
        
        $("#warning-form").dialog({
                      autoOpen: false,
                        height: 400,
                        width: 400,
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
                             var id= $("#deleteExpId").html(); 
              
                             if(id.length>0){
${remoteFunction(controller:'experiment', action:'deleteExperiment',  params:'\'id=\'+id' ,update:[success:'updateMe',failure:'resource'], onFailure:'warning()') };
      }                                  
                                        $( this ).dialog( "close" );
                                          $("#deleteExpName").html(""); 
                                          $("#deleteExpId").html(""); 
                                

                                }, //
                          Cancel: function() {
              
                              $( this ).dialog( "close" );
                                        
                                }
                        },
                       close: function() {
                                allFields.val( "" ).removeClass( "ui-state-error" );
                        }
        });

       	$( "#dialog-form" ).dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        modal: true,
                        buttons: {
                                "Create a task": function() {
                                  var experimentName= $("#expname1").val();
         
                                  var sourcetemplate=$("#sourcetemplate").val();
                                 var targettemplate=$("#targettemplate").val();
                                
                                  var setUpResourceId="${session.getAttribute('setUpResourceId')}";
${remoteFunction(controller:'lab', action:'initialExp', params:'\'experimentName=\'+experimentName+\'&sourcetemplate=\'+sourcetemplate+\'&targettemplate=\'+targettemplate+\'&setUpResourceId=\'+setUpResourceId', update:'updateMe', onLoading:'showSpinner()', onComplete:'hideSpinner()') };
                                  $( this ).dialog( "close" );

                                }, //end create experiment

                                Cancel: function() {
${remoteFunction(controller:'lab', action:'clear', params:'\'after=\'+1', update:'resource', onLoading:'showSpinner()', onComplete:'hideSpinner()') };
                          
                                        $( this ).dialog( "close" );
                                        afterUpload(1);
                                       
                                }
                        },
                        close: function() {
                                allFields.val( "" ).removeClass( "ui-state-error" );
                                 
                        }
                });
                
    
        $('select#sourcetemplate').selectmenu({style:'popup'});
        $('select#targettemplate').selectmenu({style:'popup'});
        // $('select#parsingway').selectmenu({style:'popup'});

       var experimentName="${session.getAttribute('experimentName')}";

         if(experimentName!=""){
             $("#expname2").val(experimentName)
         }
         var setUp="${session.getAttribute('setUpResourceName')}";
          if(setUp!=""){
             $("#up1").html("<p><img src='${createLinkTo(dir: 'images/ui/', file: 'ready.gif')}' alt='set up uploaded'>  "+setUp+" uploaded</p>");
             $("#mySetUpUploadTd1").html("");
             $("#dialog-form" ).dialog( "open" );          
            }
            
       var select="${session.getAttribute('selector')}";
         if(select!=""){
           activeButton(select);
             }
     refreshTableSorter();

          
    
     }//end load
     
     function sectionSelection(nr){
     $("#accordion").accordion({ active: nr}); //works
 
     }
     function resetEmail(val){
         $("#email").val(val); 
     }
     
     function activeButton(selector){
       if(selector=="new"){
                $("#dialog-form").parent().find("button:contains('Create an experiment')").attr('disabled', false);
       }
       
     }
 function showSpinner() {
      $("#spinner").appendTo('#waitingPlace').show();
   }
   function hideSpinner() {
      $("#spinner").hide();
   }
 
     function refreshTableSorter(){
     if ($("#templateList").find("tr").size() > 1){ 
     $("#templateList")
    .tablesorter();
     }
     if ($("#experimentList").find("tr").size() > 1){
     $("#experimentList")
    .tablesorter()
    .tablesorterPager({container: $("#pager")});
    }
}
  
       
        function afterUpload(nr, nnr){
        $("#up"+nr).html($("#hidden"+nr).html());
        $("#up"+nnr).html($("#hidden"+nnr).html());
        $("#mySetUpUploadTd"+nr).html($("#hiddensubmit").html());
        $("#expname"+nr).val("");
       }
//         function afterUpload2(){
//        $("#up2").html($("#hidden2").html());
//        $("#expname2").val("");
//       }
       

     function initialExperiment(){
          $("#dialog-form").parent().find("button:contains('Create an experiment')").attr('disabled', true);
     
     $("#dialog-form" ).dialog( "open" );
     
     }

     function chooseLayout(){
      $("#layout-form" ).dialog( "open" );
     }

     function uploadRawData(id){
       /**
        * ./lab/uploadr?experimentId='+id,'Uploading'
        */
       str="./lab/uploadr?experimentType=new&&experimentId="+id

     document.location.href =str
     }
       function uploadGels(id){
       /**
        * ./lab/uploadr?experimentId='+id,'Uploading'
        */
       str="./lab/uploadr?experimentType=old&&experimentId="+id

     document.location.href =str
     }

     function warning(){
        $("#warning-form" ).dialog( "open" );
     }
     function warningMessage(str){
         $("#warning-form").html( "<p style='font-family: serif; color:  blue;'>"+str+"</p>");
          $("#warning-form" ).dialog( "open" );
     }
     function updateProfile(){
       
          $("#profile-form" ).dialog( "open" ); 
     }


     function DeleteExp(id, name){
        $("#deleteExpName").html(name); 
         $("#deleteExpId").html(id); 
  $("#confirm-dialog" ).dialog( "open" );
     }

    </script>

  <r:layoutResources/>

</head>
<body>

<g:render template="/ui/user/labheader"/>
<g:set var="userObject" value="${User.get(Integer.parseInt(sec.loggedInUserInfo(field:'id').toString()?:'0'))}"/>
<div class="maincontent">
  <div class="buttoncontent">
    <span class="navspan" onclick="initialExperiment()">  <img alt="New Experiment" src="${createLinkTo(dir: 'images/ui', file: 'plus.png')}" title="Has A Parsing Task" />New Task
    </span>

    <span> <p id="waitingPlace"></p></span>
  </div>

  <div id="resource" class="messagecontent">
    <g:if test="${flash.message}">
      <P style="float:right;" > oops~ <img onclick="warning()" style=" cursor: pointer"  src="${createLinkTo(dir:'images/ui', file:'warning.png')}" alt="warning" >
      </P> 
    </g:if>
  </div>

  <div id="accordion" class="corecontent" >
    <h5><a href="#section1">Parsing Tasks </a></h5>
    <div  id="updateMe" style=" max-height: 350px; overflow:  auto" >
      <g:render template="/ui/user/experiment" model="['experimentInstanceList': Experiment?.findAllByAuthor(userObject).findAll{it.type=='new'}]"/>
    </div>

    <h5><a href="#section2">Template Library</a></h5>
    <div id="updateTemplate" style=" max-height: 350px; overflow:  auto" >

      <g:render template="/ui/user/template" model="['templateInstanceList': Template?.findAll('from Template as b where b.type =:templateType and b.visible=:visibility',[templateType:'public', visibility:true]), 'templateInstanceTotal': Template?.findAll('from Template as b where b.type =:templateType and b.visible=:visibility',[templateType:'public', visibility:true]).size(), params:params]"/>
    </div>
  </div>

  <div class="hiddencontent">
    <div id="dialog-form" title="Initialize A New Experiment" >
      <table style=" width:550px; border-collapse: separate; border-spacing: 10px;" class="ui-widget" >
        <tr><td><b>Step 1:</b></td></tr>
        <g:form id="upForm" controller="lab" action="upload" enctype="multipart/form-data"  method="POST">
          <tr style=" width:550px; ">
            <td id="up1">
              <label for="mySetUp" style="width: 200px"><b>Source File:</b></label>
              <input name="mySetUp" id="mySetUp"  type="file" style="width: 350px"/>
            </td>
          </tr>
          <tr>
            <td id="mySetUpUploadTd1"  style=" text-align:  right" ><input class="ui-button ui-state-default  ui-widget ui-corner-all "  type="submit" name="submit" value="Upload" />
            </td>
          </tr>
        </g:form>
        <tr><td><hr size="3" width="100%" noshade   align="right"></td></tr>  

        <tr><td><b>Step 2:</b></td></tr>
        <tr  style=" width:550px; "><td><label for="expname1"><b>Parsing Task Name: </b></label> <input  style=" width:250px; " type="text" name="expname1" id="expname1" class="text ui-widget-content ui-corner-all" /></td></tr>
        <tr><td>
             <g:render template="/ui/user/templateSelec" model="['sourceTemplateInstanceList': Template?.findAll('from Template as b where b.type =:templateType and b.visible=:visibility and b.purpose=:purpose',[templateType:'public', visibility:true, purpose:'mapping']), 'targetTemplateInstanceList': Template?.findAll('from Template as b where b.type =:templateType and b.visible=:visibility and b.purpose=:purpose',[templateType:'public', visibility:true, purpose:'loading'])]"/>
        <br></td>
        </tr>
<!--        <tr><td><label for="parsingway"><b>Parsing Way: </b></label><select name="parsingway" id="parsingway">
              <option value="recordbased">record by record</option>
              <option value="blockbased">block by block</option>
            </select>
          </td>
        </tr>-->
      </table>
    </div>
    <div id="spinner" style="display: none; position: absolute">
      <img src="${createLinkTo(dir: 'images/ui', file: 'spinner.gif')}" alt="Loading..." /> Please waiting .....
    </div>
    <div id="warning-form" title="Warning">
      <p style="font-family: serif; color:  blue;"> ${flash.message}</p>
    </div>

    <div id="profile-form" title="Your Current User Profile">
      <g:render template="/ui/user/userProfile" model="['userInstance':User.findById((sec.loggedInUserInfo(field:'id')).toString())]"/>
    </div>

    <div id="confirm-dialog" title="Are you sure" >
      <p class="ui-widget" > Are you sure you want to delete the whole <span style="color:blue" id="deleteExpName"></span><span style="display: none" id="deleteExpId"></span>??</p>
    </div>

    <div style="display: none " id="hidden1" >
      <label for="mySetUp" style="width: 200px"><b>Set Up File:</b></label>
      <input name="mySetUp" id="mySetUp"  type="file" style="width: 350px"/>

    </div>
    <div  style="display: none " id="hiddensubmit"> <input class="ui-button ui-state-default ui-widget ui-corner-all"  type="submit" name="submit" value="Upload" /></div>
  </div>
</div>
<g:applyLayout name="foot">
</g:applyLayout>

<r:layoutResources/>
</body>
</html>
