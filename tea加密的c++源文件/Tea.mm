//
//  Tea.m
//  HyundaiLiving
//
//  Created by ehailong on 14-4-1.
//  Copyright (c) 2014年 ehailong. All rights reserved.
//

#include "Tea.h"

//TEA加密多个字节，前提是length是8的整数倍
static void encryptLongAlignedBuffer(unsigned int *v, unsigned int *k, unsigned int length);

//TEA解密多个字节，前提是length是8的整数倍
static void decryptLongAlignedBuffer(unsigned int *v, unsigned int *k, unsigned int length);

//TEA加密8个字节
void encrypt (unsigned int* v, unsigned int* k) {
    unsigned int v0=v[0], v1=v[1], sum=0, i;           /* set up */
    unsigned int delta=0x9e3779b9;                     /* a key schedule constant */
    unsigned int k0=k[0], k1=k[1], k2=k[2], k3=k[3];   /* cache key */
    for (i=0; i < 32; i++) {                       /* basic cycle start */
        sum += delta;
        v0 += ((v1<<4) + k0) ^ (v1 + sum) ^ ((v1>>5) + k1);
        v1 += ((v0<<4) + k2) ^ (v0 + sum) ^ ((v0>>5) + k3);
    }                                              /* end cycle */
    v[0]=v0; v[1]=v1;
}

//TEA解密8个字节
void decrypt (unsigned int* v, unsigned int* k) {
    unsigned int v0=v[0], v1=v[1], sum=0xC6EF3720, i;  /* set up */
    unsigned int delta=0x9e3779b9;                     /* a key schedule constant */
    unsigned int k0=k[0], k1=k[1], k2=k[2], k3=k[3];   /* cache key */
    for (i=0; i<32; i++) {                         /* basic cycle start */
        v1 -= ((v0<<4) + k2) ^ (v0 + sum) ^ ((v0>>5) + k3);
        v0 -= ((v1<<4) + k0) ^ (v1 + sum) ^ ((v1>>5) + k1);
        sum -= delta;
    }                                              /* end cycle */
    v[0]=v0; v[1]=v1;
}

//TEA加密一个字符串,不足8的整数倍的，在头部补齐字节，方式：位数xxxxxx...,例如：80000000，补全8位
char* encryptString(char *stringBuffer, unsigned int *key, unsigned int length)
{
    if (length <= 0)
    {
        return NULL;
    }
    
    int denominator = 8;
    int modValue = length % denominator;
    int alignCount = denominator - modValue;
    int alignedBufferSize = alignCount + length;
    char *alignedBuffer = (char *)malloc(alignedBufferSize + 16);
    char *pBufferCursor = alignedBuffer;
    
    memset(alignedBuffer, 0, alignedBufferSize + 16);
    //补齐的位数
    memset(alignedBuffer, alignCount, 1);
    
    pBufferCursor += alignCount;
    memcpy(pBufferCursor, stringBuffer, length);
    
    encryptLongAlignedBuffer((unsigned int *)alignedBuffer, key, alignCount + length);
    
    return alignedBuffer;
}


//TEA解密一个字符串
char* decryptString(char *stringBuffer, unsigned int *key, unsigned int length)
{
    if (length <= 0)
    {
        return NULL;
    }
    
    char *alignedBuffer = (char *)malloc(length + 16);
    char *pBufferCursor = alignedBuffer;
    memset(alignedBuffer, 0, length + 16);
    memcpy(alignedBuffer, stringBuffer, length);
    
    decryptLongAlignedBuffer((unsigned int *)alignedBuffer, key, length);
    int alignCount = alignedBuffer[0];
    pBufferCursor += alignCount;
    
    return pBufferCursor;
}


//TEA加密多个字节，前提是length是8的整数倍
void encryptLongAlignedBuffer(unsigned int *v, unsigned int *k, unsigned int length)
{
    char *pCursor = NULL;
    char *head = (char *)v;
    for(pCursor = head; pCursor - head < length - 1; pCursor += 8)
    {
        encrypt((unsigned int *)pCursor, k);
    }
}


//TEA解密多个字节，前提是length是8的整数倍
void decryptLongAlignedBuffer(unsigned int *v, unsigned int *k, unsigned int length)
{
    char *pCursor = NULL;
    char *head = (char *)v;
    for(pCursor = head; pCursor - head < length - 1; pCursor += 8)
    {
        decrypt((unsigned int *)pCursor, k);
    }
}