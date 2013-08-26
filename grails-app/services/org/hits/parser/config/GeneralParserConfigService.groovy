/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hits.parser.config

import org.hits.ui.Experiment   // what we call Experiment, the instance is actually a complete parsing procedure
import org.hits.ui.ExperimentToParserDef
import org.hits.parser.ParserDef
import org.hits.parser.TargetDef
import org.hits.parser.ParserConfiguration
import org.hits.ui.KnowledgeFetcher
import org.hits.ui.Stage
import org.hits.parser.SourceDef
import org.hits.ui.exceptions.MoreThanOneKnowledgeIdentifieredException
import org.hits.parser.excelimp.GeneralCellParserAction
/**
 *
 * @author rongji
 */
class GeneralParserConfigService {   //for two stage task
    
    def config(Experiment experiment){
        //
       
        
        println "start config parsers for such experiment $experiment.id"
        if( beforeConfig(experiment)){
            doConfig(experiment)
        }
                 
        
    }
    def delete(Experiment experiment){
        beforeConfig(experiment)
        
    }
    
    def deleteConfig(Experiment experiment){
        beforeConfig(experiment)
    }
    
    private doConfig(Experiment experiment){

        def stages=experiment.stages
     
        println stages
        def indexes=stages.collect{it.stageIndex}
        int minIndex=indexes.min()
        int maxIndex=indexes.max()
        
        for (int i=minIndex ; i<maxIndex; i++){
            Stage curStage=stages.find{it.stageIndex==i}
            Stage posStage=stages.find{it.stageIndex==i+1}    
             
            def parserDefInstance=ParserDef.findByName("${experiment.id}${curStage.stageName}To${posStage.stageName}")
            if(parserDefInstance){
                log.info "you do not do beforeConfig ?"
                deleteIndividualParserDefinition(parserDefInstance)
            }
            
            
        }
        if(stages.size()==2){  // the case we are now focus on
            def experimentParsingDefinitions=defineStagesParsers(experiment)
            log.info " ExperimentToParserDefs ${experimentParsingDefinitions}"      
        }
   
        
    }
    private defineStagesParsers(experiment){//the parser action is traverser block by block, differ at the how you set the sources
         
        Stage sourceStage=experiment.stages.find{it.stageIndex==1}
        Stage targetStage=experiment.stages.find{it.stageIndex==2}  
        
        def possibleConcepts=sourceStage.stageTemplate.knowledgeList.collect{it.knowledgeName} as Set
      
        println "possibleConcepts $possibleConcepts"  
        def parsingway=sourceStage.stageTemplate.parsingway
        def parserConfigurations=[]
        def parsertype
        def metaAction
        possibleConcepts.each{concept->
            metaAction="NO ACTION"  //default is take no action
            def sourceSources=[]
            def sourceConceptDetails
            if(KnowledgeFetcher.containKnowledge(sourceStage.stageTemplate, ["$concept"])){
                println "contain $concept"
                sourceConceptDetails=KnowledgeFetcher.conceptFetcher(sourceStage.stageTemplate, concept)
                if(parsingway=="recordbased"){
                    parsertype="GeneralRecordParser"   
                }else if(parsingway=="blockbased"){
                    log.info "we are not ready"
                    parsertype="GeneralBlockParser"  //not ready
                }
             
                sourceConceptDetails.each{detail->
                    metaAction=detail.action
                    def cellRange
                    if(detail.cellRange!=null){
                        cellRange=detail.cellRange 
                    }else{
                        cellRange=detail.startCellRange
                    }
                   
                    def source=new SourceDef(cellRange:cellRange, sheetNum:detail.sheetNum,resourceType:"SIMPLE",template:sourceStage.stageTemplate, knowledgeComment:detail.label, autoFindDirection:detail.autoFindDirection);
                    sourceSources << source
             
                } 
     
            }
                    
            def target
            def targetConceptDetails=KnowledgeFetcher.conceptFetcher(targetStage.stageTemplate, concept)
            println "targetConceptDetails ${targetConceptDetails.size()}"
            if(targetConceptDetails.size()==1){
       
                target=new TargetDef(cellRange:"${targetConceptDetails[0].startCellRange}" ,sheetNum:targetConceptDetails[0].sheetNum, sheetName:"auto",  template:targetStage.stageTemplate)
            
            }else{
                throw new MoreThanOneKnowledgeIdentifieredException("duplicate concepts or columns existing in target template! we can not deal with this case now!")
            }
            
            
         
            def conceptParserConfiguration= new ParserConfiguration(sources:sourceSources, 
                name:concept.toString().toLowerCase(),
                action:metaAction.toString(), 
                parserType:parsertype,
                parsingWay:parsingway,
                nodeType:"right", 
                target:target)
            parserConfigurations << conceptParserConfiguration               
      
        }
        println "parserConfigurations $parserConfigurations" 
        
        def parserDefInstance = new ParserDef(name:"${experiment.id}${sourceStage.stageName}To${targetStage.stageName}", nextStageName: "${targetStage.stageName}", parserConfigurations:parserConfigurations)
                
        def experimentToParserDefInstance=new ExperimentToParserDef(experiment:experiment, parserDefs:[parserDefInstance] ).save(failOnError: true);
       
        
        return experimentToParserDefInstance
    }

    
    private beforeConfig(Experiment experiment){
        //find out if there is already some parser configuration set for such experiment
        println "first to make sure it is a clean experiment/procedure"
        boolean proved=true
        
        def existingParsingDefinitions=ExperimentToParserDef.findAllByExperiment(experiment)     
        if(existingParsingDefinitions){
            log.info "cleanning dirty experiment/procedure $experiment.id"
            existingParsingDefinitions.each{stageParserDefinition->
                //get stage specific parser definition instance
                try{
                    deleteStageParsingDefinition(stageParserDefinition)  
                    
                }catch(Exception e){
                    
                    proved=false
                }
            }
        }else{
            log.info " it is a clean experiment/procedure"
        }
        return proved
        
    }
    private deleteStageParsingDefinition(ExperimentToParserDef stageParserDefinition){
        log.info "cleanning dirty stage parsing definition $stageParserDefinition"
        // def templateParserInstance= TemplateToParserDef.findByTemplate(templateInstance)             
        if(stageParserDefinition!=null){                         
            def allParserDefs = []
            allParserDefs+= stageParserDefinition.parserDefs
            stageParserDefinition.parserDefs.clear()
            log.info "parserDefs count ${allParserDefs.size()}"   //get series of parser definitions in such stage 
            allParserDefs.each{definition->
                if(definition){
                    // stageParserDefinition.removeFromParserDefs(definition)
                    deleteIndividualParserDefinition(definition)                 
                }

              
            }
               
            stageParserDefinition.delete()  
            log.info "StageParserDefinition $stageParserDefinition.id delete"
        }
    }
    
