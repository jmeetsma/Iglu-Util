/*
 * Copyright 2011-2013 Jeroen Meetsma - IJsberg
 *
 * This file is part of Iglu.
 *
 * Iglu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Iglu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Iglu.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.ijsberg.iglu.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * Wrapper class for a specific part of a collection
 * (which is currently to be displayed in some user interface).
 * The collection may have been divided in parts to make it browsable.
 * The wrapper has data on other parts of the full collection as well.
 */
public class CollectionPart {
	private Collection part;
	private int fullCollectionSize;
	private int maxPartSize;
	private int offset;
	private CollectionPartDescription[] collectionPartIds;
	private int currentCollectionPartIdIndex;

	/**
	 * Describes this and other parts of a collection
	 */
	public static class CollectionPartDescription {
		public CollectionPartDescription(int offset, int size, boolean isCurrentCollectionPart) {
			this.offset = offset;
			this.size = size;
			this.isCurrentCollectionPart = isCurrentCollectionPart;
		}

		public int offset;
		public int size;
		public boolean isCurrentCollectionPart;
	}


	/**
	 * Constructs a CollectionPart based on a complete Collection.
	 *
	 * @param full		the complete collection this is part of
	 * @param maxPartSize size of this part or less if it's the last bit
	 * @param offset	  distance in elements from 0
	 */
	public CollectionPart(Collection full, int maxPartSize, int offset) {
		this.fullCollectionSize = full.size();
		this.maxPartSize = maxPartSize;
		this.offset = offset;
		part = new ArrayList();
		if (offset < full.size()) {
			int count = 0;
			Iterator i = full.iterator();
			while (i.hasNext()) {
				Object o = i.next();
				if ((count >= offset) && (count < (offset + maxPartSize))) {
					part.add(o);
				}
				count++;
			}
		}
		calculateCollectionPartDescriptions();
	}

	/**
	 * Constructs a CollectionPart based on a previously defined subset of a Collection.
	 *
	 * @param part			   subset of the complete Collection
	 * @param maxPartSize		size of this part or less if it's the last bit
	 * @param offset			 distance in elements from 0
	 * @param fullCollectionSize size of the complete collection
	 */
	public CollectionPart(Collection part, int maxPartSize, int offset, int fullCollectionSize) {
		this.fullCollectionSize = fullCollectionSize;
		this.maxPartSize = maxPartSize;
		this.offset = offset;
		this.part = part;
		calculateCollectionPartDescriptions();
	}


	/**
	 * @return subset of the complete collection
	 */
	public Collection getPart() {
		return part;
	}


	/**
	 * @return size of the complete collection
	 */
	public int getFullCollectionSize() {
		return fullCollectionSize;
	}


	/**
	 * @return default size of collection parts
	 */
	public int getMaxPartSize() {
		return maxPartSize;
	}


	/**
	 * @return actual size of this part of the collection
	 */
	public int getPartSize() {
		if (part == null) {
			return 0;
		}
		return part.size();
	}


	/**
	 * @return the total number of parts the complete collection is divided in
	 */
	public int getNrofParts() {
		int result = fullCollectionSize / maxPartSize;
		int rest = fullCollectionSize % maxPartSize;
		if (rest > 0) {
			return result + 1;
		}
		return result;
	}


	/**
	 * @return the size of the last part of the collection (which mey be less than the desired size)
	 */
	public int getLastPartSize() {
		int rest = fullCollectionSize % maxPartSize;
		if (rest == 0) {
			return maxPartSize;
		}
		return rest;
	}

	/**
	 * @return a String representation of the wrapped Collection
	 */
	public String toString() {
		return part.toString();
	}


	/**
	 * @return a description of the preceeding CollectionPart or null if that does not exist
	 */
	public CollectionPartDescription getPreceedingCollectionPartDescription() {
		if (currentCollectionPartIdIndex > 0) {
			return collectionPartIds[currentCollectionPartIdIndex - 1];
		}
		return null;
	}


	/**
	 * @return a description of the trailing CollectionPart or null if that does not exist
	 */
	public CollectionPartDescription getTrailingCollectionPartDescription() {
		if (currentCollectionPartIdIndex < collectionPartIds.length - 1) {
			return collectionPartIds[currentCollectionPartIdIndex + 1];
		}
		return null;
	}

	/**
	 * @return a description of the first CollectionPart or null if that does not exist
	 */
	public CollectionPartDescription getFirstCollectionPartDescription() {
		if (collectionPartIds.length > 4 && 0 != currentCollectionPartIdIndex && 1 != currentCollectionPartIdIndex) {
			return collectionPartIds[0];
		}
		return null;
	}

	/**
	 * @return a description of the last CollectionPart or null if that does not exist
	 */
	public CollectionPartDescription getLastCollectionPartDescription() {
		if (collectionPartIds.length > 4 && (collectionPartIds.length - 1) != currentCollectionPartIdIndex && (collectionPartIds.length - 2) != currentCollectionPartIdIndex) {
			return collectionPartIds[collectionPartIds.length - 1];
		}
		return null;
	}


	/**
	 * @param scope the number of CollectonParts around this CollectionPart to be shown
	 * @return descriptions of CollectionParts within a certain range around this part
	 */
	public CollectionPartDescription[] getNearCollectionPartDescriptions(int scope) {
		if (scope % 2 == 0) {
			scope += 1;
		}
		int halfScope = (scope - 1) / 2;

		if (scope >= getNrofParts()) {
			return collectionPartIds;
		}


		CollectionPartDescription[] result = new CollectionPartDescription[scope];

		if (currentCollectionPartIdIndex - halfScope < 0) {
			System.arraycopy(collectionPartIds, 0, result, 0, scope);
		}
		else if (currentCollectionPartIdIndex + halfScope > collectionPartIds.length - 1) {
			System.arraycopy(collectionPartIds, collectionPartIds.length - 1 - scope, result, 0, scope);
		}
		else {
			System.arraycopy(collectionPartIds, currentCollectionPartIdIndex - halfScope, result, 0, scope);
		}

		return result;
	}


	/**
	 * @return descriptions of all collection parts the complete collection consists of
	 */
	public CollectionPartDescription[] getAllCollectionPartDescriptions() {
		return collectionPartIds;
	}


	/**
	 * Calculates offsets and sizes of all collection parts the complete collection is divided in.
	 */
	private void calculateCollectionPartDescriptions() {
		collectionPartIds = new CollectionPartDescription[this.getNrofParts()];

		int displayOffset = 0;
		int count = 0;
		for (count = 0; count < getNrofParts() - 1; count++) {
			displayOffset = count * maxPartSize;
			if (displayOffset != offset) {
				collectionPartIds[count] = new CollectionPartDescription(displayOffset, maxPartSize, false);
			}
			else {
				collectionPartIds[count] = new CollectionPartDescription(displayOffset, maxPartSize, true);
				currentCollectionPartIdIndex = count;
			}
		}
		displayOffset = count * maxPartSize;
		if (displayOffset != offset) {
			collectionPartIds[count] = new CollectionPartDescription(displayOffset, getLastPartSize(), false);
		}
		else {
			collectionPartIds[count] = new CollectionPartDescription(displayOffset, getLastPartSize(), true);
			currentCollectionPartIdIndex = count;
		}
	}

}
