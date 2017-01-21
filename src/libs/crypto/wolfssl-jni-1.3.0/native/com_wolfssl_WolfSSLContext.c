/* com_wolfssl_WolfSSLContext.c
 *
 * Copyright (C) 2006-2015 wolfSSL Inc.
 *
 * This file is part of wolfSSL.
 *
 * wolfSSL is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * wolfSSL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

#include <stdio.h>
#include <wolfssl/options.h>
#include <wolfssl/ssl.h>
#include <wolfssl/error-ssl.h>

#include "com_wolfssl_globals.h"
#include "com_wolfssl_WolfSSLContext.h"

/* global object refs for verify, CRL callbacks */
static jobject g_verifyCbIfaceObj;
static jobject g_crlCtxCbIfaceObj;

/* custom I/O native fn prototypes */
int  NativeIORecvCb(WOLFSSL *ssl, char *buf, int sz, void *ctx);
int  NativeIOSendCb(WOLFSSL *ssl, char *buf, int sz, void *ctx);
int  NativeGenCookieCb(WOLFSSL *ssl, unsigned char *buf, int sz, void *ctx);
int  NativeVerifyCallback(int preverify_ok, WOLFSSL_X509_STORE_CTX* store);
void NativeCtxMissingCRLCallback(const char* url);
int  NativeMacEncryptCb(WOLFSSL* ssl, unsigned char* macOut,
        const unsigned char* macIn, unsigned int macInSz, int macContent,
        int macVerify, unsigned char* encOut, const unsigned char* encIn,
        unsigned int encSz, void* ctx);
int  NativeDecryptVerifyCb(WOLFSSL* ssl, unsigned char* decOut,
        const unsigned char* decIn, unsigned int decSz, int content,
        int verify, unsigned int* padSz, void* ctx);
int  NativeEccSignCb(WOLFSSL* ssl, const unsigned char* in, unsigned int inSz,
        unsigned char* out, unsigned int* outSz, const unsigned char* keyDer,
        unsigned int keySz, void* ctx);
int  NativeEccVerifyCb(WOLFSSL* ssl, const unsigned char* sig,
        unsigned int sigSz, const unsigned char* hash, unsigned int hashSz,
        const unsigned char* keyDer, unsigned int keySz, int* result,
        void* ctx);
int  NativeRsaSignCb(WOLFSSL* ssl, const unsigned char* in, unsigned int inSz,
        unsigned char* out, unsigned int* outSz, const unsigned char* keyDer,
        unsigned int keySz, void* ctx);
int  NativeRsaVerifyCb(WOLFSSL* ssl, unsigned char* sig, unsigned int sigSz,
        unsigned char** out, const unsigned char* keyDer, unsigned int keySz,
        void* ctx);
int  NativeRsaEncCb(WOLFSSL* ssl, const unsigned char* in, unsigned int inSz,
        unsigned char* out, unsigned int* outSz, const unsigned char* keyDer,
        unsigned int keySz, void* ctx);
int  NativeRsaDecCb(WOLFSSL* ssl, unsigned char* in, unsigned int inSz,
        unsigned char** out, const unsigned char* keyDer, unsigned int keySz,
        void* ctx);

JNIEXPORT jlong JNICALL Java_com_wolfssl_WolfSSLContext_newContext(JNIEnv* jenv,
        jclass jcl, jlong method)
{
    /* wolfSSL checks for NULL method ptr */
    return (jlong)wolfSSL_CTX_new((WOLFSSL_METHOD*)method);
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_useCertificateFile
  (JNIEnv* jenv, jobject jcl, jlong ctx, jstring file, jint format)
{
    jint ret = 0;
    jclass excClass;
    const char* certFile;

    if (!jenv)
        return SSL_FAILURE;

    if (!file)
    {
        excClass = (*jenv)->FindClass(jenv, "java/lang/NullPointerException");
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        /* throw NullPointerException */
        (*jenv)->ThrowNew(jenv, excClass,
                "Input certificate file is NULL");

        return SSL_FAILURE;
    }

    certFile = (*jenv)->GetStringUTFChars(jenv, file, 0);

    ret = (jint) wolfSSL_CTX_use_certificate_file((WOLFSSL_CTX*)ctx, certFile,
            (int)format);

    (*jenv)->ReleaseStringUTFChars(jenv, file, certFile);

    return ret;
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_usePrivateKeyFile
  (JNIEnv* jenv, jobject jcl, jlong ctx, jstring file, jint format)
{
    jint ret = 0;
    jclass excClass;
    const char* keyFile;

    if (!jenv)
        return SSL_FAILURE;

    if (!file)
    {
        excClass = (*jenv)->FindClass(jenv, "java/lang/NullPointerException");
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        /* throw NullPointerException */
        (*jenv)->ThrowNew(jenv, excClass,
                "Input private key file is NULL");

        return SSL_FAILURE;
    }

    keyFile = (*jenv)->GetStringUTFChars(jenv, file, 0);

    ret = (jint) wolfSSL_CTX_use_PrivateKey_file((WOLFSSL_CTX*)ctx, keyFile,
            (int)format);

    (*jenv)->ReleaseStringUTFChars(jenv, file, keyFile);

    return ret;
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_loadVerifyLocations
  (JNIEnv* jenv, jobject jcl, jlong ctx, jstring file, jstring path)
{
    jint ret = 0;
    jclass excClass;
    const char* caFile;
    const char* caPath;

    if (!jenv)
        return SSL_FAILURE;

    if (!file && !path)
    {
        excClass = (*jenv)->FindClass(jenv, "java/lang/NullPointerException");
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        /* throw NullPointerException */
        (*jenv)->ThrowNew(jenv, excClass,
                "Input file and path are both NULL");

        return SSL_FAILURE;
    }

    if (file) {
        caFile = (*jenv)->GetStringUTFChars(jenv, file, 0);
    } else {
        caFile = NULL;
    }

    if (path) {
        caPath = (*jenv)->GetStringUTFChars(jenv, path, 0);
    } else {
        caPath = NULL;
    }

    ret = (jint) wolfSSL_CTX_load_verify_locations((WOLFSSL_CTX*)ctx, caFile,
            caPath);

    if (caFile)
        (*jenv)->ReleaseStringUTFChars(jenv, file, caFile);
    if (caPath)
        (*jenv)->ReleaseStringUTFChars(jenv, path, caPath);

    return ret;
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_useCertificateChainFile
  (JNIEnv* jenv, jobject jcl, jlong ctx, jstring file)
{
    jint ret = 0;
    jclass excClass;
    const char* chainFile;

    if (!jenv)
        return SSL_FAILURE;

    /* throw exception if no input file */
    if (!file)
    {
        excClass = (*jenv)->FindClass(jenv, "java/lang/NullPointerException");
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        /* throw NullPointerException */
        (*jenv)->ThrowNew(jenv, excClass,
                "Input certificate chain file is NULL");

        return SSL_FAILURE;
    }

    chainFile = (*jenv)->GetStringUTFChars(jenv, file, 0);

    ret = (jint) wolfSSL_CTX_use_certificate_chain_file((WOLFSSL_CTX*)ctx,
            chainFile);

    (*jenv)->ReleaseStringUTFChars(jenv, file, chainFile);

    return ret;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_freeContext
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* wolfSSL checks for null pointer */
    wolfSSL_CTX_free((WOLFSSL_CTX*)ctx);
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setVerify(JNIEnv* jenv,
    jobject jcl, jlong ctx, jint mode, jobject callbackIface)
{
    if (!callbackIface) {
        wolfSSL_CTX_set_verify((WOLFSSL_CTX*)ctx, mode, NULL);
    } else {

        /* store Java verify Interface object */
        g_verifyCbIfaceObj = (*jenv)->NewGlobalRef(jenv, callbackIface);
        if (!g_verifyCbIfaceObj) {
            printf("error storing global callback interface\n");
        }

        /* set verify mode, register Java callback with wolfSSL */
        wolfSSL_CTX_set_verify((WOLFSSL_CTX*)ctx, mode, NativeVerifyCallback);
    }
}

int NativeVerifyCallback(int preverify_ok, WOLFSSL_X509_STORE_CTX* store)
{
    JNIEnv*   jenv;
    jint      vmret  = 0;
    jint      retval = -1;
    jclass    excClass;
    jmethodID verifyMethod;
    jobjectRefType refcheck;

    if (!g_vm) {
        /* we can't throw an exception yet, so just return 0 (failure) */
        return 0;
    }

    /* get JNIEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return -101;    /* failed to attach JNIEnv to thread */
        }
    } else if (vmret != JNI_OK) {
        return -102;        /* unable to get JNIEnv from JavaVM */
    }

    /* find exception class */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLException");
    if( (*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return -103;
    }

    /* check if our stored object reference is valid */
    refcheck = (*jenv)->GetObjectRefType(jenv, g_verifyCbIfaceObj);
    if (refcheck == 2) {

        /* lookup WolfSSLVerifyCallback class from global object ref */
        jclass verifyClass = (*jenv)->GetObjectClass(jenv, g_verifyCbIfaceObj);
        if (!verifyClass) {
            if ((*jenv)->ExceptionOccurred(jenv)) {
                (*jenv)->ExceptionDescribe(jenv);
                (*jenv)->ExceptionClear(jenv);
            }

            (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLVerifyCallback class reference");
            return -104;
        }

        verifyMethod = (*jenv)->GetMethodID(jenv, verifyClass,
                                            "verifyCallback", "(IJ)I");
        if (verifyMethod == 0) {
            if ((*jenv)->ExceptionOccurred(jenv)) {
                (*jenv)->ExceptionDescribe(jenv);
                (*jenv)->ExceptionClear(jenv);
            }

            (*jenv)->ThrowNew(jenv, excClass,
                "Error getting verifyCallback method from JNI");
            return -105;
        }

        retval = (*jenv)->CallIntMethod(jenv, g_verifyCbIfaceObj,
                verifyMethod, preverify_ok, (jlong) store);

        if ((*jenv)->ExceptionOccurred(jenv)) {
            /* exception occurred on the Java side during method call */
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            return -106;
        }

    } else {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }

        (*jenv)->ThrowNew(jenv, excClass,
                "Object reference invalid in NativeVerifyCallback");
        return -1;
    }

    return retval;
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_memsaveCertCache
  (JNIEnv* jenv, jobject jcl, jlong ctx, jbyteArray mem, jint sz,
    jintArray used)
{
    int ret;
    int usedTmp;
    char memBuf[sz];

    if (!jenv || !ctx || !mem || (sz <= 0))
        return BAD_FUNC_ARG;

    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return SSL_FAILURE;
    }

    ret = wolfSSL_CTX_memsave_cert_cache((WOLFSSL_CTX*)ctx, memBuf,
            sz, &usedTmp);

    /* set used value for return */
    (*jenv)->SetIntArrayRegion(jenv, used, 0, 1, &usedTmp);
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);

        (*jenv)->ThrowNew(jenv, excClass,
                "Failed to set array region in native memsaveCertCache");
        return SSL_FAILURE;
    }

    /* set jbyteArray for return */
    if (usedTmp >= 0) {
        (*jenv)->SetByteArrayRegion(jenv, mem, 0, usedTmp, (jbyte*)memBuf);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->ThrowNew(jenv, excClass,
                    "Failed to set byte region in native memsaveCertCache");
            return SSL_FAILURE;
        }
    }

    return ret;
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_memrestoreCertCache
  (JNIEnv* jenv, jobject jcl, jlong ctx, jbyteArray mem, jint sz)
{
    int ret;
    char memBuf[sz];

    if (!jenv || !ctx || !mem || (sz <= 0))
        return BAD_FUNC_ARG;

    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return SSL_FAILURE;
    }

    (*jenv)->GetByteArrayRegion(jenv, mem, 0, sz, (jbyte*)memBuf);
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);

        (*jenv)->ThrowNew(jenv, excClass,
                "Failed to get byte region in native memrestoreCertCache");
        return SSL_FAILURE;
    }

    ret = wolfSSL_CTX_memrestore_cert_cache((WOLFSSL_CTX*)ctx, memBuf, sz);

    return ret;
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_getCertCacheMemsize
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* wolfSSL checks for null pointer */
    return wolfSSL_CTX_get_cert_cache_memsize((WOLFSSL_CTX*)ctx);
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_setCipherList
  (JNIEnv* jenv, jobject jcl, jlong ctx, jstring list)
{
    jint ret = 0;
    jclass excClass;
    const char* cipherList;

    if (!jenv)
        return SSL_FAILURE;

    if (!list)
    {
        excClass = (*jenv)->FindClass(jenv, "java/lang/NullPointerException");
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }

        /* throw NullPointerException */
        (*jenv)->ThrowNew(jenv, excClass,
                "Input cipher list is NULL");

        return SSL_FAILURE;
    }

    cipherList = (*jenv)->GetStringUTFChars(jenv, list, 0);

    ret = (jint) wolfSSL_CTX_set_cipher_list((WOLFSSL_CTX*)ctx,
            cipherList);

    (*jenv)->ReleaseStringUTFChars(jenv, list, cipherList);

    return ret;
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_loadVerifyBuffer
  (JNIEnv* jenv, jobject jcl, jlong ctx, jbyteArray in, jlong sz, jint format)
{
    unsigned char buff[sz];

    if (!jenv || !ctx || !in || (sz < 0))
        return BAD_FUNC_ARG;

    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return SSL_FAILURE;
    }

    (*jenv)->GetByteArrayRegion(jenv, in, 0, sz, (jbyte*)buff);
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);

        (*jenv)->ThrowNew(jenv, excClass,
                "Failed to get byte region in native loadVerifyBuffer");
        return SSL_FAILURE;
    }

    return wolfSSL_CTX_load_verify_buffer((WOLFSSL_CTX*)ctx, buff, sz, format);
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_useCertificateBuffer
  (JNIEnv* jenv, jobject jcl, jlong ctx, jbyteArray in, jlong sz, jint format)
{
    unsigned char buff[sz];

    if (!jenv || !ctx || !in || (sz < 0))
        return BAD_FUNC_ARG;

    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return SSL_FAILURE;
    }

    (*jenv)->GetByteArrayRegion(jenv, in, 0, sz, (jbyte*)buff);
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);

        (*jenv)->ThrowNew(jenv, excClass,
                "Failed to get byte region in native useCertificateBuffer");
        return SSL_FAILURE;
    }

    return wolfSSL_CTX_use_certificate_buffer((WOLFSSL_CTX*)ctx, buff, sz,
            format);
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_usePrivateKeyBuffer
  (JNIEnv* jenv, jobject jcl, jlong ctx, jbyteArray in, jlong sz, jint format)
{
    unsigned char buff[sz];

    if (!jenv || !ctx || !in || (sz < 0))
        return BAD_FUNC_ARG;

    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return SSL_FAILURE;
    }

    (*jenv)->GetByteArrayRegion(jenv, in, 0, sz, (jbyte*)buff);
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);

        (*jenv)->ThrowNew(jenv, excClass,
                "Failed to get byte region in native usePrivateKeyBuffer");
        return SSL_FAILURE;
    }

    return wolfSSL_CTX_use_PrivateKey_buffer((WOLFSSL_CTX*)ctx, buff, sz,
            format);
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_useCertificateChainBuffer
  (JNIEnv* jenv, jobject jcl, jlong ctx, jbyteArray in, jlong sz)
{
    unsigned char buff[sz];

    if (!jenv || !ctx || !in || (sz < 0))
        return BAD_FUNC_ARG;

    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return SSL_FAILURE;
    }

    (*jenv)->GetByteArrayRegion(jenv, in, 0, sz, (jbyte*)buff);
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);

        (*jenv)->ThrowNew(jenv, excClass,
                "Failed to get byte region in native "
                "useCertificateChainBuffer");
        return SSL_FAILURE;
    }

    return wolfSSL_CTX_use_certificate_chain_buffer((WOLFSSL_CTX*)ctx,
            buff, sz);
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_setGroupMessages
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    if (!jenv || !ctx)
        return BAD_FUNC_ARG;

    return wolfSSL_CTX_set_group_messages((WOLFSSL_CTX*)ctx);
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setIORecv(JNIEnv* jenv,
        jobject jcl, jlong ctx)
{
    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    if(ctx) {
        /* set I/O recv callback */
        wolfSSL_SetIORecv((WOLFSSL_CTX*)ctx, NativeIORecvCb);

    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when setting IORecv");
    }
}

