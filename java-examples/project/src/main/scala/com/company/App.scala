package com.company

/**
 * @author ${user.name}
 */
object App {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {
    println( "Hello World!THis is my first step to Spark!!HAPPYYYYY!!!" )
    println("concat arguments = " + foo(args))
  }

}
