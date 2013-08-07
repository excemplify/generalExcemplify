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

<div id="savesourcetemplateas" title="Save As">
  <p id="purposeintro">The template will be treated as an unknown template, the containing "Concepts" will be different from its "Column Labels"</p>
  <p style="display: none" id="purpose"></p>
  <br>
  It will be saved under the name:  <input id="saveas" type="text" size="30" value="${oldname}">

  <br>

  <table class="saveastable" id="savetable"> 
<!--   Please define tell us which action to take for which concept
<g:if test="${uniqueKnowledgeMap?.size()>0}">
      <g:each in="${uniqueKnowledgeMap}" status="i" var="uniqueKnowlegeInstance">
        <tr>
          <td>Concept <b>${uniqueKnowlegeInstance.key}</b> take </td>
          <td> <g:if test="${uniqueKnowlegeInstance.value>0}">
          <select id="${uniqueKnowlegeInstance.key}action" style="width:60%" >
            <optgroup label="We Currently Consider Only">
              <option selected="">COPY</option>
              <option selected="">COPYANDASSIGN</option>
            </optgroup>
          </select>
        </g:if>
        <g:else>
          <select id="${uniqueKnowlegeInstance.key}action" style="width:60%" >
            <optgroup label="We Currently Consider Only">
              <option selected="">MERGE</option>
              <option selected="">TRANSPOSELIST</option>
              <option selected="">TRANSPOSELISTANDASSIGNLABEL</option>
            </optgroup>
          </select>
        </g:else>        
        </td>
        </tr>
      </g:each>
    </g:if>
    <g:else>
      <color red > you do not have any concept defined!!!</color>
    </g:else>-->


    <tr>
      <td style="width:40%">
      The template are </td>
      <td style="width:60%" > 
        <select id="parsingway" >         
      <option selected="true" >recordbased</option>
      <option >blockbased</option>

    </select></td>

    </tr>

  </table>

</div>

