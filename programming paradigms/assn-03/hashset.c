#include "hashset.h"
#include <assert.h>
#include <stdlib.h>
#include <string.h>

void HashSetNew(hashset *h, int elemSize, int numBuckets,
		HashSetHashFunction hashfn, HashSetCompareFunction comparefn, HashSetFreeFunction freefn)
{
	assert(numBuckets > 0 && elemSize > 0 && hashfn != NULL && comparefn != NULL);
	h->buckets = malloc(numBuckets * sizeof(vector));
	h->compareFn = comparefn;
	h->hashFn = hashfn;
	h->freeFn = freefn;
	h->elemSize = elemSize;
	h->numBuckets = numBuckets;
	h->elemCount = 0;

	for (int i = 0; i < numBuckets; i++) {
		vector *v = (vector*)((char*)h->buckets + (i * sizeof(vector)));
		VectorNew(v, h->elemSize, h->freeFn, 4);
	}
}

void HashSetDispose(hashset *h)
{
	for (int i = 0; i < h->numBuckets; i++) {
		vector *v = (vector*)((char*)h->buckets + (i * sizeof(vector)));
		VectorDispose(v);
	}
	free(h->buckets);
}

int HashSetCount(const hashset *h)
{
	return h->elemCount;
}

void HashSetMap(hashset *h, HashSetMapFunction mapfn, void *auxData)
{
	assert(mapfn != NULL);
	for (int i = 0; i < h->numBuckets; i++) {
		vector *v = (vector*)((char*)h->buckets + (i * sizeof(vector)));
		VectorMap(v, mapfn, auxData);
	}
}

void HashSetEnter(hashset *h, const void *elemAddr)
{
	assert(elemAddr != NULL);
	int code = h->hashFn(elemAddr, h->numBuckets);
	assert(code >= 0 && code < h->numBuckets);

	vector *v = (vector*)((char*)h->buckets + (code * sizeof(vector)));

	int i = VectorSearch(v, elemAddr, h->compareFn, 0, false);
	if (i == -1) {
		VectorAppend(v, elemAddr);
		h->elemCount++;
	}
	else {
		VectorReplace(v, elemAddr, i);
	}
}

void *HashSetLookup(const hashset *h, const void *elemAddr)
{
	assert(elemAddr != NULL);
	int code = h->hashFn(elemAddr, h->numBuckets);
	assert(code >= 0 && code < h->numBuckets);

	vector *v = (vector*)((char*)h->buckets + (code * sizeof(vector)));
	int ans = VectorSearch(v, elemAddr, h->compareFn, 0, false);
	
	if (ans == -1) {
		return NULL;
	}
	return VectorNth(v, ans);
}
