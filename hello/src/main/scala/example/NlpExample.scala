package example

import edu.stanford.nlp.simple.Sentence
import scala.collection.JavaConverters._

object NlpExample extends App {
    println("Helloo Nlp")
    val sent:Sentence =new Sentence("South Korean authorities have seized a second vessel suspected of transferring oil products to North Korea in violation of international sanctions.")

    //val dg=sent.dependencyGraph()
    // println(dg.getRoots)
    //dg.prettyPrint()
    val sequence_struct="Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"
    println("----")
    println(sequence_struct)
    val words=sent.words().asScala
    // nope ... you have to think.... fuck the efficiency.... just get work done.
    // and it should be generic.....

    val lt=sent.tokens().subList(2,5).asScala
    println(lt.map(_.word()))
    //println(sent.tokens().get(1).originalText())
    println("----")

    /*val pos=sent.posTags()
    pos.forEach(println(_))
*/
    //Now we will find the main verb, or sequence


}
