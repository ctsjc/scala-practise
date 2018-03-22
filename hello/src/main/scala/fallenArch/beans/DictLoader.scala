package fallenArch.beans

class DictLoader{
  private val seized=Entry(
    "seized",
    Raw("take","x take y"),
    "take hold of suddenly and forcibly",
    SequenceX( List(
      ("have seized-suspected of-in violation of-","Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"),
      ("have seized","Ψ1 have seized Ψ5"),
      ("seized-suspected of-at-in breach of","Ψ1 seized Ψ2 suspected of Ψ3 at Ψ5 in breach of Ψ4"),
      ("was seized at ","Ψ2 was seized at Ψ5"),
      ("have seized-suspected of","Ψ1 have seized Ψ2 suspected of Ψ3"))),
    QuestionsX(Map("who seized"->"Ψ1",
      "what is seized"-> "Ψ2",
      "why it is seized"-> "Ψ3",
      "under which law it is seized"-> "Ψ4",
      "where is seized"->"Ψ5",
      "how it is seized"->"Ψ6")))

  private val held=Entry("held",
    Raw("",""),
    "",
    SequenceX( List(
      ("is being held at","Ψ1 is being held at Ψ2")
      )),
    QuestionsX(
      Map("what is being held"->"Ψ1",
      "where it is being held"-> "Ψ2",
      ))
  )
  //South Korean authorities impounded a second ship suspected of transferring oil to North Korea in violation of U.N. sanctions
  private val impounded=Entry("impounded",
    Raw("",""),
    "",
    SequenceX( List(
      ("impounded-suspected of-in violation","Ψ1 impounded Ψ2 suspected of Ψ3 in violation Ψ4")
    )),
    QuestionsX(
      Map("who"->"Ψ1",
        "what"-> "Ψ2",
        "why"-> "Ψ3",
        "under which law"-> "Ψ4"
      ))
  )

  //transferring transferring oil products to North Korea
  private val transferring=Entry("transferring",
    Raw("",""),
    "",
    SequenceX( List(
      ("transferring-to","transferring-Ψ1-to-Ψ2")
    )),
    QuestionsX(
      Map("what"->"Ψ1",
        "whom"-> "Ψ2",
        "who"-> "Ψ3",
      ))
  )
  val dictionary:Map[String,Entry]=Map(seized.word ->seized, held.word->held, impounded.word->impounded,
    transferring.word->transferring)
}

/*

private val held=Entry("held",
    Raw("",""),
    "",
    SequenceX( List(
      ("",""),
      ("",""),
      )),
    QuestionsX(
      Map(""->"Ψ1",
      ""-> "Ψ2",
      ))
  )
#1- , was seizrd at #2

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