package ch.waan.mcddpui.predef

import java.util.UUID

trait ViewWindow[T] {

    def showView(uuid: UUID): Unit

}