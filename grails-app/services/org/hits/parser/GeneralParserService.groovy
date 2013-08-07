
/* ===================================================
 * Copyright 2010-2013 HITS gGmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ========================================================== */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hits.parser


import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.web.context.request.RequestContextHolder
import javax.servlet.http.HttpSession
import groovy.util.Eval

import org.hits.parser.core.*
import org.hits.parser.excelimp.*
import org.hits.ui.Template
import org.hits.ui.Experiment
import org.hits.ui.ExperimentToParserDef
import org.hits.parser.ParserConfiguration
import org.hits.ui.exceptions.CustomizedException

/**
 *
 * @author rongji
 */

class GeneralParserService {

    // static transactional = true   default is true

    def getAllParserNames(){
        def names=[]
        ParserDef.findAll().each{
            names<<it.name
        }
        return names
    }
    
   
    def parseSpreadsheet(byte[] fileData,  params) throws ParsingException,CustomizedException{  //params are those parameters outside general Experiment properties
        
        InputStream is = new ByteArrayInputStream(fileData)
        File file = File.createTempFile(session.getAttribute("fileName"),"")
        file.bytes=fileData
        
        parseSpreadsheet(file,params)
        
    }
    
    
    def parseSpreadsheet(File file, params ) throws ParsingException, CustomizedException{//params are those parameters outside general Experiment properties
        String nextStageName = "target"
        Experiment experiment=params.experiment
        if(params.targetStageIndex!=null){
            nextStageName=experiment.stages.find{it.stageIndex==params.targetStageIndex}.stageName
        }

        HttpSession session = getSession()
        
      
        StateAndQueue state = session.getAttribute("StateAndQueue")?:new StateAndQueue()  
        state.state.experimentWorkbookFile=session.getAttribute("ExperimentWorkbook") //the current experiment results/planning/etc workbook
        state.state.file=file // the file for parsing
        state.state.fileName=session.getAttribute("fileName")
        println "params $params"

        try{
              
            println "about to get the parser"
            
            Block rootBlock = createParserBlock(state, nextStageName,params)
           
            rootBlock.applyQueue(state)
              
            
            //  session.putValue("blankcells",state.state.blankcells)// this needs to be more general
        
            session.putValue("parsedFile",state.state.parsedFile)
            session.putValue("parsedFileName","parsed_"+state.state.fileName)
          
           
            return state
        }
           
        catch(ParsingException pex){
            println    pex.message  
            println "exception caught applying queue"
            throw pex
            //return //something to the controller about the need for more input
        } 
        catch(CustomizedException pex){
            println    pex.getMessage() 
            println "some customized exception caught"
            throw pex
  
            //return //something to the controller about the need for more input
        } 
    }

  
    def createParserBlock(StateAndQueue state, String targetStageName, params) throws ParserConfigException{
        Experiment experiment=params.experiment
        def parserDef = ExperimentToParserDef.findByExperiment(experiment).parserDefs.find{ it.nextStageName==targetStageName }
       
        println "creating parser block for name: ${targetStageName}"
        
        //println parserDef.parserConfigurations
        parserDef.parserConfigurations.each{
            println it.properties
        }
        Block rootBlock
        Block prevBlock = null
        def rootNodTypeConfigurations= parserDef.parserConfigurations.findAll{it.nodeType=="root"}
        if(rootNodTypeConfigurations==null || rootNodTypeConfigurations.size()==0){
            int max=1
                parserDef.parserConfigurations.each{ParserConfiguration config->
                    if(config.sources.size()>=max){
                        max=config.sources.size()
                    }
        
                }

   
            println "max $max"
            rootNodTypeConfigurations=parserDef.parserConfigurations.findAll{it.sources.size()==max}
            println "config found ${rootNodTypeConfigurations.size()}"
        }
        if(rootNodTypeConfigurations.size()>1){
            println" more than one root nodType configurations ?"  
        }

        def rootnewmap=[:]
        //def configmap=it.properties
        rootNodTypeConfigurations[0].properties.each{k,v-> rootnewmap.put(k,v)}
            
        def rootconfigmap=rootnewmap
        rootconfigmap.put("experiment", Experiment)
        Block rblock=new Block(name:rootNodTypeConfigurations[0].name, parser:ParserFactory.getParser(state,rootconfigmap) )//configmap has to hold source, target etc
        rootBlock=rblock
        prevBlock=rblock
   
        parserDef.parserConfigurations.findAll{it.name!=rootNodTypeConfigurations[0].name}.each{ it->
            def newmap=[:]
            //def configmap=it.properties
            it.properties.each{k,v-> newmap.put(k,v)}
            
            def configmap=newmap
            configmap.put("experiment", Experiment)

            //iterate thru list of config mappings
            //may need to double check the tree-likeness of this structure
            Block block = new Block(name:it.name, parser:ParserFactory.getParser(state,configmap) )//configmap has to hold source, target etc
            println "block=${block.name}"
            if (prevBlock!=null){
                if(configmap.nodeType=="root"){
                    rootBlock=block
                    rootBlock.right=prevBlock
               

                }else if (configmap.nodeType=="right"){
                    prevBlock.right = block
                }
                else{
                    prevBlock.down=block
                }
            }
            else {//we have the root node
                rootBlock=block
            }
            prevBlock=block
        }
        rootBlock.buildQueue(state)
        
        return rootBlock
        
        
    }
    
   
    
    
    private HttpSession getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }
  

}
