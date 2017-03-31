//
//  Tea.h
//  HyundaiLiving
//
//  Created by ehailong on 14-4-1.
//  Copyright (c) 2014年 ehailong. All rights reserved.
//

#ifndef _TEA_H_
#define _TEA_H_

#ifdef __cplusplus
extern "C" {
#endif
    
//TEA加密一个字符串,不足8的整数倍的，在头部补齐字节，方式：位数xxxxxx...,例如：80000000，补全8位
char* encryptString(char *stringBuffer, unsigned int *key, unsigned int length);
    
//TEA解密一个字符串
char* decryptString(char *stringBuffer, unsigned int *key, unsigned int length);
    
#ifdef __cplusplus
}
#endif


#endif