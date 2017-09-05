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
package org.hyperledger.indy.sdk.jwrapper.pool;

/**
 * A bean representing a Pool Handle, 
 * this bean is used by most of the PoolAPI methods for parameters and return type
 * @version 1.0 04-Sep-2017
 */
public class Pool {
  
  private int poolHandle;
  private String poolName;
  
  /**
   * A path to genesis transaction file. If NULL, then a default one will be used. 
   * If file doesn't exists default one will be created
   */
  private String sandboxFile;
  private PoolStatus status = PoolStatus.UNUSED;
  
  /**
   * Forces pool ledger to be refreshed immediately after opening
   */
  private boolean refreshOnOpen;
  
  /**
   * After this time in minutes pool ledger will be automatically refreshed, use 0 to disable
   */
  private int autoRefreshTime;
  
  /**
   * Network timeout for communication with nodes in milliseconds
   */
  private int networkTimeout;

  public Pool(String poolName) {
    super();
    this.poolName = poolName;
  }

  public int getPoolHandle() {
    return poolHandle;
  }
  public void setPoolHandle(int poolHandle) {
    this.poolHandle = poolHandle;
  }

  public String getPoolName() {
    return poolName;
  }
  public void setPoolName(String poolName) {
    this.poolName = poolName;
  }

  public String getSandboxFile() {
    return sandboxFile;
  }
  public void setSandboxFile(String sandboxFile) {
    this.sandboxFile = sandboxFile;
  }

  public PoolStatus getStatus() {
    return status;
  }
  public void setStatus(PoolStatus status) {
    this.status = status;
  }

  public boolean isRefreshOnOpen() {
    return refreshOnOpen;
  }
  public void setRefreshOnOpen(boolean refreshOnOpen) {
    this.refreshOnOpen = refreshOnOpen;
  }

  public int getAutoRefreshTime() {
    return autoRefreshTime;
  }
  public void setAutoRefreshTime(int autoRefreshTime) {
    this.autoRefreshTime = autoRefreshTime;
  }

  public int getNetworkTimeout() {
    return networkTimeout;
  }
  public void setNetworkTimeout(int networkTimeout) {
    this.networkTimeout = networkTimeout;
  }
}
