package fountainhead


object VerbParser {
  def process(nounPhraseList:List[phrase2]):List[phrase2]={
    // now find out the main verb, conjunction and structure
    var verbs=nounPhraseList.filter( (p)=>if(p.t == "verb" || p.t =="conjuction") true else false)

    // check into db to find the matching strength
    // search with verb and conjuction ???

    verbs
  }
}
