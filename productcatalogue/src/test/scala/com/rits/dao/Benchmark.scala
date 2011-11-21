package com.rits.dao
import com.rits.model.Product
import com.rits.model.Price
import scala.io.Source
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level
import java.util.concurrent.atomic.AtomicInteger
import com.googlecode.concurrent.ExecutorServiceManager

/**
 * @author kostantinos.kougios
 *
 * 20 Nov 2011
 */
object Benchmark extends App {

	val THREADS = 64

	import Daos._

	// mute logger
	val logger = LoggerFactory.getLogger("com.googlecode.mapperdao").asInstanceOf[ch.qos.logback.classic.Logger]
	logger.setLevel(Level.ERROR)

	// load the words file
	val words = Source.fromInputStream(getClass.getResourceAsStream("/benchmark/words.txt")).getLines.toArray
	val wordsSz = words.size
	val wordCounter = new AtomicInteger

	val persistCsv = new StringBuilder("persist\niterations,throughput\n")
	val selectCsv = new StringBuilder("select\niterations,throughput\n")

	val loops = new AtomicInteger

	println("starting benchmark")

	List(5000, 1000, 5000, 50000, 250000, 500000, 1000000) foreach { iterations =>
		println("%d iterations".format(iterations))
		doPersist(iterations)
		doSelect(iterations)
	}

	def doPersist(iterations: Int) {
		cleanup
		loops.set(0)
		val dt = persist(iterations, THREADS)
		val throughput = (1000 * loops.get) / dt
		println("persist: threads %d , loops: %d , %d millis, throughput: %d / sec".format(THREADS, loops.get, dt, throughput))
		persistCsv append loops.get append "," append throughput append "\n"
		println(persistCsv.toString)
	}
	def persist(iterations: Int, threads: Int) = benchmark {
		ExecutorServiceManager.lifecycle(threads, iterations / 100) { i =>
			for (j <- 1 to 100)
				try {
					val a1 = attributesDao.getOrCreate(nextWord, nextWord)
					val a2 = attributesDao.getOrCreate(nextWord, nextWord)
					val attributes = Set(a1, a2)

					val cat = categoriesDao.createHierarchy(List(nextWord, nextWord))
					val categories = List(cat)
					val p = Product(nextWord, nextWord, Set(Price("GBP", 10.5, 9.99), Price("EUR", 12.50, 11.05)), attributes, categories, Set(nextWord, nextWord))
					productsDao.create(p)
					loops.incrementAndGet
				} catch {
					case e => System.err.println(e)
				}
		}
	}

	def doSelect(iterations: Int) {
		loops.set(0)
		val dt = select(iterations, THREADS)
		val throughput = (1000 * loops.get) / dt
		println("select: threads %d , loops: %d , %d millis, throughput: %d / sec".format(THREADS, loops.get, dt, throughput))
		selectCsv append loops.get append "," append throughput append "\n"
		println(selectCsv.toString)
	}
	def select(iterations: Int, threads: Int) = benchmark {
		println("getting list of product id's")
		val ids = jdbc.queryForList("select id from product").map(_("id").asInstanceOf[Int]).grouped(THREADS).toList
		println("running benchmark with %d sets of id's".format(ids.size))
		ExecutorServiceManager.lifecycle(threads, ids) { list =>
			list.foreach { id =>
				productsDao.retrieve(id)
				loops.incrementAndGet
			}
		}
	}

	def nextWord = {
		val pos = wordCounter.incrementAndGet % wordsSz
		words(pos)
	}
	def cleanup = {

		println("cleaning up")

		List("tags", "price", "product_attribute", "product_category").foreach { table =>
			jdbc.update("truncate table %s".format(table))
			jdbc.update("vacuum full analyse %s".format(table))
		}
		List("category", "attribute", "product").foreach { table =>
			println("deleting %s".format(table))
			jdbc.update("delete from %s".format(table))
			println("vacuum %s".format(table))
			jdbc.update("vacuum full analyse %s".format(table))
		}
		println("done cleaning up")
	}

	def benchmark(f: => Unit) = {
		val start = System.currentTimeMillis
		f
		val dt = System.currentTimeMillis - start
		dt
	}
}