package com.github.hatimiti.flutist.common.message;

import static java.util.Objects.*;
import static java.util.function.Function.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class AppMessagesContainer implements Serializable {

	/** key=owner(GlobalMessagesの場合はOptinal.empty()) */
	protected Map<Optional<String>, AppMessages> messages;

	public AppMessagesContainer() {
		this.messages = new HashMap<>();
	}

	public void add(AppMessages messages) {
		requireNonNull(messages);
		if (messages instanceof OwnedMessages) {
			String owner = ((OwnedMessages) messages).getOwner();
			getOwnedMessageList(owner).addAll(messages);
		} else {
			getGlobalMessageList().addAll(messages);
		}
	}

	public void addAll(AppMessagesContainer container) {
		requireNonNull(container);
		this.messages.putAll(container.messages);
	}

	public boolean isEmpty() {
		return this.messages.isEmpty();
	}

	public List<AppMessage> getGlobalMessages() {
		return getGlobalMessageList();
	}

	public List<AppMessage> getInfoGlobalMessages() {
		return getGlobalMessageList().filterByInfoLevel();
	}

	public List<AppMessage> getWarnGlobalMessages() {
		return getGlobalMessageList().filterByWarnLevel();
	}

	public List<AppMessage> getErrorGlobalMessages() {
		return getGlobalMessageList().filterByErrorLevel();
	}

	public List<AppMessage> getOwnedMessages(String owner) {
		return getOwnedMessageList(owner);
	}

	public List<AppMessage> getInfoOwnedMessages(String owner) {
		return getOwnedMessageList(owner).filterByInfoLevel();
	}

	public List<AppMessage> getWarnOwnedMessages(String owner) {
		return getOwnedMessageList(owner).filterByWarnLevel();
	}

	public List<AppMessage> getErrorOwnedMessages(String owner) {
		return getOwnedMessageList(owner).filterByErrorLevel();
	}

	public List<AppMessage> getOwnedMessagesByPrefix(String ownerPrefix) {
		return this.messages.entrySet().stream()
			.filter(e -> e.getKey().map(identity()).orElse("").startsWith(ownerPrefix))
			.map(e -> e.getValue())
			.reduce((a, b) -> { a.addAll(b); return a; })
			.orElse(new OwnedMessages(ownerPrefix));
	}

	public boolean hasGlobalMessages() {
		return !this.messages.isEmpty();
	}

	public boolean hasOwnedMessagesOf(String owner) {
		requireNonNull(owner);
		return !getOwnedMessages(owner).isEmpty();
	}

	public AppMessages getAllMessageList() {
		return this.messages.values().stream()
				.reduce((a, b) -> { a.addAll(b); return a; }).orElse(new GlobalMessages());
	}

	protected AppMessages getGlobalMessageList() {
		return this.messages.computeIfAbsent(Optional.empty(), k -> new GlobalMessages());
	}

	protected AppMessages getOwnedMessageList(String owner) {
		return this.messages.computeIfAbsent(Optional.of(owner), k -> new OwnedMessages(owner));
	}

	@Override
	public String toString() {
		StringBuilder r = new StringBuilder(
				this.getClass().getName() + "@" + this.hashCode() + System.lineSeparator());
		r.append("[" + this.messages.toString() + "]");
		return r.toString();
	}

}
