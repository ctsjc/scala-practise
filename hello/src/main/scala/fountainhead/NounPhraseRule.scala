package fountainhead

case class Rule(seq:String, outcome:String,priority:Int)

object NounPhraseRules{
  var rules:List[Rule]=List{Rule("adj-noun-conjunction-noun","noun",1)}
}
