package fallenArch

import edu.stanford.nlp.simple.Sentence
import fallenArch.beans.{DictLoader, Entry, QuestionsX, SequenceX}

import scala.collection.JavaConverters._

object NlpExample extends App {
    println("Hello Nlp")
    val text= "South Korean authorities have seized a second vessel suspected of transferring oil products to North Korea in violation of international sanctions."

    val dl:DictLoader=new DictLoader
    val sent=toSentence(text)
    val mainVerb=getMainVerb(sent)
    val dict_entry:Entry=getEntry(mainVerb)
    val dict_sequence=getSequence(mainVerb,sent.text())
    val splittedSeq=splitSequence(dict_sequence._1)
    val entryX=getMap(dict_sequence._2,splittedSeq,sent)
    println(entryX)

    println(" Done ")
    def toSentence(text:String):Sentence={
        new Sentence(text)
    }

    def getMainVerb(sentence: Sentence):String ={
        println(sentence.text())
/*
        var x:CoreAnnotations.TextAnnotation=new CoreAnnotations.TextAnnotation()
        var y =sentence.dependencyGraph().getFirstRoot.getString(x.getClass)
        y
        */
        "seized"
    }

    def getEntry(mainVerb: String):Entry ={
        dl.dictionary(mainVerb)
    }
    def getSequence(verb:String, text:String):(String,Entry) ={

        // It will check the matching sequence in the dictionary
        // get sequences for verb
        // check which fits perfect
        // return that one to back
        // Read the sequences from dictionary and select which matches most...
        def getSequence(verb:String,sentence:String):(String,Entry)={
            // Convert it to read from json
            val sequences=dl.dictionary(verb).sequence.pairs.map(_._1)

            var resp=sequences.map(seq => {
                percentageMatch(sentence, seq)
            }).reduceLeft((x,y)=>{
                if(x._1 > y._1)
                    x
                else if(x._1 == y._1 && x._2 > y._2)
                    x
                else
                    y
            })._3
            //println(">>>"+resp+""+dl.dictionary(verb).sequence.pairs.find(_._1==resp))
            val newEntry=dl.dictionary(verb).copy(sequence = SequenceX(List(dl.dictionary(verb).sequence.pairs.find(_._1==resp).get)))
            (dl.dictionary(verb).sequence.pairs.find(_._1==resp).get._2,newEntry)
        }

        def percentageMatch(sentence:String, dictionarySequence:String): (Int,Int,String) = {

            val l:List[String]= sentence.split(" ").toList
            val m:List[String] = dictionarySequence.split("-").filter(!_.isEmpty).map(_.trim).toList

            var lastFound : Int = 0
            var curFound : Int = 0
            var totalFound : Int = 0
           // println("--------------------")
            for (d<-m;e<- l.sliding( d.split(" ").size)) {
             //   println(">> "+d+" === "+e)
                if( d == e.mkString(" ")) {

                    curFound=l.indexOfSlice(e)
                    if(curFound < lastFound)
                        return ((totalFound/d.size)*100,m.size,dictionarySequence)
                    totalFound +=1
                    lastFound=curFound
                   // println("totalFound "+totalFound)
                }
            }
            ((totalFound/m.size)*100,m.size,dictionarySequence)
        }

        var dict_sequence:(String,Entry)= getSequence(verb,text) //"Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"
        // So there will be map seq-> ΨSeq
       // dict_sequence="Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"
        dict_sequence
    }

    def splitSequence(dict_sequence:String):List[Array[String]]={
        val parsed_dict_seq=dict_sequence.split("Ψ[0-9]+[ ]?").filter(!_.isEmpty).map(_.trim).toList
        var splittedSequence= parsed_dict_seq.map( _.split(" "))
        splittedSequence
    }

    def getMap(entry:Entry,splittedSeq:List[Array[String]], sent:Sentence):Entry={
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
                end=l.head
                map = map+(mapIndex -> sent.words.asScala.slice(start,end).mkString(" "))
                start=l.last+1
                mapIndex+=1
            })
            map = map+(mapIndex -> sent.words.asScala.drop(start).mkString(" "))
        }
        var mq:Map[String,String]=Map()

        entry.questions.quest.foreach(  (e) => {
            if (map.contains( e._2.substring(1).toInt ))
                mq +=(e._1 -> map( e._2.substring(1).toInt ))
        })
        mq.foreach(e=>println(e._1+"-"+e._2))
        val newEntry=entry.copy(questions = QuestionsX(mq))
        newEntry
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

    QuestionsX(Map("who seized"->"Ψ1",
      "what is seized"-> "Ψ2",
      "why it is seized"-> "Ψ3",
      "under which law it is seized"-> "Ψ4",
      "where is seized"->"ΨX",
      "how it is seized"->"ΨX")))


Entry(
seized,
Raw(take,x take y),
take hold of suddenly and forcibly,
SequenceX(
    List((have seized-suspected of-in violation of-,Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4))),
QuestionsX(
    Map(
        under which law it is seized -> international sanctions .,
        why it is seized -> transferring oil products to North Korea,
        who seized -> South Korean authorities,
        what is seized -> a second vessel)))
**/