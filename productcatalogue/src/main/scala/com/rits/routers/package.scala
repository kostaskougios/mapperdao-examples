package com.rits.routers
package object routers {

	import ru.circumflex.web._
	import org.apache.commons.lang3.StringUtils

	import scala.collection.mutable.HashMap

	type GrcvMap = HashMap[String, HashMap[String, HashMap[String, String]]]
	/**
	 * a group has a name, a column and row name and a value, hence (group name, column name, row name, value)
	 * where value is the submited value. I.e. for param("price[unitPrice,1]")="val"
	 * the group name is "price", the column is "unitPrice", the row name is "1" and the
	 * value is "val"
	 */
	protected class Groups(groups: GrcvMap) {
		/**
		 * calls the provided function for each row of the group. The function must be
		 * a function row name,Map(column name, value) => V
		 */
		def group[V](groupName: String)(f: (String, Map[String, String]) => V): Iterable[V] = groups(groupName).map(t => f(t._1, t._2.toMap))
	}

	def getGroups =
		{
			val m = new GrcvMap
			param.filter { _._1.contains('[') }.foreach { t =>
				val (k, v) = t

				import StringUtils._
				val g = substringBetween(k, "[", "]")
				if (g.isEmpty) throw new IllegalArgumentException("invalid name:%s".format(k))
				val group = substringBefore(k, "[")
				val column = substringBefore(g, ",")
				val row = substringAfter(g, ",")

				val rows = m.getOrElseUpdate(group, new HashMap[String, HashMap[String, String]])
				val columns = rows.getOrElseUpdate(row, new HashMap[String, String])
				if (columns.contains(column)) throw new IllegalArgumentException("column already contained : %s".format(column))
				columns += column -> v
			}
			new Groups(m)
		}
}
