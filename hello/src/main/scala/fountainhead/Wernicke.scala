package fountainhead

import fountainhead.Parser.{getNounPhraseList, getPhraseList}
import fountainhead.VerbParser.{dictionaryEntryOfMainVerb,getMainVerbQuestionAnswer}

/*This will call each major step , Its like a controller for the project */
object Wernicke  extends  App {
  var text: String = "I would like to see better read and write speed as this card has no where near the speed it advertise"
  var phraseList = getPhraseList(text, 0)
  var nounPhraseList = getNounPhraseList(phraseList)
  var dictEntryVerb = dictionaryEntryOfMainVerb(nounPhraseList)
  var mvq=getMainVerbQuestionAnswer(dictEntryVerb,nounPhraseList)
  println(mvq.w)
  mvq.q.foreach{case (key, value) => println (key + "-->" + value)}

  // now check for another sentence

}//end of object