int NativeIORecvCb(WOLFSSL *ssl, char *buf, int sz, void *ctx)
{
    jint       retval = 0;
    jint       vmret  = 0;

    JNIEnv*    jenv;                  /* JNI environment */
    jclass     excClass;              /* WolfSSLJNIException class */
    int        needsDetach = 0;       /* Should we explicitly detach? */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass     sessClass;             /* WolfSSLSession class */
    jfieldID   ctxFid;                /* WolfSSLSession->ctx FieldID */
    jmethodID  getCtxMethodId;        /* WolfSSLSession->getAssCtxPtr() ID */

    jobject    ctxRef;                /* WolfSSLContext object */
    jclass     innerCtxClass;         /* WolfSSLContext class */
    jmethodID  recvCbMethodId;        /* internalIORecvCallback ID */
    jbyteArray inData;

    if (!g_vm || !ssl || !buf || !ctx) {
        /* can't throw exception yet, just return error */
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return WOLFSSL_CBIO_ERR_GENERAL;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* find exception class in case we need it */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativeIORecvCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLSession class reference in "
            "NativeIORecvCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext field ID in NativeIORecvCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* find WolfSSLContext.getAssociatedContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
        "getAssociatedContextPtr",
        "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get getAssociatedContextPtr() method ID in "
            "NativeIORecvCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* get WolfSSLContext(ctx) object from Java WolfSSLSession object */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get WolfSSLContext object in NativeIORecvCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* get WolfSSLContext class reference from object */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext class reference in "
            "NativeIORecvCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* call internal I/O recv callback */
    recvCbMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
            "internalIORecvCallback",
            "(Lcom/wolfssl/WolfSSLSession;[BI)I");
    if (!recvCbMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Error getting internalIORecvCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* create jbyteArray to hold received data */
    inData = (*jenv)->NewByteArray(jenv, sz);
    if (!inData) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Error getting internalIORecvCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* call Java send callback, ignore native ctx since Java
     * handles it */
    retval = (*jenv)->CallIntMethod(jenv, ctxRef, recvCbMethodId,
                                (jobject)(*g_cachedSSLObj),
                                inData, (jint)sz);

    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inData);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* copy jbyteArray into char array */
    if (retval >= 0) {
        (*jenv)->GetByteArrayRegion(jenv, inData, 0, retval,
                (jbyte*)buf);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, inData);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return WOLFSSL_CBIO_ERR_GENERAL;
        }
    }

    /* delete local refs, detach JNIEnv from thread */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    (*jenv)->DeleteLocalRef(jenv, inData);
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setIOSend
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class in case we need it */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
    }

    if (ctx) {
        /* set I/O send callback */
        wolfSSL_SetIOSend((WOLFSSL_CTX*)ctx, NativeIOSendCb);

    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when setting IOSend");
    }
}

int NativeIOSendCb(WOLFSSL *ssl, char *buf, int sz, void *ctx)
{
    jint       retval = 0;
    jint       vmret  = 0;

    JNIEnv*    jenv;                  /* JNI environment */
    jclass     excClass;              /* WolfSSLJNIException class */
    int        needsDetach = 0;       /* Should we explicitly detach? */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass     sessClass;             /* WolfSSLSession class */
    jfieldID   ctxFid;                /* WolfSSLSession->ctx FieldID */
    jmethodID  getCtxMethodId;        /* WolfSSLSession->getAssCtxPtr() ID */

    jobject    ctxRef;                /* WolfSSLContext object */
    jclass     innerCtxClass;         /* WolfSSLContext class */
    jmethodID  sendCbMethodId;        /* internalIOSendCallback ID */
    jbyteArray outData;               /* jbyteArray for data to send */

    if (!g_vm || !ssl || !buf || !ctx) {
        /* can't throw exception yet, just return error */
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return WOLFSSL_CBIO_ERR_GENERAL;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* find exception class in case we need it */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativeIOSendCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLSession class reference");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext field ID in NativeIOSendCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* find WolfSSLContext.getAssociatedContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
        "getAssociatedContextPtr",
        "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get getAssociatedContextPtr() method ID in "
            "NativeIOSendCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* get WolfSSLContext(ctx) object from Java WolfSSLSession object */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get WolfSSLContext object in NativeIOSendCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* get WolfSSLContext class reference from Java land */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext class reference in "
            "NativeIOSendCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    /* call internal I/O recv callback */
    sendCbMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
            "internalIOSendCallback",
            "(Lcom/wolfssl/WolfSSLSession;[BI)I");
    if (!sendCbMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Error getting internalIOSendCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return WOLFSSL_CBIO_ERR_GENERAL;
    }

    if (sz >= 0)
    {
        /* create jbyteArray to hold received data */
        outData = (*jenv)->NewByteArray(jenv, sz);
        if (!outData) {
            (*jenv)->ThrowNew(jenv, excClass,
                    "Error getting internalIOSendCallback method from JNI");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return WOLFSSL_CBIO_ERR_GENERAL;
        }

        (*jenv)->SetByteArrayRegion(jenv, outData, 0, sz, (jbyte*)buf);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, outData);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return WOLFSSL_CBIO_ERR_GENERAL;
        }

        /* call Java send callback, ignore native ctx since Java
         * handles it */
        retval = (*jenv)->CallIntMethod(jenv, ctxRef, sendCbMethodId,
                                (jobject)(*g_cachedSSLObj), outData, (jint)sz);

        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, outData);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return WOLFSSL_CBIO_ERR_GENERAL;
        }

        /* delete local refs */
        (*jenv)->DeleteLocalRef(jenv, outData);
    }

    /* delete local refs, detach JNIEnv from thread */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setGenCookie
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class in case we need it */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
    }

    if (ctx) {
        /* set gen cookie callback */
        wolfSSL_CTX_SetGenCookie((WOLFSSL_CTX*)ctx, NativeGenCookieCb);

    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when setting "
                "genCookieCb");
    }
}

