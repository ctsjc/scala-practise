package fountainhead

import fountainhead.Parser.{getNounPhraseList, getPhraseList}
import fountainhead.VerbParser.dictionaryEntryOfMainVerb

/*This will call each major step , Its like a controller for the project */
object Wernicke  extends  App {
  var text: String = "I would like to see better read and write speed as this card has no where near the speed it advertise"
  var phraseList = getPhraseList(text, 0)
  var nounPhraseList = getNounPhraseList(phraseList)

  var dictEntryVerb = dictionaryEntryOfMainVerb(nounPhraseList)
  nounPhraseList.map(_.w).foreach(println(_))


  var indexOf_wouldLikeToSee_As_variation=dictEntryVerb.template.structures.head.variation.split("-").map((v)=>{
    nounPhraseList.map(_.w).indexOf(v)
  }).toList

  //var dsf:List[phrase2]=List()
  var sc=0
  // convert the list into words which are left to would like to see and right of it.
  // List[1] = I
  // List[2] = would like to see
  // List[3] = better read and write speed etc.
  var leftRight:List[List[phrase2]]=List()
  var left:List[phrase2]=List()
  for(index <- nounPhraseList.indices){
    // last list
    if(sc==indexOf_wouldLikeToSee_As_variation.length){
      left = left :+nounPhraseList(index)
    }
    // only one element here. its the joint
    else if(index==indexOf_wouldLikeToSee_As_variation(sc)){
      sc+=1
      // grab old list
      leftRight = leftRight :+ left
      // add the current element
      left=List()
      left = left :+nounPhraseList(index)
      leftRight = leftRight :+ left
      // reset the list
      left=List()

    }
    //   in between elements
    else{
      left = left :+nounPhraseList(index)
    }
  }
  leftRight = leftRight :+ left

  // noow map them into
  // 1->I, 2->would like to see
  var mapOfIndexToPart =dictEntryVerb.template.structures.head.sequence.split("-") zip leftRight toMap

  var questionAnswer=dictEntryVerb.template.structures.head.question.map(m => m._1 -> mapOfIndexToPart(m._2.toString).map(_.w).mkString(" ") )
  questionAnswer.foreach(m => println(m._1+"-"+m._2 ))
}//end of object

case class verbQuestion(w:String, q:Map[String, String])