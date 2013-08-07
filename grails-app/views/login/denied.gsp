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



<head>

  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" href="${resource(dir:'css',file:'index.css')}" />
  <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
  <title>General Excemplify</title>
  <link rel="shortcut icon" href="${resource(dir: 'images/ui/', file: 'logo.ico')}" type="image/x-icon">
</head>

<body>

<g:render template="/ui/header"/>

<div class="maincontent">
  <div class="alert">
    <img alt="warning" src="${resource(dir: 'images/ui', file: 'alert.png')}" /> <g:message code="springSecurity.denied.message" />
  </div>

</div>
<g:applyLayout name="foot">
</g:applyLayout>

</body>
