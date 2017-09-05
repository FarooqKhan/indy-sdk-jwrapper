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

import org.hyperledger.indy.sdk.jwrapper.ErrorCode;
import org.hyperledger.indy.sdk.jwrapper.GenericResult;
import org.hyperledger.indy.sdk.jwrapper.IIndyApi;
import org.hyperledger.indy.sdk.jwrapper.IndyCallback;
import org.hyperledger.indy.sdk.jwrapper.IndyNativeApi;
import org.hyperledger.indy.sdk.jwrapper.IndyNativeApi.NativeApi;
import org.hyperledger.indy.sdk.jwrapper.pool.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Callback;

/**
 * A class that holds all Wallet related API's
 * @version 1.0 29-Jul-2017
 */
public class WalletApi implements IIndyApi {
  private static Logger logger = LoggerFactory.getLogger(WalletApi.class);
  private AtomicInteger cmdHandleCounter;
  private NativeApi nativeApiInstance;

  public WalletApi(IndyNativeApi apiInstance) {
    this.nativeApiInstance = apiInstance.getNativeApiInstance();
    this.cmdHandleCounter = apiInstance.getCmdHandleCounter();
  }
  
  /**
   * A asynchronous create wallet API
   *
   * @param pool handle to a already open Pool
   * @param wallet handle to a unused Wallet
   * @param walletCredentialsJson Wallet credentials json, List of supported keys in credential depend on wallet type,
   *                         pass null when using default type, this parameter is not stored in 
   *                         Wallet object for security reasons
   * @return A future that returns a IndyResult
   */
  public Future<GenericResult> createWalletAsync(Pool pool, Wallet wallet, String walletCredentialsJson) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
    
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
    
    int rc = nativeApiInstance.indy_create_wallet(cmdHandle, pool.getPoolName(), wallet.getWalletName(),
        wallet.getWalletType(), wallet.getWalletConfigJson(), walletCredentialsJson, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous create wallet API
   *
   * @see org.hyperledger.indy.sdk.jwrapper.wallet.WalletApi#createWalletAsync()
   */
  public Wallet createWallet(Pool pool, Wallet wallet, String walletCredentialsJson) throws InterruptedException, ExecutionException {

    final Future<GenericResult> future = createWalletAsync(pool, wallet, walletCredentialsJson);

    GenericResult r = future.get();
    
    if (r.getErrorCode().equals(ErrorCode.Success)) {
      wallet.setStatus(WalletStatus.CREATED);
      return wallet;
    } else {
      logger.error("Failed to create Wallet. Returnvalue: {}, ErrorCode: {}", r.getReturnValue(), r.getErrorCode());
      return null;
    }
  }
  
  /**
   * A asynchronous open wallet API
   *
   * @param walletHandle handle to created Wallet
   * @param walletCredentialsJson Wallet credentials json, List of supported keys in credential depend on wallet type,
   *                         pass null when using default type, this parameter is not stored in 
   *                         Wallet object for security reasons
   * @return A future that returns a IndyResult which will also contain wallet handle
   */
  public Future<GenericResult> openWalletAsync(Wallet wallet, String walletCredentialsJson) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
    
    Callback callback = new IndyCallback.HandleReturningCallback(future, iResult);
    
    int rc = nativeApiInstance.indy_open_wallet(cmdHandle, wallet.getWalletName(),
        wallet.getWalletConfigJson(), walletCredentialsJson, callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous open wallet API
   *
   * @see org.hyperledger.indy.sdk.jwrapper.wallet.WalletApi#openWalletAsync()
   */
  public Wallet openWallet(Wallet wallet, String walletCredentialsJson) throws InterruptedException, ExecutionException {
    final Future<GenericResult> future = openWalletAsync(wallet, walletCredentialsJson);
    GenericResult r = future.get();
    
    if (r.getErrorCode().equals(ErrorCode.Success)) {
      wallet.setWalletHandle(r.getReturnHandle());
      wallet.setStatus(WalletStatus.OPEN);
      return wallet;
    } else {
      logger.error("Failed to open wallet. Returnvalue: {}, ErrorCode: {}", r.getReturnValue(), r.getErrorCode());
      return null;
    }
  }
  
  /**
   * A asynchronous close wallet API
   *
   * @param wallet walletHandle returned by openWallet()
   * @return A future that returns a IndyResult
   */
  public Future<GenericResult> closeWalletAsync(Wallet wallet) {
    final CompletableFuture<GenericResult> future = new CompletableFuture<GenericResult>();
    GenericResult iResult = new GenericResult();
    
    int cmdHandle = cmdHandleCounter.incrementAndGet();
   
    Callback callback = new IndyCallback.SimpleCallback(future, iResult);
 
    int rc = nativeApiInstance.indy_close_wallet(cmdHandle, wallet.getWalletHandle(), callback);
    iResult.setReturnValue(rc);
    return future;
  }
  
  /**
   * A synchronous close wallet API
   *
   * @see org.hyperledger.indy.sdk.jwrapper.wallet.WalletApi#closeWalletAsync()
   */
  public Wallet closeWallet(Wallet wallet) throws InterruptedException, ExecutionException {
    final Future<GenericResult> future = closeWalletAsync(wallet);
    GenericResult r = future.get();
    
    if (r.getErrorCode().equals(ErrorCode.Success)) {
      wallet.setStatus(WalletStatus.CLOSED);
      return wallet;
    } else {
      logger.error("Failed to close wallet. Returnvalue: {}, ErrorCode: {}", r.getReturnValue(), r.getErrorCode());
      return null;
    }
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
