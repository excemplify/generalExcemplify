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
<%@ page import="grails.plugins.springsecurity.SpringSecurityService" %>
<% def springSecurityService %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${resource(dir:'css',file:'index.css')}" />
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery-1.5.2.min.js')}">
    </script>
    <!--jQuery Table Sort-->
   <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.tablesorter.min.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery.tablesorter.pager.js')}"></script>
    <!--jQuery Table Sort-->
    <title>General Excemplify</title>
      <script type="text/javascript">
        window.onload = function(){
                $("#allExperimentsList").tablesorter().tablesorterPager({container: $("#pager")});
              }    

    </script>
    <link rel="shortcut icon" href="${resource(dir: 'images/ui/', file: 'logo.ico')}" type="image/x-icon">
  

  <r:layoutResources/>

</head>
<body>
<g:render template="/ui/header"/>
  

  <div id="publicExp" class="maincontent">
    <g:render template="/ui/admin/allExp" model="['allExperimentsList': Experiment?.findAll()]"/>
  </div>
  <g:applyLayout name="foot">
  </g:applyLayout>
<r:layoutResources/>
</body>
</html>