int NativeGenCookieCb(WOLFSSL *ssl, unsigned char *buf, int sz, void *ctx)
{
    jint       retval = 0;
    jint       vmret  = 0;

    JNIEnv*    jenv;                  /* JNI environment */
    jclass     excClass;              /* WolfSSLJNIException class */
    int        needsDetach = 0;       /* Should we explicitly detach? */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass     sessClass;             /* WolfSSLSession class */
    jfieldID   ctxFid;                /* WolfSSLSession->ctx FieldID */
    jmethodID  getCtxMethodId;        /* WolfSSLSession->getAssCtxPtr() ID */

    jobject    ctxRef;                /* WolfSSLContext object */
    jclass     innerCtxClass;         /* WolfSSLContext class */
    jmethodID  cookieCbMethodId;      /* internalGenCookieCallback ID */
    jbyteArray inData;                /* jbyteArray to hold cookie data */

    if (!g_vm || !ssl || !buf) {
        /* can't throw exception yet, just return error */
        return GEN_COOKIE_E;
    }

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return GEN_COOKIE_E;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return GEN_COOKIE_E;
    }

    /* find exception class in case we need it */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return GEN_COOKIE_E;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativeGenCookieCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return GEN_COOKIE_E;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLSession class reference in "
            "NativeGenCookieCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return GEN_COOKIE_E;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext field ID in "
            "NativeGenCookieCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return GEN_COOKIE_E;
    }

    /* find WolfSSLSession.getAssociatedContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
        "getAssociatedContextPtr",
        "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get getAssociatedContextPtr() method ID in "
            "NativeGenCookieCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return GEN_COOKIE_E;
    }

    /* get WolfSSLContext(ctx) object from WolfSSLSession object */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get WolfSSLContext object in NativeGenCookieCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return GEN_COOKIE_E;
    }

    /* get WolfSSLContext class reference from Java land */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext class reference in "
            "NativeGenCookieCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return GEN_COOKIE_E;
    }

    /* call internal gen cookie callback */
    cookieCbMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
            "internalGenCookieCallback",
            "(Lcom/wolfssl/WolfSSLSession;[BI)I");

   if (!cookieCbMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Error getting internalGenCookieCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return GEN_COOKIE_E;
    }

    if (sz >= 0)
    {
        /* create jbyteArray to hold cookie data */
        inData = (*jenv)->NewByteArray(jenv, sz);
        if (!inData) {
            (*jenv)->ThrowNew(jenv, excClass,
                    "Error getting internalGenCookieCallback method from JNI");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return GEN_COOKIE_E;
        }

        /* call Java cookie callback */
        retval = (*jenv)->CallIntMethod(jenv, ctxRef, cookieCbMethodId,
                                    (jobject)(*g_cachedSSLObj),
                                    inData, (jint)sz);

        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, inData);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return GEN_COOKIE_E;
        }

        /* copy jbyteArray into char array */
        if (retval >= 0) {
            (*jenv)->GetByteArrayRegion(jenv, inData, 0, retval,
                    (jbyte*)buf);
            if ((*jenv)->ExceptionOccurred(jenv)) {
                (*jenv)->ExceptionDescribe(jenv);
                (*jenv)->ExceptionClear(jenv);
                (*jenv)->DeleteLocalRef(jenv, ctxRef);
                (*jenv)->DeleteLocalRef(jenv, inData);
                if (needsDetach)
                    (*g_vm)->DetachCurrentThread(g_vm);
                return GEN_COOKIE_E;
            }
        }

        /* delete local refs */
        (*jenv)->DeleteLocalRef(jenv, inData);
    }

    /* delete local refs, detach JNIEnv from thread */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_enableCRL
  (JNIEnv* jenv, jobject jcl, jlong ctx, jint options)
{
    if (!jenv || !ctx)
        return BAD_FUNC_ARG;

    return wolfSSL_CTX_EnableCRL((WOLFSSL_CTX*)ctx, options);
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_disableCRL
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    if (!jenv || !ctx)
        return BAD_FUNC_ARG;

    return wolfSSL_CTX_DisableCRL((WOLFSSL_CTX*)ctx);
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_loadCRL
  (JNIEnv* jenv, jobject jcl, jlong ctx, jstring path, jint type, jint monitor)
{
    int ret;
    const char* crlPath;

    if (!jenv || !ctx || !path)
        return BAD_FUNC_ARG;

    crlPath = (*jenv)->GetStringUTFChars(jenv, path, 0);

    ret = wolfSSL_CTX_LoadCRL((WOLFSSL_CTX*)ctx, crlPath, type, monitor);

    (*jenv)->ReleaseStringUTFChars(jenv, path, crlPath);

    return ret;
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_setCRLCb
  (JNIEnv* jenv, jobject jcl, jlong ctx, jobject cb)
{
    int ret = 0;
    jclass excClass;

    if (!jenv || !ctx || !cb) {
        return BAD_FUNC_ARG;
    }

    /* store Java CRL callback Interface object */
    g_crlCtxCbIfaceObj = (*jenv)->NewGlobalRef(jenv, cb);

    if (!g_crlCtxCbIfaceObj) {
        excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "error storing global missing CTX CRL callback interface");
    }

    ret = wolfSSL_CTX_SetCRL_Cb((WOLFSSL_CTX*)ctx, NativeCtxMissingCRLCallback);

    return ret;
}

void NativeCtxMissingCRLCallback(const char* url)
{
    JNIEnv*   jenv;
    jint      vmret  = 0;
    jclass    excClass;
    jmethodID crlMethod;
    jobjectRefType refcheck;

    /* get JNIEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            printf("Failed to attach JNIEnv to thread\n");
        }
    } else if (vmret != JNI_OK) {
        printf("Unable to get JNIEnv from JavaVM\n");
    }

    /* find exception class */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    /* check if our stored object reference is valid */
    refcheck = (*jenv)->GetObjectRefType(jenv, g_crlCtxCbIfaceObj);
    if (refcheck == 2) {

        /* lookup WolfSSLMissingCRLCallback class from global object ref */
        jclass crlClass = (*jenv)->GetObjectClass(jenv, g_crlCtxCbIfaceObj);
        if (!crlClass) {
            (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLMissingCRLCallback class reference");
            return;
        }

        crlMethod = (*jenv)->GetMethodID(jenv, crlClass,
                                            "missingCRLCallback",
                                            "(Ljava/lang/String;)V");
        if (crlMethod == 0) {
            if ((*jenv)->ExceptionOccurred(jenv)) {
                (*jenv)->ExceptionDescribe(jenv);
                (*jenv)->ExceptionClear(jenv);
            }

            (*jenv)->ThrowNew(jenv, excClass,
                "Error getting missingCRLCallback method from JNI");
            return;
        }

        /* create jstring from char* */
        jstring missingUrl = (*jenv)->NewStringUTF(jenv, url);

        (*jenv)->CallVoidMethod(jenv, g_crlCtxCbIfaceObj, crlMethod,
                missingUrl);

        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            return;
        }

    } else {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }

        (*jenv)->ThrowNew(jenv, excClass,
                "Object reference invalid in NativeMissingCRLCallback");
    }
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_enableOCSP
  (JNIEnv* jenv, jobject jcl, jlong ctx, jlong options)
{
    /* wolfSSL checks for null pointer */
    return wolfSSL_CTX_EnableOCSP((WOLFSSL_CTX*)ctx, options);
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_disableOCSP
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* wolfSSL checks for null pointer */
    return wolfSSL_CTX_DisableOCSP((WOLFSSL_CTX*)ctx);
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_setOCSPOverrideUrl
  (JNIEnv* jenv, jobject jcl, jlong ctx, jstring urlString)
{
    jint ret = 0;
    jclass excClass;
    const char* url;

    /* wolfSSL checks ctx for NULL */
    if (!jenv)
        return BAD_FUNC_ARG;

    if (urlString == NULL)
    {
        excClass = (*jenv)->FindClass(jenv, "java/lang/NullPointerException");
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }

        /* throw NullPointerException */
        (*jenv)->ThrowNew(jenv, excClass,
                "Input URL is NULL in setOCSPOverrideUrl()");

        return SSL_FAILURE;
    }

    url = (*jenv)->GetStringUTFChars(jenv, urlString, 0);

    ret = (jint) wolfSSL_CTX_SetOCSP_OverrideURL((WOLFSSL_CTX*)ctx, url);

    (*jenv)->ReleaseStringUTFChars(jenv, urlString, url);

    return ret;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setMacEncryptCb
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    if(ctx) {
        /* set MAC encrypt callback */
        wolfSSL_CTX_SetMacEncryptCb((WOLFSSL_CTX*)ctx, NativeMacEncryptCb);

    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when "
                "setting MacEncrypt");
    }
}

