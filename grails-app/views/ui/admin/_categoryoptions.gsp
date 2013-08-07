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

<div id="categoryOptions" title="Concepts">
  <p style="font-family: serif; color: blue;"> Please select one existing loaded concepts <b>Category</b> </p>
  <select id="category" style="width:60%" >
    <optgroup label="Existing Categories">
       <g:each in="${categoryList}" status="i" var="categoryInstance">
      <option selected="">${categoryInstance.category}</option>
       </g:each>
    </optgroup>
  </select>
</div>