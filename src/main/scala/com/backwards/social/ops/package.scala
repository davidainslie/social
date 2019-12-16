package com.backwards.social

package object ops {
  implicit class Pipe[T](val v: T) extends AnyVal {
    def |>[U](f: T => U): U = f(v)
  }
}