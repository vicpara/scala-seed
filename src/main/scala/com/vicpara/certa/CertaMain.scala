package com.vicpara.certa

import java.util.concurrent.{ ExecutorService, Executors }

import scala.language.reflectiveCalls
import org.rogach.scallop.{ ScallopConf, ScallopOption }

import scala.concurrent.{ Await, ExecutionContext }

// java -cp $(find ~/work/seed -name "*.jar" | tr "\n" ":") com.vicpara.autocs.jobs.FrontUserInteractions
object CertaMain {

  val ioThreadPool: ExecutorService = Executors.newWorkStealingPool(4)
  implicit val ec: ExecutionContext = new ExecutionContext {
    val threadPool: ExecutorService = ioThreadPool

    def execute(runnable: Runnable) {
      threadPool.submit(runnable)
    }

    def reportFailure(t: Throwable): Unit = {
    }
  }

  def main(args: Array[String]) {
    val conf = new ScallopConf(args) {
      val key: ScallopOption[String] = opt[String](required = false, descr = "Key to use to retrieve messages")
      val rw: ScallopOption[Boolean] = opt[Boolean](required = false, descr = "Run just ReadWrite test")
    }
    conf.printHelp()
    println(conf.summary)

  }
}
