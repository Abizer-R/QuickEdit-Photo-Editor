package io.github.abizerr.quickedit.engine.impl

import io.github.abizerr.quickedit.engine.api.EditSnapshot

internal class HistoryManager(maxUndo: Int) {
  private val cap = maxUndo.coerceAtLeast(0)
  private val past = ArrayDeque<EditSnapshot>()
  private var present: EditSnapshot? = null
  private val future = ArrayDeque<EditSnapshot>()

  fun setInitial(snapshot: EditSnapshot) { present = snapshot; past.clear(); future.clear() }

  fun push(next: EditSnapshot) {
    present?.let { past.addLast(it); if (past.size > cap) past.removeFirst() }
    present = next; future.clear()
  }

  fun undo(): EditSnapshot? {
    val prev = past.removeLastOrNull() ?: return null
    present?.let { future.addLast(it) }
    present = prev
    return present
  }

  fun redo(): EditSnapshot? {
    val next = future.removeLastOrNull() ?: return null
    present?.let { past.addLast(it); if (past.size > cap) past.removeFirst() }
    present = next
    return present
  }

  fun current(): EditSnapshot? = present
  fun canUndo() = past.isNotEmpty()
  fun canRedo() = future.isNotEmpty()
  fun undoCount() = past.size
  fun redoCount() = future.size
}
