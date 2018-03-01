package fallenArch.beans

class DictLoader{
  private val seized=Entry(
    "seized",
    Raw("take","x take y"),
    "take hold of suddenly and forcibly",
    SequenceX( List(
      ("have seized-suspected of-in violation of-","Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"),
      ("have seized","Ψ1 have seized Ψ2"),
      ("have seized-suspected of","Ψ1 have seized Ψ2 suspected of Ψ3"))),
    QuestionsX(Map("who seized"->"Ψ1",
      "what is seized"-> "Ψ2",
      "why it is seized"-> "Ψ3",
      "under which law it is seized"-> "Ψ4",
      "where is seized"->"Ψ5",
      "how it is seized"->"Ψ6")))

  val dictionary:Map[String,Entry]=Map(seized.word ->seized)
}

/*
case class Seize(raw:Raw,
                 meaning:String,
                 sequence:SequenceX,
                 questions:QuestionsX )
case class Raw(typex:String, explore:String)
case class SequenceX(seq:String,pha:String)
case class QuestionsX(quest:Map[String,String])

List("have seized-suspected of-in violation of-",
                "seized",
                "have seized-suspected of",
                "seized-because of
* */