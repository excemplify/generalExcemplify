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
<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page import="org.hits.ui.ParserMetaphor" %>
<%@ page import="org.hits.ui.Template" %>
<%@ page import="org.hits.ui.UIConcepts" %>
<%@ page import="org.hits.ui.Knowledge" %>
<%@ page import="java.util.ArrayList" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
     <META HTTP-EQUIV="Expires" CONTENT="-1">-->

    <title>General Excemplify</title>
    <link rel="shortcut icon" href="${resource(dir: 'images/ui', file: 'logo.ico')}" type="image/x-icon">
    <!--Required-->
    <link rel="stylesheet" href="${resource(dir:'css',file:'index.css')}" />
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'jquery.sheet.css')}" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'jquery.ui.selectmenu.css')}" />


    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery-1.5.2.min.js')}">
    </script>


    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.sheet.js')}">
    </script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.core.js')}">
    </script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.widget.js')}">
    </script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.accordion.js')}">
    </script>
      <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.ui.selectmenu.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'parser.js')}">
    </script>

    <!--/Required-->
    <!--Optional-->
    <!--jQuery UI-->
    <script type="text/javascript" src="${createLinkTo(dir:'jquery-ui/ui', file:'jquery-ui.min.js')}"></script>
    <!--/jQuery UI-->

    <!--ColorPicker-->
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'jquery.colorPicker.css')}"/>
    <script type="text/javascript" src="${createLinkTo(dir:'js/plugins', file:'jquery.colorPicker.min.js')}"></script>
    <!--/ColorPicker-->
    <!--Raphaeljs - for charts-->
    <script type="text/javascript" src="${createLinkTo(dir:'js/plugins', file:'raphael-min.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js/plugins', file:'g.raphael-min.js')}"></script>
    <!--/Raphaeljs-->
    <script type="text/javascript" src="${createLinkTo(dir:'js/plugins', file:'jquery.elastic.min.js')}"></script>
    <!--/Elastic-->
    <!--Advanced_Math-->
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.sheet.advancedfn.js')}"></script>
    <!--/Advanced_Math-->

    <!--/Optional-->

    <script type="text/javascript">

 window.onload = function(){
    //Here is where we initiate the sheets
    //every time sheet is created it creates a new jQuery.sheet.instance (array), to manipulate each sheet, the jQuery object is returned
 $( "#accordion" ).accordion();

 $("#savesourcetemplateas").dialog({
                      autoOpen: false,
                        height: 400,
                        width: 500,
                        modal: true,
                        buttons:{
                           "Save":function(){
                           var saveas= $("#saveas").val();
                          var purpose=$("#purpose").val();
                          var parsingway=$("#parsingway").val();

${remoteFunction(controller:'admin', action:'saveTemplate',  params:'\'saveas=\'+saveas+\'&purpose=\'+purpose+\'&parsingway=\'+parsingway', update:'warning') };  
                           $( this ).dialog( "close" );
                           },
                           "Close": function() {
                          
                              $( this ).dialog( "close" );

                                }
                        },
                       close: function() {
                                allFields.val( "" ).removeClass( "ui-state-error" );
                        }
        });
//        
  $('select#parsingway').selectmenu({style:'popup'});      
  
        
  $("#categoryOptions").dialog({
                      autoOpen: false,
                        height: 300,
                        width: 400,
                        modal: true,
                        buttons:{
                           "Load":function(){                
                          var category=$("#category").val()   //purpose -> stage
${remoteFunction(controller:'admin', action:'loadConcepts',params:'\'category=\'+category',update:'warning') };

                           $( this ).dialog( "close" );
                           },
                           "Close": function() {
                          
                              $( this ).dialog( "close" );

                                }
                        },
                       close: function() {
                                allFields.val( "" ).removeClass( "ui-state-error" );
                        }
        });        
 $("#warning-form").dialog({
                      autoOpen: false,
                        height: 300,
                        width: 400,
                        modal: true,
                        buttons:{
                           "Close": function() {
                              $("#warning").html("");
                              $( this ).dialog( "close" );

                                }
                        },
                       close: function() {
                                allFields.val( "" ).removeClass( "ui-state-error" );
                        }
        });
    
 $( "#information" ).dialog({
                        autoOpen: false,
                        show: "blind",
                        hide: "explode"
                });

 var follower=$("#follower");
 var mark=false;

 $("#radio").change(function(){
followMe();

});

    var locationStart;
    var locationEnd;
    var openFile="empty";

    $( "#tabs" ).tabs();
    var xmlString="${session.getAttribute('xml')}";

    if(xmlString==""){
    $('#jQuerySheet0').sheet({
    title: 'Admin Workspace',
    purpose: 'template',
     inlineMenu: inlineMenu($.sheet.instance, '#sourceInlineMenu'),
    urlMenu: 'menu.html',
    resizable: 	false,
    buildSheet: $.sheet.makeTable.fromSize('10x20'),
    autoFiller: true
    });
    }else{
    openFile="${session.getAttribute('openFileName')}";
    $('#jQuerySheet0').sheet({
    title: openFile,
    inlineMenu: inlineMenu($.sheet.instance, '#sourceInlineMenu'),
    urlMenu: 'menu.html',
     purpose: 'template',
    resizable: 	false,
    buildSheet: $.sheet.makeTable.xml(xmlString),
    autoFiller: true
    });
  }



 

}//end load

 function warning(){
        $("#warning-form" ).dialog( "open" );
     }


 function saveAs(){
   
        $("#savesourcetemplateas" ).dialog( "open" );
     }


