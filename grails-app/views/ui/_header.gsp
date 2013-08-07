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

<div class="ui-widget-header" style="height: 11%" > 
  <table style="width: 100%;  vertical-align:  middle;">
    <tr><td colspan="1" style=" text-align: left; color: #ffffff; cursor: pointer" >  <a href="${createLink(uri:'/')}" > 
          <img alt="logo"  title="Back to Welcome Page" src="${createLinkTo(dir:'images/ui', file:'excemplify.png')}" style="cursor: pointer" ></a></td></tr>
    <tr>  <td style="width:100%; text-align: right;vertical-align: baseline; font-family: serif;font-size: 15px; font-weight:normal;  color: black">
    <sec:ifLoggedIn>
      <img alt="log in as"  src="${resource(dir: 'images/ui', file: 'user.png')}" /><sec:username/>(<g:link class="link" controller="logout">log out</g:link>)
    
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
      <g:link controller="login" class="link" action="auth" >Login</g:link>
    </sec:ifNotLoggedIn>
  </td>
  </tr>
  
  </table>
</div>
