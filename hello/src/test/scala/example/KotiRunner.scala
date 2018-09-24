package example

import java.io.StringReader

import edu.stanford.nlp.parser.nndep.DependencyParser
import edu.stanford.nlp.process.DocumentPreprocessor
import edu.stanford.nlp.tagger.maxent.MaxentTagger
import fallenArch.{NlpWerner, PosMeaning}

object KotiRunner extends App {
  var nlpWerner:NlpWerner=new NlpWerner

  //var newsPassage="South Korean authorities have seized a second vessel suspected of transferring oil products to North Korea in violation of international sanctions. The Panama-flagged tanker, KOTI, was seized at Pyeongtaek-Dangjin port, on the west coast, a South Korean customs official said. A marine official said the seizure had happened recently. The KOTI’s estimated arrival at the port was 19 December, according to VesselFinder, a tracking service. The Yonhap news agency reported that the ship could carry 5,100 tonnes of oil and had a crew mostly from China and Myanmar. Yonhap said South Korea’s intelligence and customs officials were conducting a joint investigation into the vessel. A foreign ministry spokesman confirmed that an investigation was under way."
  var newsPassage="South Korean authorities have seized a second vessel suspected of transferring oil products to North Korea in violation of international sanctions."

  var sentenceList=nlpWerner.passageToSentences(newsPassage)


  var tagger=new MaxentTagger("res/english-left3words-distsim.tagger")
  var parser=DependencyParser.loadFromModelFile(DependencyParser.DEFAULT_MODEL)

  sentenceList.zipWithIndex.foreach{case(v,i)=>
    println(v)
    nlpWerner.noAdjSentence(v)
    //val tagged=tagger.tagString(v).split(" ").map(_.split("_")).map(t=>(t(0),PosMeaning.getLable(t(1))))

    //for( (k,v) <- tagged) println(k+"\t"+v)
   /* val entryX=nlpWerner.getEntry(v,null)
    for((x,y)<- entryX.questions.quest) println(x+" -> "+y)
    println()*/
  }
}