int NativeMacEncryptCb(WOLFSSL* ssl, unsigned char* macOut,
        const unsigned char* macIn, unsigned int macInSz, int macContent,
        int macVerify, unsigned char* encOut, const unsigned char* encIn,
        unsigned int encSz, void* ctx)
{
    jint       retval = 0;
    jint       vmret  = 0;

    JNIEnv*    jenv;                  /* JNI environment */
    jclass     excClass;              /* WolfSSLJNIException class */
    int        needsDetach = 0;       /* Should we explicitly detach? */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass     sessClass;             /* WolfSSLSession class */
    jfieldID   ctxFid;                /* WolfSSLSession->ctx FieldID */
    jmethodID  getCtxMethodId;        /* WolfSSLSession->getAssCtxPtr() ID */

    jobject    ctxRef;                /* WolfSSLContext object */
    jclass     innerCtxClass;         /* WolfSSLContext class */
    jmethodID  macEncryptMethodId;    /* internalMacEncryptCallback ID */

    int        hmacSize;
    jbyteArray j_macIn;

    if (!g_vm || !ssl || !macOut || !macIn || !encOut || !encIn) {
        return -1;
    }

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return -1;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return -1;
    }

    /* find exception class in case we need it */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativeMacEncryptCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLSession class reference in "
            "NativeMacEncryptCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext field ID in "
            "NativeMacEncryptCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* find getContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
        "getAssociatedContextPtr",
        "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get getAssociatedContextPtr() method ID in "
            "NativeMacEncryptCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext ctx object from Java land */
    ctxRef = (*jenv)->CallObjectMethod(jenv,
            (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get WolfSSLContext object in NativeMacEncryptCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext class reference from Java land */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext class reference in "
            "NativeMacEncryptCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get ref to internal MAC encrypt callback */
    macEncryptMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
            "internalMacEncryptCallback",
            "(Lcom/wolfssl/WolfSSLSession;Ljava/nio/ByteBuffer;"
            "[BJIILjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;J)I");

    if (!macEncryptMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Error getting internalMacEncryptCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        retval = -1;
    }

    if (retval == 0)
    {
        hmacSize = wolfSSL_GetHmacSize((WOLFSSL*)ssl);

        /* create ByteBuffer to wrap macOut */
        jobject macOutBB = (*jenv)->NewDirectByteBuffer(jenv, macOut,
                hmacSize);
        if (!macOutBB) {
            (*jenv)->ThrowNew(jenv, excClass,
                    "failed to create macOut ByteBuffer");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }

        /* create jbyteArray to hold macIn, since macIn is read-only */
        j_macIn = (*jenv)->NewByteArray(jenv, macInSz);
        if (!j_macIn) {
            (*jenv)->ThrowNew(jenv, excClass,
                    "failed to create macIn ByteBuffer");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, macOutBB);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }

        (*jenv)->SetByteArrayRegion(jenv, j_macIn, 0, macInSz,
                (jbyte*)macIn);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, macOutBB);
            (*jenv)->DeleteLocalRef(jenv, j_macIn);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }

        /* create ByteBuffer to wrap encOut */
        jobject encOutBB = (*jenv)->NewDirectByteBuffer(jenv, encOut,
                encSz);
        if (!encOutBB) {
            (*jenv)->ThrowNew(jenv, excClass,
                    "failed to create encOut ByteBuffer");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, macOutBB);
            (*jenv)->DeleteLocalRef(jenv, j_macIn);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }

        /* create ByteBuffer to wrap encIn - use encOut b/c it's not a
         * const, but points to same memory. This will be important
         * in Java-land in order to have an updated encIn array after
         * doing the MAC operation. */
        jobject encInBB = (*jenv)->NewDirectByteBuffer(jenv, encOut,
                encSz);
        if (!encInBB) {
            (*jenv)->ThrowNew(jenv, excClass,
                    "failed to create encIn ByteBuffer");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, macOutBB);
            (*jenv)->DeleteLocalRef(jenv, j_macIn);
            (*jenv)->DeleteLocalRef(jenv, encOutBB);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }

        /* call Java MAC/encrypt callback */
        retval = (*jenv)->CallIntMethod(jenv, ctxRef, macEncryptMethodId,
                (jobject)(*g_cachedSSLObj), macOutBB, j_macIn, (jlong)macInSz,
                macContent, macVerify, encOutBB, encInBB, (jlong)encSz);

        if ((*jenv)->ExceptionOccurred(jenv) || retval != 0) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->ThrowNew(jenv, excClass,
                "Call to Java callback failed in NativeMacEncryptCb");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, macOutBB);
            (*jenv)->DeleteLocalRef(jenv, j_macIn);
            (*jenv)->DeleteLocalRef(jenv, encOutBB);
            (*jenv)->DeleteLocalRef(jenv, encInBB);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }

        /* delete local refs */
        (*jenv)->DeleteLocalRef(jenv, macOutBB);
        (*jenv)->DeleteLocalRef(jenv, j_macIn);
        (*jenv)->DeleteLocalRef(jenv, encOutBB);
        (*jenv)->DeleteLocalRef(jenv, encInBB);
    }

    /* detach JNIEnv from thread */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setDecryptVerifyCb
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    if(ctx) {
        /* set decrypt/verify callback */
        wolfSSL_CTX_SetDecryptVerifyCb((WOLFSSL_CTX*)ctx,
                NativeDecryptVerifyCb);

    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when "
                "setting MacDecrypt");
    }
}

int  NativeDecryptVerifyCb(WOLFSSL* ssl, unsigned char* decOut,
        const unsigned char* decIn, unsigned int decSz, int content,
        int verify, unsigned int* padSz, void* ctx)
{
    jint       retval = 0;
    jint       vmret  = 0;

    JNIEnv*    jenv;                  /* JNI environment */
    jclass     excClass;              /* WolfSSLJNIException class */
    int        needsDetach = 0;       /* Should we explicitly detach? */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass     sessClass;             /* WolfSSLSession class */
    jfieldID   ctxFid;                /* WolfSSLSession->ctx FieldID */
    jmethodID  getCtxMethodId;        /* WolfSSLSession->getAssCtxPtr() ID */

    jobject    ctxRef;                /* WolfSSLContext object */
    jclass     innerCtxClass;         /* WolfSSLContext class */
    jmethodID  decryptVerifyMethodId;

    jbyteArray j_decIn;
    jlongArray j_padSz;

    if (!g_vm || !ssl || !decOut || !decIn || !padSz) {
        return -1;
    }

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return -1;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return -1;
    }

    /* find exception class in case we need it */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativeMacEncryptCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLSession class reference in "
            "NativeMacEncryptCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext field ID "
            "in NativeDecryptVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* find getContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
        "getAssociatedContextPtr",
        "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get getAssociatedContextPtr() method ID "
            "in NativeDecryptVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext ctx object from Java land */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get WolfSSLContext object in NativeDecryptVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext class reference from Java land */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext class reference "
            "in NativeDecryptVerifyCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call internal decrypt/verify callback */
    decryptVerifyMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
            "internalDecryptVerifyCallback",
            "(Lcom/wolfssl/WolfSSLSession;Ljava/nio/ByteBuffer;[BJII[J)I");

    if (!decryptVerifyMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Error getting internalDecryptVerifyCallback method "
                "from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    if (retval == 0)
    {
        /* create ByteBuffer to wrap decOut */
        jobject decOutBB = (*jenv)->NewDirectByteBuffer(jenv, decOut,
                decSz);
        if (!decOutBB) {
            (*jenv)->ThrowNew(jenv, excClass,
                    "failed to create decOut ByteBuffer");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }

        /* create jbyteArray to hold decIn */
        j_decIn = (*jenv)->NewByteArray(jenv, decSz);
        if (!j_decIn) {
            (*jenv)->ThrowNew(jenv, excClass,
                    "failed to create decIn ByteArray");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, decOutBB);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }

        (*jenv)->SetByteArrayRegion(jenv, j_decIn, 0, decSz, (jbyte*)decIn);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, decOutBB);
            (*jenv)->DeleteLocalRef(jenv, j_decIn);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }

        /* create jlongArray to hold padSz, since we need to use it as
         * an OUTPUT parameter from Java. Only needs to have 1 element */
        j_padSz = (*jenv)->NewLongArray(jenv, 1);
        if (!j_padSz) {
            (*jenv)->ThrowNew(jenv, excClass,
                    "failed to create padSz longArray");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, decOutBB);
            (*jenv)->DeleteLocalRef(jenv, j_decIn);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }

        /* call Java decrypt/verify callback, java layer handles
         * adding decrypt/verify CTX reference */
        retval = (*jenv)->CallIntMethod(jenv, ctxRef, decryptVerifyMethodId,
                (jobject)(*g_cachedSSLObj), decOutBB, j_decIn, (jlong)decSz,
                content, verify, j_padSz);

        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, decOutBB);
            (*jenv)->DeleteLocalRef(jenv, j_decIn);
            (*jenv)->DeleteLocalRef(jenv, j_padSz);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }

        if (retval == 0) {
            /* copy j_padSz into padSz */
            jlong tmpVal;
            (*jenv)->GetLongArrayRegion(jenv, j_padSz, 0, 1, &tmpVal);
            if ((*jenv)->ExceptionOccurred(jenv)) {
                (*jenv)->ExceptionDescribe(jenv);
                (*jenv)->ExceptionClear(jenv);
                (*jenv)->DeleteLocalRef(jenv, ctxRef);
                (*jenv)->DeleteLocalRef(jenv, decOutBB);
                (*jenv)->DeleteLocalRef(jenv, j_decIn);
                (*jenv)->DeleteLocalRef(jenv, j_padSz);
                if (needsDetach)
                    (*g_vm)->DetachCurrentThread(g_vm);
                return -1;
            }
            *padSz = (unsigned int)tmpVal;
        }

        /* delete local refs */
        (*jenv)->DeleteLocalRef(jenv, decOutBB);
        (*jenv)->DeleteLocalRef(jenv, j_decIn);
        (*jenv)->DeleteLocalRef(jenv, j_padSz);
    }

    /* delete local refs, detach JNIEnv from thread */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setEccSignCb
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    if(ctx) {
        /* set ECC sign callback */
        wolfSSL_CTX_SetEccSignCb((WOLFSSL_CTX*)ctx, NativeEccSignCb);

    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when setting "
                "EccSignCb");
    }
}

