package net.fabricmc.fabric.api.util;

import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;

public class TriMap<K, L, R> {

	private final Set<Entry<K, L, R>> entries = Sets.newHashSet();

	private void addEntry(K key, L left, R right) {
		this.entries.add(new Entry<>(key, left, right));
	}

	public void add(K key, L left, R right) {
		for(Entry<K, L, R> entry : entries){
			if(entry.key == key){
				return;
			}
		}
		this.addEntry(key, left, right);
	}

	public void remove(K key){
		for(Entry<K, L, R> entry : entries){
			if(entry.key == key){
				entries.remove(entry);
				return;
			}
		}
	}

	public void forEach(TriConsumer<K, L, R> action){
		Objects.requireNonNull(action);
		for (Entry<K, L, R> entry : entries) {
			action.accept(entry.key, entry.left, entry.right);
		}
	}

	private static class Entry<K, L, R>{
		private final K key;
		private final L left;
		private final R right;

		private Entry(K k, L l, R r){
			this.key = k;
			this.left = l;
			this.right = r;
		}
	}
}
