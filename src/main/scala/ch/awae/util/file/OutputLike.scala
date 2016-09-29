package ch.awae.util.file

import annotation.implicitNotFound
import java.net.URL
import java.io.OutputStream
import java.io.FileOutputStream

@implicitNotFound("No member of type class OutputLike in scope for ${T}")
trait OutputLike[T] {
    def stream(t: T): OutputStream
}

object OutputLike {

    implicit object FileNameOutput extends OutputLike[String] {
        def stream(t: String) = new FileOutputStream(t)
    }

    implicit object FileOutput extends OutputLike[java.io.File] {
        def stream(t: java.io.File) = new FileOutputStream(t)
    }

    implicit object OutputStreamOutput extends OutputLike[OutputStream] {
        def stream(t: OutputStream) = t
    }

}