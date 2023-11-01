#include "vector.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <search.h>

void VectorNew(vector *v, int elemSize, VectorFreeFunction freeFn, int initialAllocation) // 100% Correct
{
    assert(elemSize > 0);
    if (initialAllocation == 0) {
        initialAllocation = 4;
    }
    v->start = malloc(initialAllocation * elemSize);
    v->elemSize = elemSize;
    v->freeFn = freeFn;
    v->allocLen = initialAllocation;
    v->allocIncrement = initialAllocation;
    v->realLen = 0;
}

void VectorDispose(vector *v) 
{
    if (v->freeFn != NULL) {
        for (int i = 0; i < v->realLen; i++) {
            v->freeFn((char*)v->start + (i * v->elemSize));
        }
    }
    free(v->start);
}

int VectorLength(const vector *v)
{
    return v->realLen;
}

void *VectorNth(const vector *v, int position)
{
    assert(position >= 0 && position < v->realLen);
    return (char*)v->start + (position * v->elemSize);
}

void VectorReplace(vector *v, const void *elemAddr, int position)
{
    assert(position >= 0 && position < v->realLen);
    void *dest = (char*)v->start + (position * v->elemSize);
    
    if (v->freeFn != NULL) {
        v->freeFn(dest);
    }

    memcpy(dest, elemAddr, v->elemSize);
}

void VectorInsert(vector *v, const void *elemAddr, int position)
{
    assert(position >= 0 && position <= v->realLen);
    if (v->realLen == v->allocLen) {
        expandVector(v);
    }
    if (position == v->realLen) {
        VectorAppend(v, elemAddr);
        return;
    }
    
    void *dest = (char*)v->start + (position * v->elemSize);
    memmove((char*)dest + v->elemSize, dest, (v->realLen - position) * v->elemSize);
    memcpy(dest, elemAddr, v->elemSize);
    v->realLen++;
}

void VectorAppend(vector *v, const void *elemAddr)
{
    if (v->realLen == v->allocLen) {
        expandVector(v);
    }
    void *dest = (char*)v->start + (v->realLen * v->elemSize);
    memcpy(dest, elemAddr, v->elemSize);
    v->realLen++;
}

void VectorDelete(vector *v, int position)
{
    assert(position >= 0 && position < v->realLen);
    void *toDelete = (char*)v->start + (position * v->elemSize);
    void *toMove = (char*) v->start + ((position + 1) * v->elemSize);

    if (v->freeFn != NULL) {
        v->freeFn(toDelete);
    }
    memmove(toDelete, toMove, (v->realLen - position - 1) * v->elemSize);
    v->realLen--;
}

void VectorSort(vector *v, VectorCompareFunction compare)
{
    qsort(v->start, v->realLen, v->elemSize, compare);
}

void VectorMap(vector *v, VectorMapFunction mapFn, void *auxData)
{
    assert(mapFn != NULL);
    for (int i = 0; i < v->realLen; i++) {
        mapFn(VectorNth(v, i), auxData);
    }
}

static const int kNotFound = -1;
int VectorSearch(const vector *v, const void *key, VectorCompareFunction searchFn, int startIndex, bool isSorted)
{   
    void *elemAddr = NULL;

    
    if (isSorted) {
        elemAddr = bsearch(key, v->start, v->realLen, v->elemSize, searchFn);
    }
    else {
        size_t len = v->realLen;
        elemAddr = lfind(key, v->start, &len, v->elemSize, searchFn);
    }
    
    if (elemAddr == NULL) {
        return kNotFound;
    }
    return (int)((char*)elemAddr - (char*)v->start) / v->elemSize;
} 

void expandVector(vector *v)
{
    v->allocLen += v->allocIncrement;
    void *newStart = realloc(v->start, v->allocLen * v->elemSize);
    assert(newStart != NULL);
    v->start = newStart;
}