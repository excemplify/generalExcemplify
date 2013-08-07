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


<html>
  <!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>Log View For Excemplify</title>
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'index.css')}" />
    <link rel="shortcut icon" href="${createLinkTo(dir: 'images/ui/', file: 'logo.ico')}" type="image/x-icon"/>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery-1.5.2.js')}"></script>
    <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'jquery-ui/sunny', file:'jquery-ui-1.8.20.custom.css')}" />
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'highlight.min.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'jquery-1.5.2.js')}"></script>
    <script type="text/javascript" src="${createLinkTo(dir:'js', file:'d3.v2.js')}"></script>
    <style>
      #chart{
        width:100%;
        height:78%;
        min-width: 800px;
        max-width: 1280;
        min-height: 500px;

      }
      text {
        font: 8px serif;
      }

      .dot {
        stroke: #000;
        opacity: 0.5;
      }

      .axis path, .axis line {
        fill: none;
        stroke: #000;
        shape-rendering: crispEdges;
      }

      .label {
        fill: blue;
      }

      .time.label {
        font: 500 30px "Helvetica Neue";
        fill: lightgrey;

      }

      .time.label.active {
        fill: darkblue;
      }

      .overlay {
        fill: none;
        opacity: 0.8;
        pointer-events: all;
        cursor: ew-resize;
        border:1px;
        border-color: grey;
      }

    </style>
  <script type="text/javascript">

          function backToLab(){
     
          document.location.href ="${createLink(uri:'/lab')}"
         }
          function backToPublic(){
     
          document.location.href ="${createLink(uri:'/public')}"
         }

    </script>
  <r:layoutResources/>
</head>
<body>

<g:render template="/ui/header"/>

  <div class="maincontent">
<g:set var="expId" value="${request.getParameter('experimentId')}"/>
 <div class="buttoncontent">
   <sec:ifLoggedIn>
     <span class="navspan" onclick="backToLab()">  <img alt="Back" src="${createLinkTo(dir: 'images/ui', file: 'back.png')}" title="Back" />Back
 </span>
</sec:ifLoggedIn>
   <sec:ifNotLoggedIn>
      <span class="navspan" onclick="backToPublic()">  <img alt="Back" src="${createLinkTo(dir: 'images/ui', file: 'back.png')}" title="Back" />Back
 </span> 
     </sec:ifNotLoggedIn>
 </div>
<div style=" text-align: left">
<p class="title">Log View For Experiment ${request.getParameter('experimentId')} </p>
<div id="chart" ></div>
</div>
  </div>
  <g:applyLayout name="foot">
  </g:applyLayout>
<script>

 // Various accessors that specify the four dimensions of data to visualize.

 function x(d) { return d.times; }
 function y(d) { return d.states; }

 function color(d) { return d.name; }
 function key(d) { return d.file; }

 // Chart dimensions.
var margin = {top: 19.5, right: 50, bottom: 30, left: 50},
   width =  $("#chart").width()- margin.right-margin.left,
   height = $("#chart").height()- margin.top - margin.bottom,
   formate = d3.time.format("%Y-%m-%d %H:%M:%S");
      
 // Load the data.
 //d3.json("../data/log.json", function(log) {
    var log=${session.getAttribute('experimentLog')};

    var timeArray = new Array()
 
 for(var i=0;i<log.length;i++){
  for(var j=0;j<log[i].times.length;j++){
    if(inArray(log[i].times[j][0],timeArray)==-1){
       timeArray.push(log[i].times[j][0]);
       
    }
    

  }
 }
   

    var timeTickArray=new Array()
    if(timeArray.length>10){   
var stepCount=Math.floor(timeArray.length/10)
timeTickArray.push(timeArray[0]);
timeTickArray.push(timeArray[timeArray.length-1]);
  for(var z=0;z<timeArray.length;z=z+stepCount){
    if(inArray(timeArray[z],timeTickArray)==-1){
       timeTickArray.push(timeArray[z]);
       
    }
    

  }
    }else{
     timeTickArray=timeArray; 
    }  
 function inArray(elem,array)
{
var len = array.length;
for(var i = 0 ; i < len;i++)
{
   if(array[i] == elem){return i;}
}
return -1;
} 

  timeArray=timeArray.sort(timeorder);

   
  var domainArray=["deactive","initial","add/create","active","update","converted to xls","auto generate","export"]
 // Various scales. These domains make assumptions of data, naturally.


 var xScale = d3.scale.ordinal().domain(timeArray).rangePoints([100, width]),       
     yScale = d3.scale.ordinal().domain(domainArray).rangeBands([height,0], 1),

     colorScale = d3.scale.category20();

 // The x & y axes.

 var xAxis = d3.svg.axis().orient("bottom").scale(xScale).tickValues(timeTickArray),  
 yAxis = d3.svg.axis().scale(yScale).orient("left");
      

 // Create the SVG container and set the origin.
 var svg = d3.select("#chart").append("svg")
     .attr("width", width + margin.left + margin.right)
     .attr("height", height + margin.top + margin.bottom)
      .append("g")
     .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

 // Add the x-axis.
 svg.append("g")
     .attr("class", "x axis")
     .attr("transform", "translate(0," + height*(domainArray.length-1)/(domainArray.length+1) + ")")
     .call(xAxis);

 // Add the y-axis.
 svg.append("g")
     .attr("class", "y axis")
     .attr("transform", "translate(100,0)")
     .call(yAxis);
      
