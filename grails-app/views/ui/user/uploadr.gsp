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

<html>
  <head>

    <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">

    <title>General Excemplify</title> 
    <link rel="stylesheet" href="${resource(dir:'css',file:'index.css')}" />
    <link rel="shortcut icon" href="${resource(dir: 'images/ui/', file: 'logo.ico')}" type="image/x-icon"/>
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
   

    <script type="text/javascript">
      window.onload=function(){

  
      BrowserDetect.init(); 

//  //    $("#browser").html("Your current browser is <font color='blue'>"+BrowserDetect.browser+"</font> version:<font color='blue'>"+BrowserDetect.version+"</font> under:<font color='blue'>"+BrowserDetect.OS+"</font>")
////      
//     if((BrowserDetect.browser=="Chrome")&&(BrowserDetect.OS=="Linux")){
//       $('.uploadr[name=uploadr]').load("/excemplify/lab/warning?experimentId=${request.getParameter('experimentId')}");
//
//      }
//      

    }  

         function warning(){
           alert("something happens.");
         }
         

         function goBack(){
           alert("back");
           document.location.href ="${createLink(uri:'/lab')}";
         }

      function deleteResource(fileName){

${remoteFunction(controller:'experiment', action:'deleteResourceDuringUpload', params:'\'deleteFileName=\'+fileName') };

 }
    function deleteGelResource(fileName){

${remoteFunction(controller:'experiment', action:'deleteGelResourceDuringUpload', params:'\'deleteFileName=\'+fileName') };

 }
     var BrowserDetect = {
        init: function () {
                this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
                this.version = this.searchVersion(navigator.userAgent)
                        || this.searchVersion(navigator.appVersion)
                        || "an unknown version";
                this.OS = this.searchString(this.dataOS) || "an unknown OS";
        },
        searchString: function (data) {
                for (var i=0;i<data.length;i++)	{
                        var dataString = data[i].string;
                        var dataProp = data[i].prop;
                        this.versionSearchString = data[i].versionSearch || data[i].identity;
                        if (dataString) {
                                if (dataString.indexOf(data[i].subString) != -1)
                                        return data[i].identity;
                        }
                        else if (dataProp)
                                return data[i].identity;
                }
        },
        searchVersion: function (dataString) {
                var index = dataString.indexOf(this.versionSearchString);
                if (index == -1) return;
                return parseFloat(dataString.substring(index+this.versionSearchString.length+1));
        },
        dataBrowser: [
                {
                        string: navigator.userAgent,
                        subString: "Chrome",
                        identity: "Chrome"
                },
                { 	string: navigator.userAgent,
                        subString: "OmniWeb",
                        versionSearch: "OmniWeb/",
                        identity: "OmniWeb"
                },
                {
                        string: navigator.vendor,
                        subString: "Apple",
                        identity: "Safari",
                        versionSearch: "Version"
                },
                {
                        prop: window.opera,
                        identity: "Opera",
                        versionSearch: "Version"
                },
                {
                        string: navigator.vendor,
                        subString: "iCab",
                        identity: "iCab"
                },
                {
                        string: navigator.vendor,
                        subString: "KDE",
                        identity: "Konqueror"
                },
                {
                        string: navigator.userAgent,
                        subString: "Firefox",
                        identity: "Firefox"
                },
                {
                        string: navigator.vendor,
                        subString: "Camino",
                        identity: "Camino"
                },
                {		// for newer Netscapes (6+)
                        string: navigator.userAgent,
                        subString: "Netscape",
                        identity: "Netscape"
                },
                {
                        string: navigator.userAgent,
                        subString: "MSIE",
                        identity: "Explorer",
                        versionSearch: "MSIE"
                },
                {
                        string: navigator.userAgent,
                        subString: "Gecko",
                        identity: "Mozilla",
                        versionSearch: "rv"
                },
                { 		// for older Netscapes (4-)
                        string: navigator.userAgent,
                        subString: "Mozilla",
                        identity: "Netscape",
                        versionSearch: "Mozilla"
                }
        ],
        dataOS : [
                {
                        string: navigator.platform,
                        subString: "Win",
                        identity: "Windows"
                },
                {
                        string: navigator.platform,
                        subString: "Mac",
                        identity: "Mac"
                },
                {
                           string: navigator.userAgent,
                           subString: "iPhone",
                           identity: "iPhone/iPod"
            },
                {
                        string: navigator.platform,
                        subString: "Linux",
                        identity: "Linux"
                }
        ]

};




    </script>   
    <style>
      .convention {
        padding:2px; 
        margin: 2px
      }
      .convention li{
        padding:5px; 
        margin: 10px
      }
    </style>



  <r:require modules="uploadr"/>

  <r:layoutResources/>
