package com.mycontacts.contact.edit.manager;

import java.util.ArrayDeque;
import java.util.Deque;

import com.mycontacts.contact.edit.command.EditContactCommand;

public class UndoRedoManager {
	private final Deque<EditContactCommand> undoStack = new ArrayDeque<>();
	private final Deque<EditContactCommand> redoStack = new ArrayDeque<>();

	public void executeCommand(EditContactCommand command) {
		if (command == null) {
			return;
		}
		command.execute();
		undoStack.push(command);
		redoStack.clear();
	}

	public void undo() {
		if (undoStack.isEmpty()) {
			return;
		}
		EditContactCommand command = undoStack.pop();
		command.undo();
		redoStack.push(command);
	}

	public void redo() {
		if (redoStack.isEmpty()) {
			return;
		}
		EditContactCommand command = redoStack.pop();
		command.execute();
		undoStack.push(command);
	}
}
