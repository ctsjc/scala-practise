package fountainhead.dict

case class DEntry(id:DID, word:String,typ:String, nature:String,dstructure:DStructure)
case class DStructure(dsequence:String, dquestion: Map[String,String])
case class DID(oid:String)