function saveTemplate(){
  $("#purposeintro").val("The template will be treated as an unknown template, the containing 'Concepts' will be different from its 'Column Labels'")
  $("#purpose").val("mapping")
 $("#savetable").show()
${remoteFunction(controller:'admin', action:'saveTemplatePre',update:'saveop') };

}

function saveTargetTemplate(){
    $("#purposeintro").val("The template will be treated as a known template, the containing 'Concepts' will be same as its 'Column Labels'")
  $("#purpose").val("loading")
 $("#savetable").hide()
${remoteFunction(controller:'admin', action:'saveTemplatePre',update:'saveop') };
}

function redirect(){
      document.location.href ="${createLink(uri:'/admin')}"
}

//function openExistTemplate(){
//    $("#template-list" ).dialog( "open" );
//}

function updateTargetClass(fromr, fromc, endr, endc, updateClassStr){

  //alert(fromr+fromc+endr+endc+updateClassStr);
 for (var i=parseInt(fromr);i<=parseInt(endr);i++){
        for(var j=parseInt(fromc); j<=parseInt(endc); j++)
        {
        var tdString="#"+targetSheetInstance+ '_table0_cell_c' + j + '_r' + i;
        $(tdString).addClass(updateClassStr);
        }
        }

 }


function updateKnowledge(fromr, fromc, endr, endc, sheet, updateClassStr){
//alert(" fc:"+fromc+"fr:"+fromr+" ec:"+endc+" er:"+endr+" sheet:"+sheet+" str:"+updateClassStr);
 for (var i=parseInt(fromr);i<=parseInt(endr);i++){
      //alert(i);
        for(var j=parseInt(fromc); j<=parseInt(endc); j++)
        {
         //alert(j);
        var tdString="#0"+ '_table' + sheet+ '_cell_c' + j + '_r' + i;
        $(tdString).css({'background-color': updateClassStr}); 
        }
        }
      //  alert("finish");
 }


   function makeThemDroppable(sheetInstanceIndex,sheetIndex,fromr, endr, fromc, endc){
     for (var i=parseInt(fromr);i<=parseInt(endr);i++){
        for(var j=parseInt(fromc); j<=parseInt(endc); j++)
        { var tdString="#"+sheetInstanceIndex+ '_table' + sheetIndex+ '_cell_c' + j + '_r' + i;
        $(tdString).droppable({ drop: handleDropEvent} );
        }
        }
}

function handleDropEvent( event, ui) {

var draggable = ui.draggable;
var knowledgeName=draggable.attr('id');
var knowledgeClass=draggable.attr("title");
var templateObject=$.sheet.instance[0];
var row=(templateObject.getTdLocation([event.target])).row;
var col=(templateObject.getTdLocation([event.target])).col;
var activeSheetIndex=templateObject.i;

var endrow=templateObject.sheetSize().height;

var location=jSE.parseCellName(col,row);
 var excel="${session.getAttribute('openFileName')}";
//alert(location);
 $(this).removeClass('ui-droppable');
${remoteFunction(controller:'admin', action:'updateKnowledge', params:'\'location=\'+location+\'&endrow=\'+endrow+\'&knowledgeName=\'+knowledgeName+\'&knowledgeClass=\'+knowledgeClass+\'&file=\'+excel+\'&activesheet=\'+activeSheetIndex', update:'knowledge') };
}




