package nl.knaw.dans.easy.fixity

import com.yourmediashelf.fedora.client.FedoraClient._
import scala.collection.JavaConversions._

case class PidIterator(namespace: String) extends Iterator[String] {
  var hasNextBatch = true
  var token: String = null
  var batch: Seq[String] = null
  var i = 0

  override def hasNext: Boolean =
    nextFromBatch match {
      case None => nextBatch match {
        case None => false
        case Some(b) => batch = b; true
      }
      case Some(_) => true
    }

  override def next() =
    hasNext match {
      case true => 
        val n = nextFromBatch.get
        i = i + 1
        n
      case false => throw new NoSuchElementException
    }

  def nextFromBatch: Option[String] =
    if (batch == null || i >= batch.size) None
    else Some(batch(i))

  def nextBatch: Option[Seq[String]] =
    if (hasNextBatch) {
      var search = findObjects().pid().query(s"pid~$namespace:*")
      if (token != null && !token.isEmpty) search = search.sessionToken(token)
      val response = search.execute()
      hasNextBatch = response.hasNext
      token = response.getToken
      if (response.getPids.isEmpty()) None
      else Some(response.getPids)
    } else None
}