</head>
<body>

<div class="ui-widget-header2" > 
  <table style="width: 100%;  vertical-align:  middle ">
    <tr><td colspan="1" style=" text-align: left; color: #ffffff; cursor: pointer" >  <a href="${createLink(uri:'/')}" > 
          <img alt="logo"  title="Back to Welcome Page" src="${createLinkTo(dir:'images/ui', file:'excemplify.png')}" style="cursor: pointer" ></a></td></tr>
    <tr>  <td style=" width: 100%;text-align: right;vertical-align: baseline; font-family: serif;font-size: 15px; font-weight:normal;  color: black">
    <sec:ifLoggedIn>
      <img alt="log in as"  src="${resource(dir: 'images/ui', file: 'user.png')}" /><sec:username/>(<g:link class="link" controller="logout">log out</g:link>)
    
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
      <g:link controller="login" class="link" action="auth" >Login</g:link>
    </sec:ifNotLoggedIn>
  </td></tr></table>
</div>
  <div class="maincontent" id="message">
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
   
    <g:set var="expId" value="${request.getParameter('experimentId')}"/>
    <g:set var="expType" value="${request.getParameter('experimentType')}"/>

    <div style=" height: 500px">
      <table style="width:100%"> 
        <tr style="vertical-align: top; padding:20px ">
          <td style="width:40%; min-height: 400px">
        <g:if test="${request.getParameter('experimentType')=="new"}">  
          <uploadr:add name="uploadr" id="uploadr"  unsupported="/excemplify/lab/warning?experimentType=${request.getParameter('experimentType')}&&experimentId=${request.getParameter('experimentId')}"  path="rawdata" viewable="false" downloadable="false"   direction="up" maxVisible="10"  action="uploadrAction" controller="experiment">
            <uploadr:onDelete>deleteResource(file.fileName); return true; </uploadr:onDelete>
          </uploadr:add>
        </g:if>
        <g:else>
          <uploadr:add name="uploadr2" id="uploadr2"  unsupported="/excemplify/lab/warning?experimentType=${request.getParameter('experimentType')}&&experimentId=${request.getParameter('experimentId')}"  path="gelinspectordata" viewable="false" downloadable="false"   direction="up" maxVisible="10"  action="uploadrGelAction" controller="experiment">
            <uploadr:onDelete>deleteGelResource(file.fileName); return true; </uploadr:onDelete>
          </uploadr:add>
        </g:else>
        <br/>

        </td>
        <td  style="width:20%"> </td>
        <td style="padding: 5px; width:40%">
          <p id="browser"></p>
          <b>Instructions:</b> 
          <ul class="convention">
            <li>You can either <font color="blue">drag and drop</font> multiple files to the <p style=" display: inline-block; vertical-align: middle;  height: 30px; width: 30px; border: 2px dashed grey;"> </p> dashed area, or using traditional upload by clicking the <img alt="traditional upload " src="${createLinkTo(dir:'images/ui', file:'select.gif')}" />button. </li>
            <li>If the uploading is failed, you can point to the state text "<font color="red">failed</font>" to check the reason.</li>
            <li>After uploading files, you need to click <a href="">Send</a>, to make your raw data files bind to the experiment.</li>
          </ul>
          <b>Conventions:</b> 
          <ul class="convention">
            <li>Currently, only <font color="blue"><b>.txt</b></font> and <font color="blue" ><b>.xls/.xlsx</b></font> file are accepted.</li>
            <li>If the file is a raw data file, the name of file must match the pattern <font color="blue"><b>proteinName</b></font>_blot<font color="blue"><b>num</b></font>. e.g. smad2_blot1.xls or smad2_blot2.txt </li></ul></td>
        </tr>
      </table>
      <!--    <input onclick="startUpload();" value="Send" type="submit">-->
    </div>
    <div style="text-align:left; font-size: 15px; padding: 10px;position:  relative">
      <g:if test="${request.getParameter('experimentType')=="new"}">  
        <g:link controller="experiment"  action="finishUpload" id="${expId}">Send</g:link>
      </g:if>
      <g:else>
       <g:link controller="experiment"  action="finishGelUpload" id="${expId}">Send</g:link>
      </g:else>
    </div>
    <div id="upd">

    </div>
  </div>
 <g:applyLayout name="foot">
  </g:applyLayout>

<r:layoutResources/>
</body>
</html>