for (var q=0;q<domainArray.length;q++) {
  if(q<1){
     svg.append("line")
   .attr("x1", 100)
   .attr("y1", height*(domainArray.length)/(domainArray.length+1))
   .attr("x2", width)
   .attr("y2", height*(domainArray.length)/(domainArray.length+1))
   .style("stroke", "grey")
   .attr("stroke-dasharray", "10 10");    
  }
  if(q>1){
     svg.append("line")
   .attr("x1", 100)
   .attr("y1", height*(domainArray.length-q)/(domainArray.length+1))
   .attr("x2", width)
   .attr("y2", height*(domainArray.length-q)/(domainArray.length+1))
   .style("stroke", "rgb(6,120,155)").attr("stroke-dasharray", "10 10");   
  }
}    
      
   


 // Add an x-axis label.
 svg.append("text")
     .attr("class", "x label")
     .attr("text-anchor", "end")
     .attr("x", width)
     .attr("y", height*(domainArray.length-1)/(domainArray.length+1) - 6)
     .text("times");

 // Add a y-axis label.
 svg.append("text")
     .attr("class", "y label")
     .attr("text-anchor", "end")
     .attr("x", 6)
     .attr("y", 106)
     .attr("dy", ".75em")
     .attr("transform", "rotate(-90)")
     .text("states");

 // Add the time label; the value is set on transition.
 var label = svg.append("text")
     .attr("class", "time label")
     .attr("text-anchor", "end")
     .attr("y", height/(domainArray.length+1)-24 )
     .attr("x", 200+width/2)
     .text(timeArray[0]);


// alert(timeArray);
 //var bisect = d3.bisector(function(d) { return d[0]; });

   // Add a dot per nation. Initialize the data at 1800, and set the colors.
   var dot = svg.append("g")
       .attr("class", "dots")
       .selectAll(".dot")
       .data(interpolateData(formate.parse(timeArray[0]))).enter()
     .append("g").call(position);
      
   dot.append("circle").attr("class", "dot").attr("r", function(d){if(d.blot=="blot1"){return 15;}else if(d.blot=="blot2"){return 20;}else if(d.blot=="blot3"){return 25;}else{return 10;}})
     .style("fill", function(d) { return colorScale(color(d)); });
