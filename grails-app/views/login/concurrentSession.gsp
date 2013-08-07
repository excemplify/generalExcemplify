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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${resource(dir:'css',file:'index.css')}" />
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
    <title>General Excemplify</title>
    <link rel="shortcut icon" href="${resource(dir: 'images/ui/', file: 'logo.ico')}" type="image/x-icon">
  </head>
  <body>
    <div class="ui-widget-header" > 
      <table style="width: 100%; vertical-align:  middle ">
        <tr><td colspan="1" style=" text-align: left; color: #ffffff; cursor: pointer" >  <a href="${createLink(uri:'/')}" > 
              <img alt="logo" src="${createLinkTo(dir:'images/ui', file:'excemplify.png')}" style="cursor: pointer" ></a></td></tr>
        <tr>  <td style="text-align: right;vertical-align: baseline;">
        <sec:ifLoggedIn>
          <img alt="log in as " src="${resource(dir: 'images/ui', file: 'user.png')}" /> <sec:username/> (<g:link class="userInfo" controller="logout">log out</g:link>)
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
          <g:link controller="login" class="menuButton" action="auth" >Login</g:link>
        </sec:ifNotLoggedIn>
        </td></tr></table>
    </div>
     <div style="width: 100%; height:80%; min-height: 750px">
    <div class="alert">
      <img alt="warning" src="${resource(dir: 'images/ui', file: 'alert.png')}" /> You properly logged in at another place. Current session is expired.
    </div>
     </div>
   <g:applyLayout name="foot">
  </g:applyLayout>
  </body>
</html>
