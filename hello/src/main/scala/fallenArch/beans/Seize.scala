package fallenArch.beans

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
}*/
case class Seize(raw:Raw,meaning:String,sequence:SequenceX,questions:QuestionsX ) {

}
case class Raw(typex:String, explore:String)

case class SequenceX(seq:String,pha:String)

case class QuestionsX()