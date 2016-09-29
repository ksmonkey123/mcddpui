package ch.awae.util.file

import annotation.implicitNotFound
import java.io.InputStream
import java.io.FileInputStream
import java.net.URL

@implicitNotFound("No member of type class InputLike in scope for ${T}")
trait InputLike[T] {
    def stream(t: T): InputStream
}

object InputLike {

    implicit object FileNameInput extends InputLike[String] {
        def stream(t: String) = new FileInputStream(t)
    }

    implicit object URLInput extends InputLike[URL] {
        def stream(t: URL) = t.openStream()
    }

    implicit object FileInput extends InputLike[java.io.File] {
        def stream(t: java.io.File) = new FileInputStream(t)
    }

    implicit object InputStreamInput extends InputLike[InputStream] {
        def stream(t: InputStream) = t
    }

}