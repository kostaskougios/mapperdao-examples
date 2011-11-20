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

	val csv = new StringBuilder("iterations,throughput\n")
	cleanup
	println("warming up ...")

	persist(1000, THREADS)
	println("starting benchmark")

	(10000 to 100000 by 10000) foreach { iterations =>
		cleanup
		val dt = persist(iterations, THREADS)
		val throughput = (1000 * iterations) / dt
		println("threads %d , iterations: %d , %d millis, throughput: %d / sec".format(THREADS, iterations, dt, throughput))
		csv append iterations append "," append throughput append "\n"
		println(csv.toString)
	}

	def persist(iterations: Int, threads: Int) = benchmark {

		ExecutorServiceManager.lifecycle(threads, iterations / 100) { i =>
			for (i <- 1 to 100)
				try {
					val a1 = attributesDao.getOrCreate(nextWord, nextWord)
					val a2 = attributesDao.getOrCreate(nextWord, nextWord)
					val attributes = Set(a1, a2)

					val cat = categoriesDao.createHierarchy(List(nextWord, nextWord))
					val categories = List(cat)
					val p = Product(nextWord, nextWord, Set(Price("GBP", 10.5, 9.99), Price("EUR", 12.50, 11.05)), attributes, categories, Set(nextWord, nextWord))
					productsDao.create(p)
				} catch {
					case e => System.err.println(e)
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
			jdbc.update("vacuum %s".format(table))
		}
		List("category", "attribute", "product").foreach { table =>
			println("deleting %s".format(table))
			jdbc.update("delete from %s".format(table))
			println("vacuum %s".format(table))
			jdbc.update("vacuum %s".format(table))
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