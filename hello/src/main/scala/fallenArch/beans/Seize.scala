package fallenArch.beans

import fallenArch.beans.tagType.{dashedKey, tridentValue}

/*{
  "raw": {
    "typex":"take",
    "explore":"x take y."
  },
  "meaning":"take hold of suddenly and forcibly",
  "sequences":["Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4",
    "Ψ1 have seized Ψ2",
    "Ψ1 have seized Ψ2 suspected of Ψ3"],
  "questions":{
    "who seized":"Ψ1",
    "what is seized": "Ψ2",
    "why it is seized": "Ψ3",
    "under which law it is seized": "Ψ4",
    "where is seized":"ΨX",
    "how it is seized":"ΨX"
  }
  ("have seized-suspected of-in violation of-","Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"),
}*/
package object tagType {
  trait dashedSequenceSeriesType
  trait tridentSequenceSeriesType

  import shapeless.tag.@@

  type dashedKey=String @@ dashedSequenceSeriesType
  type tridentValue=String @@ tridentSequenceSeriesType
}
case class Entry(word:String,
                 raw:Raw,
                 meaning:String,
                 sequence:SequenceX,
                 questions:QuestionsX )
case class Raw(typex:String, explore:String)
case class SequenceX(pairs: List[(dashedKey,tridentValue)] )
case class QuestionsX(quest:Map[String,String])

