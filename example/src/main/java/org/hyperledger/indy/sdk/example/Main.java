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
import org.hyperledger.indy.sdk.jwrapper.wallet.WalletApi;

@SuppressWarnings("unused")
public class Main {
  
  public static void main(String[] args) throws Exception {
    IndyNativeApi api = new IndyNativeApi(new File("./libindy.dylib"));

    PoolApi poolApi = new PoolApi(api);
    WalletApi walletApi = new WalletApi(api);
    
    String walletName = "MyWallet";
    String walletType = null; //Passing null so default is used
    String walletConfigJson = null; //Passing null so default is used
    String walletCredentialsJson = null; //Passing null so default is used
    
    try {
      String poolName = "vagrant";
      String sandboxFile = "./pool_transactions_sandbox";
      Pool poolHandle = poolApi.createPoolLedgerConfig(poolName, sandboxFile);
      if (null != poolHandle) {
        System.out.println("createPoolLedgerConfig() -> Successfully created pool" + poolName);
      }
    
      if (null != poolHandle) {
        poolHandle.setRefreshOnOpen(true);
        poolHandle = poolApi.openPoolLedger(poolHandle);
        if (null != poolHandle) {
          System.out.println("openPoolLedger() -> Returned Pool Handle: " + poolHandle.getPoolHandle());
        }
      }
/*    
      GenericResult r3 = walletApi.createWallet(poolName, walletName, walletType, walletConfigJson, walletCredentialsJson);
      System.out.println("createWallet() -> Returnvalue: " + r3.getReturnValue() + " ErrorCode: " + r3.getErrorCode() 
                      + " ReturnHandle: " + r3.getReturnHandle());
    
      GenericResult r4 = walletApi.openWallet(walletName, walletConfigJson, walletCredentialsJson);
      System.out.println("openWallet() -> Returnvalue: " + r4.getReturnValue() + " ErrorCode: " + r4.getErrorCode() 
                      + " ReturnHandle: " + r4.getReturnHandle());*/
      
      
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
      GenericResult r100 = poolApi.deletePoolLedger(poolName);
      System.out.println("deletePoolLedger() -> Returnvalue: " + r100.getReturnValue() + " ErrorCode: " + r100.getErrorCode() 
                      + " ReturnHandle: " + r100.getReturnHandle());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }
}