int  NativeEccSignCb(WOLFSSL* ssl, const unsigned char* in, unsigned int inSz,
        unsigned char* out, unsigned int* outSz, const unsigned char* keyDer,
        unsigned int keySz, void* ctx)
{
    jint       retval = 0;
    jint       vmret  = 0;

    JNIEnv*    jenv;                  /* JNI Environment */
    jclass     excClass;              /* WolfSSLJNIException class */
    int        needsDetach = 0;       /* Should we explicitly detach? */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass     sessClass;             /* WolfSSLSession class */
    jfieldID   ctxFid;                /* WolfSSLSession->ctx FieldID */
    jmethodID  getCtxMethodId;        /* WolfSSLSession->getAssCtxPtr() ID */

    jobject    ctxRef;                /* WolfSSLContext object */
    jclass     innerCtxClass;         /* WolfSSLContext class */
    jmethodID  eccSignMethodId;

    jlongArray j_outSz;

    if (!g_vm || !ssl || !in || !out || !outSz || !keyDer)
        return -1;

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return -1;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return -1;
    }

    /* find exception class in case we need it */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativeEccSignCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLSession class reference in "
            "NativeEccSignCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext field ID "
            "in NativeEccSignCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* find getContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
        "getAssociatedContextPtr",
        "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get getAssociatedContextPtr() method ID "
            "in NativeEccSignCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext ctx object from Java land */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get WolfSSLContext object in NativeEccSignCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext class reference from Java land */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext class reference "
            "in NativeEccSignCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call internal decrypt/verify callback */
    eccSignMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
            "internalEccSignCallback",
            "(Lcom/wolfssl/WolfSSLSession;Ljava/nio/ByteBuffer;"
            "JLjava/nio/ByteBuffer;[JLjava/nio/ByteBuffer;J)I");

    if (!eccSignMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Error getting internalEccSignCallback method "
                "from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap out */
    jobject outBB = (*jenv)->NewDirectByteBuffer(jenv, out, *outSz);
    if (!outBB) {
        (*jenv)->ThrowNew(jenv, excClass,
                "failed to create eccSign out ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap in */
    jobject inBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)in, inSz);
    if (!inBB) {
        (*jenv)->ThrowNew(jenv, excClass,
                "failed to create eccSign in ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap keyDer */
    jobject keyDerBB = (*jenv)->NewDirectByteBuffer(jenv,
            (void*)keyDer, keySz);
    if (!keyDerBB) {
        (*jenv)->ThrowNew(jenv, excClass,
                "failed to create eccSign keyDer ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create jlongArray to hold outSz, since we need to use it as
     * an OUTPUT parameter from Java. Only needs to have 1 element */
    j_outSz = (*jenv)->NewLongArray(jenv, 1);
    if (!j_outSz) {
        (*jenv)->ThrowNew(jenv, excClass,
                "failed to create outSz longArray");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        (*jenv)->DeleteLocalRef(jenv, keyDerBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* copy outSz into j_outSz */
    (*jenv)->SetLongArrayRegion(jenv, j_outSz, 0, 1, (jlong*)outSz);
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        (*jenv)->ThrowNew(jenv, excClass,
                "failed to set j_outSz longArray");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        (*jenv)->DeleteLocalRef(jenv, keyDerBB);
        (*jenv)->DeleteLocalRef(jenv, j_outSz);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call Java ECC sign callback, java layer handles
     * adding decrypt/verify CTX reference */
    retval = (*jenv)->CallIntMethod(jenv, ctxRef, eccSignMethodId,
            (jobject)(*g_cachedSSLObj), inBB, (jlong)inSz, outBB, j_outSz,
            keyDerBB, (jlong)keySz);

    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
    }

    if (retval == 0) {
        /* copy j_outSz into outSz */
        jlong tmpVal;
        (*jenv)->GetLongArrayRegion(jenv, j_outSz, 0, 1, &tmpVal);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, outBB);
            (*jenv)->DeleteLocalRef(jenv, inBB);
            (*jenv)->DeleteLocalRef(jenv, keyDerBB);
            (*jenv)->DeleteLocalRef(jenv, j_outSz);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }
        *outSz = (unsigned int)tmpVal;
    }

    /* delete local refs */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    (*jenv)->DeleteLocalRef(jenv, outBB);
    (*jenv)->DeleteLocalRef(jenv, inBB);
    (*jenv)->DeleteLocalRef(jenv, keyDerBB);
    (*jenv)->DeleteLocalRef(jenv, j_outSz);

    /* detach JNIEnv from thread */
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setEccVerifyCb
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    if(ctx) {
        /* set ECC verify callback */
        wolfSSL_CTX_SetEccVerifyCb((WOLFSSL_CTX*)ctx, NativeEccVerifyCb);

    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when setting "
                "EccVerifyCb");
    }
}

int  NativeEccVerifyCb(WOLFSSL* ssl, const unsigned char* sig,
        unsigned int sigSz, const unsigned char* hash, unsigned int hashSz,
        const unsigned char* keyDer, unsigned int keySz, int* result,
        void* ctx)
{
    jint       retval = 0;
    jint       vmret  = 0;

    JNIEnv*    jenv;                  /* JNI Environment */
    jclass     excClass;              /* WolfSSLJNIException class */
    int        needsDetach = 0;       /* Should we explicitly detach? */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass     sessClass;             /* WolfSSLSession class */
    jfieldID   ctxFid;                /* WolfSSLSession->ctx FieldID */
    jmethodID  getCtxMethodId;        /* WolfSSLSession->getAssCtxPtr() ID */

    jobject    ctxRef;                /* WolfSSLContext object */
    jclass     innerCtxClass;         /* WolfSSLContext class */
    jmethodID  eccVerifyMethodId;
    jintArray  j_result;

    if (!g_vm || !ssl || !sig || !hash || !keyDer || !result)
        return -1;

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return -1;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return -1;
    }

    /* find exception class in case we need it */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativeEccVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLSession class reference in "
            "NativeEccVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext field ID "
            "in NativeEccVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* find getContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
        "getAssociatedContextPtr",
        "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get getAssociatedContextPtr() method ID "
            "in NativeEccVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext ctx object from Java land */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get WolfSSLContext object in NativeEccVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext class reference from Java land */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext class reference "
            "in NativeEccVerifyCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call internal ECC verify callback */
    eccVerifyMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
            "internalEccVerifyCallback",
            "(Lcom/wolfssl/WolfSSLSession;Ljava/nio/ByteBuffer;"
            "JLjava/nio/ByteBuffer;JLjava/nio/ByteBuffer;J[I)I");

    if (!eccVerifyMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Error getting internalEccVerifyCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'sig' */
    jobject sigBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)sig, sigSz);
    if (!sigBB) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Failed to create eccVerify out ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'hash' */
    jobject hashBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)hash, hashSz);
    if (!hashBB) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Failed to create eccVerify hash ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, sigBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'keyDer' */
    jobject keyDerBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)keyDer,
            keySz);
    if (!keyDerBB) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Failed to create eccVerify keyDer ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, sigBB);
        (*jenv)->DeleteLocalRef(jenv, hashBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create jintArray to hold result, since we need to use it as
     * an OUTPUT parameter from Java. Only needs to have 1 element */
    j_result = (*jenv)->NewIntArray(jenv, 1);
    if (!j_result) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Failed to create result intArray in EccVerifyCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, sigBB);
        (*jenv)->DeleteLocalRef(jenv, hashBB);
        (*jenv)->DeleteLocalRef(jenv, keyDerBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call Java ECC verify callback, java layer handles
     * adding CTX reference */
    retval = (*jenv)->CallIntMethod(jenv, ctxRef, eccVerifyMethodId,
            (jobject)(*g_cachedSSLObj), sigBB, (jlong)sigSz, hashBB,
            (jlong)hashSz, keyDerBB, (jlong)keySz, j_result);

    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, sigBB);
        (*jenv)->DeleteLocalRef(jenv, hashBB);
        (*jenv)->DeleteLocalRef(jenv, keyDerBB);
        (*jenv)->DeleteLocalRef(jenv, j_result);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* copy j_result into result */
    jint tmpVal;
    (*jenv)->GetIntArrayRegion(jenv, j_result, 0, 1, &tmpVal);
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, sigBB);
        (*jenv)->DeleteLocalRef(jenv, hashBB);
        (*jenv)->DeleteLocalRef(jenv, keyDerBB);
        (*jenv)->DeleteLocalRef(jenv, j_result);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }
    *result = tmpVal;

    /* delete local refs */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    (*jenv)->DeleteLocalRef(jenv, sigBB);
    (*jenv)->DeleteLocalRef(jenv, hashBB);
    (*jenv)->DeleteLocalRef(jenv, keyDerBB);
    (*jenv)->DeleteLocalRef(jenv, j_result);

    /* detach JNIEnv from thread */
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setRsaSignCb
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    if(ctx) {
        /* set RSA sign callback */
        wolfSSL_CTX_SetRsaSignCb((WOLFSSL_CTX*)ctx, NativeRsaSignCb);

    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when setting "
                "RsaSignCb");
    }
}

