/*
 * Copyright (c) 2017 Evernym Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to 
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.hyperledger.indy.sdk.jwrapper;

import java.util.concurrent.CompletableFuture;

import com.sun.jna.Callback;

/**
 * A placeholder class that holds all the various types of callbacks
 * @version 1.0 29-Jul-2017
 */
public class IndyCallback {

  /**
   * A simple callback that expects only the cmdHandle and error if any
   * @version 1.0 29-Jul-2017
   */
  public static class SimpleCallback implements Callback {
    private CompletableFuture<GenericResult> future;
    private GenericResult iResult;
    public SimpleCallback(CompletableFuture<GenericResult> future, GenericResult iResult) {
      this.future = future;
      this.iResult = iResult;
    }
    
    public void callback(int cmdHandle, int error) {
      ErrorCode errorCode = ErrorCode.valueOf(error);
      iResult.setErrorCode(errorCode);
      future.complete(iResult);
    }
  }
  
  /**
   * A callback that expects the cmdHandle and error if any, as well as a returning handle
   * For example when invoking indy_open_pool_ledger() you get the opened Pool Handle
   * @version 1.0 29-Jul-2017
   */
  public static class HandleReturningCallback implements Callback {
    private CompletableFuture<GenericResult> future;
    private GenericResult iResult;
    public HandleReturningCallback(CompletableFuture<GenericResult> future, GenericResult iResult) {
      this.future = future;
      this.iResult = iResult;
    }
    
    public void callback(int cmdHandle, int error, int returnHandle) {
      ErrorCode errorCode = ErrorCode.valueOf(error);
      iResult.setErrorCode(errorCode);
      iResult.setReturnHandle(returnHandle);
      future.complete(iResult);
    }
  }
  
  /**
   * A callback that expects the cmdHandle and error if any, as well as a return Json string
   * For example when invoking indy_submit_request() you get a return Json string
   * @version 1.0 29-Jul-2017
   */
  public static class JsonReturningCallback implements Callback {
    private CompletableFuture<GenericResult> future;
    private GenericResult iResult;
    public JsonReturningCallback(CompletableFuture<GenericResult> future, GenericResult iResult) {
      this.future = future;
      this.iResult = iResult;
    }
    
    public void callback(int cmdHandle, int error, String returnJson) {
      ErrorCode errorCode = ErrorCode.valueOf(error);
      iResult.setErrorCode(errorCode);
      iResult.setReturnJson(returnJson);
      future.complete(iResult);
    }
  }
}
