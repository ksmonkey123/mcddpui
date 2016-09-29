package ch.awae.util.file

import java.io.InputStream
import java.net.URL

object IO {

    implicit class StringToURL(val string: String) extends AnyVal {

        def toURL = new URL(string)

    }

    implicit class Reading[E: InputLike](val element: E) {

        def read[T: InputParserLike] =
            implicitly[InputParserLike[T]].read(implicitly[InputLike[E]].stream(element))

    }

    implicit class Writing[E: OutputLike](val element: E) {
        def write[T: OutputParserLike](t: T) =
            implicitly[OutputParserLike[T]].write(t, implicitly[OutputLike[E]].stream(element))
    }

}