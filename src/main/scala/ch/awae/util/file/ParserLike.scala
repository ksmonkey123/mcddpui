package ch.awae.util.file

import annotation.implicitNotFound
import java.io.InputStream
import java.io.OutputStream
import scala.io.Source
import java.nio.file.Files
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

@implicitNotFound("No member of type class InputParserLike in scope for ${T}")
trait InputParserLike[T] {
    def read(input: InputStream): T
}

@implicitNotFound("No member of type class OutputParserLike in scope for ${T}")
trait OutputParserLike[T] {
    def write(item: T, output: OutputStream): Unit
}

object InputParserLike {

    implicit object StringInputParser extends InputParserLike[String] {
        def read(input: InputStream) = Source.fromInputStream(input).mkString
    }

    implicit object ImageInputParser extends InputParserLike[BufferedImage] {
        def read(inputStream: InputStream) = ImageIO.read(inputStream)
    }

}

object OutputParserLike {
    
    implicit class BufferedImageToTuple(val image : BufferedImage) extends AnyVal {
        def apply(format : String) = (image, format)
    }

    implicit object StringOutputParser extends OutputParserLike[String] {
        def write(item: String, output: OutputStream) = {
            val writer = new BufferedWriter(new OutputStreamWriter(output))
            try {
                writer write item
                writer.flush
            } finally {
                writer.close
            }
        }
    }

    implicit object ImageOutputParser extends OutputParserLike[(BufferedImage, String)] {
        def write(item: (BufferedImage, String), output: OutputStream) = ImageIO.write(item._1, item._2, output)
    }
}