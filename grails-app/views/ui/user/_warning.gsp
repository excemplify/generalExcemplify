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
--}%<%@ page contentType="text/html;charset=UTF-8" %>
<%
/**
 *  Uploadr, a multi-file uploader plugin
 *  Copyright (C) 2011 Jeroen Wesbeek
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  $Author: duh $
 *  $Rev: 76616 $
 *  $Date: 2011-11-17 12:07:31 +0000 (Thu, 17 Nov 2011) $
 */
def msie = request.getHeader('user-agent').contains("MSIE")


%>
<html>
  <head>

    <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'customuploadr.css')}" />



    <script type="text/javascript">
        $("#browser").html("Your current browser is <font color='blue'>"+BrowserDetect.browser+"</font> version:<font color='blue'>"+BrowserDetect.version+"</font> under:<font color='blue'>"+BrowserDetect.OS+"</font>")
      var info="${session.getAttribute('info')}";
      if(info!=""){
          $("#info").html("<b>Uploaded files</b>: "+info);
      }
  


    </script>
  </head>
  <body>
    <div class="cuswarning">
      <div id="browser"  class="message">  </div>
      <div class="message">
        To use this <font color="blue">drag and drop</font> upload you need a <a href="http://en.wikipedia.org/wiki/HTML5" target="_html5">HTML5</a> capable browser:
      </div>
      <div class="browsers">
        <div class="browser chrome">
          <div class="anchor">
            <a href="http://www.google.com/chrome" target="_chrome">Get Chrome Frame <br>(Currently Only For Windows)</a>
          </div>
        </div>
        <div class="browser firefox">
          <div class="anchor">
            <a href="http://www.mozilla.com/firefox" target="_firefox">Get Firefox</a>
          </div>
        </div>
        <div class="browser safari">
          <div class="anchor">
            <a href="http://www.apple.com/safari/" target="_safari">Get Safari</a>
          </div>
        </div>
      </div>
     
      <g:if test="${msie}">
        <div class="message">
          upgrade to <a href="http://ie.microsoft.com/testdrive/" target="_ie10">Internet Explorer 10</a> or install the<br/>
          <a href="http://www.google.com/chromeframe" target="_chromeframe">Google Chrome Frame</a> browser plugin for Internet Explorer:
        </div>
        <div class="button">
          <a href="http://www.google.com/chromeframe" target="_chromeframe">Install Google Chrome Frame Plugin</a>
        </div>
      </g:if>
      <div  class="message"> Or you can use the traditional one by one upload tool below.</div>
      <g:form id="traditionalUpload" controller="experiment" action="traditionalUploadAction" params="[experimentId:request.getParameter('experimentId')]"  enctype="multipart/form-data"  method="POST">
        <label for="myRawData"><b>Raw Data File:</b></label>
        <input name="myRawData" id="myRawData"  type="file" />
        <input id="traditionalUploadButton"  type="submit" name="submit" value="Upload" />
      </g:form>
      <div style="height: 100px" id="info">Uploaded files:</div>
      
      
<!--      <input type="file" name="file_upload" id="file_upload" />-->
    </div>

  </body></html>