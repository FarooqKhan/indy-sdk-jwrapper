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
import org.hyperledger.indy.sdk.jwrapper.GenericResult;

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
  public Future<GenericResult> createWalletAsync(String poolName, String walletName, 
      String walletType, String walletConfigJson, String walletCredentialsJson) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
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
   * @see org.hyperledger.indy.sdk.jwrapper.wallet.WalletApi#createWalletAsync()
   */
  public GenericResult createWallet(String poolName, String walletName, String walletType, 
      String walletConfigJson, String walletCredentialsJson) throws InterruptedException, ExecutionException {

    final Future<GenericResult> future = createWalletAsync(poolName, walletName, walletType, 
        walletConfigJson, walletCredentialsJson);

    return future.get();
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
  public Future<GenericResult> openWalletAsync(String walletName, String walletConfigJson, String walletCredentialsJson) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
    
    Callback callback = new IndyCallback.HandleReturningCallback(future, iResult);
    
    int rc = nativeApiInstance.indy_open_wallet(cmdHandle, walletName, walletConfigJson, walletCredentialsJson, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous open wallet API
   *
   * @see org.hyperledger.indy.sdk.jwrapper.wallet.WalletApi#openWalletAsync()
   */
  public GenericResult openWallet(String walletName, String walletConfigJson, String walletCredentialsJson) 
      throws InterruptedException, ExecutionException {
    final Future<GenericResult> future = openWalletAsync(walletName, walletConfigJson, walletCredentialsJson);
    return future.get();
  }
  
  /**
   * A asynchronous close wallet API
   *
   * @param walletHandle walletHandle returned by openWallet()
   * @return A future that returns a IndyResult
   */
  public Future<GenericResult> closeWalletAsync(int walletHandle) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
   
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_close_wallet(cmdHandle, walletHandle, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous close wallet API
   *
   * @see org.hyperledger.indy.sdk.jwrapper.wallet.WalletApi#closeWalletAsync()
   */
  public GenericResult closeWallet(int walletHandle) throws InterruptedException, ExecutionException {
    final Future<GenericResult> future = closeWalletAsync(walletHandle);
    return future.get();
  }
  
  /**
   * A asynchronous delete wallet API
   * @param walletName Name of the wallet configuration to delete
   * @param walletCredentialsJson Wallet credentials json, List of supported keys in credential depend on wallet type,
   *                         pass null when using default type
   * @return A future that returns a IndyResult
   */
  public Future<GenericResult> deleteWalletAsync(String walletName, String walletCredentialsJson) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
   
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_delete_wallet(cmdHandle, walletName, walletCredentialsJson, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous delete wallet API
   * 
   * @see org.hyperledger.indy.sdk.jwrapper.wallet.WalletApi#deleteWalletAsync()
   */
  public GenericResult deleteWallet(String walletName, String walletCredentialsJson) 
      throws InterruptedException, ExecutionException {
    final Future<GenericResult> future = deleteWalletAsync(walletName, walletCredentialsJson);
    return future.get();
  }
}
