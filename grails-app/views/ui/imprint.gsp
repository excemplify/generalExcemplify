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
--}%<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${resource(dir:'css',file:'index.css')}" />
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
    <title>Imprint-Excemplify</title>
        <link rel="shortcut icon" href="./images/ui/logo.ico" />
  </head>
  <body>
    <div class="ui-widget-header" > 
      <table style="width: 100%; vertical-align:  middle ">
        <tr><td colspan="2" style=" text-align: left; color: #ffffff; cursor: pointer">
            <a href="${createLink(uri:'/')}"> <img  alt="home"  title="Back to Welcome Page" src="${resource(dir: 'images/ui/', file: 'excemplify.png')}" style="cursor: pointer" ></a></td></tr>
        <tr>
          <td  style=" width: 70%; text-align: left; vertical-align: baseline;">
         </td>
          <td style="width: 30%; text-align: right;vertical-align: baseline;">
        <sec:ifLoggedIn>
        <img alt="log in as " src="${resource(dir: 'images/ui/', file: 'user.png')}" /> <sec:username/> (<g:link class="userInfo" controller="logout">log out</g:link>)
      </sec:ifLoggedIn>
      <sec:ifNotLoggedIn>
          <g:link controller="login" class="menuButton" action="auth" >Login</g:link>
      </sec:ifNotLoggedIn>
        </td></tr></table>
    </div>
    <div  class="maincontent">

      <br>
      <br>
      <div class="corecontent"> 
      <table id="contact" style="font-family:serif; padding: 1em" bgcolor="#FFFFFF" border="0"  cellpadding="2" cellspacing="0" title="Contact" >
        <caption><b>Contact</b></caption>
        <tr>
            <td ><span>
              <br><b> Priv.-Doz. Dr. Wolfgang Müller  </b><br><br>
              Scientific  Databases and Visualization<br>HITS gGmbH<br>Schloss-Wolfsbrunnenweg 35<br>69118&nbsp;Heidelberg<br>Telefon: +49 (0)6221 - 533 - 231<br><span>
                <script language="JavaScript">
                <!--
                var x = "wolfgang.mueller";
                var y = "h-its";
                var z = "&#46;org";
                document.write ('<' + 'br' + '>' + 'Email: ');
                document.write('<' + 'a' + ' href="&#109;a&#105;&#108;t&#111;:' + x + '&#64;' + y + z +'"' + '>');
                document.write(x + '&#64;' + y + z +'</a>');
                //-->
                </script></span>
              <br>

            </span><br>
          </td>
        </tr>

        <tr>
          <td ><span>
              <br><b> Lenneke Jong </b><br><br>
              Scientific  Databases and Visualization<br>HITS gGmbH<br>Schloss-Wolfsbrunnenweg 35<br>69118&nbsp;Heidelberg<br>Telefon: +49 (0)6221 - 533 - 214<br>
              <br>

            </span><br>
          </td>
          <td ><span>
              <br><b> Lei Shi </b><br>
              <br>
              Scientific Databases and Visualization<br>HITS gGmbH<br>Schloss-Wolfsbrunnenweg 35<br>69118&nbsp;Heidelberg<br>Telefon: +49 (0)6221 - 533 - 214<br><span>
                <script language="JavaScript">
                <!--
                var x = "Lei.Shi";
                var y = "h-its";
                var z = "&#46;org";
                document.write ('<' + 'br' + '>' + 'Email: ');
                document.write('<' + 'a' + ' href="&#109;a&#105;&#108;t&#111;:' + x + '&#64;' + y + z +'"' + '>');
                document.write(x + '&#64;' + y + z +'</a>');
                //-->
                </script></span>
              <br>

            </span><br>
          </td>
          <td>

          </td>

        </tr>
        <tr>
          <td width="592" class="text">&nbsp;</td>
        </tr>
        <tr>
          <td width="592">
            <br><br>

          </td>
        </tr>
      </table>

    </div>
    </div>
 <g:applyLayout name="foot">
  </g:applyLayout>
  </body>
</html>
