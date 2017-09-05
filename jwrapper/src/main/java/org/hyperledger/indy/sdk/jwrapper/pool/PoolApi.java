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

import org.hyperledger.indy.sdk.jwrapper.ErrorCode;
import org.hyperledger.indy.sdk.jwrapper.IIndyApi;
import org.hyperledger.indy.sdk.jwrapper.IndyCallback;
import org.hyperledger.indy.sdk.jwrapper.IndyNativeApi;
import org.hyperledger.indy.sdk.jwrapper.IndyNativeApi.NativeApi;
import org.hyperledger.indy.sdk.jwrapper.GenericResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.Callback;

/**
 * A class that holds all Pool related API's
 * @version 1.0 28-Jul-2017
 */
public class PoolApi implements IIndyApi {
  private static Logger logger = LoggerFactory.getLogger(PoolApi.class);
  
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
   * @param pool handle to a unused Pool instance
   * @param genesisFilePath A path to genesis transaction file. If NULL, then a default one will be used. 
   *                        If file doesn't exists default one will be created.
   * @return A future that returns a IndyResult
   */
  public Future<GenericResult> createPoolLedgerConfigAsync(Pool pool) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
    String configJson = null;

    if (null != pool.getSandboxFile()) {
      CreatePoolLedgerConfig config = new CreatePoolLedgerConfig(pool.getSandboxFile());
      try {
        configJson = objectMapper.writeValueAsString(config);
      } catch (JsonProcessingException e) {
        future.completeExceptionally(e);
        return future;
      }
    }
    
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
    
    int rc = nativeApiInstance.indy_create_pool_ledger_config(cmdHandle, pool.getPoolName(), configJson, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous create pool ledger config API
   *
   * @param pool handle to a unused Pool instance
   * @param sandboxFile A path to genesis transaction file. If NULL, then a default one will be used. 
   *                        If file doesn't exists default one will be created.
   * @return A PoolHandle a Pool Instance
   */
  public Pool createPoolLedgerConfig(Pool pool) throws InterruptedException, ExecutionException {
    final Future<GenericResult> future = createPoolLedgerConfigAsync(pool);
    GenericResult r = future.get();
    
    if (r.getErrorCode().equals(ErrorCode.Success)) {
      pool.setStatus(PoolStatus.CREATED);
      return pool;
    } else {
      logger.error("Failed to create pool ledger. Returnvalue: {}, ErrorCode: {}", r.getReturnValue(), r.getErrorCode());
      return null;
    }
  }
  
  /**
   * A asynchronous open pool ledger API
   * @param pool handle to a already created Pool
   * @return A future that returns a IndyResult which will also contain pool handle
   */
  public Future<GenericResult> openPoolLedgerAsync(Pool pool) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
    OpenPoolLedgerConfig config = new OpenPoolLedgerConfig();
    config.setRefreshOnOpen(pool.isRefreshOnOpen());
    config.setAutoRefreshTime(pool.getAutoRefreshTime());
    config.setNetworkTimeout(pool.getNetworkTimeout());
 
    String configJson = null;
    try {
      configJson = objectMapper.writeValueAsString(config);
    } catch (JsonProcessingException e) {
      future.completeExceptionally(e);
      return future;
    }
   
    Callback callback = new IndyCallback.HandleReturningCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_open_pool_ledger(cmdHandle, pool.getPoolName(), configJson, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous open pool ledger API
   * @param pool handle to a already created Pool
   * @return A PoolHandle Instance with settings the Pool was created with.
   */
  public Pool openPoolLedger(Pool pool) throws InterruptedException, ExecutionException {
    
    final Future<GenericResult> future = openPoolLedgerAsync(pool);
    GenericResult r = future.get();
    
    if (r.getErrorCode().equals(ErrorCode.Success)) {
      pool.setPoolHandle(r.getReturnHandle());
      pool.setStatus(PoolStatus.OPEN);
    } else {
      logger.error("Failed to open pool ledger. Returnvalue: {}, ErrorCode: {}", r.getReturnValue(), r.getErrorCode());
      return null;
    }
    
    return pool;
  }
  
  /**
   * A asynchronous refresh pool ledger API
   * @param pool pool handle returned by openPoolLedger()
   * @return A future that returns a IndyResult
   */
  public Future<GenericResult> refreshPoolLedgerAsync(Pool pool) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
   
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_refresh_pool_ledger(cmdHandle, pool.getPoolHandle(), callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous refresh pool ledger API
   * @param pool pool handle returned by openPoolLedger()
   * @return A IndyResult instance
   */
  public GenericResult refreshPoolLedger(Pool pool) throws InterruptedException, ExecutionException {
    final Future<GenericResult> future = refreshPoolLedgerAsync(pool);
    return future.get();
  }
  
  /**
   * A asynchronous close pool ledger API
   * @param pool pool handle returned by openPoolLedger()
   * @return A future that returns a IndyResult
   */
  public Future<GenericResult> closePoolLedgerAsync(Pool pool) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
   
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_close_pool_ledger(cmdHandle, pool.getPoolHandle(), callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous close pool ledger API
   * @param pool pool handle returned by openPoolLedger()
   * @return A PoolHandle Instance with settings the Pool was created with.
   */
  public Pool closePoolLedger(Pool pool) throws InterruptedException, ExecutionException {
    final Future<GenericResult> future = closePoolLedgerAsync(pool);
    GenericResult r = future.get();

    if (r.getErrorCode().equals(ErrorCode.Success)) {
      pool.setStatus(PoolStatus.CLOSED);
    } else {
      logger.error("Failed to close pool ledger. Returnvalue: {}, ErrorCode: {}", r.getReturnValue(), r.getErrorCode());
      return null;
    }
    
    return pool;
  }
  
  /**
   * A asynchronous delete pool ledger API
   * @param poolName Name of the pool ledger configuration to delete
   * @return A future that returns a IndyResult
   */
  public Future<GenericResult> deletePoolLedgerAsync(String poolName) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
   
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_delete_pool_ledger_config(cmdHandle, poolName, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous delete pool ledger API
   * @param poolName Name of the pool ledger configuration to delete
   * @return A IndyResult instance
   */
  public GenericResult deletePoolLedger(String poolName) throws InterruptedException, ExecutionException {
    final Future<GenericResult> future = deletePoolLedgerAsync(poolName);
    return future.get();
  }
}
