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
package org.hyperledger.indy.sdk.example;

import java.io.File;
import java.util.concurrent.ExecutionException;

import org.hyperledger.indy.sdk.jwrapper.IndyNativeApi;
import org.hyperledger.indy.sdk.jwrapper.GenericResult;
import org.hyperledger.indy.sdk.jwrapper.pool.PoolApi;
import org.hyperledger.indy.sdk.jwrapper.pool.Pool;
import org.hyperledger.indy.sdk.jwrapper.wallet.Wallet;
import org.hyperledger.indy.sdk.jwrapper.wallet.WalletApi;
import org.hyperledger.indy.sdk.jwrapper.wallet.WalletStatus;

@SuppressWarnings("unused")
public class Main {
  
  public static void main(String[] args) throws Exception {
    IndyNativeApi api = new IndyNativeApi(new File("./libindy.dylib"));

    PoolApi poolApi = new PoolApi(api);
    WalletApi walletApi = new WalletApi(api);
    
    try {
      Pool poolHandle = new Pool("vagrant_pool");
      poolHandle.setSandboxFile("./pool_transactions_sandbox");
      
      poolHandle = poolApi.createPoolLedgerConfig(poolHandle);
      if (null != poolHandle) {
        System.out.println("createPoolLedgerConfig() -> Successfully created pool: " + poolHandle.getPoolName());
      }
    
      if (null != poolHandle) {
        poolHandle.setRefreshOnOpen(true);
        poolHandle = poolApi.openPoolLedger(poolHandle);
        if (null != poolHandle) {
          System.out.println("openPoolLedger() -> Successfully opened pool handle: " + poolHandle.getPoolName());
        }
      }
      
      String walletCredentialsJson = null;
      Wallet walletHandle = null;
      if (null != poolHandle) {
        walletHandle = new Wallet("MyWallet");
        walletHandle = walletApi.createWallet(poolHandle, walletHandle, walletCredentialsJson);
        if (null != walletHandle) {
          System.out.println("createWallet() -> Successfully created wallet: " + walletHandle.getWalletName());
        }
      }
      
      if (null != walletHandle) {
        walletHandle = walletApi.openWallet(walletHandle, walletCredentialsJson);
        System.out.println("openWallet() -> Successfully opened wallet handle: " + walletHandle.getWalletName());
      }
      
      System.out.println("Sleeping or 60000");
      Thread.sleep(60000);
      if (null != poolHandle) {
        poolHandle = poolApi.closePoolLedger(poolHandle);
        if (null != poolHandle) {
          System.out.println("closePoolLedger() -> Returned Pool Handle: " + poolHandle.getPoolHandle());
        }
      }
    
    
      System.out.println("Sleeping or 60000");
      Thread.sleep(60000);
      GenericResult r100 = poolApi.deletePoolLedger(poolHandle.getPoolName());
      System.out.println("deletePoolLedger() -> Returnvalue: " + r100.getReturnValue() + " ErrorCode: " + r100.getErrorCode() 
                      + " ReturnHandle: " + r100.getReturnHandle());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }
}
