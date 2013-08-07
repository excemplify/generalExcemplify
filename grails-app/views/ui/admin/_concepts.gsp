<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>
<g:if test="${conceptLists==null}">

    <h5><a href="#section1">Concepts</a></h5> 
    <div>
      <p>Please <a style="cursor: pointer" href="#" onclick="loadConcepts()"> Load </a>Your Target Concepts Set First!</p>

    </div>
 
</g:if>
<g:else>

    <h5><a href="#section1">${category} Concepts</a></h5>
    <div>

      <g:each in="${conceptLists}" status="i" var="conceptInstance">
        <span align="middle" id="${conceptInstance.concept}" title=""> <label id="${conceptInstance.concept}label" style="border: 1px black solid; font-size: small; background-color:randomcolor">${conceptInstance.concept}${randomcolor}</label>
          <g:javascript>
            var color=getRandomColor();
            $("#${conceptInstance.concept}label").css({'background-color': color}); 
            $("#${conceptInstance.concept}").attr('title',color);
            $("#${conceptInstance.concept}").draggable({appendTo: 'body',cursor:'move',containment:'#area', helper: "clone", scroll:false});
          </g:javascript>
        </span >


      </g:each>
      <br>
      <br>
   
  </div>
</g:else>