int  NativeRsaSignCb(WOLFSSL* ssl, const unsigned char* in, unsigned int inSz,
        unsigned char* out, unsigned int* outSz, const unsigned char* keyDer,
        unsigned int keySz, void* ctx)
{
    jint       retval = 0;
    jint       vmret  = 0;

    JNIEnv*    jenv;                  /* JNI Environment */
    jclass     excClass;              /* WolfSSLJNIException class */
    int        needsDetach = 0;       /* Should we explicitly detach? */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass     sessClass;             /* WolfSSLSession class */
    jfieldID   ctxFid;                /* WolfSSLSession->ctx FieldID */
    jmethodID  getCtxMethodId;        /* WolfSSLSession->getAssCtxPtr() ID */

    jobject    ctxRef;                /* WolfSSLContext object */
    jclass     innerCtxClass;         /* WolfSSLContext class */
    jmethodID  rsaSignMethodId;
    jintArray j_outSz;

    if (!g_vm || !ssl || !in || !out || !outSz || !keyDer)
        return -1;

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return -1;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return -1;
    }

    /* find exception class in case we need it */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativeRsaSignCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLSession class reference in "
            "NativeRsaSignCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext field ID "
            "in NativeRsaSignCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* find getContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
        "getAssociatedContextPtr",
        "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get getAssociatedContextPtr() method ID "
            "in NativeRsaSignCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext ctx object from Java land */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get WolfSSLContext object in NativeRsaSignCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext class reference from Java land */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext class reference "
            "in NativeRsaSignCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call internal RSA sign callback */
    rsaSignMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
            "internalRsaSignCallback",
            "(Lcom/wolfssl/WolfSSLSession;Ljava/nio/ByteBuffer;"
            "JLjava/nio/ByteBuffer;[ILjava/nio/ByteBuffer;J)I");

    if (!rsaSignMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Error getting internalRsaSignCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'in' */
    jobject inBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)in, inSz);
    if (!inBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaSign in ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'out' */
    jobject outBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)out, *outSz);
    if (!outBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaSign out ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'keyDer' */
    jobject keyDerBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)keyDer,
            keySz);
    if (!keyDerBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaSign keyDer ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create jintArray to hold outSz, since we need to use it as
     * an OUTPUT parameter from Java. Only needs to have 1 element */
    j_outSz = (*jenv)->NewIntArray(jenv, 1);
    if (!j_outSz) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create result intArray in RsaSignCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        (*jenv)->DeleteLocalRef(jenv, keyDerBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* copy outSz into j_outSz */
    (*jenv)->SetIntArrayRegion(jenv, j_outSz, 0, 1, (jint*)outSz);
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to set j_outSz intArray in RsaSignCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        (*jenv)->DeleteLocalRef(jenv, keyDerBB);
        (*jenv)->DeleteLocalRef(jenv, j_outSz);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call Java RSA sign callback, java layer handles
     * adding CTX reference */
    retval = (*jenv)->CallIntMethod(jenv, ctxRef, rsaSignMethodId,
            (jobject)(*g_cachedSSLObj), inBB, (jlong)inSz, outBB, j_outSz,
            keyDerBB, (jlong)keySz);

    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        (*jenv)->DeleteLocalRef(jenv, keyDerBB);
        (*jenv)->DeleteLocalRef(jenv, j_outSz);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
    }

    if (retval == 0) {
        /* copy j_outSz into outSz */
        jint tmpVal;
        (*jenv)->GetIntArrayRegion(jenv, j_outSz, 0, 1, &tmpVal);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, inBB);
            (*jenv)->DeleteLocalRef(jenv, outBB);
            (*jenv)->DeleteLocalRef(jenv, keyDerBB);
            (*jenv)->DeleteLocalRef(jenv, j_outSz);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }
        *outSz = tmpVal;
    }

    /* delete local refs */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    (*jenv)->DeleteLocalRef(jenv, inBB);
    (*jenv)->DeleteLocalRef(jenv, outBB);
    (*jenv)->DeleteLocalRef(jenv, keyDerBB);
    (*jenv)->DeleteLocalRef(jenv, j_outSz);

    /* detach JNIEnv from thread */
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setRsaVerifyCb
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    if(ctx) {
        /* set RSA verify callback */
        wolfSSL_CTX_SetRsaVerifyCb((WOLFSSL_CTX*)ctx, NativeRsaVerifyCb);

    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when setting "
                "RsaVerifyCb");
    }
}

int  NativeRsaVerifyCb(WOLFSSL* ssl, unsigned char* sig, unsigned int sigSz,
        unsigned char** out, const unsigned char* keyDer, unsigned int keySz,
        void* ctx)
{
    jint       retval = 0;
    jint       vmret  = 0;

    JNIEnv*    jenv;                  /* JNI Environment */
    jclass     excClass;              /* WolfSSLJNIException class */
    int        needsDetach = 0;       /* Should we explicitly detach? */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass     sessClass;             /* WolfSSLSession class */
    jfieldID   ctxFid;                /* WolfSSLSession->ctx FieldID */
    jmethodID  getCtxMethodId;        /* WolfSSLSession->getAssCtxPtr() ID */

    jobject    ctxRef;                /* WolfSSLContext object */
    jclass     innerCtxClass;         /* WolfSSLContext class */
    jmethodID  rsaVerifyMethodId;

    if (!g_vm || !ssl || !sig || !out || !keyDer)
        return -1;

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return -1;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return -1;
    }

    /* find exception class in case we need it */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativeRsaVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLSession class reference in "
            "NativeRsaVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext field ID "
            "in NativeRsaVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* find getContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
        "getAssociatedContextPtr",
        "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get getAssociatedContextPtr() method ID "
            "in NativeRsaVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext ctx object from Java land */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get WolfSSLContext object in NativeRsaVerifyCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext class reference from Java land */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext class reference "
            "in NativeRsaVerifyCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call internal RSA verify callback */
    rsaVerifyMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
            "internalRsaVerifyCallback",
            "(Lcom/wolfssl/WolfSSLSession;Ljava/nio/ByteBuffer;"
            "JLjava/nio/ByteBuffer;JLjava/nio/ByteBuffer;J)I");

    if (!rsaVerifyMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Error getting internalRsaVerifyCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'sig' */
    jobject sigBB = (*jenv)->NewDirectByteBuffer(jenv, sig, sigSz);
    if (!sigBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaVerify sig ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'out', since we're actually
     * doing this inline, outBB points to the same address as
     * sigBB */
    jobject outBB = (*jenv)->NewDirectByteBuffer(jenv, sig, sigSz);
    if (!outBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaVerify out ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, sigBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'keyDer' */
    jobject keyDerBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)keyDer,
            keySz);
    if (!keyDerBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaVerify keyDer ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, sigBB);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call Java RSA verify callback, java layer handles
     * adding CTX reference */
    retval = (*jenv)->CallIntMethod(jenv, ctxRef, rsaVerifyMethodId,
            (jobject)(*g_cachedSSLObj), sigBB, (jlong)sigSz, outBB,
            (jlong)sigSz, keyDerBB, (jlong)keySz);

    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
    }

    /* point out* to the beginning of our decrypted buffer */
    if (retval > 0)
        *out = sig;

    /* delete local refs */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    (*jenv)->DeleteLocalRef(jenv, sigBB);
    (*jenv)->DeleteLocalRef(jenv, outBB);
    (*jenv)->DeleteLocalRef(jenv, keyDerBB);

    /* detach JNIEnv from thread */
    (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setRsaEncCb
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    if(ctx) {
        /* set RSA encrypt callback */
        wolfSSL_CTX_SetRsaEncCb((WOLFSSL_CTX*)ctx, NativeRsaEncCb);

    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when setting "
                "RsaEncCb");
    }
}

int  NativeRsaEncCb(WOLFSSL* ssl, const unsigned char* in, unsigned int inSz,
        unsigned char* out, unsigned int* outSz, const unsigned char* keyDer,
        unsigned int keySz, void* ctx)
{
    jint       retval = 0;
    jint       vmret  = 0;

    JNIEnv*    jenv;                  /* JNI Environment */
    jclass     excClass;              /* WolfSSLJNIException class */
    int        needsDetach = 0;       /* Should we explicitly detach? */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass     sessClass;             /* WolfSSLSession class */
    jfieldID   ctxFid;                /* WolfSSLSession->ctx FieldID */
    jmethodID  getCtxMethodId;        /* WolfSSLSession->getAssCtxPtr() ID */

    jobject    ctxRef;                /* WolfSSLContext object */
    jclass     innerCtxClass;         /* WolfSSLContext class */
    jmethodID  rsaEncMethodId;
    jintArray j_outSz;

    if (!g_vm || !ssl || !in || !out || !outSz || !keyDer)
        return -1;

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return -1;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return -1;
    }

    /* find exception class in case we need it */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativeRsaEncCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLSession class reference in "
            "NativeRsaEncCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext field ID "
            "in NativeRsaEncCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* find getContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
        "getAssociatedContextPtr",
        "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get getAssociatedContextPtr() method ID "
            "in NativeRsaEncCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext ctx object from Java land */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get WolfSSLContext object in NativeRsaEncCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext class reference from Java land */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext class reference "
            "in NativeRsaEncCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call internal RSA enc callback */
    rsaEncMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
            "internalRsaEncCallback",
            "(Lcom/wolfssl/WolfSSLSession;Ljava/nio/ByteBuffer;"
            "JLjava/nio/ByteBuffer;[ILjava/nio/ByteBuffer;J)I");

    if (!rsaEncMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Error getting internalRsaEncCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'in' */
    jobject inBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)in, inSz);
    if (!inBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaEnc in ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'out' */
    jobject outBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)out, *outSz);
    if (!outBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaEnc out ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'keyDer' */
    jobject keyDerBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)keyDer,
            keySz);
    if (!keyDerBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaEnc keyDer ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create jintArray to hold outSz, since we need to use it as
     * an OUTPUT parameter from Java. Only needs to have 1 element */
    j_outSz = (*jenv)->NewIntArray(jenv, 1);
    if (!j_outSz) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create result intArray in RsaEncCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        (*jenv)->DeleteLocalRef(jenv, keyDerBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* copy outSz into j_outSz */
    (*jenv)->SetIntArrayRegion(jenv, j_outSz, 0, 1, (jint*)outSz);
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to set j_outSz intArray in RsaEncCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        (*jenv)->DeleteLocalRef(jenv, keyDerBB);
        (*jenv)->DeleteLocalRef(jenv, j_outSz);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call Java RSA enc callback, java layer handles
     * adding CTX reference */
    retval = (*jenv)->CallIntMethod(jenv, ctxRef, rsaEncMethodId,
            (jobject)(*g_cachedSSLObj), inBB, (jlong)inSz, outBB, j_outSz,
            keyDerBB, (jlong)keySz);

    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
    }

    if (retval == 0) {
        /* copy j_outSz into outSz */
        jint tmpVal;
        (*jenv)->GetIntArrayRegion(jenv, j_outSz, 0, 1, &tmpVal);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, inBB);
            (*jenv)->DeleteLocalRef(jenv, outBB);
            (*jenv)->DeleteLocalRef(jenv, keyDerBB);
            (*jenv)->DeleteLocalRef(jenv, j_outSz);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return -1;
        }
        *outSz = tmpVal;
    }

    /* delete local refs */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    (*jenv)->DeleteLocalRef(jenv, inBB);
    (*jenv)->DeleteLocalRef(jenv, outBB);
    (*jenv)->DeleteLocalRef(jenv, keyDerBB);
    (*jenv)->DeleteLocalRef(jenv, j_outSz);

    /* detach JNIEnv from thread */
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setRsaDecCb
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    if(ctx) {
        /* set RSA encrypt callback */
        wolfSSL_CTX_SetRsaDecCb((WOLFSSL_CTX*)ctx, NativeRsaDecCb);

    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when setting "
                "RsaDecCb");
    }
}

