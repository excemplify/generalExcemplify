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
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery-1.5.2.min.js')}">
    </script>
    <title>General Excemplify</title>
    <link rel="shortcut icon" href="${resource(dir: 'images/ui', file: 'logo.ico')}" type="image/x-icon">

  <r:layoutResources/>
</head>
<body>

<g:render template="/ui/header"/>

<div class="maincontent">
  <table style="width:100%; height: 550px ">
    <tr> 
      <td colspan="2" style="height: 20%"></td>
    </tr>
    <tr>

      <td class="link"  style="width:50%;height: 50%" align="center"  >
        <img alt="admin login " src="${resource(dir: 'images/ui', file: 'adminuser.png')}" />
        <a href="${createLink(uri:'/admin')}" >Login As Lab Admin</a> 
      </td>
  
      <td class="link" style="width:50%;height: 50%" align="center" >
        <img alt="experimenter login " src="${resource(dir: 'images/ui', file: 'labuser.png')}"  />
        <a href="${createLink(uri:'/lab')}" >Login As Experimenter</a> 
      </td>
    </tr>
<!--    <tr>

      <td class="link"   style="width:100%;height:40%" align="center" colspan="2" >
        <img alt="Public Experiment Library " src="${resource(dir: 'images/ui', file: 'publicuser.png')}" />
        <a href="${createLink(uri:'/public')}" >Public Experiment Library</a> 
      </td>
    </tr>-->
    
  </table>
</div>

<g:applyLayout name="foot">
</g:applyLayout>

<r:layoutResources/>
</body>
</html>
