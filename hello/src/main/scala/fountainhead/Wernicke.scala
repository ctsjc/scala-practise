package fountainhead

import fountainhead.Parser.{getNounPhraseList, getPhraseList}
import fountainhead.VerbParser.dictionaryEntryOfMainVerb

/*This will call each major step , Its like a controller for the project */
object Wernicke  extends  App {
  var text: String = "I would like to see better read and write speed as this card has no where near the speed it advertise"
  var phraseList = getPhraseList(text, 0)
  var nounPhraseList = getNounPhraseList(phraseList)

  var p = dictionaryEntryOfMainVerb(nounPhraseList)
  nounPhraseList.map(_.w).foreach(println(_))
  //println(">"+p.template.structures(0).variation)
  // now find all  those thing which are left and right to `would like to see` And `as`
  var dseq = p.template.structures(0).sequence.split("-").toList
  var dss:List[Int]=List()

  p.template.structures(0).variation.split("-").foreach( (v)=>{

    dss=dss :+ nounPhraseList.map(_.w).indexOf(v)
  })


  var dsf:List[phrase2]=List()
  var sc=0
  var leftRight:List[List[phrase2]]=List()
  var left:List[phrase2]=List()
  for(index <- nounPhraseList.indices){
    // last list
    if(sc==dss.length){
      left = left :+nounPhraseList(index)
    }
    // only one element here. its the joint
    else if(index==dss(sc)){
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

  println("==========="+leftRight)

  // noow map them into
  var ml =p.template.structures.head.sequence.split("-") zip leftRight
  val mk=ml.toMap


  // convert to the verb questionaries
  println("----")

  //p.template.structures(0).question.foreach( m => println(m._1+" - "+ mk.get(m._2.toString)))
  // change to clean format
  var vqm:Map[String,String]=Map()
  p.template.structures.head.question.foreach( m => vqm = vqm + (m._1->
    mk.get(m._2.toString).get.map(_.w).mkString(" ") ))

  vqm.foreach( m => println(m._1+"-"+m._2 ))

}
//phraseList = phraseList :+ phrase(splittedList._1, GalDict.galaxy(possiblePhrase))
case class verbQuestion(w:String, q:Map[String, String])