int  NativeRsaDecCb(WOLFSSL* ssl, unsigned char* in, unsigned int inSz,
        unsigned char** out, const unsigned char* keyDer, unsigned int keySz,
        void* ctx)
{
    jint       retval = 0;
    jint       vmret  = 0;

    JNIEnv*    jenv;                  /* JNI Environment */
    jclass     excClass;              /* WolfSSLJNIException class */
    int        needsDetach = 0;       /* Should we explicitly detach? */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass     sessClass;             /* WolfSSLSession class */
    jfieldID   ctxFid;                /* WolfSSLSession->ctx FieldID */
    jmethodID  getCtxMethodId;        /* WolfSSLSession->getAssCtxPtr() ID */

    jobject    ctxRef;                /* WolfSSLContext object */
    jclass     innerCtxClass;         /* WolfSSLContext class */
    jmethodID  rsaDecMethodId;

    if (!g_vm || !ssl || !in || !out || !keyDer)
        return -1;

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return -1;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return -1;
    }

    /* find exception class in case we need it */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativeRsaDecCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLSession class reference in "
            "NativeRsaDecCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext field ID "
            "in NativeRsaDecCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* find getContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
        "getAssociatedContextPtr",
        "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get getAssociatedContextPtr() method ID "
            "in NativeRsaDecCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext ctx object from Java land */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get WolfSSLContext object in NativeRsaDecCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* get WolfSSLContext class reference from Java land */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Can't get native WolfSSLContext class reference "
            "in NativeRsaDecCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call internal ECC verify callback */
    rsaDecMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
            "internalRsaDecCallback",
            "(Lcom/wolfssl/WolfSSLSession;Ljava/nio/ByteBuffer;"
            "JLjava/nio/ByteBuffer;JLjava/nio/ByteBuffer;J)I");

    if (!rsaDecMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
            "Error getting internalRsaDecCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'in' */
    jobject inBB = (*jenv)->NewDirectByteBuffer(jenv, in, inSz);
    if (!inBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaDec in ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'out', since we're actually
     * doing this inline, outBB points to the same address as
     * inBB */
    jobject outBB = (*jenv)->NewDirectByteBuffer(jenv, in, inSz);
    if (!outBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaDec out ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* create ByteBuffer to wrap 'keyDer' */
    jobject keyDerBB = (*jenv)->NewDirectByteBuffer(jenv, (void*)keyDer,
            keySz);
    if (!keyDerBB) {
        (*jenv)->ThrowNew(jenv, excClass,
            "Failed to create rsaDec keyDer ByteBuffer");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return -1;
    }

    /* call Java RSA decrypt callback, java layer handles
     * adding CTX reference */
    retval = (*jenv)->CallIntMethod(jenv, ctxRef, rsaDecMethodId,
            (jobject)(*g_cachedSSLObj), inBB, (jlong)inSz, outBB, (jlong)inSz,
            keyDerBB, (jlong)keySz);

    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, inBB);
        (*jenv)->DeleteLocalRef(jenv, outBB);
        (*jenv)->DeleteLocalRef(jenv, keyDerBB);
        retval = -1;
    }

    /* point out* to the beginning of our decrypted buffer */
    if (retval > 0)
        *out = in;

    /* delete local refs */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    (*jenv)->DeleteLocalRef(jenv, inBB);
    (*jenv)->DeleteLocalRef(jenv, outBB);
    (*jenv)->DeleteLocalRef(jenv, keyDerBB);

    /* detach JNIEnv from thread */
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setPskClientCb
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    if (ctx) {
        /* set PSK client callback */
        wolfSSL_CTX_set_psk_client_callback((WOLFSSL_CTX*)ctx,
                                            NativePskClientCb);
    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when setting "
                "NativePskClientCb");
    }
}

unsigned int NativePskClientCb(WOLFSSL* ssl, const char* hint, char* identity,
        unsigned int id_max_len, unsigned char* key, unsigned int max_key_len)
{
    jint        vmret  = 0;
    jlong       retval = 0;
    int         usingSSLCallback = 0;
    int         needsDetach = 0;      /* Should we explicitly detach? */

    JNIEnv*     jenv;                 /* JNI environment */
    jclass      excClass;             /* class: WolfSSLJNIException */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass      sessClass;            /* WolfSSLSession class */
    jfieldID    ctxFid;               /* WolfSSLSession->ctx fieldID */
    jmethodID   getCtxMethodId;       /* WolfSSLSession->getAssCtxPtr() ID */

    jfieldID    internPskClientCbFid; /* WolfSSLContext->internPskClientCb ID */
    jobject     internPskClientCbObj; /*      "    ->internPskClientCb object */

    jobject     ctxRef;               /* WolfSSLContext object */
    jclass      innerCtxClass;        /* WolfSSLContext class */
    jmethodID   pskClientMethodId;    /* internalPskClientCallback ID */

    jstring     hintString;           /* String, for 'hint' */
    jclass      strBufClass;          /* StringBuffer class for 'identity' */
    jmethodID   strBufMethodId;       /* StringBuffer constructor ID */
    jobject     strBufObj;            /* StringBuffer object */
    jbyteArray  keyArray;             /* byte[] for key in/out */
    jmethodID   toStringId;           /* StringBuffer.toString() ID */
    jstring     bufString;            /* StringBuffer.toString() result */
    const char* tmpString;            /* tmp output char* for String out */

    /* Note: since this is called from C, not the JVM, we need to explicitly
     * free all object refs with DeleteLocalRef() */

    if (!g_vm || !ssl || !hint || !identity || !key) {
        /* we can't throw an exception yet, so just return 0 (failure) */
        return 0;
    }

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return 0;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return 0;
    }

    /* find exception class */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativePskClientCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession class reference in "
                "NativePskClientCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLContext field ID in "
                "NativePSKClientCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* find WolfSSLSession.getAssociatedContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
            "getAssociatedContextPtr",
            "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get getAssociatedContextPtr() method ID in "
                "NativePSKClientCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* get WolfSSLContext(ctx) object from Java WolfSSLSession object */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get WolfSSLContext object in NativePskClientCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* get WolfSSLContext class reference from object */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLContext class reference in "
                "NativePskClientCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* figure out if we need to call the CTX or SSL level callback */

    /*  1.  Get internPskClientCb FieldID */
    internPskClientCbFid = (*jenv)->GetFieldID(jenv, innerCtxClass,
            "internPskClientCb",
            "Lcom/wolfssl/WolfSSLPskClientCallback;");
    if (!internPskClientCbFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native internPskClientCb field ID in "
                "NativePSKClientCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /*  2.  Get WolfSSLPskClientCallback object (or null) */
    internPskClientCbObj = (*jenv)->GetObjectField(jenv, ctxRef,
            internPskClientCbFid);
    if (!internPskClientCbObj) {
        printf("Using SSL level PSK Client callback!!!\n");
        usingSSLCallback = 1;
    }

    if (usingSSLCallback == 1) {
        /* WolfSSLSession level callback */
        pskClientMethodId = (*jenv)->GetMethodID(jenv, sessClass,
                "internalPskClientCallback",
                "(Lcom/wolfssl/WolfSSLSession;Ljava/lang/String;"
                "Ljava/lang/StringBuffer;J[BJ)J");
    } else {
        /* WolfSSLContext level callback */
        pskClientMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
                "internalPskClientCallback",
                "(Lcom/wolfssl/WolfSSLSession;Ljava/lang/String;"
                "Ljava/lang/StringBuffer;J[BJ)J");
    }

    if (!pskClientMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }

        (*jenv)->ThrowNew(jenv, excClass,
                "Error getting internalPskClientCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* create String to wrap 'hint' */
    hintString = (*jenv)->NewStringUTF(jenv, hint);
    if (!hintString) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Error creating String for PSK client hint");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* find StringBuffer class to wrap 'identity' */
    strBufClass = (*jenv)->FindClass(jenv, "java/lang/StringBuffer");
    if (!strBufClass) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Error finding StringBuffer class for PSK client identity");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, hintString);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* find StringBuffer Constructor */
    strBufMethodId = (*jenv)->GetMethodID(jenv, strBufClass,
            "<init>", "()V");
    if (!strBufMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get StringBuffer constructor method ID "
                "in NativePskClientCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, hintString);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* create new StringBuffer object */
    strBufObj = (*jenv)->NewObject(jenv, strBufClass, strBufMethodId);
    if (!strBufObj) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get StringBuffer object in NativePskClientCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, hintString);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* create jbyteArray to hold received key */
    keyArray = (*jenv)->NewByteArray(jenv, max_key_len);
    if (!keyArray) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Error creating jbyteArray for PSK client key");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, hintString);
        (*jenv)->DeleteLocalRef(jenv, strBufObj);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* call Java PSK client callback */
    if (usingSSLCallback == 1) {
        /* call WolfSSLSession level callback */
        retval = (*jenv)->CallLongMethod(jenv, (jobject)(*g_cachedSSLObj),
                pskClientMethodId, (jobject)(*g_cachedSSLObj), hintString,
                strBufObj, (jlong)id_max_len, keyArray, (jlong)max_key_len);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, hintString);
            (*jenv)->DeleteLocalRef(jenv, strBufObj);
            (*jenv)->DeleteLocalRef(jenv, keyArray);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return 0;
        }
    } else {
        /* call WolfSSLContext level callback */
        retval = (*jenv)->CallLongMethod(jenv, ctxRef, pskClientMethodId,
                (jobject)(*g_cachedSSLObj), hintString, strBufObj,
                (jlong)id_max_len, keyArray, (jlong)max_key_len);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, hintString);
            (*jenv)->DeleteLocalRef(jenv, strBufObj);
            (*jenv)->DeleteLocalRef(jenv, keyArray);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return 0;
        }
    }

    if (retval > 0) {

        /* copy jbyteArray into char key array */
        (*jenv)->GetByteArrayRegion(jenv, keyArray, 0, retval, (jbyte*)key);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, hintString);
            (*jenv)->DeleteLocalRef(jenv, strBufObj);
            (*jenv)->DeleteLocalRef(jenv, keyArray);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return 0;
        }

        /* get the String from the StringBuffer */
        toStringId = (*jenv)->GetMethodID(jenv, strBufClass,
                "toString", "()Ljava/lang/String;");
        if (!toStringId) {
            if ((*jenv)->ExceptionOccurred(jenv)) {
                (*jenv)->ExceptionDescribe(jenv);
                (*jenv)->ExceptionClear(jenv);
            }
            (*jenv)->ThrowNew(jenv, excClass,
                    "Error getting String ID from StringBuffer in PSK CB");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, hintString);
            (*jenv)->DeleteLocalRef(jenv, strBufObj);
            (*jenv)->DeleteLocalRef(jenv, keyArray);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return 0;
        }

        bufString = (jstring) (*jenv)->CallObjectMethod(jenv,
                strBufObj, toStringId);
        if (!bufString) {
            if ((*jenv)->ExceptionOccurred(jenv)) {
                (*jenv)->ExceptionDescribe(jenv);
                (*jenv)->ExceptionClear(jenv);
            }
            (*jenv)->ThrowNew(jenv, excClass,
                    "Error getting String from StringBuffer in PSK CB");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, hintString);
            (*jenv)->DeleteLocalRef(jenv, strBufObj);
            (*jenv)->DeleteLocalRef(jenv, keyArray);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return 0;
        }

        /* convert string to C string (char*) */
        tmpString = (*jenv)->GetStringUTFChars(jenv, bufString, 0);
        if (!tmpString) {
            (*jenv)->ThrowNew(jenv, excClass,
                    "Error with GetStringUTFChars in PSK Client CB");
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, hintString);
            (*jenv)->DeleteLocalRef(jenv, strBufObj);
            (*jenv)->DeleteLocalRef(jenv, keyArray);
            (*jenv)->DeleteLocalRef(jenv, bufString);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return 0;
        }
        strcpy(identity, tmpString);
        (*jenv)->ReleaseStringUTFChars(jenv, bufString, tmpString);
        (*jenv)->DeleteLocalRef(jenv, bufString);
    }

    /* delete local obj refs, detach JNIEnv from thread */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    (*jenv)->DeleteLocalRef(jenv, hintString);
    (*jenv)->DeleteLocalRef(jenv, strBufObj);
    (*jenv)->DeleteLocalRef(jenv, keyArray);
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT void JNICALL Java_com_wolfssl_WolfSSLContext_setPskServerCb
  (JNIEnv* jenv, jobject jcl, jlong ctx)
{
    /* find exception class */
    jclass excClass = (*jenv)->FindClass(jenv,
            "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        return;
    }

    if (ctx) {
        /* set PSK server callback */
        wolfSSL_CTX_set_psk_server_callback((WOLFSSL_CTX*)ctx,
                                            NativePskServerCb);
    } else {
        (*jenv)->ThrowNew(jenv, excClass,
                "Input WolfSSLContext object was null when setting "
                "NativePskServerCb");
    }
}

