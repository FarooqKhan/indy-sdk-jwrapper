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
package org.hyperledger.indy.sdk.jwrapper.wallet;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.hyperledger.indy.sdk.jwrapper.IIndyApi;
import org.hyperledger.indy.sdk.jwrapper.IndyCallback;
import org.hyperledger.indy.sdk.jwrapper.IndyNativeApi;
import org.hyperledger.indy.sdk.jwrapper.IndyNativeApi.NativeApi;
import org.hyperledger.indy.sdk.jwrapper.IndyResult;

import com.sun.jna.Callback;

/**
 * A class that holds all Wallet related API's
 * @version 1.0 29-Jul-2017
 */
public class WalletApi implements IIndyApi {
  private AtomicInteger cmdHandleCounter;
  private NativeApi nativeApiInstance;

  public WalletApi(IndyNativeApi apiInstance) {
    this.nativeApiInstance = apiInstance.getNativeApiInstance();
    this.cmdHandleCounter = apiInstance.getCmdHandleCounter();
  }
  
  /**
   * A asynchronous create wallet API
   *
   * @param poolName Name of the pool that corresponds to this wallet
   * @param walletName Name of the wallet 
   * @param walletType Type of the wallet, pass null to use default
   * @param walletConfigJson Wallet configuration json, List of supported keys depend on wallet type,
   *                         pass null when using default type
   * @param walletCredentialsJson Wallet credentials json, List of supported keys in credential depend on wallet type,
   *                         pass null when using default type
   * @return A future that returns a IndyResult
   */
  public Future<IndyResult> createWalletAsync(String poolName, String walletName, 
      String walletType, String walletConfigJson, String walletCredentialsJson) {
    final CompletableFuture<IndyResult> future = new CompletableFuture<IndyResult>();
    IndyResult iResult = new IndyResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
    
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
    
    int rc = nativeApiInstance.indy_create_wallet(cmdHandle, poolName, walletName,
        walletType, walletConfigJson, walletCredentialsJson, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous create wallet API
   *
   * @param poolName Name of the pool that corresponds to this wallet
   * @param walletName Name of the wallet 
   * @param walletType Type of the wallet, pass null to use default
   * @param walletConfigJson Wallet configuration json, List of supported keys depend on wallet type,
   *                         pass null when using default type
   * @param walletCredentialsJson Wallet credentials json, List of supported keys in credential depend on wallet type,
   *                         pass null when using default type
   * @return A IndyResult instance
   */
  public IndyResult createWallet(String poolName, String walletName, 
      String walletType, String walletConfigJson, String walletCredentialsJson) {
    final Future<IndyResult> future = createWalletAsync(poolName, walletName,
        walletType, walletConfigJson, walletCredentialsJson
        );
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
   * A asynchronous open wallet API
   *
   * @param walletName Name of the wallet 
   * @param walletConfigJson Wallet configuration json, List of supported keys depend on wallet type,
   *                         pass null when using default type
   * @param walletCredentialsJson Wallet credentials json, List of supported keys in credential depend on wallet type,
   *                         pass null when using default type
   * @return A future that returns a IndyResult which will also contain wallet handle
   */
  public Future<IndyResult> openWalletAsync(String walletName, String walletConfigJson, String walletCredentialsJson) {
    final CompletableFuture<IndyResult> future = new CompletableFuture<IndyResult>();
    IndyResult iResult = new IndyResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
    
    Callback callback = new IndyCallback.HandleReturningCallback(future, iResult);
    
    int rc = nativeApiInstance.indy_open_wallet(cmdHandle, walletName, walletConfigJson, walletCredentialsJson, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous open wallet API
   *
   * @param walletName Name of the wallet 
   * @param walletConfigJson Wallet configuration json, List of supported keys depend on wallet type,
   *                         pass null when using default type
   * @param walletCredentialsJson Wallet credentials json, List of supported keys in credential depend on wallet type,
   *                         pass null when using default type
   * @return A IndyResult instance which will also contain wallet handle
   */
  public IndyResult openWallet(String walletName, String walletConfigJson, String walletCredentialsJson) {
    final Future<IndyResult> future = openWalletAsync(walletName, walletConfigJson, walletCredentialsJson
        );
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
   * A asynchronous close wallet API
   * @param walletHandle walletHandle returned by openWallet()
   * @return A future that returns a IndyResult
   */
  public Future<IndyResult> closeWalletAsync(int walletHandle) {
    final CompletableFuture<IndyResult> future = new CompletableFuture<IndyResult>();
    IndyResult iResult = new IndyResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
   
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_close_wallet(cmdHandle, walletHandle, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous close wallet API
   * @param walletHandle walletHandle returned by openWallet()
   * @return A IndyResult instance
   */
  public IndyResult closeWallet(int walletHandle) {
    final Future<IndyResult> future = closeWalletAsync(walletHandle);
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
   * A asynchronous delete wallet API
   * @param walletName Name of the wallet configuration to delete
   * @param walletCredentialsJson Wallet credentials json, List of supported keys in credential depend on wallet type,
   *                         pass null when using default type
   * @return A future that returns a IndyResult
   */
  public Future<IndyResult> deleteWalletAsync(String walletName, String walletCredentialsJson) {
    final CompletableFuture<IndyResult> future = new CompletableFuture<IndyResult>();
    IndyResult iResult = new IndyResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
   
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_delete_wallet(cmdHandle, walletName, walletCredentialsJson, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous delete wallet API
   * @param walletName Name of the wallet configuration to delete
   * @param walletCredentialsJson Wallet credentials json, List of supported keys in credential depend on wallet type,
   *                         pass null when using default type
   * @return A IndyResult instance
   */
  public IndyResult deleteWallet(String walletName, String walletCredentialsJson) {
    final Future<IndyResult> future = deleteWalletAsync(walletName, walletCredentialsJson);
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
