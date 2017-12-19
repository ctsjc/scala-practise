package fountainhead.dict

case class DEntry(id:DID, word:String,typ:String, nature:String,template:DTemplate)
case class DTemplate(structures:List[DStructure])
case class DStructure(sequence:String,variation:String, question: Map[String,Any])
case class DID(oid:String)