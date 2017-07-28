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
 * A class representing the result obtained from a Sdk API Call
 * @version 1.0 27-Jul-2017
 */
public class IndyResult {
  private int returnValue;
  private ErrorCode errorCode;
  private Exception exception;

  public IndyResult() {
    super();
  }
  public IndyResult(int returnValue) {
    super();
    this.returnValue = returnValue;
  }
  public int getReturnValue() {
    return returnValue;
  }
  public void setReturnValue(int returnValue) {
    this.returnValue = returnValue;
  }
  public ErrorCode getErrorCode() {
    return errorCode;
  }
  public void setErrorCode(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
  public Exception getException() {
    return exception;
  }
  public void setException(Exception exception) {
    this.exception = exception;
  }
}
