package example

import java.util.Dictionary

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
    val sent:Sentence =new Sentence(text)


    //val dg=sent.dependencyGraph()
    // println(dg.getRoots)
    //dg.prettyPrint()
    val dict_sequence="Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"


    val parsed_dict_seq=dict_sequence.split("Ψ[0-9]+[ ]?").filter(!_.isEmpty).map(_.trim)
    var map:Map[Int,String] =Map()

    var k:Int=0
    val t=parsed_dict_seq.map( _.split(" "))
    var start:Int=0
    var end:Int=0
    var mapIndex=1
    if(parsed_dict_seq.forall(text.contains(_))){
        // that word and exact that word.... then divide the list
        t.foreach(d => {
            var index=0
            // possible introduction of bug. we have to find the consecutive, so deciding the initial value of k is bit tricky
            var l:List[Int]=Nil
            while(index < d.length ){
                k=sent.words.asScala.indexOf(d(index),k)
                l=l:+k
                index=1+index
            }

            // now list should have only consecutive elements
            // TODO
            // now slice the sentence and put them into the map
            end=l(0)
            var seq= sent.words.asScala.slice(start,end)
            //map = map+(mapIndex -> seq.mkString(" "),mapIndex+1->d.mkString(" ") )
            //mapIndex+=2
            map = map+(mapIndex -> seq.mkString(" "))
            mapIndex+=1
            start=l.last+1
        })
        map = map+(mapIndex -> sent.words.asScala.drop(start).mkString(" "))
        for((k,v) <- map) println(s"$k - $v")
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

**/