function followMe(){
var rb = $("input:radio:checked").val();
var lastMarkStart=null;
var lastMarkEnd=null;
var followedSheet= $.sheet.instance[0];
$('#jQuerySheet0').bind({
  mouseover:function(e){
  if(rb=="follow"){
        followedSheet.obj.sheet().bind({
                    mouseleave: function(e) {
                   followedSheet.obj.sheet().unbind('mouseover');
                   if((lastMarkStart!=null)&&(lastMarkEnd!=null)){
                          followedSheet.clearMarkCells(lastMarkStart,lastMarkEnd);
                        }
                    },
                    mouseover: function(e) {
                        if((lastMarkStart!=null)&&(lastMarkEnd!=null)){
                          followedSheet.clearMarkCells(lastMarkStart,lastMarkEnd);
                        }
                        var startloc=followedSheet.getTdLocation([e.target])
                        startloc.row=startloc.row
                        startloc.col=startloc.col
                        var rowloc=startloc.row
                        var colloc=startloc.col
                         var endloc = {
                         row: rowloc+5,
                         col: colloc+1
                         };

                        followedSheet.markCells(startloc, endloc);
                         var startName=jSE.parseCellName(startloc.col, startloc.row);
                         var endName=jSE.parseCellName(endloc.col, endloc.row);
                        // alert("start:"+startName+" end:"+endName)
                        lastMarkStart=startloc;
                        lastMarkEnd=endloc;


                    },
                    mouseenter:function(e){
                    //  alert("enter");
                    }

                });
  }else{
     followedSheet.obj.sheet().unbind('mouseover mouseleave');

  }
    }

});

}


   function openInfo(){
    $("#information").dialog( "open");
   }
   function closeInfo(){
     $('#information' ).dialog('close');
   }


   function setCurrentSheet(index){
        currentSheet=index;
   }

   function warnTest(elementid){

     if($(elementid).val()==""){
       warnUser(elementid)
     }else{

       removeWarnUser(elementid)
     }
   }
   function removeWarnUser(elementid){
       $(elementid).removeClass( "warning" );
   }
   function warnUser(elementid){

       $(elementid).addClass( "warning" );


   }

   function fetchTargetStartPoint(i,row, col){
    targetSheetInstance=i
   if(i!=0){
    var targetStartPoint=jSE.parseCellName(col, row);
    $("#targetloc").val(targetStartPoint);
    warnTest("#targetloc");

     }
}


function inlineMenu(I, id){
                I = (I ? I.length : 0);
                var html = $(id).html().replace(/sheetInstance/g, "$.sheet.instance[" + I + "]");
                var menu = $(html);
                return menu;
}



function notifyMark(fromr, fromc, endr, endc, sheetIndex, sheetInstanceIndex){ //not used anymore
    var cols=endc-fromc+1;
    var start=jSE.parseCellName(fromc, fromr);
    var end=jSE.parseCellName(endc, endr);
    var colsNum=cols.toString();
    
  makeThemDroppable(sheetInstanceIndex,sheetIndex,fromr,endr,fromc,endc);
 
}




