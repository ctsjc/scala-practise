package fallenArch

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.simple.Sentence

import scala.collection.JavaConverters._

object NlpExample extends App {
    println("Hello Nlp")
    val text= "South Korean authorities " +
      "have seized " +
      "a second vessel " +
      "suspected of " +
      "transferring oil products to North Korea " +
      "in violation of " +
      "international sanctions."

    val sent=toSentence(text)
    val mainVerb=getMainVerb(sent)

    val dict_sequence=getSequence(mainVerb,sent.text())
    val splittedSeq=splitSequence(dict_sequence)
    val map=getMap(splittedSeq,sent)
    for((k,v) <- map) println(s"$k - $v")

    def toSentence(text:String):Sentence={
        new Sentence(text)
    }



    def getMainVerb(sentence: Sentence):String ={
        println(sentence.text())
        var x:CoreAnnotations.TextAnnotation=new CoreAnnotations.TextAnnotation()
        var y =sentence.dependencyGraph().getFirstRoot.getString(x.getClass)
        println(">><<>>"+y)
        y
    }

    def getSequence(verb:String, text:String):String ={

        // It will check the matching sequence in the dictionary
        // get sequences for verb
        // check which fits perfect
        // return that one to back
        // Read the sequences from dictionary and select which matches most...

        val dict_sequence:String="Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"
        dict_sequence
    }

    def splitSequence(dict_sequence:String):List[Array[String]]={
        val parsed_dict_seq=dict_sequence.split("Ψ[0-9]+[ ]?").filter(!_.isEmpty).map(_.trim).toList
        var splittedSequence= parsed_dict_seq.map( _.split(" "))
        splittedSequence
    }

    def getMap(splittedSeq:List[Array[String]], sent:Sentence):Map[Int,String]={
        var map:Map[Int,String] =Map()
        var tracker:Int=0
        var start:Int=0
        var end:Int=0
        var mapIndex=1
        if(splittedSeq.flatten.forall(sent.text().contains(_))){
            splittedSeq.foreach(d => {
                var index=0
                var l:List[Int]=Nil
                while(index < d.length ){
                    tracker=sent.words.asScala.indexOf(d(index),tracker)
                    l=l:+tracker
                    index=1+index
                }
                end=l(0)
                map = map+(mapIndex -> sent.words.asScala.slice(start,end).mkString(" "))
                start=l.last+1
                mapIndex+=1
            })
            map = map+(mapIndex -> sent.words.asScala.drop(start).mkString(" "))
        }
        map
    }

    println("----")

    /*val pos=sent.posTags()
    pos.forEach(println(_))
*/
    //Now we will find the main verb, or sequence


}

/*
Customer has questioned about his deliverable.
He has not performed as per client's expectation.
Customer suggested to look for replacement of him. Learning score also very poor.

You need more improvement on client deliverables.
Please be sure that your code deliverables should be zero/minimal defects and good in quality.
Please improve proactive communication skill as well.


Refactor
Add NLP parser
Add one test for it

Start about Spark code
**/