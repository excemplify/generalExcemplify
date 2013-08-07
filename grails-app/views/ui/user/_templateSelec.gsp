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
<table class="templateSelect">
  <tr>
    <td colspan="2" ><label><b>Using Templates (Stage Specific):</b></label></td>
  </tr>
  <tr>
    <td style=" width:60px"><label for="sourcetemplate">Source Template</label> </td>
    <td>
      <select name="sourcetemplate" id="sourcetemplate">
        <g:each in="${sourceTemplateInstanceList}" status="i" var="stemplateInstance">
          <option value="${stemplateInstance.id}">${i+1}. ${fieldValue(bean: stemplateInstance, field: "templateName")}</option>
        </g:each>
      </select>
    </td>
  </tr>
   <tr>
    <td style=" width:60px"><label for="targettemplate">Target Template</label> </td>
    <td>
      <select name="targettemplate" id="targettemplate">
        <g:each in="${targetTemplateInstanceList}" status="i" var="ttemplateInstance">
          <option value="${ttemplateInstance.id}">${i+1}. ${fieldValue(bean: ttemplateInstance, field: "templateName")}</option>
        </g:each>
      </select>
    </td>
  </tr>
</table>
