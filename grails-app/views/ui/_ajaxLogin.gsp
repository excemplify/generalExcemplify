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


<style>
#ajaxLogin {
   margin: 15px 0px; padding: 0px;
   text-align: center;
   display: none;
   float: right;
   
   font-family: serif;
   position: relative;
}
 #login {
        margin: 30px 0px;
        padding: 0px;
        text-align: center;
        font-family: serif;
        font-size: 12px;
      }

      #login .inner {
        width: 340px;
        padding-bottom: 6px;
        margin: 60px auto;
        text-align: left;
        border: 1px solid #aab;
/*        background-color: #f0f0fa;*/
        -moz-box-shadow: 2px 2px 2px #eee;
        -webkit-box-shadow: 2px 2px 2px #eee;
        -khtml-box-shadow: 2px 2px 2px #eee;
        box-shadow: 2px 2px 2px #eee;
      }

      #login .inner .fheader {
        padding: 18px 26px 14px 26px;
/*        background-color: #f7f7ff;*/
        margin: 0px 0 14px 0;
        color: #2e3741;
        font-size: 18px;
        font-weight: bold;
      }

      #login .inner .cssform p {
        clear: left;
        margin: 0;
        padding: 4px 0 3px 0;
        padding-left: 105px;
        margin-bottom: 20px;
        height: 1%;
      }

      #login .inner .cssform input[type='text'] {
        width: 120px;
      }

      #login .inner .cssform label {
        font-weight: bold;
        float: left;
        text-align: right;
        margin-left: -105px;
        width: 110px;
        padding-top: 3px;
        padding-right: 10px;
      }

      #login #remember_me_holder {
        padding-left: 120px;
      }

      #login #submit {
        margin-left: 15px;
      }

      #login #remember_me_holder label {
        float: none;
        margin-left: 0;
        text-align: left;
        width: 100px
      }

      #login .inner .login_message {
        padding: 6px 25px 20px 25px;
        color: #c33;
      }

      #login .inner .text_ {
        width: 120px;
      }

      #login .inner .chk {
        height: 12px;
      }
</style>

<div id='ajaxLogin'> 
   <div id='login'>
      <div class='inner'>
        <div class='fheader'><g:message code="springSecurity.login.header"/></div>

        <g:if test='${flash.message}'>
          <div class='login_message'>${flash.message}</div>
        </g:if>


   <form action='${request.contextPath}/j_spring_security_check' method='POST'
       id='ajaxLoginForm' name='ajaxLoginForm' class='cssform'>
          <p>
            <label for='username'><g:message code="springSecurity.login.username.label"/>:</label>
            <input type='text' class='text_' name='j_username' id='username'/>
          </p>

          <p>
            <label for='password'><g:message code="springSecurity.login.password.label"/>:</label>
            <input type='password' class='text_' name='j_password' id='password'/>
          </p>

          <p id="remember_me_holder">
          <input type='checkbox' class='chk' name='_spring_security_remember_me' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>/>
          <label for='remember_me'><g:message code="springSecurity.login.remember.me.label"/></label>
          </p>

          <p>
           <input type='submit' id="submit" value='${message(code: "springSecurity.login.button")}'/>
           <input type='button' onclick="cancelLogin()" value='Cancel'/>  
       
          </p>
        </form>
      </div>
    </div>
</div>
  
<!--  
  <div class='inner'> <div class='fheader'>Please Login..</div> <form action='${request.contextPath}/j_spring_security_check' method='POST' id='ajaxLoginForm' name='ajaxLoginForm' class='cssform'> <p> <label for='username'>Login ID</label> <input type='text' class='text_' name='j_username' id='username' /> </p> <p> <label for='password'>Password</label> <input type='password' class='text_' name='j_password' id='password' /> </p> <p> <label for='remember_me'>Remember me</label> <input type='checkbox' class='chk' id='remember_me' name='_spring_security_remember_me'/> </p> <p> <a href='javascript:void(0)' onclick='authAjax(); return false;'>Login</a> <a href='javascript:void(0)' onclick='cancelLogin(); return false;'>Cancel</a> </p> </form> <div style='display: none; text-align: left;' id='loginMessage'></div> </div> </div>-->

<script type='text/javascript'>

// center the form Event.observe(window, 'load', function() { var ajaxLogin = $('ajaxLogin'); $('ajaxLogin').style.left = ((document.body.getDimensions().width - ajaxLogin.getDimensions().width) / 2) + 'px'; $('ajaxLogin').style.top = ((document.body.getDimensions().height - ajaxLogin.getDimensions().height) / 2) + 'px'; });

function showLogin() { $('#ajaxLogin').show(); }

function cancelLogin() { $('#ajaxLogin').hide(); }

</script>
