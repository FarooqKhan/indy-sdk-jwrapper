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
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.hyperledger.indy.sdk.jwrapper.IndyNativeApi;
import org.hyperledger.indy.sdk.jwrapper.IndyNativeApi.NativeApi;
import org.hyperledger.indy.sdk.jwrapper.IIndyApi;
import org.hyperledger.indy.sdk.jwrapper.IndyCallback;
import org.hyperledger.indy.sdk.jwrapper.IndyResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.Callback;

/**
 * A class that holds all Pool related API's
 * @version 1.0 28-Jul-2017
 */
public class PoolApi implements IIndyApi {
  private ObjectMapper objectMapper;
  private AtomicInteger cmdHandleCounter;
  private NativeApi nativeApiInstance;

  public PoolApi(IndyNativeApi apiInstance) {
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
  public Future<IndyResult> createPoolLedgerConfigAsync(String configName, String genesisFilePath) {
    final CompletableFuture<IndyResult> future = new CompletableFuture<IndyResult>();
    IndyResult iResult = new IndyResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
    String configJson = null;

    if (null != genesisFilePath) {
      CreatePoolLedgerConfig config = new CreatePoolLedgerConfig(genesisFilePath);
      try {
        configJson = objectMapper.writeValueAsString(config);
      } catch (JsonProcessingException e) {
        future.completeExceptionally(e);
        return future;
      }
    }
    
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
    
    int rc = nativeApiInstance.indy_create_pool_ledger_config(cmdHandle, configName, configJson, callback);
    iResult.setReturnValue(rc);
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
    final Future<IndyResult> future = createPoolLedgerConfigAsync(configName, genesisFilePath);
    IndyResult iResult = null;
    try {
      iResult = future.get();
    } catch (InterruptedException | ExecutionException e) {
      iResult = new IndyResult();
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
   * @return A future that returns a IndyResult which will also contain pool handle
   */
  public Future<IndyResult> openPoolLedgerAsync(String configName, 
      boolean refreshOnOpen, int autoRefreshTime, int networkTimeout) {
    final CompletableFuture<IndyResult> future = new CompletableFuture<IndyResult>();
    IndyResult iResult = new IndyResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
    OpenPoolLedgerConfig config = new OpenPoolLedgerConfig();
    config.setRefreshOnOpen(refreshOnOpen);
    config.setAutoRefreshTime(autoRefreshTime);
    config.setNetworkTimeout(networkTimeout);
 
    String configJson = null;
    try {
      configJson = objectMapper.writeValueAsString(config);
    } catch (JsonProcessingException e) {
      future.completeExceptionally(e);
      return future;
    }
   
    Callback callback = new IndyCallback.HandleReturningCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_open_pool_ledger(cmdHandle, configName, configJson, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous open pool ledger API
   * @param configName config name to create
   * @param refreshOnOpen Forces pool ledger to be refreshed immediately after opening
   * @param autoRefreshTime After this time in minutes pool ledger will be automatically refreshed, use 0 to disable
   * @param networkTimeout Network timeout for communication with nodes in milliseconds
   * @return A IndyResult instance which will also contain pool handle
   */
  public IndyResult openPoolLedger(String configName, boolean refreshOnOpen, int autoRefreshTime, int networkTimeout) {
    
    final Future<IndyResult> future = openPoolLedgerAsync(configName, 
                                                  refreshOnOpen, autoRefreshTime, networkTimeout);
    IndyResult iResult = null;
    try {
      iResult = future.get();
    } catch (InterruptedException | ExecutionException e) {
      iResult = new IndyResult();
      iResult.setException(e);
    }
    return iResult;
  }
  
  /**
   * A asynchronous refresh pool ledger API
   * @param poolHandle pool handle returned by openPoolLedger()
   * @return A future that returns a IndyResult
   */
  public Future<IndyResult> refreshPoolLedgerAsync(int poolHandle) {
    final CompletableFuture<IndyResult> future = new CompletableFuture<IndyResult>();
    IndyResult iResult = new IndyResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
   
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_refresh_pool_ledger(cmdHandle, poolHandle, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous refresh pool ledger API
   * @param poolHandle pool handle returned by openPoolLedger()
   * @return A IndyResult instance
   */
  public IndyResult refreshPoolLedger(int poolHandle) {
    final Future<IndyResult> future = refreshPoolLedgerAsync(poolHandle);
    IndyResult iResult = null;
    try {
      iResult = future.get();
    } catch (InterruptedException | ExecutionException e) {
      iResult = new IndyResult();
      iResult.setException(e);
    }
    return iResult;
  }
  
  /**
   * A asynchronous close pool ledger API
   * @param poolHandle pool handle returned by openPoolLedger()
   * @return A future that returns a IndyResult
   */
  public Future<IndyResult> closePoolLedgerAsync(int poolHandle) {
    final CompletableFuture<IndyResult> future = new CompletableFuture<IndyResult>();
    IndyResult iResult = new IndyResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
   
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_close_pool_ledger(cmdHandle, poolHandle, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous close pool ledger API
   * @param poolHandle pool handle returned by openPoolLedger()
   * @return A IndyResult instance
   */
  public IndyResult closePoolLedger(int poolHandle) {
    final Future<IndyResult> future = closePoolLedgerAsync(poolHandle);
    IndyResult iResult = null;
    try {
      iResult = future.get();
    } catch (InterruptedException | ExecutionException e) {
      iResult = new IndyResult();
      iResult.setException(e);
    }
    return iResult;
  }
  
  /**
   * A asynchronous delete pool ledger API
   * @param configName Name of the pool ledger configuration to delete
   * @return A future that returns a IndyResult
   */
  public Future<IndyResult> deletePoolLedgerAsync(String configName) {
    final CompletableFuture<IndyResult> future = new CompletableFuture<IndyResult>();
    IndyResult iResult = new IndyResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
   
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_delete_pool_ledger_config(cmdHandle, configName, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous delete pool ledger API
   * @param configName Name of the pool ledger configuration to delete
   * @return A IndyResult instance
   */
  public IndyResult deletePoolLedger(String configName) {
    final Future<IndyResult> future = deletePoolLedgerAsync(configName);
    IndyResult iResult = null;
    try {
      iResult = future.get();
    } catch (InterruptedException | ExecutionException e) {
      iResult = new IndyResult();
      iResult.setException(e);
    }
    return iResult;
  }
}
