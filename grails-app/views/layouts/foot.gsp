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

<div id="footer" class="footer">
      <div id="footerNavigation">
        <ul>
          <li><a target="blank" href="http://www.h-its.org">&copy; Hits gGmbH</a></li>
          <li>|<a href="${createLink(uri:'/imprint')}">Imprint</a></li>
          <li>|<g:link controller="admin" class="menuButton" action="downloadTutorial">Tutorial</g:link></li>
        </ul>
      </div>
      <br>
      <br>
      <div id="funding">
        <ul><li id="item1">
            <a  href="http://www.dfg.de/" title="project funder" target="_blank"></a></li>
        <!--      <a href="http://www.klaus-tschira-stiftung.de/english/index.html" target="_blank"><img alt="Sabio-RK is funded by the Klaus Tschira Foundation (KTF)" src="images/ui/ktf.png"/></a>-->
          <li id="item2"><a href="http://h-its.org/" title="implemented by SDBV group" target="_blank"></a></li>
          <li id="item3"><a  href="http://www.dkfz.de/" title="cooperate with DKFZ" target="_blank"></a></li>
          <li id="item4"><a  href="http://sabiork.h-its.org/" title="associated database" target="_blank"></a></li>
          <li id="item5"><a  href="http://seek.virtual-liver.de" title="other related project" target="_blank"></a></li>
        </ul>
      </div>
 </div>


