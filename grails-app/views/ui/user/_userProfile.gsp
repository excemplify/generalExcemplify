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

<table>
  <tr>
    <td><label for="curusername" style="width: 300px"><b>login name</b></label></td>
    <td><input name="curusername" id="curusername"  type="text" size="10" value="${userInstance?.username}" /><input name="curuserid" id="curuserid" style="display: none"  type="text" value="${sec.loggedInUserInfo(field:"id")}"/></td>
  </tr>
  <tr><td><br></td></tr>

  <tr>
    <td><label for="curuserpwd" style="width: 300px"><b>password</b></label></td>

    <td><input name="curuserpwd" id="curuserpwd" type="text" size="40" value="${userInstance?.password}" /></td>

  </tr>
    <tr><td colspan="2" style="color: blue" >(note: leave it if you do not want to change password)</td></tr>
  <tr><td><br></td></tr>
  <tr>
    <td><label for="curemail" style="width: 300px"><b>SEEK VLN email</b></label></td>
    <td><input name="curemail" id="curemail"  type="text" size="15" value="${userInstance?.seekEmailAddress}" /></td>
  </tr>
</table>
