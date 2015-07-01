package com.github.hatimiti.flutist.common.message;

import static java.util.Objects.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class AppMessagesContainer implements Serializable {

	/** key=owner(GlobalMessagesの場合はOptinal.empty()) */
	protected Map<Optional<Owner>, AppMessages<?>> messages;

	public AppMessagesContainer() {
		this.messages = new HashMap<>();
	}

	public void add(AppMessages<?> messages) {
		requireNonNull(messages);
		if (messages instanceof OwnedMessages) {
			Owner owner = ((OwnedMessages) messages).getOwner();
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

	public GlobalMessages getGlobalMessages() {
		return getGlobalMessageList();
	}

	public GlobalMessages getInfoGlobalMessages() {
		return getGlobalMessageList().filterByInfoLevel();
	}

	public GlobalMessages getWarnGlobalMessages() {
		return getGlobalMessageList().filterByWarnLevel();
	}

	public GlobalMessages getErrorGlobalMessages() {
		return getGlobalMessageList().filterByErrorLevel();
	}

	public OwnedMessages getOwnedMessages(Owner owner) {
		return getOwnedMessageList(owner);
	}

	public OwnedMessages getInfoOwnedMessages(Owner owner) {
		return getOwnedMessageList(owner).filterByInfoLevel();
	}

	public OwnedMessages getWarnOwnedMessages(Owner owner) {
		return getOwnedMessageList(owner).filterByWarnLevel();
	}

	public OwnedMessages getErrorOwnedMessages(Owner owner) {
		return getOwnedMessageList(owner).filterByErrorLevel();
	}

//	public Map<Owner, OwnedMessages> getOwnedMessagesByPrefixGroupByOwner(String ownerPrefix) {
//		return this.messages.entrySet().stream()
//			.filter(e -> e.getKey().map(k -> k.toString()).orElse("").startsWith(ownerPrefix))
//			.map(e -> (OwnedMessages) e.getValue())
//			.collect(Collectors.groupingBy(OwnedMessages::getOwner, OwnedMessages.merge));
//	}

	public List<OwnedMessages> getOwnedMessagesByPrefix(String ownerPrefix) {
		return this.messages.entrySet().stream()
			.filter(e -> e.getKey().map(k -> k.toString()).orElse("").startsWith(ownerPrefix))
			.map(e -> (OwnedMessages) e.getValue())
			.collect(Collectors.toList());
	}

	public boolean hasGlobalMessages() {
		return !this.messages.get(Optional.empty()).isEmpty();
	}

	public boolean hasOwnedMessagesOf(Owner owner) {
		requireNonNull(owner);
		return !getOwnedMessages(owner).isEmpty();
	}

	public AppMessages<?> getAllMessageList() {
		return this.messages.values().stream()
				.reduce((a, b) -> { a.addAll(b); return a; }).orElse(new GlobalMessages());
	}

	protected GlobalMessages getGlobalMessageList() {
		return (GlobalMessages) this.messages.computeIfAbsent(Optional.empty(), k -> new GlobalMessages());
	}

	protected OwnedMessages getOwnedMessageList(Owner owner) {
		return (OwnedMessages) this.messages.computeIfAbsent(Optional.of(owner), k -> new OwnedMessages(owner));
	}

	@Override
	public String toString() {
		StringBuilder r = new StringBuilder(
				this.getClass().getName() + "@" + this.hashCode() + System.lineSeparator());
		r.append("[" + this.messages.toString() + "]");
		return r.toString();
	}

}
