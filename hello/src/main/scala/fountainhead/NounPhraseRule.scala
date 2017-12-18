package fountainhead

case class Rule(seq:String, outcome:String, phrase:String,priority:Int)

object NounPhraseRules{
  var rules:List[Rule]=List(Rule("adj-noun-conjunction-noun-noun","nounphrase","adj-noun-conjunction-noun-noun",1),
    Rule("conjuction-noun-pronoun-verb","nounphrase","verb-noun",1))

}