    private deleteIndividualParserDefinition(ParserDef definition){
        log.info "cleanning individual dirty parserDef $definition.name"
        def allConfigs=[]
        allConfigs+= definition.parserConfigurations
        definition.parserConfigurations.clear()
        //get all detail configurations inside such individual parser definition instance
        log.info "parserConfigs count for parserDef $definition.id is ${allConfigs.size()}" 
        allConfigs.each{ParserConfiguration conf->
            if(conf){                   
                def allSources=[]
                //get all sources definition in such configuration
                allSources+=conf.sources  
                log.info "sourceDef count for parserConfigs $conf.id is ${allSources.size()}"
                conf.sources.clear()
                allSources.each{source->
                    //clean the source one by one
                    if(source){                  
                        // conf.removeFromSources(source)  
                        if(source.delete())
                        { log.info "sourceDef $source.id delete"}
                    
                    }          
                }
                       
                //get all target definition in such configuration, there is always one target in each configuration              
                def targets=TargetDef.findAllByParserConfiguration(conf)
                targets.each{TargetDef target-> 
                    //clean the target
                    if(target.delete())
                    {log.info "targetDef $target.id delete"}
                }
                
                // definition.removeFromParserConfigurations(conf)  
                if( conf.delete(flush: true)){
                    log.info "parserConfig $conf.id delete"   
                }
            
            }
            
        }
        log.info "parserDef $definition.id delete"
        if( definition.delete()){
            log.info "parserDef $definition.id delete"   
        }

 
    }
    
	
}