function  buildKnowledgeForLabel(text, fromr, endr, fromc, endc, sheetIndex,sheetInstanceIndex, autoend){
//alert(jS.sheetSize(jQuery('#' + jS.id.sheet + jS.i)));
 var excel="${session.getAttribute('openFileName')}";
var name=escape(text);
alert(name);
  var fromnode=jSE.parseCellName(fromc, fromr);
  var endnode=jSE.parseCellName(endc, endr);
 var auto=autoend
   makeThemDroppable(sheetInstanceIndex,sheetIndex,fromr,endr,fromc,endc);
 if(auto){
${remoteFunction(controller:'admin', action:'buildKnowledge', params:'\'columnName=\'+name+\'&frow=\'+fromr+\'&sheetIndex=\'+sheetIndex+\'&fcol=\'+fromc+\'&ecol=\'+endc+\'&erow=\'+endr+\'&fromlocation=\'+fromnode+\'&endlocation=\'+fromnode+\'&file=\'+excel', update:'knowledge') };

 }else{
${remoteFunction(controller:'admin', action:'buildKnowledge', params:'\'columnName=\'+name+\'&frow=\'+fromr+\'&sheetIndex=\'+sheetIndex+\'&fcol=\'+fromc+\'&ecol=\'+endc+\'&erow=\'+endr+\'&fromlocation=\'+fromnode+\'&endlocation=\'+endnode+\'&file=\'+excel', update:'knowledge') };
  
 }

}


function loadConcepts(){
    $("#categoryOptions").dialog( "open");
}

 function warningMessage(str){
         $("#warning-form").html( "<p style='font-family: serif; color:  blue;'>"+str+"</p>");      
     }

function getRandomColor(){

    var r = function () { return Math.floor(200+Math.random()*56) };
   var color="rgb(" + r() + "," + r() + "," + r() + ")";
 
    return color;
}

    </script>




    <!--Page styles-->

  <r:layoutResources/>
</head>
<body>
<g:render template="/ui/admin/adminheader"/>
<div style="width: 100%; height:85%; min-height: 750px">
  <table style="width: 100%; height:650px;  vertical-align:  middle; position:  relative">
    <tr>
      <td style=" width: 50%; height:90%">  
        <br/>
        <br/>
        <div id="jQuerySheet0" class="jQuerySheet" style=" width: 100%;height:90%;">
        </div>
      </td>
      <td style=" width: 2%;height:100% "></td>
      <td style=" width: 48%; height: 100%;">
        <div id="warning" style="height:5%">
          <g:render template="/ui/admin/warningMessage" />
        </div>
        <br/>
        <div id="accordion" style=" width: 100%;height: 40%; position: relative">

          <g:if test="${session.getAttribute('category')!=null}">
            <g:render template="/ui/admin/concepts" model="['conceptLists': UIConcepts?.findByCategory(session.getAttribute('category'))?.concepts, 'category':session.getAttribute('category') ]" />
          </g:if>
          <g:else>
            <g:render template="/ui/admin/concepts"/>
          </g:else>

        </div>
        <br/>
        <br/>     
        <br/>
        <div id="knowledge" style="padding-top:2em; width: 100%;height: 45%; position: relative; overflow: scroll "  >
          <g:if test="${session.getAttribute('templateType')=="old"}">
            <g:render template="/ui/admin/knowledge" model="['templateKnowledgeList': Knowledge?.findAllByFileName(session.getAttribute('openFileName'))]"/>
          </g:if>
          <g:else>
            <g:render template="/ui/admin/knowledge"/>
          </g:else>
        </div>
      </td>
    </tr>

  </table>
</div>
<g:applyLayout name="foot">
</g:applyLayout>
</div>
<div id="warning-form" title="Warning">
  <p style="font-family: serif; color: blue;"> ${flash.message}</p>
</div>
<g:render template="/ui/admin/categoryoptions" model="['categoryList': UIConcepts?.findAll()]"/>
<div id="saveop">
  <g:render  template="/ui/admin/stageoptions"  model="['oldname': session.getAttribute('openFileName')]"/>
</div>
<div id="follower" style="display:none"> mark </div>

<g:form controller="admin" action="openSheet"  method="post" enctype="multipart/form-data"  >
  <input id="myFile" type="file" style="display:none;visibility:hidden;" name="myFile"  onChange="javascript:document.getElementById('myFileSub').click()"/>
  <input id="myFileSub" style="display:none;visibility:hidden;" type="submit" />
</g:form>
<g:form controller="admin" action="openTemplate" method="post" enctype="multipart/form-data"  >
  <input id="myFile2" type="file" style="display:none;visibility:hidden;" name="myFile2"  onChange="javascript:document.getElementById('myFileSub2').click()"/>
  <input id="myFileSub2" style="display:none;visibility:hidden;" type="submit" />
</g:form>
<g:render template="/ui/admin/inlineMenue" />
<r:layoutResources/>

</body>

</html>