//      .sort(order);
    
   // Add a title.
   dot.append("title")
       .text(function(d) { return d.file; });

 dot.append("text").attr("transform", function(d) {if(d.blot=="blot1"){return "translate(0," +20*d.name.length/10+ ")";}else if(d.blot=="blot2"){return "translate(0," +25*d.name.length/20+ ")";}else if(d.blot=="blot3"){return "translate(0," +40*d.name.length/20+ ")";}else{return "translate(0," +18+ ")";} }).attr("text-anchor", "middle").text(function(d) {return d.name});

   // Add an overlay for the time label.
   var box = label.node().getBBox();
  

   var overlay = svg.append("rect")
         .attr("class", "overlay")
         .attr("x", box.x)
         .attr("y", box.y)
         .attr("width", box.width)
         .attr("height", box.height)
         .on("mouseover", enableInteraction);

   // Start a transition that interpolates the data based on time.
   svg.transition()
       .duration(20000)
       .delay(1000) 
       .ease("cubic-in-out")
       .tween("time", tweenTime)
       .each("end", enableInteraction);

   // Positions the dots based on data.
   function position(dot) {
    
               dot.attr("transform", function(d) { return "translate(" + x(d)+ "," + y(d) + ")"; });

   }

   // Defines a sort order so that the smallest dots are drawn on top.
   function order(a, b) {
   if(a==null||b==null){return 0;}
   else if (formate.parse(a[0]).getTime()==formate.parse(b[0]).getTime()){return 0;}
   else if(formate.parse(a[0]).getTime()>formate.parse(b[0]).getTime()){return 1;}
   else{return -1;} 
   }
    
      function timeorder(a, b) {
   if(a==null||b==null){return 0;}
   else if (formate.parse(a).getTime()==formate.parse(b).getTime()){return 0;}
   else if(formate.parse(a).getTime()>formate.parse(b).getTime()){return 1;}
   else{return -1;} 
   }

   // After the transition finishes, you can mouseover to change the year.
   function enableInteraction() {

     var timeScale = d3.scale.ordinal().domain(timeArray).rangePoints([box.x+10, box.x+box.width-10]);

     // Cancel the current transition, if any.
     svg.transition().duration(0);

     overlay
         .on("mouseover", mouseover)
         .on("mouseout", mouseout)
         .on("mousemove", mousemove)
         .on("touchmove", mousemove);

     function mouseover() {
       label.classed("active", true);
     }

     function mouseout() {
       label.classed("active", false);
     }

     function mousemove() {
      label.classed("active", true);
       // alert(timeScale.invert(d3.mouse(this)[0]));
      displayTimeDuringInteraction(formate.parse(timeScale.invert(d3.mouse(this)[0])));
     }
   }
    
   function displayTimeDuringInteraction(time) {

     dot.data(interpolateData(time), key).call(position);

    label.text(formate(time));
 
 
   }

   // Tweens the entire chart by first tweening the year, and then the data.
   // For the interpolated data, the dots and label are redrawn.
   function tweenTime() {

 var time = d3.interpolateRound(0, timeArray.length);



     return function(t) { displayTime(time(t));};
   }


   // Updates the display to show the specified time.
   function displayTime(time) {

var currenttime=formate.parse(timeArray[time]);


     dot.data(interpolateData(currenttime), key).call(position);

    label.text(formate(currenttime));
 
 
   }

   // Interpolates the dataset for the given (fractional) time.
   function interpolateData(time) {
 // alert("display "+time);
  
     return log.map(function(d) {
       
       return {
         name: d.name,
         file: d.file,
         blot:d.blot,
         states: interpolateValues(d.states.sort(order) , time),
         times: interpolateTimeValues(d.times.sort(order) , time)
       };
            
     });
   }

   // Finds (and possibly interpolates) the value for the specified time.
   function interpolateValues(values, time) { 
   
   var index=0;
   for(var i=0;i<values.length;i++){
     if(i==0){
        if(formate.parse(values[i][0]).getTime()<=time.getTime()){
          index=i;
   
        }
     }else{
        if(formate.parse(values[i-1][0]).getTime()<time.getTime()){
           index=i;
        }
     }
     
   }

a = values[index];

   if (index > 0) {
     var b = values[index - 1];

     if(formate.parse(b[0]).getTime()<=time.getTime()&&formate.parse(a[0]).getTime()>time.getTime()){
if((formate.parse(a[0]).getTime()-formate.parse(b[0]).getTime())>30000 &&(formate.parse(a[0]).getTime()-time.getTime())<30000){
       return yScale(b[1])+((time.getTime()-formate.parse(b[0]).getTime())*(yScale(a[1])-yScale(b[1])) /(formate.parse(a[0]).getTime()-formate.parse(b[0]).getTime()));
    
   }else{
    return yScale(b[1]);

   }
       //   return yScale(b[1]);
    
     }else if(formate.parse(a[0]).getTime()<=time.getTime()){
       return yScale(a[1]);
  
     }

   }else{
           
     if(formate.parse(a[0]).getTime()<=time.getTime()){
   
     return yScale(a[1]);
 
   } else if(formate.parse(a[0]).getTime()>time.getTime()) {
   
  return -101;
   }
 }
  
}


   // Finds (and possibly interpolates) the value for the specified time.
   function interpolateTimeValues(values, time) { 
 
   var index=0;
   for(var i=0;i<values.length;i++){
     if(i==0){
        if(formate.parse(values[i][0]).getTime()<=time.getTime()){
          index=i;
   
        }
     }else{
        if(formate.parse(values[i-1][0]).getTime()<time.getTime()){
           index=i;
        }
     }
     
   }

a = values[index];

   if (index > 0) {
     var b = values[index - 1];
      
     if(formate.parse(b[0]).getTime()<=time.getTime()&&formate.parse(a[0]).getTime()>time.getTime()){

if((formate.parse(a[0]).getTime()-formate.parse(b[0]).getTime())>30000 &&(formate.parse(a[0]).getTime()-time.getTime())<30000){
    return xScale(formate.parse(b[1]))+(xScale(formate.parse(a[1]))-xScale(formate.parse(b[1])))*(time.getTime()-formate.parse(b[0]).getTime())/(formate.parse(a[0]).getTime()-formate.parse(b[0]).getTime());
   
       }else{
      return xScale(formate.parse(b[1])); 
       }
    //return xScale(formate.parse(b[1])); 
     
    
     }else if(formate.parse(a[0]).getTime()<=time.getTime()){
       return xScale(formate.parse(a[1]));
  
     }else if(formate.parse(b[0]).getTime()>time.getTime()){
   
    return xScale(formate.parse(b[1])); 
     }
     
   }else{
     if(formate.parse(a[0]).getTime()>time.getTime()){
return -100;
   
 //return xScale(formate.parse(time));
 
   }else{
  return xScale(formate.parse(a[1]));

   }
 }
  
}



// });

</script>

<r:layoutResources/>
</body>
</html>
