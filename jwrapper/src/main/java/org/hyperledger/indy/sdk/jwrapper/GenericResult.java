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

/**
 * A class representing the result obtained from a Sdk API Call, not all fields are used each time
 *
 * @version 1.0 27-Jul-2017
 */
public class GenericResult {
  /**
   * The return value immediately returned by the invoked native API
   */
  private int returnValue;
  
  /**
   * The value for cmdHandle that is returned back via the callback
   */
  private int cmdHandle;
  
  /**
   * The value for returnHandle that is returned back via the callback in some instances
   * For example when invoking indy_open_pool_ledger() you get the opened Pool Handle
   */
  private int returnHandle;
  
  /**
   * The returning Json that some APIs return via the callback
   * For example 
   */
  private String returnJson;
  
  /**
   * The Error Code returned back via the callback, this indicates the actual success or failure of the invocation.
   */
  private ErrorCode errorCode;
  
  public int getReturnValue() {
    return returnValue;
  }
  public void setReturnValue(int returnValue) {
    this.returnValue = returnValue;
  }
  public int getCmdHandle() {
    return cmdHandle;
  }
  public void setCmdHandle(int cmdHandle) {
    this.cmdHandle = cmdHandle;
  }
  public int getReturnHandle() {
    return returnHandle;
  }
  public void setReturnHandle(int returnHandle) {
    this.returnHandle = returnHandle;
  }
  public String getReturnJson() {
    return returnJson;
  }
  public void setReturnJson(String returnJson) {
    this.returnJson = returnJson;
  }
  public ErrorCode getErrorCode() {
    return errorCode;
  }
  public void setErrorCode(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