unsigned int NativePskServerCb(WOLFSSL* ssl, const char* identity,
        unsigned char* key, unsigned int max_key_len)
{
    jint        vmret  = 0;
    jlong       retval = 0;
    int         usingSSLCallback = 0;
    int         needsDetach = 0;     /* Should we explicitly detach? */

    JNIEnv*     jenv;                 /* JNI environment */
    jclass      excClass;             /* class: WolfSSLJNIException */

    static jobject* g_cachedSSLObj;   /* WolfSSLSession cached object */
    jclass      sessClass;            /* WolfSSLSession class */
    jfieldID    ctxFid;               /* WolfSSLSession->ctx fieldID */
    jmethodID   getCtxMethodId;       /* WolfSSLSession->getAssCtxPtr() ID */

    jfieldID    internPskServerCbFid; /* WolfSSLContext->internPskServerCb ID */
    jobject     internPskServerCbObj; /*      "   -> internPskServerCb object */

    jobject     ctxRef;               /* WolfSSLContext object */
    jclass      innerCtxClass;        /* WolfSSLContext class */
    jmethodID   pskServerMethodId;    /* internalPskServerCallback ID */

    jstring     identityString;       /* String, for 'hint' */
    jbyteArray  keyArray;             /* byte[] for key in/out */

    /* Note: since this is called from C, not the JVM, we need to explicitly
     * free all object refs with DeleteLocalRef() */

    if (!g_vm || !ssl || !identity || !key) {
        /* we can't throw an exception yet, so just return 0 (failure) */
        return 0;
    }

    /* get JavaEnv from JavaVM */
    vmret = (int)((*g_vm)->GetEnv(g_vm, (void**) &jenv, JNI_VERSION_1_6));
    if (vmret == JNI_EDETACHED) {
#ifdef __ANDROID__
        vmret = (*g_vm)->AttachCurrentThread(g_vm, &jenv, NULL);
#else
        vmret = (*g_vm)->AttachCurrentThread(g_vm, (void**) &jenv, NULL);
#endif
        if (vmret) {
            return 0;
        }
        needsDetach = 1;
    } else if (vmret != JNI_OK) {
        return 0;
    }

    /* find exception class */
    excClass = (*jenv)->FindClass(jenv, "com/wolfssl/WolfSSLJNIException");
    if ((*jenv)->ExceptionOccurred(jenv)) {
        (*jenv)->ExceptionDescribe(jenv);
        (*jenv)->ExceptionClear(jenv);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* get stored WolfSSLSession jobject */
    g_cachedSSLObj = (jobject*) wolfSSL_get_jobject((WOLFSSL*)ssl);
    if (!g_cachedSSLObj) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession object reference in "
                "NativePskServerCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* lookup WolfSSLSession class from object */
    sessClass = (*jenv)->GetObjectClass(jenv, (jobject)(*g_cachedSSLObj));
    if (!sessClass) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLSession class reference in "
                "NativePskServerCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* lookup WolfSSLContext private member fieldID */
    ctxFid = (*jenv)->GetFieldID(jenv, sessClass, "ctx",
            "Lcom/wolfssl/WolfSSLContext;");
    if (!ctxFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLContext field ID in "
                "NativePSKClientCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* find WolfSSLSession.getAssociatedContextPtr() method */
    getCtxMethodId = (*jenv)->GetMethodID(jenv, sessClass,
            "getAssociatedContextPtr",
            "()Lcom/wolfssl/WolfSSLContext;");
    if (!getCtxMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get getAssociatedContextPtr() method ID in "
                "NativePSKClientCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* get WolfSSLContext(ctx) object from Java WolfSSLSession object */
    ctxRef = (*jenv)->CallObjectMethod(jenv, (jobject)(*g_cachedSSLObj),
            getCtxMethodId);
    if (!ctxRef) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get WolfSSLContext object in NativePskServerCb");
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* get WolfSSLContext class reference from object */
    innerCtxClass = (*jenv)->GetObjectClass(jenv, ctxRef);
    if (!innerCtxClass) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native WolfSSLContext class reference in "
                "NativePskServerCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* figure out if we need to call the CTX or SSL level callback */

    /*  1.  Get the internPskServerCb FieldID */
    internPskServerCbFid = (*jenv)->GetFieldID(jenv, innerCtxClass,
            "internPskServerCb",
            "Lcom/wolfssl/WolfSSLPskServerCallback;");
    if (!internPskServerCbFid) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Can't get native internPskServerCb field ID in "
                "NativePskServerCb");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /*  2.  Get WolfSSLPskServerCallback object (or null) */
    internPskServerCbObj = (*jenv)->GetObjectField(jenv, ctxRef,
            internPskServerCbFid);
    if (!internPskServerCbObj) {
        printf("Using SSL level PSK Server callback!!!\n");
        usingSSLCallback = 1;
    }

    if (usingSSLCallback == 1) {
        /* WolfSSLSession level callback */
        pskServerMethodId = (*jenv)->GetMethodID(jenv, sessClass,
                "internalPskServerCallback",
                "(Lcom/wolfssl/WolfSSLSession;Ljava/lang/String;"
                "[BJ)J");
    } else {
        /* WolfSSLContext level callback */
        pskServerMethodId = (*jenv)->GetMethodID(jenv, innerCtxClass,
                "internalPskServerCallback",
                "(Lcom/wolfssl/WolfSSLSession;Ljava/lang/String;"
                "[BJ)J");
    }

    if (!pskServerMethodId) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Error getting internalPskServerCallback method from JNI");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* create String to wrap 'identity' */
    identityString = (*jenv)->NewStringUTF(jenv, identity);
    if (!identityString) {
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
        }
        (*jenv)->ThrowNew(jenv, excClass,
                "Error creating String for PSK client identity");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* create jbyteArray to hold received key */
    keyArray = (*jenv)->NewByteArray(jenv, max_key_len);
    if (!keyArray) {
        (*jenv)->ThrowNew(jenv, excClass,
                "Error creating jbyteArray for PSK server key");
        (*jenv)->DeleteLocalRef(jenv, ctxRef);
        (*jenv)->DeleteLocalRef(jenv, identityString);
        if (needsDetach)
            (*g_vm)->DetachCurrentThread(g_vm);
        return 0;
    }

    /* call Java PSK server callback */
    if (usingSSLCallback == 1) {
        retval = (*jenv)->CallLongMethod(jenv, (jobject)(*g_cachedSSLObj),
                pskServerMethodId, (jobject)(*g_cachedSSLObj), identityString,
                keyArray, (jlong)max_key_len);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, identityString);
            (*jenv)->DeleteLocalRef(jenv, keyArray);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return 0;
        }
    } else {
        retval = (*jenv)->CallLongMethod(jenv, ctxRef, pskServerMethodId,
                (jobject)(*g_cachedSSLObj), identityString,
                keyArray, (jlong)max_key_len);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, identityString);
            (*jenv)->DeleteLocalRef(jenv, keyArray);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return 0;
        }
    }

    if (retval > 0) {

        /* copy jbyteArray into char key array */
        (*jenv)->GetByteArrayRegion(jenv, keyArray, 0, retval, (jbyte*)key);
        if ((*jenv)->ExceptionOccurred(jenv)) {
            (*jenv)->ExceptionDescribe(jenv);
            (*jenv)->ExceptionClear(jenv);
            (*jenv)->DeleteLocalRef(jenv, ctxRef);
            (*jenv)->DeleteLocalRef(jenv, identityString);
            (*jenv)->DeleteLocalRef(jenv, keyArray);
            if (needsDetach)
                (*g_vm)->DetachCurrentThread(g_vm);
            return 0;
        }
    }

    /* delete local obj refs, detach JNIEnv from thread */
    (*jenv)->DeleteLocalRef(jenv, ctxRef);
    (*jenv)->DeleteLocalRef(jenv, identityString);
    (*jenv)->DeleteLocalRef(jenv, keyArray);
    if (needsDetach)
        (*g_vm)->DetachCurrentThread(g_vm);

    return retval;
}

JNIEXPORT jint JNICALL Java_com_wolfssl_WolfSSLContext_usePskIdentityHint
  (JNIEnv* jenv, jobject obj, jlong ctx, jstring hint)
{
    jint ret;
    const char* nativeHint;

    if (!jenv || !ctx || !hint)
        return SSL_FAILURE;

    nativeHint = (*jenv)->GetStringUTFChars(jenv, hint, 0);

    ret = (jint)wolfSSL_CTX_use_psk_identity_hint((WOLFSSL_CTX*)ctx,
            nativeHint);

    (*jenv)->ReleaseStringUTFChars(jenv, hint, nativeHint);

    return ret;
}
