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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.hyperledger.indy.sdk.jwrapper.ErrorCode;
import org.hyperledger.indy.sdk.jwrapper.IndyApi;
import org.hyperledger.indy.sdk.jwrapper.IndyResult;
import org.hyperledger.indy.sdk.jwrapper.IndyApi.NativeApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.Callback;

/**
 * @version 1.0 28-Jul-2017
 */
public class PoolApi {
  private ObjectMapper objectMapper;
  private AtomicInteger cmdHandleCounter;
  private NativeApi nativeApiInstance;

  public PoolApi(IndyApi apiInstance) {
    this.nativeApiInstance = apiInstance.getNativeApiInstance();
    this.objectMapper = apiInstance.getObjectMapper();
    this.cmdHandleCounter = apiInstance.getCmdHandleCounter();
  }

  /**
   * A asynchronous create pool ledger config API
   *
   * @param configName name of the pool ledger configuration
   * @param genesisFilePath A path to genesis transaction file. If NULL, then a default one will be used. 
   *                        If file doesn't exists default one will be created.
   * @return A future that returns a IndyResult
   */
  public CompletableFuture<IndyResult> createPoolLedgerConfigAsync(String configName, String genesisFilePath) {
    CompletableFuture<IndyResult> future = new CompletableFuture<IndyResult>();
    IndyResult iResult = new IndyResult(-1);
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
    String configStr = null;

    if (null != genesisFilePath) {
      CreatePoolLedgerConfig config = new CreatePoolLedgerConfig(genesisFilePath);
      try {
        configStr = objectMapper.writeValueAsString(config);
      } catch (JsonProcessingException e) {
        future.completeExceptionally(e);
        return future;
      }
    }

    Callback callback = new Callback() {
      @SuppressWarnings("unused")
      public void callback(int cmdHandle, int error) {
        ErrorCode errorCode = ErrorCode.valueOf(error);
        iResult.setErrorCode(errorCode);
        future.complete(iResult);
      }
    };
 
    int rc = nativeApiInstance.indy_create_pool_ledger_config(cmdHandle, configName, configStr, callback);
    iResult.setReturnValue(rc);
    future.complete(iResult);
    return future;
  }
  
  /**
   * A synchronous create pool ledger config API
   *
   * @param configName name of the pool ledger configuration
   * @param genesisFilePath A path to genesis transaction file. If NULL, then a default one will be used. 
   *                        If file doesn't exists default one will be created.
   * @return A IndyResult instance
   */
  public IndyResult createPoolLedgerConfig(String configName, String genesisFilePath) {
    
    final CompletableFuture<IndyResult> future = createPoolLedgerConfigAsync(configName, genesisFilePath);
    
    IndyResult iResult = null;
    try {
      iResult = future.get();
    } catch (InterruptedException | ExecutionException e) {
      iResult = new IndyResult(-1);
      iResult.setException(e);
    }
    return iResult;
  }
  
  /**
   * A asynchronous open pool ledger API
   * @param configName name of the pool ledger configuration
   * @param refreshOnOpen Forces pool ledger to be refreshed immediately after opening
   * @param autoRefreshTime After this time in minutes pool ledger will be automatically refreshed, use 0 to disable
   * @param networkTimeout Network timeout for communication with nodes in milliseconds
   * @return A future that returns a IndyResult
   */
  public CompletableFuture<IndyResult> openPoolLedgerAsync(String configName, 
      boolean refreshOnOpen, int autoRefreshTime, int networkTimeout) {
    CompletableFuture<IndyResult> future = new CompletableFuture<IndyResult>();
    IndyResult iResult = new IndyResult(-1);
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
    OpenPoolLedgerConfig config = new OpenPoolLedgerConfig();
    config.setRefreshOnOpen(refreshOnOpen);
    config.setAutoRefreshTime(autoRefreshTime);
    config.setNetworkTimeout(networkTimeout);
 
    String configStr = null;
    try {
      configStr = objectMapper.writeValueAsString(config);
    } catch (JsonProcessingException e) {
      future.completeExceptionally(e);
      return future;
    }
   
    Callback callback = new Callback() {
      @SuppressWarnings("unused")
      public void callback(int cmdHandle, int error) {
        ErrorCode errorCode = ErrorCode.valueOf(error);
        iResult.setErrorCode(errorCode);
        future.complete(iResult);
      }
    };
 
    int rc = nativeApiInstance.indy_open_pool_ledger(cmdHandle, configName, configStr, callback);
    iResult.setReturnValue(rc);
    future.complete(iResult);
    return future;
  }
  
  /**
   * A synchronous open pool ledger API
   * @param configName config name to create
   * @param refreshOnOpen Forces pool ledger to be refreshed immediately after opening
   * @param autoRefreshTime After this time in minutes pool ledger will be automatically refreshed, use 0 to disable
   * @param networkTimeout Network timeout for communication with nodes in milliseconds
   * @return A IndyResult instance
   */
  public IndyResult openPoolLedger(String configName,
      boolean refreshOnOpen, int autoRefreshTime, int networkTimeout) {
    
    final CompletableFuture<IndyResult> future = openPoolLedgerAsync(configName, 
                                                  refreshOnOpen, autoRefreshTime, networkTimeout);
    
    IndyResult iResult = null;
    try {
      iResult = future.get();
    } catch (InterruptedException | ExecutionException e) {
      iResult = new IndyResult(-1);
      iResult.setException(e);
    }
    return iResult;
  }
}
