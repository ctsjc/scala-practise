package fountainhead

import fountainhead.Parser.{getNounPhraseList, getPhraseList}
import fountainhead.VerbParser.process

/*This will call each major step , Its like a controller for the project */
object Wernicke  extends  App{
  var text: String = "I would like to see better read and write speed as this card has no where near the speed it advertise"
  var phraseList=getPhraseList(text,0)
  var nounPhraseList=getNounPhraseList(phraseList)

  var p = process(nounPhraseList)
  p.foreach(println(_))

}
