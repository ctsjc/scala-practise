package fallenArch

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.simple.Sentence
import fallenArch.forfun.text

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

    println(" Done ")
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
        def getSequence(verb:String,sentence:String):String={
            // Convert it to read from json
            val sequences=List("have seized-suspected of-in violation of-",
                "seized",
                "have seized-suspected of",
                "seized-because of")

            sequences.map(seq => {
                percentageMatch(sentence, seq)
            }).reduceLeft((x,y)=>{
                if(x._1 > y._1)
                    x
                else if(x._1 == y._1 && x._2 > y._2)
                    x
                else
                    y
            })._3
        }

        def percentageMatch(sentence:String, dictionarySequence:String): (Int,Int,String) = {

            val l:List[String]= sentence.split(" ").toList
            val m:List[String] = dictionarySequence.split("-").filter(!_.isEmpty).map(_.trim).toList

            var lastFound : Int = 0
            var curFound : Int = 0
            var totalFound : Int = 0

            for (d<-m;e<- l.sliding( d.split(" ").size)) {
                if( d == e.mkString(" ")) {
                    curFound=l.indexOfSlice(e)
                    if(curFound < lastFound)
                        return ((totalFound/d.size)*100,m.size,dictionarySequence)
                    totalFound +=1
                    lastFound=curFound
                }
            }
            ((totalFound/m.size)*100,m.size,dictionarySequence)
        }

        var dict_sequence:String= getSequence(verb,text) //"Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"
        println(dict_sequence)
        // So there will be map seq-> ΨSeq
        dict_sequence="Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"
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